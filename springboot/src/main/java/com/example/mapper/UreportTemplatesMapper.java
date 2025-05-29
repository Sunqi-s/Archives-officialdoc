package com.example.mapper;

import java.util.List;
import com.example.entity.UreportTemplates;

/**
 * 报表模板 Mapper 接口
 */
public interface UreportTemplatesMapper {

    UreportTemplates selectByName(String name);

    int deleteByName(String name);

    int insert(UreportTemplates template);

    int update(UreportTemplates template);

    List<UreportTemplates> selectList(UreportTemplates template);
}
