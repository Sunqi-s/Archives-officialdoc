package com.example.provider;

import com.bstek.ureport.provider.report.ReportFile;
import com.bstek.ureport.provider.report.ReportProvider;
import com.example.entity.UreportTemplates;
import com.example.service.UreportTemplatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Ureport Mysql 报表存储设计
 * @author 高小川
 * @version 2024年5月9日
 *
 */
@Component
public class UreportMySQLProvider implements ReportProvider {

    // 提供者名称
    private static final String NAME = "mysql-provider";
    // 前缀，用于标识报表操作的特定前缀
    private String prefix = "mysql:";
    // 是否禁用标志
    private boolean disabled;

    // 自动注入报表模板服务接口
    @Autowired
    private UreportTemplatesService ureportTemplatesService;

    // 加载报表内容
    @Override
    public InputStream loadReport(String name) {
        // 根据报表名称获取报表模板对象
        UreportTemplates reportTemplates = ureportTemplatesService.selectUreportTemplatesByName(getNameWithoutPrefix(name));
        // 从报表模板对象中获取内容字节数组并转换为输入流返回
        byte[] content = reportTemplates.getContent().getBytes();
        return new ByteArrayInputStream(content);
    }

    // 删除报表
    @Override
    public void deleteReport(String name) {
        // 根据正确格式的名称删除报表模板
        ureportTemplatesService.deleteUreportTemplatesByName(getNameWithoutPrefix(name));
    }

    // 获取报表文件列表
    @Override
    public List<ReportFile> getReportFiles() {
        // 查询所有报表模板对象列表
        List<UreportTemplates> list = ureportTemplatesService.selectUreportTemplatesList(new UreportTemplates());
        // 构造报表文件对象列表
        List<ReportFile> reportList = new ArrayList<>();
        for (UreportTemplates templates : list) {
            reportList.add(new ReportFile(templates.getName(), templates.getUpdateTime()));
        }
        return reportList;
    }

    // 保存报表
    @Override
    public void saveReport(String name, String content) {
        // 校正报表名称
        name = getNameWithoutPrefix(name);
        // 根据报表名称查询报表模板对象
        UreportTemplates reportTemplates = ureportTemplatesService.selectUreportTemplatesByName(name);
        // 获取当前日期时间
        Date currentDate = new Date();
        byte[] cString = null;
        try {
            // 将报表内容转换为UTF-8编码的字节数组
            cString = content.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 判断报表模板对象是否为空
        if (reportTemplates == null) {
            // 如果为空，新建报表模板对象并设置属性值
            reportTemplates = new UreportTemplates();
            reportTemplates.setName(name);
            reportTemplates.setContent(new String(cString));
            reportTemplates.setCreateTime(currentDate);
            reportTemplates.setUpdateTime(currentDate);
            // 插入报表模板对象到数据库
            ureportTemplatesService.insertUreportTemplates(reportTemplates);
        } else {
            // 如果不为空，更新报表模板对象的属性值
            reportTemplates.setContent(new String(cString));
            reportTemplates.setUpdateTime(currentDate);
            // 更新报表模板对象到数据库
            ureportTemplatesService.updateUreportTemplates(reportTemplates);
        }
    }

    // 获取提供者名称
    @Override
    public String getName() {
        return NAME;
    }

    // 获取是否禁用的标志
    @Override
    public boolean disabled() {
        return disabled;
    }

    // 获取报表操作的前缀
    @Override
    public String getPrefix() {
        return prefix;
    }

    // 获取没有前缀的报表名称
    private String getNameWithoutPrefix(String name) {
        if (name.startsWith(prefix)) {
            name = name.substring(prefix.length());
        }
        return name;
    }
}
