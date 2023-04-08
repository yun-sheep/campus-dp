package com.example.campusdianping.controller.upload;

import com.example.campusdianping.common.domian.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description 用于照片上传到文件服务器的
 * @auther j2-yizhiyang
 * @date 2023/4/5 21:35
 */
//教程中是放在nigx目录下面的
@Slf4j
@RestController
@RequestMapping("upload")
public class UploadController {
    public Result uploadImage(@RequestParam("file")MultipartFile image){
        //TODO 连接obs服务器，存入照片，然后返回存储的链接
        return null;
    }
}
