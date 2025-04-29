package com.tourwise.backend.repository;

import com.tourwise.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// 要操作的实体是：User（对应数据库里的 users 表）, 它的主键类型是：Long
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Spring Data JPA 会自动根据你的方法名生成 SQL:  SELECT * FROM users WHERE email = ?
    User findByEmail(String email);
}
