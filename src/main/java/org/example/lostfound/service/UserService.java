package org.example.lostfound.service;

import org.example.lostfound.mapper.UserMapper;
import org.example.lostfound.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 用户登录
     */
    public Map<String, Object> login(String username, String password) {
        Map<String, Object> result = new HashMap<>();
        User user = userMapper.login(username, password);

        if (user != null) {
            result.put("success", true);
            result.put("message", "登录成功");
            result.put("user", user);
        } else {
            result.put("success", false);
            result.put("message", "用户名或密码错误");
        }
        return result;
    }

    /**
     * 根据ID查询用户
     */
    public User getUserById(Integer userId) {
        return userMapper.getUserById(userId);
    }

    /**
     * 根据用户名查询用户
     */
    public User getUserByUsername(String username) {
        return userMapper.getUserByUsername(username);
    }

    /**
     * 查询所有用户
     */
    public List<User> getAllUsers() {
        return userMapper.getAllUsers();
    }

    /**
     * 新增用户
     */
    public int insertUser(User user) {
        return userMapper.insertUser(user);
    }

    /**
     * 更新用户信息
     */
    public int updateUser(User user) {
        return userMapper.updateUser(user);
    }

    /**
     * 软删除用户
     */
    public int deleteUser(Integer userId) {
        return userMapper.deleteUser(userId);
    }

    /**
     * 查询用户统计信息
     */
    public Map<String, Object> getUserStats(Integer userId) {
        return userMapper.getUserStats(userId);
    }
}
