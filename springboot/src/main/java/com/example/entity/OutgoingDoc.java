package com.example.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 发文信息实体类
 */
public class OutgoingDoc implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;                   // 主键ID（bigint类型对应Long）
    private String senderOrg;          // 发文单位
    private String title;              // 文件标题
    private String secretLevel;        // 密级（普通/秘密/机密/绝密），默认普通
    private String archiveStatus;      // 存档状态（处理中/存档完成），默认处理中
    private Date archiveDate;          // 存档日期（存档完成时记录）
    private Integer fileNo;            // 件号（4位纯数字，按年重置）
    private String recordNo;           // 档号（扩展字段，存储年度+件号等组合信息）
    private Integer creator;           // 创建者ID
    private Date createDate;           // 创建时间
    private Integer updater;           // 更新者ID
    private Date updateDate;           // 更新时间
    private Date limitDate;
    private Integer pageCount;         // 页数
    private String processType;        // 处理类型（审批/签发/归档）
    private String handlingOrg;        // 办理单位
    private Integer attachmentId;     // 附件ID列表（逗号分隔）
    private Integer SecretType;       // 密级类型（0：普通，1：秘密，2：机密，3：绝密）
    private String senderDocNo;        // 发文号
    private Integer status;            // 状态
    private Boolean feedbackRequired;  // 限时反馈（勾选框：0-否，1-是），默认否

    List<OutgoingOpinion> opinions;  // 审批意见（前端传递，非数据库字段）
    private String opinion; // 拟办意见（前端传递，非数据库字段）
    private Attachment file; // 附件列表（前端传递，非数据库字段）

    private String objectName; // 审批对象名称（前端传递，非数据库字段）

    // 完整getter和setter
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getSenderOrg() { return senderOrg; }
    public void setSenderOrg(String senderOrg) { this.senderOrg = senderOrg; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getSecretLevel() { return secretLevel; }
    public void setSecretLevel(String secretLevel) { this.secretLevel = secretLevel; }
    public String getArchiveStatus() { return archiveStatus; }
    public void setArchiveStatus(String archiveStatus) { this.archiveStatus = archiveStatus; }
    public Date getArchiveDate() { return archiveDate; }
    public void setArchiveDate(Date archiveDate) { this.archiveDate = archiveDate; }
    public Integer getFileNo() { return fileNo; }
    public void setFileNo(Integer fileNo) { this.fileNo = fileNo; }
    public String getRecordNo() { return recordNo; }
    public void setRecordNo(String recordNo) { this.recordNo = recordNo; }
    public Integer getCreator() { return creator; }
    public void setCreator(Integer creator) { this.creator = creator; }
    public Date getCreateDate() { return createDate; }
    public void setCreateDate(Date createDate) { this.createDate = createDate; }
    public Integer getUpdater() { return updater; }
    public void setUpdater(Integer updater) { this.updater = updater; }
    public Date getUpdateDate() { return updateDate; }
    public void setUpdateDate(Date updateDate) { this.updateDate = updateDate; }
    public Date getLimitDate() { return limitDate; }
    public void setLimitDate(Date limitDate) { this.limitDate = limitDate; }
    public Integer getPageCount() { return pageCount; }
    public void setPageCount(Integer pageCount) { this.pageCount = pageCount; }
    public String getProcessType() { return processType; }
    public void setProcessType(String processType) { this.processType = processType; }
    public String getHandlingOrg() { return handlingOrg; }
    public void setHandlingOrg(String handlingOrg) { this.handlingOrg = handlingOrg; }
    public List<OutgoingOpinion> getOpinions() {
        return opinions;
    }
    public void setOpinions(List<OutgoingOpinion> opinions) {
        this.opinions = opinions;
    }
    public String getOpinion() {
        return opinion;
    }
    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }
    public Integer getAttachmentId() { return attachmentId; }
    public void setAttachmentId(Integer attachmentId) { this.attachmentId = attachmentId; }
    public Integer getSecretType() { return SecretType; }
    public void setSecretType(Integer secretType) { SecretType = secretType; }
    public String getSenderDocNo() { return senderDocNo; }
    public void setSenderDocNo(String senderDocNo) { this.senderDocNo = senderDocNo; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Boolean getFeedbackRequired() { return feedbackRequired; }
    public void setFeedbackRequired(Boolean feedbackRequired) { this.feedbackRequired = feedbackRequired; }
    public Attachment getFile() { return file; }
    public void setFile(Attachment file) { this.file = file; }
    public String getObjectName() { return objectName; }
    public void setObjectName(String objectName) { this.objectName = objectName; }
}
