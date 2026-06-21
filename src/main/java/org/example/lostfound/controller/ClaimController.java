package org.example.lostfound.controller;

import org.example.lostfound.model.Claim;
import org.example.lostfound.service.ClaimService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/claims")
public class ClaimController {

    @Autowired
    private ClaimService claimService;

    /**
     * 查询所有认领记录
     */
    @GetMapping
    public List<Claim> getAllClaims(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer itemId) {
        claimService.autoExpireClaims();
        return claimService.getAllClaims(status, itemId);
    }

    /**
     * 查询用户的认领记录
     */
    @GetMapping("/my")
    public List<Claim> getUserClaims(@RequestParam Integer userId) {
        return claimService.getUserClaims(userId);
    }

    /**
     * 查询待审核认领
     */
    @GetMapping("/pending")
    public List<Claim> getPendingClaims() {
        return claimService.getPendingClaims();
    }

    /**
     * 查询认领详情
     */
    @GetMapping("/{id}")
    public Claim getClaimById(@PathVariable Integer id) {
        return claimService.getClaimById(id);
    }

    /**
     * 提交认领
     */
    @PostMapping
    public Map<String, Object> submitClaim(@RequestBody Map<String, Object> params) {
        Claim claim = new Claim();
        claim.setItemId((Integer) params.get("itemId"));
        claim.setClaimerId((Integer) params.get("claimerId"));
        claim.setReason((String) params.get("reason"));

        return claimService.submitClaim(claim);
    }

    /**
     * 审核认领
     */
    @PutMapping("/{id}/review")
    public Map<String, Object> reviewClaim(@PathVariable Integer id, @RequestBody Map<String, Object> params) {
        String status = (String) params.get("status");
        String adminNote = (String) params.get("adminNote");
        Integer reviewedBy = (Integer) params.get("reviewedBy");

        return claimService.reviewClaim(id, status, adminNote, reviewedBy);
    }

    /**
     * 删除认领
     */
    @DeleteMapping("/{id}")
    public Map<String, Object> deleteClaim(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        boolean success = claimService.deleteClaim(id);
        result.put("success", success);
        result.put("message", success ? "删除成功" : "删除失败");
        return result;
    }

    /**
     * 查询认领统计信息
     */
    @GetMapping("/stats")
    public Map<String, Object> getClaimStats() {
        return claimService.getClaimStats();
    }
}
