package com.example.mapper;

import com.example.entity.IncomingDoc;
import com.example.entity.IncomingOpinion;
import com.example.entity.OutgoingDoc;
import com.example.entity.OutgoingOpinion;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OutgoingDocMapper {

    /**
     * 新增发文
     */
    int insert(OutgoingDoc record);

    /**
     * 根据ID删除发文
     */
    int deleteById(@Param("id") Integer id);

    /**
     * 更新发文信息
     */
    int updateById(OutgoingDoc doc);

    /**
     * 根据ID查询收文（主表字段）
     */
    OutgoingDoc selectById(@Param("id") Integer id);

    /**
     * 根据来文编号查询收文（唯一性校验）
     */
    OutgoingDoc selectBySenderDocNo(@Param("senderDocNo") String senderDocNo);

    /**
     * 查询收文关联的流转意见（按时间升序）
     */
    List<OutgoingOpinion> selectOpinionsByDocId(@Param("docId") Integer docId);

    /**
     * 动态条件分页查询收文列表（XML实现条件）
     */
    List<OutgoingDoc> selectPageList(@Param("query") OutgoingDoc query);

    List<OutgoingDoc> selectAllWithLimitDate();


}
