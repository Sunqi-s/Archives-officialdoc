package com.example.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 收文审批意见实体类
 */
public class IncomingOpinion implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;                // 主键ID
    private Integer docId;             // 关联收文表ID（incoming_doc.id）
    private Integer objectId;          // 关联流转对象ID（flow_object.id）
    private String name;                // 领导姓名
    private String position;            // 职务
    private Integer level;              // 级别
    private String opinion;            // 手写意见内容
    private Integer nextObjectId;      // 下一流转领导ID（无则空）
    private String opinionType;        // 意见类型（办公室拟办意见/主要领导意见等）
    private Integer creator;           // 创建者ID
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;           // 创建时间
    private Integer updater;           // 更新者ID
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateDate;           // 更新时间

    // 完整getter和setter
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getDocId() { return docId; }
    public void setDocId(Integer docId) { this.docId = docId; }
    public Integer getObjectId() { return objectId; }
    public void setObjectId(Integer objectId) { this.objectId = objectId; }
    public String getOpinion() { return opinion; }
    public void setOpinion(String opinion) { this.opinion = opinion; }
    public Integer getNextObjectId() { return nextObjectId; }
    public void setNextObjectId(Integer nextObjectId) { this.nextObjectId = nextObjectId; }
    public String getOpinionType() { return opinionType; }
    public void setOpinionType(String opinionType) { this.opinionType = opinionType; }
    public Integer getCreator() { return creator; }
    public void setCreator(Integer creator) { this.creator = creator; }
    public Date getCreateDate() { return createDate; }
    public void setCreateDate(Date createDate) { this.createDate = createDate; }
    public Integer getUpdater() { return updater; }
    public void setUpdater(Integer updater) { this.updater = updater; }
    public Date getUpdateDate() { return updateDate; }
    public void setUpdateDate(Date updateDate) { this.updateDate = updateDate; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    public Integer getLevel() { return level; }
    public void setLevel(Integer level) { this.level = level; }
}
