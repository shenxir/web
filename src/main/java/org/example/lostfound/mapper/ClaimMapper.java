package org.example.lostfound.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.lostfound.model.Claim;

import java.util.List;
import java.util.Map;

/**
 * 认领Mapper接口
 */
@Mapper
public interface ClaimMapper {

    /**
     * 查询认领详情（多表联查）
     */
    Claim getClaimById(@Param("claimId") Integer claimId);

    /**
     * 查询所有认领记录（支持状态筛选）
     */
    List<Claim> getAllClaims(@Param("status") String status, @Param("itemId") Integer itemId);

    /**
     * 查询用户的认领记录
     */
    List<Claim> getUserClaims(@Param("userId") Integer userId);

    /**
     * 查询物品的认领记录
     */
    List<Claim> getItemClaims(@Param("itemId") Integer itemId);

    /**
     * 查询待审核认领
     */
    List<Claim> getPendingClaims();

    /**
     * 提交认领
     */
    int insertClaim(Claim claim);

    /**
     * 审核认领
     */
    int reviewClaim(@Param("claimId") Integer claimId,
                    @Param("status") String status,
                    @Param("adminNote") String adminNote,
                    @Param("reviewedBy") Integer reviewedBy);

    /**
     * 审核通过时更新物品状态（多表联动）
     */
    int updateItemStatusOnApproved(@Param("itemId") Integer itemId);

    /**
     * 删除认领
     */
    int deleteClaim(@Param("claimId") Integer claimId);

    /**
     * 批量更新过期认领
     */
    int autoExpireClaims();

    /**
     * 查询认领统计信息
     */
    Map<String, Object> getClaimStats();

    /**
     * 检查重复认领
     */
    int checkDuplicateClaim(@Param("itemId") Integer itemId, @Param("claimerId") Integer claimerId);

    /**
     * 查询用户待审核认领数量
     */
    int getUserPendingClaimCount(@Param("userId") Integer userId);

    /**
     * 查询最近7天认领趋势
     */
    List<Map<String, Object>> getClaimTrend();
}
