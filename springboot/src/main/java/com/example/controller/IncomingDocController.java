package com.example.controller;

import com.example.common.Result;
import com.example.entity.IncomingDoc;
import com.example.service.IncomingDocService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/incomingDoc")
public class IncomingDocController {
    @Resource
    private IncomingDocService incomingDocService;

    /**
     * 新增收文
     */
    @PostMapping("/add")
    public Result add(@RequestBody IncomingDoc doc) {
        incomingDocService.add(doc);
        return Result.success("收文登记成功");
    }

    /**
     * 查询单条收文（含流转意见）
     */
    @GetMapping("/{id}")
    public Result get(@PathVariable Integer id) {
        IncomingDoc doc = incomingDocService.getWithOpinions(id);
        return Result.success(doc);
    }

    /**
     * 分页查询收文列表（使用实体类封装查询条件，风格与ClassController一致）
     */
    @GetMapping("/selectPage")
    public Result selectPage(IncomingDoc incomingDoc,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<IncomingDoc> page = incomingDocService.selectPage(incomingDoc, pageNum, pageSize);
        return Result.success(page);
    }

    /**
     * 存档收文（更新状态）
     */
    @PutMapping("/archive/{id}")
    public Result archive(@PathVariable Integer id) {
        incomingDocService.archive(id);
        return Result.success("存档成功");
    }

    /**
     * 获取所有流转对象（领导/审批人）
     */
    @GetMapping("/flowObjects")
    public Result getFlowObjects() {
        return Result.success(incomingDocService.getAllFlowObjects());
    }

    /**
     * 删除单条收文
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        incomingDocService.deleteById(id);
        return Result.success("收文删除成功");
    }

    /**
     * 批量删除收文
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        incomingDocService.deleteBatch(ids);
        return Result.success("批量删除成功");
    }

    /**
     * 更新收文信息
     */
    @PutMapping("/update")
    public Result update(@RequestBody IncomingDoc doc) {
        incomingDocService.updateById(doc);
        return Result.success("收文更新成功");
    }
}
