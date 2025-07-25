package com.example.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 发文审批意见实体类
 */
public class OutgoingOpinion implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer docId;          // 关联收文ID
    private Integer objectId;       // 当前流转对象ID
    private String opinion;      // 意见内容
    private Integer nextObjectId;   // 下一流转对象ID（可选）
    private Integer creator;        // 创建人
    private Date createDate;
    private Integer updater;
    private Date updateDate;
    private String opinionType;
    private String name;
    private String position;
    private Integer level;

    // 扩展字段（非数据库字段）
    private String creatorName;  // 创建人姓名
    private String objectName;   // 当前流转对象姓名
    private String nextObjectName; // 下一流转对象姓名

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDocId() {
        return docId;
    }

    public void setDocId(Integer docId) {
        this.docId = docId;
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public Integer getNextObjectId() {
        return nextObjectId;
    }

    public void setNextObjectId(Integer nextObjectId) {
        this.nextObjectId = nextObjectId;
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

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getNextObjectName() {
        return nextObjectName;
    }

    public void setNextObjectName(String nextObjectName) {
        this.nextObjectName = nextObjectName;
    }

    public String getOpinionType() {
        return opinionType;
    }
    public void setOpinionType(String opinionType) {
        this.opinionType = opinionType;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPosition() {
        return position;
    }
    public void setPosition(String position) {
        this.position = position;
    }
    public Integer getLevel() {
        return level;
    }
    public void setLevel(Integer level) {
        this.level = level;
    }
}
