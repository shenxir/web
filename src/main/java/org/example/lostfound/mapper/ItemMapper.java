package org.example.lostfound.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.lostfound.model.Item;

import java.util.List;
import java.util.Map;

/**
 * 物品Mapper接口
 */
@Mapper
public interface ItemMapper {

    /**
     * 查询物品详情（联表查询分类、地点、用户信息）
     */
    Item getItemById(@Param("itemId") Integer itemId);

    /**
     * 搜索物品（多条件联表查询）
     */
    List<Item> searchItems(@Param("keyword") String keyword,
                           @Param("categoryId") Integer categoryId,
                           @Param("locationId") Integer locationId,
                           @Param("status") String status,
                           @Param("ownerType") String ownerType,
                           @Param("userId") Integer userId);

    /**
     * 查询所有物品
     */
    List<Item> getAllItems();

    /**
     * 查询用户的物品
     */
    List<Item> getUserItems(@Param("userId") Integer userId);

    /**
     * 查询最新物品
     */
    List<Item> getLatestItems(@Param("limit") Integer limit);

    /**
     * 新增物品
     */
    int insertItem(Item item);

    /**
     * 更新物品状态
     */
    int updateItemStatus(@Param("itemId") Integer itemId, @Param("status") String status);

    /**
     * 批量更新过期物品
     */
    int autoExpireItems();

    /**
     * 软删除物品
     */
    int deleteItem(@Param("itemId") Integer itemId);

    /**
     * 检查重复发布
     */
    int checkDuplicate(@Param("userId") Integer userId, @Param("title") String title);

    /**
     * 查询物品统计信息
     */
    Map<String, Object> getItemStats();

    /**
     * 查询分类统计
     */
    List<Map<String, Object>> getCategoryStats();

    /**
     * 查询地点统计
     */
    List<Map<String, Object>> getLocationStats();

    /**
     * 增加浏览次数
     */
    int incrementViews(@Param("itemId") Integer itemId);

    /**
     * 检查物品是否可被认领
     */
    boolean checkItemClaimable(@Param("itemId") Integer itemId);

    /**
     * 检查用户是否已认领过该物品
     */
    boolean checkUserClaimed(@Param("itemId") Integer itemId, @Param("userId") Integer userId);
}
