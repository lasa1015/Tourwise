package com.tourwise.backend.service;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.tourwise.backend.repository.UserRepository;
import com.tourwise.backend.model.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // secureRandom 用于生成密码加盐（salt）
    private SecureRandom secureRandom = new SecureRandom();

    // SECRET_KEY 是用于签发 JWT 的密钥
    private static final String SECRET_KEY = "SecretKeyToGenJWTs";

    // 注册新用户
    public boolean registerUser(String email, String password) {

        // 先检查邮箱是否已存在，如果存在就不允许注册
        if (userRepository.findByEmail(email) != null) {
            return false;
        }
        User user = new User();
        user.setEmail(email);

        // 为密码生成生成随机 salt盐值，并将盐值和密码拼接后用 SHA-256 加密，再保存进数据库。
        String salt = generateSalt();
        user.setSalt(salt);
        String saltedPassword = salt + password;
        user.setPassword(hashPassword(saltedPassword));

        // 保存进数据库
        userRepository.save(user);
        return true;
    }

    // 登录认证
    public boolean authenticateUser(String email, String password) {

        // 查找用户，如果找不到就返回 false
        User user = userRepository.findByEmail(email);
        if (user != null) {

            // 使用之前保存的盐值拼接密码，加密后与数据库中存储的密码对比
            String saltedPassword = user.getSalt() + password;
            return hashPassword(saltedPassword).equals(user.getPassword());
        }

        return false;
    }

    // 生成 JWT 令牌
    public String generateToken(String email) {

        // 把邮箱作为 token 的 subject，设置过期时间为 10 天，用密钥加密签名
        return JWT.create()
                .withSubject(email)
                .withExpiresAt(new Date(System.currentTimeMillis() + 864_000_000)) // 10 天有效期
                .sign(Algorithm.HMAC512(SECRET_KEY));
    }

    // 校验 JWT 是否有效
    public boolean verifyToken(String token) {
        try {

            // 去掉前缀 "Bearer "，用密钥进行验证，如果出错就说明 token 无效
            JWTVerifier verifier = JWT.require(Algorithm.HMAC512(SECRET_KEY)).build();
            DecodedJWT jwt = verifier.verify(token.replace("Bearer ", ""));
            return true;

        } catch (JWTVerificationException exception) {
            return false;
        }
    }

    // 从 token 中提取邮箱
    public String getEmailFromToken(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC512(SECRET_KEY)).build();
        DecodedJWT jwt = verifier.verify(token.replace("Bearer ", ""));
        return jwt.getSubject();
    }

    // 生成加密用的 salt
    private String generateSalt() {

        // 生成 16 字节的随机数，转成 Base64 编码返回
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    // 对密码进行 SHA-256 加密
    private String hashPassword(String password) {
        return DigestUtils.sha256Hex(password);
    }

    // 修改密码
    public boolean changePassword(String email, String oldPassword, String newPassword) {

        // 查找用户
        User user = userRepository.findByEmail(email);

        if (user != null) {

            String saltedPassword = user.getSalt() + oldPassword;

            // 检查旧密码是否正确（加盐后再加密）
            if (hashPassword(saltedPassword).equals(user.getPassword())) {

                // 重新生成新的 salt 和加密密码
                String salt = generateSalt();
                user.setSalt(salt);
                user.setPassword(hashPassword(salt + newPassword));

                // 更新数据库记录
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }
}