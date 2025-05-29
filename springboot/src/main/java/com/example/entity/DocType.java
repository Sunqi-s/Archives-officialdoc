package com.example.entity;

import java.io.Serializable;
import java.util.Date;

public class DocType implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;          // 主键ID
    private String name;         // 类型名称
    private Integer creator;     // 创建者ID
    private Date createDate;     // 创建时间
    private Integer updater;     //更新者ID
    private Date updateDate;     //更新时间

    // 完整getter和setter
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getCreator() { return creator; }
    public void setCreator(Integer creator) { this.creator = creator; }
    public Date getCreateDate() { return createDate; }
    public void setCreateDate(Date createDate) { this.createDate = createDate; }
    public Integer getUpdater() { return updater; }
    public void setUpdater(Integer updater) { this.updater = updater; }
    public Date getUpdateDate() { return updateDate; }
    public void setUpdateDate(Date updateDate) { this.updateDate = updateDate; }
}
