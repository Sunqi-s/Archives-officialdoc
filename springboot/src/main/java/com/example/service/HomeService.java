package com.example.service;

import com.example.mapper.HomeMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class HomeService {

    @Resource
    private HomeMapper homeMapper;


    public Map<String, Object> getHomeStatistics() {
        // 执行统计SQL
        Map<String, Object> stats = homeMapper.getHomeStatistics();

        // 组装统计数据格式
        List<Map<String, Object>> statistics = new ArrayList<>();

        // 本月发文统计
        statistics.add(buildStatistic(
                "本月发文",
                stats.get("monthly_outgoing_count"),
                "今日发文",
                stats.get("today_outgoing_count")
        ));

        // 本月收文统计
        statistics.add(buildStatistic(
                "本月收文",
                stats.get("monthly_incoming_count"),
                "今日收文",
                stats.get("today_incoming_count")
        ));

        // 待审批文件统计
        statistics.add(buildStatistic(
                "待审批文件",
                stats.get("pending_count"),
                "紧急文件",
                stats.get("pending_urgent_count")
        ));

        // 已归档文件统计
        statistics.add(buildStatistic(
                "已归档文件",
                stats.get("archived_count"),
                "本年度",
                stats.get("archived_year_count")
        ));

        return Collections.singletonMap("statistics", statistics);
    }

    // 辅助方法：构建统计项
    private Map<String, Object> buildStatistic(
            String title,
            Object value,
            String subTitle,
            Object subValue
            ) {
        Map<String, Object> stat = new HashMap<>();
        stat.put("title", title);
        stat.put("value", value);
        stat.put("subTitle", subTitle);
        stat.put("subValue", subValue != null ? subValue : 0);
        return stat;
    }
}
