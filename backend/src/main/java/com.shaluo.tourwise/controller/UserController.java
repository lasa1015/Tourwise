package com.shaluo.tourwise.controller;

import com.shaluo.tourwise.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

// 【在这个 UserController 中，部分逻辑（例如验证密码是否正确、修改密码、验证 token 是否有效）确实放在了 Controller 层，这使得这个 Controller 变得有点“重”。这个写法不算违规，但会引起一些潜在问题】

// 提供注册、登录、获取邮箱、更改密码、验证 token 的接口，和用户身份验证相关的所有 HTTP API。

@RestController  // 标记这是一个 REST API 控制器，返回的是 JSON 而不是页面
@RequestMapping("/api/users")  // 所有这个类里的接口，都会以 /users 开头
public class UserController {

    @Autowired
    private UserService userService;

    // 监听 POST 请求，路径是 /users/register
    @PostMapping("/register")
    // @RequestBody Map<String, String> request：接收前端发来的 JSON 请求体，并转换成 Map
    public ResponseEntity<Map<String, String>> register(@RequestBody Map<String, String> request) {

        // 从 request 里取出 email 和 password 字符串
        String email = request.get("email");
        String password = request.get("password");

        // 准备一个 response map 来存放返回信息
        Map<String, String> response = new HashMap<>();

        // 调用业务逻辑类 UserService 的注册方法，返回值是 true 表示注册成功，false 表示用户已存在
        try {
            boolean isRegistered = userService.registerUser(email, password);
            String message = isRegistered ? "Registration successful!" : "Email already registered.";
            response.put("message", message);
            return ResponseEntity.ok(response);
        }
        // 如果后端处理过程中抛出异常，返回 HTTP 500（服务器错误）状态
        catch (Exception e) {
            response.put("message", "Registration failed: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    //  POST 接口，路径是 /users/login，用于登录
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");
        Map<String, String> response = new HashMap<>();
        try {
            boolean isAuthenticated = userService.authenticateUser(email, password);

            // 如果登录成功，就生成一个 JWT token 发给用户。
            if (isAuthenticated) {
                String token = userService.generateToken(email);
                response.put("token", token);
                return ResponseEntity.ok(response);
            }
            // 登录失败 → 返回 401 状态码和提示信息
            else {
                response.put("message", "Invalid email or password.");
                return ResponseEntity.status(401).body(response);
            }
        } catch (Exception e) {
            response.put("message", "Login failed: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @PostMapping("/email")
    public ResponseEntity<Map<String, String>> getEmail(@RequestBody Map<String, String> request) {
        Map<String, String> response = new HashMap<>();

        String token = request.get("token");
        String email = userService.getEmailFromToken(token);
        response.put("email", email);
        return ResponseEntity.ok(response);
    }

    // 路径 /users/changePassword，接收 token、旧密码、新密码
    @PostMapping("/changePassword")
    public ResponseEntity<Map<String, String>> changePassword(@RequestBody Map<String, String> request) {
        String token = request.get("token");

        Map<String, String> response = new HashMap<>();
        if (userService.verifyToken(token)) {
            try {
                String email = userService.getEmailFromToken(token);
                String oldPassword = request.get("oldPassword");
                String newPassword = request.get("newPassword");

                boolean isChanged = userService.changePassword(email, oldPassword, newPassword);
                String message = isChanged ? "Password changed successfully!" : "Invalid email or password.";
                response.put("message", message);
                return ResponseEntity.ok(response);
            } catch (Exception e) {
                response.put("message", "Access to protected resource granted but Password change failed: " + e.getMessage());
                return ResponseEntity.status(500).body(response);
            }
        } else {
            response.put("message", "Invalid or expired token.");
            return ResponseEntity.status(401).body(response);
        }
    }

    @GetMapping("/protected")
    public ResponseEntity<Map<String, String>> protectedEndpoint(@RequestHeader("Authorization") String token) {
        Map<String, String> response = new HashMap<>();
        if (userService.verifyToken(token)) {
            response.put("message", "Access to protected resource granted.");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Invalid or expired token.");
            return ResponseEntity.status(401).body(response);
        }
    }
}