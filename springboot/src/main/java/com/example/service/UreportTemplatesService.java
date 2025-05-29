package com.example.service;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.entity.UreportTemplates;
import com.example.mapper.UreportTemplatesMapper;

/**
 * 报表模板服务实现类
 */
@Service
public class UreportTemplatesService {

    @Autowired
    private UreportTemplatesMapper ureportTemplatesMapper;

    public UreportTemplates selectUreportTemplatesByName(String name) {
        return ureportTemplatesMapper.selectByName(name);
    }

    public void deleteUreportTemplatesByName(String name) {
        ureportTemplatesMapper.deleteByName(name);
    }

    public void insertUreportTemplates(UreportTemplates template) {
        template.setCreateTime(new Date());  // 自动设置创建时间
        template.setUpdateTime(new Date());  // 自动设置更新时间
        ureportTemplatesMapper.insert(template);
    }

    public void updateUreportTemplates(UreportTemplates template) {
        template.setUpdateTime(new Date());  // 更新时自动更新时间
        ureportTemplatesMapper.update(template);
    }

    public List<UreportTemplates> selectUreportTemplatesList(UreportTemplates template) {
        return ureportTemplatesMapper.selectList(template);
    }
}
