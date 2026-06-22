package org.example.lostfound.service;

import org.example.lostfound.mapper.ItemMapper;
import org.example.lostfound.mapper.ImageMapper;
import org.example.lostfound.model.Item;
import org.example.lostfound.model.ItemImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ItemService {

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private ImageMapper imageMapper;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 查询物品详情（包含图片）
     */
    public Item getItemById(Integer itemId) {
        Item item = itemMapper.getItemById(itemId);
        if (item != null) {
            // 查询图片
            List<ItemImage> images = imageMapper.getImagesByItemId(itemId);
            item.setImages(images);
            // 增加浏览次数
            itemMapper.incrementViews(itemId);
        }
        return item;
    }

    /**
     * 搜索物品
     */
    public List<Item> searchItems(String keyword, Integer categoryId, Integer locationId,
                                  String status, String ownerType, Integer userId) {
        List<Item> items = itemMapper.searchItems(keyword, categoryId, locationId, status, ownerType, userId);
        loadImagesForItems(items);
        return items;
    }

    /**
     * 查询所有物品
     */
    public List<Item> getAllItems() {
        List<Item> items = itemMapper.getAllItems();
        loadImagesForItems(items);
        return items;
    }

    /**
     * 查询用户的物品
     */
    public List<Item> getUserItems(Integer userId) {
        List<Item> items = itemMapper.getUserItems(userId);
        loadImagesForItems(items);
        return items;
    }

    /**
     * 查询最新物品
     */
    public List<Item> getLatestItems(Integer limit) {
        List<Item> items = itemMapper.getLatestItems(limit);
        loadImagesForItems(items);
        return items;
    }

    /**
     * 批量加载物品图片
     */
    private void loadImagesForItems(List<Item> items) {
        if (items != null && !items.isEmpty()) {
            for (Item item : items) {
                List<ItemImage> images = imageMapper.getImagesByItemId(item.getId());
                item.setImages(images);
            }
        }
    }

    /**
     * 发布物品
     */
    @Transactional
    public Map<String, Object> publishItem(Item item, List<String> imageUrls) {
        Map<String, Object> result = new HashMap<>();

        // 检查重复发布
        int duplicate = itemMapper.checkDuplicate(item.getUserId(), item.getTitle());
        if (duplicate > 0) {
            result.put("success", false);
            result.put("message", "您在24小时内已发布过相同标题的物品，请勿重复发布");
            return result;
        }

        // 计算过期日期（统一7天过期）
        LocalDate today = LocalDate.now();
        String expiryDate = today.plusDays(7).format(DATE_FMT);

        // 设置默认值
        item.setPublishDate(today.format(DATE_FMT));
        item.setExpiryDate(expiryDate);
        item.setStatus("pending");

        // 插入物品
        itemMapper.insertItem(item);

        // 插入图片
        if (imageUrls != null && !imageUrls.isEmpty()) {
            for (int i = 0; i < imageUrls.size(); i++) {
                ItemImage image = new ItemImage();
                image.setItemId(item.getId());
                image.setImageUrl(imageUrls.get(i));
                image.setOriginalName("image_" + (i + 1) + ".jpg");
                image.setSortOrder(i + 1);
                imageMapper.insertImage(image);
            }
        }

        result.put("success", true);
        result.put("message", "发布成功");
        result.put("item", item);
        return result;
    }

    /**
     * 删除物品
     */
    @Transactional
    public boolean deleteItem(Integer itemId) {
        return itemMapper.deleteItem(itemId) > 0;
    }

    /**
     * 更新物品状态
     */
    public boolean updateItemStatus(Integer itemId, String status) {
        return itemMapper.updateItemStatus(itemId, status) > 0;
    }

    /**
     * 自动过期处理
     */
    public int autoExpireItems() {
        return itemMapper.autoExpireItems();
    }

    /**
     * 查询物品统计信息
     */
    public Map<String, Object> getItemStats() {
        return itemMapper.getItemStats();
    }

    /**
     * 检查物品是否可被认领
     */
    public boolean checkItemClaimable(Integer itemId) {
        return itemMapper.checkItemClaimable(itemId);
    }

    /**
     * 检查用户是否已认领过该物品
     */
    public boolean checkUserClaimed(Integer itemId, Integer userId) {
        return itemMapper.checkUserClaimed(itemId, userId);
    }
}
