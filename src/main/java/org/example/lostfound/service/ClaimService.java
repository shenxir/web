package org.example.lostfound.service;

import org.example.lostfound.mapper.ClaimMapper;
import org.example.lostfound.mapper.ImageMapper;
import org.example.lostfound.model.Claim;
import org.example.lostfound.model.ItemImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ClaimService {

    @Autowired
    private ClaimMapper claimMapper;

    @Autowired
    private ItemService itemService;

    @Autowired
    private ImageMapper imageMapper;

    /**
     * 查询认领详情
     */
    public Claim getClaimById(Integer claimId) {
        Claim claim = claimMapper.getClaimById(claimId);
        if (claim != null) {
            loadItemImages(claim);
        }
        return claim;
    }

    /**
     * 查询所有认领记录
     */
    public List<Claim> getAllClaims(String status, Integer itemId) {
        List<Claim> claims = claimMapper.getAllClaims(status, itemId);
        loadItemImagesForClaims(claims);
        return claims;
    }

    /**
     * 查询用户的认领记录
     */
    public List<Claim> getUserClaims(Integer userId) {
        List<Claim> claims = claimMapper.getUserClaims(userId);
        loadItemImagesForClaims(claims);
        return claims;
    }

    /**
     * 查询物品的认领记录
     */
    public List<Claim> getItemClaims(Integer itemId) {
        List<Claim> claims = claimMapper.getItemClaims(itemId);
        loadItemImagesForClaims(claims);
        return claims;
    }

    /**
     * 加载单个认领的物品图片
     */
    private void loadItemImages(Claim claim) {
        if (claim != null && claim.getItemId() != null) {
            List<ItemImage> images = imageMapper.getImagesByItemId(claim.getItemId());
            claim.setItemImages(images);
        }
    }

    /**
     * 批量加载认领的物品图片
     */
    private void loadItemImagesForClaims(List<Claim> claims) {
        if (claims != null && !claims.isEmpty()) {
            for (Claim claim : claims) {
                loadItemImages(claim);
            }
        }
    }

    /**
     * 查询待审核认领
     */
    public List<Claim> getPendingClaims() {
        return claimMapper.getPendingClaims();
    }

    /**
     * 提交认领
     */
    @Transactional
    public Map<String, Object> submitClaim(Claim claim) {
        Map<String, Object> result = new HashMap<>();

        // 检查物品是否可被认领
        if (!itemService.checkItemClaimable(claim.getItemId())) {
            result.put("success", false);
            result.put("message", "该物品当前不可认领");
            return result;
        }

        // 检查是否认领自己的物品
        org.example.lostfound.model.Item item = itemService.getItemById(claim.getItemId());
        if (item != null && item.getUserId().equals(claim.getClaimerId())) {
            result.put("success", false);
            result.put("message", "不能认领自己发布的物品");
            return result;
        }

        // 检查用户是否已认领过
        if (itemService.checkUserClaimed(claim.getItemId(), claim.getClaimerId())) {
            result.put("success", false);
            result.put("message", "您已提交过认领申请，请勿重复提交");
            return result;
        }

        // 检查重复认领
        int duplicate = claimMapper.checkDuplicateClaim(claim.getItemId(), claim.getClaimerId());
        if (duplicate > 0) {
            result.put("success", false);
            result.put("message", "您已提交过认领申请，请勿重复提交");
            return result;
        }

        // 插入认领
        claim.setStatus("pending");
        claimMapper.insertClaim(claim);

        result.put("success", true);
        result.put("message", "认领申请已提交");
        result.put("claim", claim);
        return result;
    }

    /**
     * 审核认领
     */
    @Transactional
    public Map<String, Object> reviewClaim(Integer claimId, String status, String adminNote, Integer reviewedBy) {
        Map<String, Object> result = new HashMap<>();

        Claim claim = claimMapper.getClaimById(claimId);
        if (claim == null) {
            result.put("success", false);
            result.put("message", "认领记录不存在");
            return result;
        }

        // 审核认领
        claimMapper.reviewClaim(claimId, status, adminNote, reviewedBy);

        // 如果审核通过，更新物品状态
        if ("approved".equals(status)) {
            claimMapper.updateItemStatusOnApproved(claim.getItemId());
        }

        result.put("success", true);
        result.put("message", "审核完成");
        return result;
    }

    /**
     * 删除认领
     */
    public boolean deleteClaim(Integer claimId) {
        return claimMapper.deleteClaim(claimId) > 0;
    }

    /**
     * 自动过期处理
     */
    public int autoExpireClaims() {
        return claimMapper.autoExpireClaims();
    }

    /**
     * 查询认领统计信息
     */
    public Map<String, Object> getClaimStats() {
        return claimMapper.getClaimStats();
    }

    /**
     * 查询用户待审核认领数量
     */
    public int getUserPendingClaimCount(Integer userId) {
        return claimMapper.getUserPendingClaimCount(userId);
    }
}
