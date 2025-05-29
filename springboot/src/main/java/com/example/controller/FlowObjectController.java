package com.example.controller;

import com.example.common.Result;
import com.example.entity.FlowObject;
import com.example.entity.IncomingDoc;
import com.example.service.FlowObjectService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/flowObject")
public class FlowObjectController {

    @Resource
    private FlowObjectService flowObjectService;

    /**
     * 新增流转对象
     */
    @PostMapping("/add")
    public Result add(@RequestBody FlowObject object) {
        flowObjectService.add(object);
        return Result.success("流转对象新增成功");
    }

    /**
     * 删除流转对象
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        flowObjectService.deleteById(id);
        return Result.success("流转对象删除成功");
    }

    /**
     * 批量删除流转对象
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        flowObjectService.deleteBatch(ids);
        return Result.success("批量删除成功");
    }

    /**
     * 更新流转对象信息
     */
    @PutMapping("/update")
    public Result update(@RequestBody FlowObject object) {
        flowObjectService.updateById(object);
        return Result.success("流转对象更新成功");
    }

    /**
     * 根据ID查询流转对象详情
     */
    @GetMapping("/{id}")
    public Result get(@PathVariable Integer id) {
        return Result.success(flowObjectService.getById(id));
    }

    /**
     * 分页查询流转对象列表
     */
    @GetMapping("/selectPage")
    public Result selectPage(FlowObject flowObject,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<FlowObject> page = flowObjectService.selectPage(flowObject, pageNum, pageSize);
        return Result.success(page);
    }

    /**
     * 查询所有流转对象（简洁列表）
     */
    @GetMapping("/all")
    public Result getAll() {
        return Result.success(flowObjectService.selectAll());
    }
}
