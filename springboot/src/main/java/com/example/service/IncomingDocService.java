package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.Constants;
import com.example.entity.Attachment;
import com.example.entity.FlowObject;
import com.example.entity.IncomingDoc;
import com.example.entity.IncomingOpinion;
import com.example.exception.CustomException;
import com.example.mapper.IncomingDocMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 收文业务处理（与IncomingDocMapper完全一致）
 */
@Service
public class IncomingDocService {

    private static final Logger log = LoggerFactory.getLogger(IncomingDocService.class);
    @Resource
    private IncomingDocMapper incomingDocMapper;
    @Resource
    private FlowObjectService flowObjectService; // 流转对象服务
    @Resource
    private IncomingOpinionService incomingOpinionService;
    @Resource
    private AttachmentService attachmentService; // 附件服务


    /**
     * 新增收文
     */
    @Transactional(rollbackFor = Exception.class)
    public void add(IncomingDoc doc) {
        // 必填参数校验
        if (ObjectUtil.isEmpty(doc.getTitle())) {
            throw new CustomException("5011", "文件标题不能为空");
        }
        if (ObjectUtil.isEmpty(doc.getSenderOrg())) {
            throw new CustomException("5012", "来文单位不能为空");
        }

        // 来文编号唯一性校验
        if (ObjectUtil.isNotEmpty(doc.getSenderDocNo())) {
            IncomingDoc existDoc = incomingDocMapper.selectBySenderDocNo(doc.getSenderDocNo());
            if (existDoc != null) {
                throw new CustomException("5014", "来文编号已存在");
            }
        }

        // 处理附件 - 增加空值检查
        Integer attachmentId = null;
        Attachment file = doc.getFile();
        if (file != null && file.getFilePath() != null) {
            Attachment attachment = new Attachment();
            attachment.setFileName(file.getFileName());
            attachment.setFileType("收文附件");
            attachment.setFilePath(file.getFilePath());
            attachment.setCreateDate(new Date());
            attachment.setCreator(TokenUtils.getCurrentUser().getId());

            // 保存附件信息
            attachmentService.save(attachment);
            attachmentId = attachment.getId();
        }
        // 自动填充系统字段
        doc.setCreator(TokenUtils.getCurrentUser().getId());
        doc.setCreateDate(new Date());
        doc.setProcessType(Constants.DEFAULT_PROCESS_TYPE);
        doc.setFeedbackRequired(doc.getFeedbackRequired() != null ? doc.getFeedbackRequired() : false);
        doc.setSecretType(doc.getSecretType() != null ? doc.getSecretType() : 0);  // 默认普通件
        doc.setStatus(doc.getStatus() != null ? doc.getStatus() : Constants.STATUS_PENDING);  // 默认待审核
        doc.setAttachmentId(attachmentId);

        incomingDocMapper.insert(doc);

        Integer docId = doc.getId(); // 获取收文id

        // 查询level=0的办公室流转对象
        List<FlowObject> officeObjects = flowObjectService.selectByLevel(0);
        FlowObject office = officeObjects.get(0); // 只有一个办公室

        // 构造拟办意见对象
        IncomingOpinion opinion = new IncomingOpinion();
        opinion.setDocId(docId);                // 收文id
        opinion.setObjectId(office.getId());    // 办公室流转对象id
        opinion.setName(office.getName());
        opinion.setPosition(office.getPosition());
        opinion.setLevel(office.getLevel());
        opinion.setOpinion(doc.getOpinion());    // 前端输入的拟办意见
        opinion.setNextObjectId(null);           // 无下一流转对象
        opinion.setOpinionType("拟办意见");       // 意见类型
        opinion.setCreator(TokenUtils.getCurrentUser().getId()); // 当前用户id
        opinion.setCreateDate(new Date());       // 创建时间

        // 保存拟办意见
        incomingOpinionService.save(opinion);
    }

    /**
     * 根据ID删除收文
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Integer id) {
        IncomingDoc doc = incomingDocMapper.selectById(id); // 检查文档存在性
        incomingDocMapper.deleteById(id);
    }

    /**
     * 批量删除收文
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            deleteById(id);
        }
    }

    /**
     * 更新收文信息（禁止修改已存档文档）
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateById(IncomingDoc doc) {
        IncomingDoc existDoc = incomingDocMapper.selectById(doc.getId());

        if (Constants.STATUS_ARCHIVED.equals(existDoc.getStatus())) {
            throw new CustomException("5015", "已存档文档不可修改");
        }

        // 拷贝更新字段（避免直接操作参数对象，防止脏数据）
        existDoc.setDocType(doc.getDocType());
        existDoc.setReceiveDate(doc.getReceiveDate());
        existDoc.setContactPerson(doc.getContactPerson());
        existDoc.setSenderOrg(doc.getSenderOrg());
        existDoc.setContactPhone(doc.getContactPhone());
        existDoc.setSecretLevel(doc.getSecretLevel());
        existDoc.setAnnual(doc.getAnnual());
        existDoc.setCopies(doc.getCopies());
        existDoc.setUrgencyLevel(doc.getUrgencyLevel());
        existDoc.setPageCount(doc.getPageCount());
        existDoc.setTitle(doc.getTitle());
        existDoc.setDocumentType(doc.getDocumentType());
        existDoc.setBoxNo(doc.getBoxNo());
        existDoc.setFileNo(doc.getFileNo());
        existDoc.setSenderDocNo(doc.getSenderDocNo());
        existDoc.setFeedbackRequired(doc.getFeedbackRequired());
        existDoc.setProcessType(doc.getProcessType());
        existDoc.setArchiveNo(doc.getArchiveNo());
        existDoc.setHandlingOrg(doc.getHandlingOrg());
        existDoc.setAttachmentId(doc.getAttachmentId());
        existDoc.setUpdater(TokenUtils.getCurrentUser().getId());
        existDoc.setUpdateDate(new Date());
        existDoc.setSecretType(doc.getSecretType());
        existDoc.setStatus(doc.getStatus());
        existDoc.setLimitDate(doc.getLimitDate());

        incomingDocMapper.updateById(existDoc);
    }

    public void updateStatusById(IncomingDoc doc) {
        IncomingDoc existDoc = incomingDocMapper.selectById(doc.getId());

        if (Constants.STATUS_ARCHIVED.equals(existDoc.getStatus())) {
            throw new CustomException("5015", "已存档文档不可修改");
        }

        // 拷贝更新字段（避免直接操作参数对象，防止脏数据）
        existDoc.setStatus(1);

        incomingDocMapper.updateById(existDoc);
    }


    /**
     * 根据ID查询收文（含流转意见）
     */
    public IncomingDoc getWithOpinions(Integer id) {
        IncomingDoc doc = incomingDocMapper.selectById(id);
        if (doc == null) {
            throw new CustomException("5016", "收文不存在");
        }
        // 关联查询流转意见（调用Mapper的selectOpinionsByDocId）
        List<IncomingOpinion> opinions = incomingDocMapper.selectOpinionsByDocId(id);
        doc.setOpinions(opinions);
        return doc;
    }

    /**
     * 分页查询收文列表（使用Mapper的XML动态查询）
     */
    public PageInfo<IncomingDoc> selectPage(IncomingDoc query, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize); // 启动分页
        // 直接调用Mapper的selectPageList（XML实现动态条件）
        List<IncomingDoc> list = incomingDocMapper.selectPageList(query);
        return PageInfo.of(list);
    }

    /**
     * 收文存档
     */
    @Transactional(rollbackFor = Exception.class)
    public void archive(Integer id) {
        IncomingDoc doc = incomingDocMapper.selectById(id);

        if (Constants.PROCESS_TYPE_ARCHIVED.equals(doc.getProcessType())) {
            throw new CustomException("5016", "文档已存档，无需重复操作");
        }
        doc.setStatus(2);
        doc.setProcessType(Constants.PROCESS_TYPE_ARCHIVED);
        doc.setArchiveDate(new Date());
        doc.setUpdater(TokenUtils.getCurrentUser().getId());
        incomingDocMapper.updateById(doc);
    }

    /**
     * 获取所有流转对象
     */
    public List<FlowObject> getAllFlowObjects() {
        List<FlowObject> list = flowObjectService.selectAll();
        if (ObjectUtil.isEmpty(list)) {
            throw new CustomException("5017", "未配置流转对象");
        }
        return list;
    }
}
