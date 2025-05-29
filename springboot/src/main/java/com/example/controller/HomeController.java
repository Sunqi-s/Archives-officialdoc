package com.example.controller;

import com.example.common.Result;
import com.example.entity.IncomingDoc;
import com.example.service.HomeService;
import com.example.service.IncomingDocService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/home")
public class HomeController {

    @Resource
    private HomeService homeService;

    @Resource
    private IncomingDocService incomingDocService;

    /**
     * 获取首页统计数据
     */
    @GetMapping("/statistics")
    public Result getStatistics() {
        Map<String, Object> statistics = homeService.getHomeStatistics();
        return Result.success(statistics);
    }


    /**
     * 获取待审批文件列表
     */
    @GetMapping("/selectPending")
    public Result selectPage(IncomingDoc incomingDoc,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<IncomingDoc> page = incomingDocService.selectPage(incomingDoc, pageNum, pageSize);
        return Result.success(page);
    }
}
