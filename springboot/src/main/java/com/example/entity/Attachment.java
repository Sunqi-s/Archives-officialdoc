package com.example.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 附件存储实体类
 */
public class Attachment implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;                // 主键ID
    private String filePath;           // 附件存储路径（如/Uploads/Doc/202505/incoming_1.pdf）
    private String fileType;           // 类型（收文/发文）
    private Integer creator;           // 创建者ID
    private Date createDate;           // 创建时间
    private Integer updater;           // 更新者ID
    private Date updateDate;           // 更新时间

    // 完整getter和setter
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }
    public Integer getCreator() { return creator; }
    public void setCreator(Integer creator) { this.creator = creator; }
    public Date getCreateDate() { return createDate; }
    public void setCreateDate(Date createDate) { this.createDate = createDate; }
    public Integer getUpdater() { return updater; }
    public void setUpdater(Integer updater) { this.updater = updater; }
    public Date getUpdateDate() { return updateDate; }
    public void setUpdateDate(Date updateDate) { this.updateDate = updateDate; }
}
