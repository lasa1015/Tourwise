package com.shaluo.tourwise.model;

import lombok.Data;

import java.util.List;

// 接收一组 ID（通常是 UUID 或数据库主键）
// 不需要 @Entity，这就是纯 DTO

@Data
public class UserSelection {

    private List<String> ids;

    @Override
    public String toString() {
        return "UserSelection{" +
                "ids=" + ids +
                '}';
    }

}