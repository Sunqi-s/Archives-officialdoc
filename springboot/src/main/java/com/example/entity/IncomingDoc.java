package com.example.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 收文信息实体类
 */
public class IncomingDoc implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;                // 主键ID
    private String docType;            // 来文类型（下拉菜单：函/通知/请示等）
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date receiveDate;          // 来文日期（带日历控件:默认当天）
    private String contactPerson;      // 联系人（输入框）
    private String senderOrg;          // 来文单位（输入框）
    private String contactPhone;       // 联系电话（输入框）
    private String secretLevel;        // 密级（普通/秘密/机密/绝密），默认普通
    private Integer annual;            // 年度（输入框，如2025）
    private Integer copies;            // 份数（输入框，填写文件份数）
    private String urgencyLevel;       // 紧急程度（普通/紧急/特急），默认普通
    private Integer pageCount;         // 文件页数（输入框）
    private String title;              // 文件标题（输入框）
    private String documentType;       // 文种/期号（输入框，如“函〔2025〕1号”）
    private String boxNo;              // 盒号（输入框）
    private Integer fileNo;            // 件号（4位纯数字，按年重置）
    private String senderDocNo;        // 来文编号（输入框，如来文单位自编编号）
    private Boolean feedbackRequired;  // 限时反馈（勾选框：0-否，1-是），默认否
    private String processType;        // 处理类型（下拉菜单：阅办/承办/存档等）
    private String archiveNo;          // 归档号
    private Date archiveDate;          // 归档日期
    private String handlingOrg;        // 办件单位
    private Integer attachmentId;      // 附件ID（关联attachment表）文件内容（扫描版）
    private Integer creator;           // 创建者ID
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;           // 创建时间
    private Integer updater;           // 更新者ID
    private Date updateDate;           // 更新时间
    private Integer secretType;  // 密件类型（0-普通件，1-密件）
    private Integer status;      // 状态（0-待审核，1-流转中，2-已存档）
    private Date limitDate;

    List<IncomingOpinion> opinions;  // 审批意见（前端传递，非数据库字段）
    private String opinion; // 拟办意见（前端传递，非数据库字段）

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public Date getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(Date receiveDate) {
        this.receiveDate = receiveDate;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getSenderOrg() {
        return senderOrg;
    }

    public void setSenderOrg(String senderOrg) {
        this.senderOrg = senderOrg;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getSecretLevel() {
        return secretLevel;
    }

    public void setSecretLevel(String secretLevel) {
        this.secretLevel = secretLevel;
    }

    public Integer getAnnual() {
        return annual;
    }

    public void setAnnual(Integer annual) {
        this.annual = annual;
    }

    public Integer getCopies() {
        return copies;
    }

    public void setCopies(Integer copies) {
        this.copies = copies;
    }

    public String getUrgencyLevel() {
        return urgencyLevel;
    }

    public void setUrgencyLevel(String urgencyLevel) {
        this.urgencyLevel = urgencyLevel;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getBoxNo() {
        return boxNo;
    }

    public void setBoxNo(String boxNo) {
        this.boxNo = boxNo;
    }

    public Integer getFileNo() {
        return fileNo;
    }

    public void setFileNo(Integer fileNo) {
        this.fileNo = fileNo;
    }

    public String getSenderDocNo() {
        return senderDocNo;
    }

    public void setSenderDocNo(String senderDocNo) {
        this.senderDocNo = senderDocNo;
    }

    public Boolean getFeedbackRequired() {
        return feedbackRequired;
    }

    public void setFeedbackRequired(Boolean feedbackRequired) {
        this.feedbackRequired = feedbackRequired;
    }

    public String getProcessType() {
        return processType;
    }

    public void setProcessType(String processType) {
        this.processType = processType;
    }

    public String getArchiveNo() {
        return archiveNo;
    }

    public void setArchiveNo(String archiveNo) {
        this.archiveNo = archiveNo;
    }

    public String getHandlingOrg() {
        return handlingOrg;
    }

    public void setHandlingOrg(String handlingOrg) {
        this.handlingOrg = handlingOrg;
    }

    public Integer getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(Integer attachmentId) {
        this.attachmentId = attachmentId;
    }

    public Integer getCreator() {
        return creator;
    }

    public void setCreator(Integer creator) {
        this.creator = creator;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getUpdater() {
        return updater;
    }

    public void setUpdater(Integer updater) {
        this.updater = updater;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public List<IncomingOpinion> getOpinions() {
        return opinions;
    }

    public void setOpinions(List<IncomingOpinion> opinions) {
        this.opinions = opinions;
    }

    public Date getArchiveDate() {
        return archiveDate;
    }

    public void setArchiveDate(Date archiveDate) {
        this.archiveDate = archiveDate;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public Integer getSecretType() {
        return secretType;
    }

    public void setSecretType(Integer secretType) {
        this.secretType = secretType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getLimitDate() { return limitDate; }

    public void setLimitDate(Date limitDate) { this.limitDate = limitDate; }
}
