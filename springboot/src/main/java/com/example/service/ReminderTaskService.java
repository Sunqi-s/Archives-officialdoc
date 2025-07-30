package com.example.service;

import com.example.entity.IncomingDoc;
import com.example.entity.OutgoingDoc;
import com.example.entity.ReminderSettings;
import com.example.common.handler.ReminderWebSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class ReminderTaskService {

    private static final Logger logger = LoggerFactory.getLogger(ReminderTaskService.class);

    @Autowired
    private IncomingDocService incomingDocService;

    @Autowired
    private OutgoingDocService outgoingDocService;

    @Autowired
    private SettingsService settingsService;

    @Autowired
    private ReminderWebSocketHandler reminderWebSocketHandler;

    // 每 30 分钟执行一次定时任务
    @Scheduled(fixedRate = 30 * 60 * 1000)
    public void checkReminders() {
        logger.info("Starting reminder check task...");
        System.out.println("Reminder check task started");

        List<String> Inmessages = new ArrayList<>();
        List<String> Outmessages = new ArrayList<>();

        // 从 doc_incoming_doc 和 doc_outgoing_doc 中查询 limit_date 不为空的数据
        List<IncomingDoc> incomingDocs = incomingDocService.getDocsWithLimitDate();
        List<OutgoingDoc> outgoingDocs = outgoingDocService.getDocsWithLimitDate();

        // 从 reminder_settings 中查询 status 为 0 的提醒设置
        ReminderSettings query = new ReminderSettings();
        query.setStatus(0);
        List<ReminderSettings> reminderSettings = settingsService.selectAll(query);

        // 合并所有文档数据
        List<Object> allDocs = new ArrayList<>();
        allDocs.addAll(incomingDocs);
        allDocs.addAll(outgoingDocs);

        // 遍历提醒设置，匹配文档数据
        for (ReminderSettings setting : reminderSettings) {
            for (Object doc : allDocs) {
                if (isMatch(setting, doc)) {
                    Date remindDate = calculateRemindDate(setting, getLimitDate(doc));
                    if (isTimeToRemind(remindDate)) {
                        if (doc instanceof IncomingDoc) {
                            IncomingDoc incomingDoc = (IncomingDoc) doc;
                            Inmessages.add(incomingDoc.getTitle());
                        } else if (doc instanceof OutgoingDoc) {
                            OutgoingDoc outgoingDoc = (OutgoingDoc) doc;
                            Outmessages.add( outgoingDoc.getTitle());
                        }
                    }
                }
            }
        }
        String inmessage = "收文提醒：";
        for (String inme : Inmessages) {
            inmessage = inmessage + inme + " ";
        }
        inmessage = inmessage + "的限办日期快到了，请及时办理。";
        String outmessage = "发文提醒：";
        for (String outme : Outmessages) {
            outmessage = outmessage + outme + " ";
        }
        outmessage = outmessage + "的限办日期快到了，请及时办理。";

        String messages = inmessage +"\n " + outmessage;

        reminderWebSocketHandler.sendReminderMessage(messages);

        logger.info("Reminder check task completed.");
    }

    private boolean isMatch(ReminderSettings setting, Object doc) {
        // 根据 limit_type 和 doc_type 进行匹配
        if (doc instanceof IncomingDoc) {
            IncomingDoc incomingDoc = (IncomingDoc) doc;
            return "INCOMING_DOC".equals(setting.getLimitType()) &&
                    incomingDoc.getSecretType().equals(setting.getDocType());
        } else if (doc instanceof OutgoingDoc) {
            OutgoingDoc outgoingDoc = (OutgoingDoc) doc;
            return "OUTGOING_DOC".equals(setting.getLimitType()) &&
                    outgoingDoc.getSecretType().equals(setting.getDocType());
        }
        return false;
    }

    private Date calculateRemindDate(ReminderSettings setting, Date limitDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(limitDate);
        switch (setting.getRemindBeforeUnit()) {
            case minutes:
                calendar.add(Calendar.MINUTE, -setting.getRemindBeforeValue());
                break;
            case hours:
                calendar.add(Calendar.HOUR, -setting.getRemindBeforeValue());
                break;
            case days:
                calendar.add(Calendar.DAY_OF_MONTH, -setting.getRemindBeforeValue());
                break;
            case months:
                calendar.add(Calendar.MONTH, -setting.getRemindBeforeValue());
                break;
        }
        return calendar.getTime();
    }

    private boolean isTimeToRemind(Date remindDate) {
        Date now = new Date();
        return now.after(remindDate);
    }

    private String generateReminderMessage(ReminderSettings setting, Object doc) {
        if (doc instanceof IncomingDoc) {
            IncomingDoc incomingDoc = (IncomingDoc) doc;

            return incomingDoc.getTitle();
        } else if (doc instanceof OutgoingDoc) {
            OutgoingDoc outgoingDoc = (OutgoingDoc) doc;
            return outgoingDoc.getTitle();
        }
        return "";
    }

    private Date getLimitDate(Object doc) {
        if (doc instanceof IncomingDoc) {
            return ((IncomingDoc) doc).getLimitDate();
        } else if (doc instanceof OutgoingDoc) {
            return ((OutgoingDoc) doc).getLimitDate();
        }
        return null;
    }
}
