package com.example.entity;

import java.util.Date;

public class UreportTemplates {
    private Integer id;
    private String name;
    private String content;
    private Date createTime;
    private Date updateTime;
    private String createBy;  // 新增
    private String updateBy;  // 新增

    // 以下为 getter 和 setter 方法
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public Date getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    public String getCreateBy() {  // 新增
        return createBy;
    }
    public void setCreateBy(String createBy) {  // 新增
        this.createBy = createBy;
    }
    public String getUpdateBy() {  // 新增
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {  // 新增
        this.updateBy = updateBy;
    }
}
