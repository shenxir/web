package org.example.lostfound.controller;

import org.example.lostfound.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        return userService.login(username, password);
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        String realName = params.get("realName");
        String phone = params.get("phone");
        return userService.register(username, password, realName, phone);
    }

    /**
     * 获取用户信息
     */
    @GetMapping("/userinfo")
    public Map<String, Object> getUserInfo(@RequestParam Integer userId) {
        Map<String, Object> result = new java.util.HashMap<>();
        var user = userService.getUserById(userId);
        if (user != null) {
            result.put("success", true);
            result.put("user", user);
        } else {
            result.put("success", false);
            result.put("message", "用户不存在");
        }
        return result;
    }

    /**
     * 修改密码
     */
    @PostMapping("/password")
    public Map<String, Object> changePassword(@RequestBody Map<String, String> params) {
        Integer userId = Integer.parseInt(params.get("userId"));
        String oldPassword = params.get("oldPassword");
        String newPassword = params.get("newPassword");
        return userService.changePassword(userId, oldPassword, newPassword);
    }
}
