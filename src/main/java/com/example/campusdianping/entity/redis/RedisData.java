package com.example.campusdianping.entity.redis;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Description 带日期的redis存储对象
 * @auther j2-yizhiyang
 * @date 2023/4/5 19:44
 */
@Data
public class RedisData {
    //因为要存入的时候要带时间。为了不修改之前写好的代码（在shop)中继承，就使用这种组合的方式（设计模式：组合优于继承）
    private LocalDateTime expireTime;

    private Object data;
}
