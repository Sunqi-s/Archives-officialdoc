package com.example.service;

import com.example.common.Constants;
import com.example.entity.*;
import com.example.mapper.OutgoingOpinionMapper;
import com.example.utils.TokenUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class OutgoingOpinionService {

    @Resource
    private OutgoingDocService outgoingDocService;
    @Resource
    private FlowObjectService flowObjectService;
    @Resource
    private OutgoingOpinionMapper outgoingOpinionMapper;

    /**
     * 提交流转意见（自动填充创建人信息）
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean save(OutgoingOpinion opinion) {

        //更新状态为流转中
        OutgoingDoc outgoingDoc = new OutgoingDoc();
        outgoingDoc.setId(opinion.getDocId());
        outgoingDoc.setStatus(Constants.STATUS_PROCESSING);
        outgoingDocService.updateStatusById(outgoingDoc);

        // 根据objectId获取流转对象信息
        FlowObject flowObject = flowObjectService.getById(opinion.getObjectId());
        if (flowObject.getLevel() == 1){
            opinion.setOpinionType("主要领导意见");
        }else if (flowObject.getLevel() == 2){
            opinion.setOpinionType("分管领导意见");
        } else if (flowObject.getLevel() == 3){
            opinion.setOpinionType("部门意见");
        } else if (flowObject.getLevel() == 0){
            opinion.setOpinionType("办公室拟办意见");
        }
        opinion.setName(flowObject.getName());
        opinion.setPosition(flowObject.getPosition());
        opinion.setLevel(flowObject.getLevel());
        opinion.setCreator(TokenUtils.getCurrentUser().getId());
        opinion.setCreateDate(new Date());
        return outgoingOpinionMapper.insert(opinion) > 0;
    }

    /**
     * 查询单条意见
     */
    public OutgoingOpinion getById(Integer id) {
        return outgoingOpinionMapper.selectById(id);
    }

    /**
     * 根据收文ID查询意见列表
     */
    public List<OutgoingOpinion> selectByDocId(Long docId) {
        return outgoingOpinionMapper.selectByDocId(docId);
    }

    /**
     * 更新意见
     */
    public void updateById(OutgoingOpinion opinion) {
        outgoingOpinionMapper.updateById(opinion);
    }

    /**
     * 删除意见
     */
    public void deleteById(Integer id) {
        outgoingOpinionMapper.deleteById(id);
    }
}
