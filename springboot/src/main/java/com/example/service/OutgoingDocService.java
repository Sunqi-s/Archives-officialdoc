package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.Constants;
import com.example.entity.*;
import com.example.exception.CustomException;
import com.example.mapper.OutgoingDocMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class OutgoingDocService {
    @Resource
    private OutgoingDocMapper outgoingDocMapper;
    @Resource
    private FlowObjectService flowObjectService; // 流转对象服务
    @Resource
    private OutgoingOpinionService outgoingOpinionService;
    @Resource
    private AttachmentService attachmentService; // 附件服务
    @Resource
    private RecordNoService recordNoService;


    @Transactional(rollbackFor = Exception.class)
    public void add(OutgoingDoc doc) {
        if (ObjectUtil.isEmpty(doc.getTitle())) {
            throw new CustomException("5011", "文件标题不能为空");
        }
        if (ObjectUtil.isEmpty(doc.getSenderOrg())) {
            throw new CustomException("5012", "发文单位不能为空");
        }
        if (ObjectUtil.isNotEmpty(doc.getSenderDocNo())) {
            OutgoingDoc existDoc = outgoingDocMapper.selectBySenderDocNo(doc.getSenderDocNo());
            if (existDoc != null) {
                throw new CustomException("5014", "发文编号已存在");
            }
        }

        // 处理附件 - 增加空值检查
        Integer attachmentId = null;
        Attachment file = doc.getFile();
        if (file != null && file.getFilePath() != null) {
            Attachment attachment = new Attachment();
            attachment.setFileName(file.getFileName());
            attachment.setFileType("收文附件");
            attachment.setFilePath(file.getFilePath());
            attachment.setCreateDate(new Date());
            attachment.setCreator(TokenUtils.getCurrentUser().getId());

            // 保存附件信息
            attachmentService.save(attachment);
            attachmentId = attachment.getId();
        }

        doc.setArchiveStatus("处理中");
        doc.setCreator(TokenUtils.getCurrentUser().getId());
        doc.setSecretType(doc.getSecretType() != null ? doc.getSecretType() : 0);  // 默认普通件
        doc.setProcessType(doc.getProcessType());
        doc.setStatus(doc.getStatus() != null ? doc.getStatus() : Constants.STATUS_PENDING);
        doc.setFeedbackRequired(doc.getFeedbackRequired() != null ? doc.getFeedbackRequired() : false);
        doc.setAttachmentId(attachmentId);
        outgoingDocMapper.insert(doc);

        Integer id = doc.getId();

        // 查询level=0的办公室流转对象
        List<FlowObject> officeObjects = flowObjectService.selectByLevel(0);
        FlowObject office = officeObjects.get(0); // 只有一个办公室

        // 构造拟办意见对象
        OutgoingOpinion opinion = new OutgoingOpinion();
        opinion.setDocId(id);                // 收文id
        opinion.setObjectId(office.getId());    // 办公室流转对象id
        opinion.setName(office.getName());
        opinion.setPosition(office.getPosition());
        opinion.setLevel(office.getLevel());
        opinion.setOpinion(doc.getOpinion());    // 前端输入的拟办意见
        opinion.setNextObjectId(null);           // 无下一流转对象
        opinion.setOpinionType("拟办意见");       // 意见类型
        opinion.setCreator(TokenUtils.getCurrentUser().getId()); // 当前用户id
        opinion.setCreateDate(new Date());       // 创建时间

        // 保存拟办意见
        outgoingOpinionService.save(opinion);
    }

    /**
     * 根据ID删除发文
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Integer id) {
        OutgoingDoc doc = outgoingDocMapper.selectById(id); // 检查文档存在性
        outgoingDocMapper.deleteById(id);
    }

    /**
     * 批量删除发文
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            deleteById(id);
        }
    }

    /**
     * 更新发文信息（禁止修改已存档文档）
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateById(OutgoingDoc doc) {
        OutgoingDoc existDoc = outgoingDocMapper.selectById(doc.getId());

        if (Constants.STATUS_ARCHIVED.equals(existDoc.getStatus())) {
            throw new CustomException("5015", "已存档文档不可修改");
        }

        // 拷贝更新字段（避免直接操作参数对象，防止脏数据）
        existDoc.setSenderOrg(doc.getSenderOrg());
        existDoc.setSecretLevel(doc.getSecretLevel());
        existDoc.setPageCount(doc.getPageCount());
        existDoc.setTitle(doc.getTitle());
        existDoc.setFileNo(doc.getFileNo());
        existDoc.setSenderDocNo(doc.getSenderDocNo());
        existDoc.setProcessType(doc.getProcessType());
        existDoc.setHandlingOrg(doc.getHandlingOrg());
        existDoc.setAttachmentId(doc.getAttachmentId());
        existDoc.setUpdater(TokenUtils.getCurrentUser().getId());
        existDoc.setUpdateDate(new Date());
        existDoc.setSecretType(doc.getSecretType());
        existDoc.setStatus(doc.getStatus());
        existDoc.setLimitDate(doc.getLimitDate());

        outgoingDocMapper.updateById(existDoc);
    }

    public void updateStatusById(OutgoingDoc doc) {
        OutgoingDoc existDoc = outgoingDocMapper.selectById(doc.getId());

        if (Constants.STATUS_ARCHIVED.equals(existDoc.getStatus())) {
            throw new CustomException("5015", "已存档文档不可修改");
        }

        // 拷贝更新字段（避免直接操作参数对象，防止脏数据）
        existDoc.setStatus(1);

        outgoingDocMapper.updateById(existDoc);
    }

    /**
     * 根据ID查询发文（含流转意见）
     */
    public OutgoingDoc getWithOpinions(Integer id) {
        OutgoingDoc doc = outgoingDocMapper.selectById(id);
        if (doc == null) {
            throw new CustomException("5016", "收文不存在");
        }
        // 关联查询流转意见（调用Mapper的selectOpinionsByDocId）
        List<OutgoingOpinion> opinions = outgoingDocMapper.selectOpinionsByDocId(id);
        doc.setOpinions(opinions);
        return doc;
    }

    /**
     * 分页查询发文列表（使用Mapper的XML动态查询）
     */
    public PageInfo<OutgoingDoc> selectPage(OutgoingDoc query, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize); // 启动分页
        // 直接调用Mapper的selectPageList（XML实现动态条件）
        List<OutgoingDoc> list = outgoingDocMapper.selectPageList(query);
        return PageInfo.of(list);
    }

    /**
     * 发文存档
     */
    @Transactional(rollbackFor = Exception.class)
    public void archive(Integer id) {
        OutgoingDoc doc = outgoingDocMapper.selectById(id);
        String recordNo = recordNoService.getRecordNo("fw");

        if (Constants.STATUS_ARCHIVED.equals(doc.getStatus())) {
            throw new CustomException("5016", "文档已存档，无需重复操作");
        }
        doc.setStatus(2);
        doc.setArchiveStatus("归档");
        doc.setArchiveDate(new Date());
        doc.setRecordNo(recordNo);
        doc.setUpdater(TokenUtils.getCurrentUser().getId());
        outgoingDocMapper.updateById(doc);
    }

    /**
     * 获取所有流转对象
     */
    public List<FlowObject> getAllFlowObjects() {
        List<FlowObject> list = flowObjectService.selectAll();
        if (ObjectUtil.isEmpty(list)) {
            throw new CustomException("5017", "未配置流转对象");
        }
        return list;
    }

    public List<OutgoingDoc> getDocsWithLimitDate() {
        List<OutgoingDoc> list = outgoingDocMapper.selectAllWithLimitDate();
        return list;
    }
}
