package org.example.lostfound.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.lostfound.model.Location;

import java.util.List;

/**
 * 地点Mapper接口
 */
@Mapper
public interface LocationMapper {

    /**
     * 查询所有启用的地点
     */
    List<Location> getAllLocations();

    /**
     * 根据ID查询地点
     */
    Location getLocationById(@Param("locationId") Integer locationId);

    /**
     * 根据名称查询地点
     */
    Location getLocationByName(@Param("name") String name);

    /**
     * 新增地点
     */
    int insertLocation(Location location);

    /**
     * 更新地点
     */
    int updateLocation(Location location);

    /**
     * 删除地点
     */
    int deleteLocation(@Param("locationId") Integer locationId);
}
