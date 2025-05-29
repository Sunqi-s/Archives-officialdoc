package com.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import java.util.Map;

@Mapper
public interface HomeMapper {
    /**
     * 获取首页统计数据
     */
    Map<String, Object> getHomeStatistics();
}
