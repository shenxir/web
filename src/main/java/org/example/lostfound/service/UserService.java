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
     * 用户注册
     */
    public Map<String, Object> register(String username, String password, String realName, String phone) {
        Map<String, Object> result = new HashMap<>();

        // 参数校验
        if (username == null || username.trim().isEmpty()) {
            result.put("success", false);
            result.put("message", "用户名不能为空");
            return result;
        }
        if (password == null || password.trim().isEmpty()) {
            result.put("success", false);
            result.put("message", "密码不能为空");
            return result;
        }
        if (realName == null || realName.trim().isEmpty()) {
            result.put("success", false);
            result.put("message", "真实姓名不能为空");
            return result;
        }

        // 用户名长度校验
        if (username.trim().length() < 3 || username.trim().length() > 20) {
            result.put("success", false);
            result.put("message", "用户名长度需在3-20个字符之间");
            return result;
        }

        // 密码长度校验
        if (password.trim().length() < 6) {
            result.put("success", false);
            result.put("message", "密码长度不能少于6个字符");
            return result;
        }

        // 检查用户名是否已存在
        User existingUser = userMapper.getUserByUsername(username.trim());
        if (existingUser != null) {
            result.put("success", false);
            result.put("message", "用户名已存在");
            return result;
        }

        // 创建新用户
        User user = new User();
        user.setUsername(username.trim());
        user.setPassword(password.trim());
        user.setRealName(realName.trim());
        user.setRole("user"); // 默认普通用户
        user.setPhone(phone);
        user.setStatus(1);

        int rows = userMapper.insertUser(user);
        if (rows > 0) {
            result.put("success", true);
            result.put("message", "注册成功");
            result.put("userId", user.getId());
        } else {
            result.put("success", false);
            result.put("message", "注册失败，请稍后重试");
        }
        return result;
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
     * 修改密码
     */
    public Map<String, Object> changePassword(Integer userId, String oldPassword, String newPassword) {
        Map<String, Object> result = new HashMap<>();

        if (oldPassword == null || oldPassword.trim().isEmpty()) {
            result.put("success", false);
            result.put("message", "原密码不能为空");
            return result;
        }
        if (newPassword == null || newPassword.trim().isEmpty()) {
            result.put("success", false);
            result.put("message", "新密码不能为空");
            return result;
        }
        if (newPassword.trim().length() < 6) {
            result.put("success", false);
            result.put("message", "新密码长度不能少于6个字符");
            return result;
        }
        if (oldPassword.trim().equals(newPassword.trim())) {
            result.put("success", false);
            result.put("message", "新密码不能与原密码相同");
            return result;
        }

        int rows = userMapper.updatePassword(userId, oldPassword.trim(), newPassword.trim());
        if (rows > 0) {
            result.put("success", true);
            result.put("message", "密码修改成功");
        } else {
            result.put("success", false);
            result.put("message", "原密码错误");
        }
        return result;
    }

    /**
     * 查询用户统计信息
     */
    public Map<String, Object> getUserStats(Integer userId) {
        return userMapper.getUserStats(userId);
    }
}
