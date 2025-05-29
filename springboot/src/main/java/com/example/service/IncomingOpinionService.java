package com.example.service;

import com.example.common.Constants;
import com.example.entity.FlowObject;
import com.example.entity.IncomingDoc;
import com.example.entity.IncomingOpinion;
import com.example.mapper.IncomingOpinionMapper;
import com.example.utils.TokenUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class IncomingOpinionService {
    @Resource
    private IncomingOpinionMapper incomingOpinionMapper;

    @Resource
    private FlowObjectService flowObjectService;

    @Resource
    private IncomingDocService incomingDocService;

    /**
     * 提交流转意见（自动填充创建人信息）
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean save(IncomingOpinion opinion) {

        //更新状态为流转中
        IncomingDoc incomingDoc = new IncomingDoc();
        incomingDoc.setId(opinion.getDocId());
        incomingDoc.setStatus(Constants.STATUS_PROCESSING);
        incomingDocService.updateStatusById(incomingDoc);

        // 根据objectId获取流转对象信息
        FlowObject flowObject = flowObjectService.getById(opinion.getObjectId());
        opinion.setName(flowObject.getName());
        opinion.setPosition(flowObject.getPosition());
        opinion.setLevel(flowObject.getLevel());
        opinion.setCreator(TokenUtils.getCurrentUser().getId());
        opinion.setCreateDate(new Date());
        return incomingOpinionMapper.insert(opinion) > 0;
    }

    /**
     * 查询单条意见
     */
    public IncomingOpinion getById(Integer id) {
        return incomingOpinionMapper.selectById(id);
    }

    /**
     * 根据收文ID查询意见列表
     */
    public List<IncomingOpinion> selectByDocId(Long docId) {
        return incomingOpinionMapper.selectByDocId(docId);
    }

    /**
     * 更新意见
     */
    public void updateById(IncomingOpinion opinion) {
        incomingOpinionMapper.updateById(opinion);
    }

    /**
     * 删除意见
     */
    public void deleteById(Integer id) {
        incomingOpinionMapper.deleteById(id);
    }
}
