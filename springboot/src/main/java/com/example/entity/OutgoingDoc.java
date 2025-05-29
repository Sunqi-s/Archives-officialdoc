package com.example.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 发文信息实体类
 */
public class OutgoingDoc implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;                   // 主键ID（bigint类型对应Long）
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

    // 完整getter和setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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
}
