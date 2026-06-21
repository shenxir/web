package org.example.lostfound.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.lostfound.model.Announcement;

import java.util.List;

/**
 * 公告Mapper接口
 */
@Mapper
public interface AnnouncementMapper {

    /**
     * 查询所有启用的公告
     */
    List<Announcement> getAllAnnouncements();

    /**
     * 根据ID查询公告
     */
    Announcement getAnnouncementById(@Param("announcementId") Integer announcementId);

    /**
     * 新增公告
     */
    int insertAnnouncement(Announcement announcement);

    /**
     * 更新公告
     */
    int updateAnnouncement(Announcement announcement);

    /**
     * 软删除公告
     */
    int deleteAnnouncement(@Param("announcementId") Integer announcementId);
}
