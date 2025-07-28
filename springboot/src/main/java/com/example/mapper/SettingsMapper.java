package com.example.mapper;

import com.example.entity.ReminderSettings;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface SettingsMapper {

    int insert(ReminderSettings reminderSettings);

    /**
     * 删除
     */
    int deleteById(Integer id);

    int updateById(ReminderSettings reminderSettings);

    ReminderSettings selectById(Integer id);

    List<ReminderSettings> selectAll(ReminderSettings reminderSettings);

    /**
     * 动态条件分页查询收文列表（XML实现条件）
     */
    List<ReminderSettings> selectPageList(@Param("query") ReminderSettings query);
}
