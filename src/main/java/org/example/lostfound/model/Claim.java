package org.example.lostfound.model;

import java.util.List;

public class Claim {
    private Integer id;
    private Integer itemId;
    private String itemTitle;
    private String itemOwnerType;
    private List<ItemImage> itemImages;
    private Integer claimerId;
    private String claimerName;
    private Integer ownerId;
    private String ownerName;
    private String reason;
    private String status;
    private String adminNote;
    private Integer reviewedBy;
    private String reviewedAt;
    private String createdAt;

    public Claim() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getItemId() { return itemId; }
    public void setItemId(Integer itemId) { this.itemId = itemId; }

    public String getItemTitle() { return itemTitle; }
    public void setItemTitle(String itemTitle) { this.itemTitle = itemTitle; }

    public String getItemOwnerType() { return itemOwnerType; }
    public void setItemOwnerType(String itemOwnerType) { this.itemOwnerType = itemOwnerType; }

    public List<ItemImage> getItemImages() { return itemImages; }
    public void setItemImages(List<ItemImage> itemImages) { this.itemImages = itemImages; }

    public Integer getClaimerId() { return claimerId; }
    public void setClaimerId(Integer claimerId) { this.claimerId = claimerId; }

    public String getClaimerName() { return claimerName; }
    public void setClaimerName(String claimerName) { this.claimerName = claimerName; }

    public Integer getOwnerId() { return ownerId; }
    public void setOwnerId(Integer ownerId) { this.ownerId = ownerId; }

    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getAdminNote() { return adminNote; }
    public void setAdminNote(String adminNote) { this.adminNote = adminNote; }

    public Integer getReviewedBy() { return reviewedBy; }
    public void setReviewedBy(Integer reviewedBy) { this.reviewedBy = reviewedBy; }

    public String getReviewedAt() { return reviewedAt; }
    public void setReviewedAt(String reviewedAt) { this.reviewedAt = reviewedAt; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
