package com.example.controller;

import com.example.common.Result;
import com.example.entity.Admin;
import com.example.entity.ReminderSettings;
import com.example.service.SettingsService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/settings")
public class SettingsController {

    @Resource
    private SettingsService settingsService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody ReminderSettings reminderSettings) {
        settingsService.add(reminderSettings);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        settingsService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        settingsService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody ReminderSettings reminderSettings) {
        settingsService.updateById(reminderSettings);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        ReminderSettings reminderSettings = settingsService.selectById(id);
        return Result.success(reminderSettings);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(ReminderSettings reminderSettings ) {
        List<ReminderSettings> list = settingsService.selectAll(reminderSettings);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(ReminderSettings reminderSettings,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<ReminderSettings> page = settingsService.selectPage(reminderSettings, pageNum, pageSize);
        return Result.success(page);
    }
}
