package com.example.mapper;


import com.example.entity.IncomingOpinion;
import com.example.entity.OutgoingOpinion;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OutgoingOpinionMapper {

    int insert(OutgoingOpinion opinion);

    OutgoingOpinion selectById(Integer id);

    List<OutgoingOpinion> selectByDocId(@Param("docId") Long docId);

    int updateById(OutgoingOpinion opinion);

    int deleteById(Integer id);


}
