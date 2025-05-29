package com.example.entity;

import java.io.Serializable;
import java.util.Date;

public class FlowObject implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;          // 修改为Integer类型
    private String name;         // 领导姓名
    private String position;     // 职务
    private Integer level;       // 级别（1-主任，2-副主任，3-部门）
    private Integer creator;     // 创建者ID（Integer类型）
    private Date createDate;     // 创建时间
    private Integer updater;     // 更新者ID（Integer类型）
    private Date updateDate;     // 更新时间

    // 完整getter和setter
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    public Integer getLevel() { return level; }
    public void setLevel(Integer level) { this.level = level; }
    public Integer getCreator() { return creator; }
    public void setCreator(Integer creator) { this.creator = creator; }
    public Date getCreateDate() { return createDate; }
    public void setCreateDate(Date createDate) { this.createDate = createDate; }
    public Integer getUpdater() { return updater; }
    public void setUpdater(Integer updater) { this.updater = updater; }
    public Date getUpdateDate() { return updateDate; }
    public void setUpdateDate(Date updateDate) { this.updateDate = updateDate; }
}
