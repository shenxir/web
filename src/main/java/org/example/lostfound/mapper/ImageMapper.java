package org.example.lostfound.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.lostfound.model.ItemImage;

import java.util.List;

/**
 * 图片Mapper接口
 */
@Mapper
public interface ImageMapper {

    /**
     * 查询物品的所有图片
     */
    List<ItemImage> getImagesByItemId(@Param("itemId") Integer itemId);

    /**
     * 根据ID查询图片
     */
    ItemImage getImageById(@Param("imageId") Integer imageId);

    /**
     * 新增图片
     */
    int insertImage(ItemImage image);

    /**
     * 批量新增图片
     */
    int batchInsertImages(@Param("images") List<ItemImage> images);

    /**
     * 删除图片
     */
    int deleteImage(@Param("imageId") Integer imageId);

    /**
     * 删除物品的所有图片
     */
    int deleteImagesByItemId(@Param("itemId") Integer itemId);
}
