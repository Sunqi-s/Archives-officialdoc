package com.example.entity;

import java.io.Serializable;
import java.util.Date;

public class ReminderSettings implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer userId;
    private Integer remindBeforeValue;
    private Date updatedTime;
    private Date createdTime;
    private Integer status;
    private String name;
    private RemindUnit remindBeforeUnit;
    private String limitType;
    private String docType;

    // 枚举类型定义时间单位
    public enum RemindUnit {
        minutes("分钟"),
        hours("小时"),
        days("天"),
        months("月");

        private final String displayName;

        RemindUnit(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public Integer getRemindBeforeValue() { return remindBeforeValue; }
    public void setRemindBeforeValue(Integer remindBeforeValue) { this.remindBeforeValue = remindBeforeValue; }
    public Date getUpdatedTime() { return updatedTime; }
    public void setUpdatedTime(Date updatedTime) { this.updatedTime = updatedTime; }
    public Date getCreatedTime() { return createdTime; }
    public void setCreatedTime(Date createdTime) { this.createdTime = createdTime; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public RemindUnit getRemindBeforeUnit() { return remindBeforeUnit; }
    public void setRemindBeforeUnit(RemindUnit remindBeforeUnit) { this.remindBeforeUnit = remindBeforeUnit; }
    public String getLimitType() { return limitType; }
    public void setLimitType(String limitType) { this.limitType = limitType; }
    public String getDocType() { return docType; }
    public void setDocType(String docType) { this.docType = docType; }
}
