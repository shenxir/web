package org.example.lostfound.controller;

import org.example.lostfound.model.Announcement;
import org.example.lostfound.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/announcements")
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    /**
     * 查询所有公告
     */
    @GetMapping
    public List<Announcement> getAllAnnouncements() {
        return announcementService.getAllAnnouncements();
    }

    /**
     * 根据ID查询公告
     */
    @GetMapping("/{id}")
    public Announcement getAnnouncementById(@PathVariable Integer id) {
        return announcementService.getAnnouncementById(id);
    }

    /**
     * 发布公告
     */
    @PostMapping
    public Map<String, Object> addAnnouncement(@RequestBody Announcement announcement) {
        Map<String, Object> result = new HashMap<>();
        try {
            announcementService.insertAnnouncement(announcement);
            result.put("success", true);
            result.put("message", "发布成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "发布失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 更新公告
     */
    @PutMapping("/{id}")
    public Map<String, Object> updateAnnouncement(@PathVariable Integer id, @RequestBody Announcement announcement) {
        Map<String, Object> result = new HashMap<>();
        announcement.setId(id);
        try {
            announcementService.updateAnnouncement(announcement);
            result.put("success", true);
            result.put("message", "更新成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "更新失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 删除公告
     */
    @DeleteMapping("/{id}")
    public Map<String, Object> deleteAnnouncement(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        try {
            announcementService.deleteAnnouncement(id);
            result.put("success", true);
            result.put("message", "删除成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "删除失败: " + e.getMessage());
        }
        return result;
    }
}
