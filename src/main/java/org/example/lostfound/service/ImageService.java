package org.example.lostfound.service;

import org.example.lostfound.mapper.ImageMapper;
import org.example.lostfound.model.ItemImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageService {

    @Autowired
    private ImageMapper imageMapper;

    public List<ItemImage> getImagesByItemId(Integer itemId) {
        return imageMapper.getImagesByItemId(itemId);
    }

    public ItemImage getImageById(Integer imageId) {
        return imageMapper.getImageById(imageId);
    }

    public int insertImage(ItemImage image) {
        return imageMapper.insertImage(image);
    }

    public int deleteImage(Integer imageId) {
        return imageMapper.deleteImage(imageId);
    }

    public int deleteImagesByItemId(Integer itemId) {
        return imageMapper.deleteImagesByItemId(itemId);
    }
}
