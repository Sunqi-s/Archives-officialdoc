package com.example.mapper;

import com.example.entity.IncomingOpinion;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IncomingOpinionMapper {
    int insert(IncomingOpinion opinion);

    IncomingOpinion selectById(Integer id);

    List<IncomingOpinion> selectByDocId(@Param("docId") Long docId);

    int updateById(IncomingOpinion opinion);

    int deleteById(Integer id);
}
