package org.example.lostfound.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.lostfound.model.User;

import java.util.List;
import java.util.Map;

/**
 * 用户Mapper接口
 */
@Mapper
public interface UserMapper {

    /**
     * 用户登录
     */
    User login(@Param("username") String username, @Param("password") String password);

    /**
     * 根据ID查询用户
     */
    User getUserById(@Param("userId") Integer userId);

    /**
     * 查询所有用户
     */
    List<User> getAllUsers();

    /**
     * 根据用户名查询用户
     */
    User getUserByUsername(@Param("username") String username);

    /**
     * 查询用户统计信息
     */
    Map<String, Object> getUserStats(@Param("userId") Integer userId);

    /**
     * 新增用户
     */
    int insertUser(User user);

    /**
     * 更新用户信息
     */
    int updateUser(User user);

    /**
     * 软删除用户
     */
    int deleteUser(@Param("userId") Integer userId);

    /**
     * 批量查询用户
     */
    List<User> getUsersByIds(@Param("userIds") List<Integer> userIds);
}
