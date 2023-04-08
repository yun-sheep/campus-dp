package com.example.campusdianping.common.domian.blog;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Description blog详情显示的dto（包含blog的详细信息和关联的用户信息）
 * @auther j2-yizhiyang
 * @date 2023/4/5 22:01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogVO {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 商户id
     */
    private Long shopId;
    //Blog中的照片
    private List<MultipartFile> images;
    //
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 用户图标
     */
    private String icon;
    /**
     * 用户姓名
     */
    private String name;
    /**
     * 是否点赞过了
     */
    private Boolean isLike;
    /**
     * 标题
     */
    private String title;
    /**
     * 探店的文字描述
     */
    private String content;

    /**
     * 点赞数量
     */
    private Integer liked;

    /**
     * 评论数量
     */
    private Integer comments;

}
