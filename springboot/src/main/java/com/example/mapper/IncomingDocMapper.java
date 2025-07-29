package com.example.mapper;

import com.example.entity.IncomingDoc;
import com.example.entity.IncomingOpinion;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 收文管理 Mapper 接口（完整 CRUD + 动态查询）
 */
public interface IncomingDocMapper {

    /**
     * 新增收文
     */
    int insert(IncomingDoc doc);

    /**
     * 根据ID删除收文
     */
    int deleteById(@Param("id") Integer id);

    /**
     * 更新收文信息
     */
    int updateById(IncomingDoc doc);

    /**
     * 根据ID查询收文（主表字段）
     */
    IncomingDoc selectById(@Param("id") Integer id);

    /**
     * 根据来文编号查询收文（唯一性校验）
     */
    IncomingDoc selectBySenderDocNo(@Param("senderDocNo") String senderDocNo);

    /**
     * 查询收文关联的流转意见（按时间升序）
     */
    List<IncomingOpinion> selectOpinionsByDocId(@Param("docId") Integer docId);

    /**
     * 动态条件分页查询收文列表（XML实现条件）
     */
    List<IncomingDoc> selectPageList(@Param("query") IncomingDoc query);

    List<IncomingDoc> selectAllWithLimitDate();
}
