package com.example.controller;

import com.example.common.Result;
import com.example.entity.IncomingOpinion;
import com.example.entity.OutgoingOpinion;
import com.example.service.OutgoingOpinionService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/outgoingOpinion")
public class OutgoingOpinionController {

    @Resource
    private OutgoingOpinionService outgoingOpinionService;

    /**
     * 提交流转意见
     */
    @PostMapping("/add")
    public Result add(@RequestBody OutgoingOpinion opinion) {
        outgoingOpinionService.save(opinion);
        return Result.success("意见提交成功");
    }

    /**
     * 查询单条流转意见
     */
    @GetMapping("/{id}")
    public Result get(@PathVariable Integer id) {
        OutgoingOpinion opinion = outgoingOpinionService.getById(id);
        return Result.success(opinion);
    }

    /**
     * 根据收文ID查询流转意见列表
     */
    @GetMapping("/list/{docId}")
    public Result listByDocId(@PathVariable Long docId) {
        List<OutgoingOpinion> opinions = outgoingOpinionService.selectByDocId(docId);
        return Result.success(opinions);
    }

    /**
     * 更新流转意见
     */
    @PutMapping("/update")
    public Result update(@RequestBody OutgoingOpinion opinion) {
        outgoingOpinionService.updateById(opinion);
        return Result.success("意见更新成功");
    }

    /**
     * 删除流转意见
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        outgoingOpinionService.deleteById(id);
        return Result.success("意见删除成功");
    }
}
