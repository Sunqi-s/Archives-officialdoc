package com.example.mapper;

import com.example.entity.DocType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DocTypeMapper {
    int insert(DocType docType);
    int deleteById(Integer id);
    int updateById(DocType docType);
    DocType selectById(Integer id);
    List<DocType> selectPageList(@Param("name") String name);
    List<DocType> selectAll();
}
