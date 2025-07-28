package com.example.service;

import com.example.entity.ReminderSettings;
import com.example.mapper.SettingsMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class SettingsService {

    @Resource
    private SettingsMapper settingsMapper;

    public void add(ReminderSettings reminderSettings){
        reminderSettings.setCreatedTime(new Date());
        reminderSettings.setUserId(TokenUtils.getCurrentUser().getId());
        settingsMapper.insert(reminderSettings);
    }

    public void deleteById(Integer id){
        settingsMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids){
        for (Integer id : ids){
            settingsMapper.deleteById(id);
        }
    }

    public void updateById(ReminderSettings reminderSettings){
        reminderSettings.setUpdatedTime(new Date());
        settingsMapper.updateById(reminderSettings);
    }

    public ReminderSettings selectById(Integer id){
        return settingsMapper.selectById(id);
    }

    public List<ReminderSettings> selectAll(ReminderSettings reminderSettings){
        return settingsMapper.selectAll(reminderSettings);
    }

    public PageInfo<ReminderSettings> selectPage(ReminderSettings reminderSettings, Integer pageNum, Integer pageSize){
        PageHelper.startPage(pageNum, pageSize); // 启动分页
        // 直接调用Mapper的selectPageList（XML实现动态条件）
        List<ReminderSettings> list = settingsMapper.selectPageList(reminderSettings);
        return PageInfo.of(list);
    }
}
