package com.example.mapper;

import com.example.entity.FlowObject;
import com.example.entity.IncomingDoc;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FlowObjectMapper {

    int insert(FlowObject object);

    int deleteById(Integer id);

    int updateById(FlowObject object);

    FlowObject selectById(Integer id);

    List<FlowObject> selectPageList(FlowObject flowObject);

    List<FlowObject> selectAll();

    List<FlowObject> selectByLevel(@Param("level") Integer level);
}
