package org.example.lostfound.model;

public class Announcement {
    private Integer id;
    private String title;
    private String content;
    private String type;
    private Integer status;
    private Integer publishBy;
    private String publishByName;
    private String createdAt;

    public Announcement() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public Integer getPublishBy() { return publishBy; }
    public void setPublishBy(Integer publishBy) { this.publishBy = publishBy; }

    public String getPublishByName() { return publishByName; }
    public void setPublishByName(String publishByName) { this.publishByName = publishByName; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
