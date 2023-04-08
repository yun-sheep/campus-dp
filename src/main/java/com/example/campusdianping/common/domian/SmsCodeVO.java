package com.example.campusdianping.common.domian;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Description 返回验证码
 * @auther j2-yizhiyang
 * @date 2023/4/3 15:21
 */
@Data
@AllArgsConstructor
public class SmsCodeVO {
    private String code;
    private Long expiredtime;
}
