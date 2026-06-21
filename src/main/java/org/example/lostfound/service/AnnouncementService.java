package org.example.lostfound.service;

import org.example.lostfound.mapper.AnnouncementMapper;
import org.example.lostfound.model.Announcement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnnouncementService {

    @Autowired
    private AnnouncementMapper announcementMapper;

    public List<Announcement> getAllAnnouncements() {
        return announcementMapper.getAllAnnouncements();
    }

    public Announcement getAnnouncementById(Integer announcementId) {
        return announcementMapper.getAnnouncementById(announcementId);
    }

    public int insertAnnouncement(Announcement announcement) {
        return announcementMapper.insertAnnouncement(announcement);
    }

    public int updateAnnouncement(Announcement announcement) {
        return announcementMapper.updateAnnouncement(announcement);
    }

    public int deleteAnnouncement(Integer announcementId) {
        return announcementMapper.deleteAnnouncement(announcementId);
    }
}
