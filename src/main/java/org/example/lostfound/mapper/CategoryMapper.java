package org.example.lostfound.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.lostfound.model.Category;

import java.util.List;

/**
 * 分类Mapper接口
 */
@Mapper
public interface CategoryMapper {

    /**
     * 查询所有启用的分类
     */
    List<Category> getAllCategories();

    /**
     * 根据ID查询分类
     */
    Category getCategoryById(@Param("categoryId") Integer categoryId);

    /**
     * 根据名称查询分类
     */
    Category getCategoryByName(@Param("name") String name);

    /**
     * 新增分类
     */
    int insertCategory(Category category);

    /**
     * 更新分类
     */
    int updateCategory(Category category);

    /**
     * 删除分类
     */
    int deleteCategory(@Param("categoryId") Integer categoryId);
}
