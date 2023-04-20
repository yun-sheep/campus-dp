package com.example.campusdianping.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Description 上传和获取cos服务器中的图片对象接口
 * @auther j2-yizhiyang
 * @date 2023/4/19 20:04
 */
public interface ICosService {
    //上传图片
    boolean UploadImages(List<MultipartFile> multipartFiles);
    //根绝blogid获取对象
    List<MultipartFile> GetImagesByblogId(Long blog_id);

}
