package com.example.controller;

import com.example.common.Result;
import com.example.entity.DocType;
import com.example.service.DocTypeService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/docType")
public class DocTypeController {

    @Resource
    private DocTypeService docTypeService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody DocType docType) {
        docTypeService.add(docType);
        return Result.success("来文类型新增成功");
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        docTypeService.deleteById(id);
        return Result.success("来文类型删除成功");
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        docTypeService.deleteBatch(ids);
        return Result.success("批量删除成功");
    }

    /**
     * 更新
     */
    @PutMapping("/update")
    public Result update(@RequestBody DocType docType) {
        docTypeService.updateById(docType);
        return Result.success("来文类型更新成功");
    }

    /**
     * 详情查询
     */
    @GetMapping("/{id}")
    public Result get(@PathVariable Integer id) {
        return Result.success(docTypeService.getById(id));
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<DocType> page = docTypeService.selectPage(name, pageNum, pageSize);
        return Result.success(page);
    }

    /**
     * 查询所有（简洁列表）
     */
    @GetMapping("/all")
    public Result getAll() {
        return Result.success(docTypeService.selectAll());
    }
}
