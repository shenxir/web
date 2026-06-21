package org.example.lostfound.controller;

import org.example.lostfound.model.Item;
import org.example.lostfound.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.text.SimpleDateFormat;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @Value("${app.upload.dir:./picture}")
    private String uploadDir;

    /**
     * 搜索物品
     */
    @GetMapping
    public List<Item> searchItems(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) Integer locationId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String ownerType,
            @RequestParam(required = false) Integer userId) {
        itemService.autoExpireItems();
        return itemService.searchItems(keyword, categoryId, locationId, status, ownerType, userId);
    }

    /**
     * 查询所有物品
     */
    @GetMapping("/all")
    public List<Item> getAllItems() {
        itemService.autoExpireItems();
        return itemService.getAllItems();
    }

    /**
     * 查询物品详情
     */
    @GetMapping("/{id}")
    public Item getItemById(@PathVariable Integer id) {
        return itemService.getItemById(id);
    }

    /**
     * 查询用户的物品
     */
    @GetMapping("/user/{userId}")
    public List<Item> getUserItems(@PathVariable Integer userId) {
        return itemService.getUserItems(userId);
    }

    /**
     * 发布物品
     */
    @PostMapping
    public Map<String, Object> publishItem(@RequestBody Map<String, Object> params) {
        System.out.println("接收到的参数: " + params);

        Item item = new Item();
        item.setTitle((String) params.get("title"));
        item.setDescription((String) params.get("description"));

        // 处理categoryId - 可能是Integer或String
        Object categoryIdObj = params.get("categoryId");
        if (categoryIdObj != null) {
            item.setCategoryId(Integer.parseInt(categoryIdObj.toString()));
        }

        // 处理locationId - 可能是Integer或String
        Object locationIdObj = params.get("locationId");
        if (locationIdObj != null) {
            item.setLocationId(Integer.parseInt(locationIdObj.toString()));
        }

        item.setOwnerType((String) params.get("ownerType"));

        // 处理userId
        Object userIdObj = params.get("userId");
        if (userIdObj != null) {
            item.setUserId(Integer.parseInt(userIdObj.toString()));
        }

        item.setUserName((String) params.get("userName"));
        item.setPhone((String) params.get("phone"));

        System.out.println("Item对象: categoryId=" + item.getCategoryId() + ", locationId=" + item.getLocationId());

        @SuppressWarnings("unchecked")
        List<String> imageUrls = (List<String>) params.get("images");

        return itemService.publishItem(item, imageUrls);
    }

    /**
     * 上传图片
     */
    @PostMapping("/upload")
    public Map<String, Object> uploadImage(@RequestParam("file") MultipartFile file) {
        Map<String, Object> result = new HashMap<>();

        try {
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            String filename = "item_" + timestamp + "_" + UUID.randomUUID().toString().substring(0, 8) + extension;

            // 使用项目根目录的绝对路径
            String projectDir = System.getProperty("user.dir");
            String uploadPath = projectDir + File.separator + "picture";
            File dir = new File(uploadPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File dest = new File(dir, filename);
            file.transferTo(dest);

            result.put("success", true);
            result.put("message", "上传成功");
            result.put("url", "/picture/" + filename);
            result.put("originalName", originalFilename);
        } catch (IOException e) {
            result.put("success", false);
            result.put("message", "上传失败: " + e.getMessage());
        }

        return result;
    }

    /**
     * 删除物品
     */
    @DeleteMapping("/{id}")
    public Map<String, Object> deleteItem(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        boolean success = itemService.deleteItem(id);
        result.put("success", success);
        result.put("message", success ? "删除成功" : "删除失败");
        return result;
    }

    /**
     * 更新物品状态
     */
    @PutMapping("/{id}/status")
    public Map<String, Object> updateItemStatus(@PathVariable Integer id, @RequestBody Map<String, String> params) {
        Map<String, Object> result = new HashMap<>();
        String status = params.get("status");
        boolean success = itemService.updateItemStatus(id, status);
        result.put("success", success);
        result.put("message", success ? "状态更新成功" : "状态更新失败");
        return result;
    }

    /**
     * 查询物品统计信息
     */
    @GetMapping("/stats")
    public Map<String, Object> getItemStats() {
        return itemService.getItemStats();
    }
}
