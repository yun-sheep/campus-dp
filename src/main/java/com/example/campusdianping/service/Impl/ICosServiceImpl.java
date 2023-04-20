package com.example.campusdianping.service.Impl;

import com.example.campusdianping.service.ICosService;
import com.qcloud.cos.model.CannedAccessControlList;
import com.qcloud.cos.model.CreateBucketRequest;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

/**
 * @Description  上传和获取cos服务器中的图片对象实现类
 * @auther j2-yizhiyang
 * @date 2023/4/19 20:08
 */
public class ICosServiceImpl implements ICosService {
    @Override
    public boolean UploadImages(List<MultipartFile> multipartFiles) {
        String bucket = "blogimagesbucket-01";
        CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucket);
        createBucketRequest.setCannedAcl(CannedAccessControlList.Private);
        return false;
    }


    @Override
    public List<MultipartFile> GetImagesByblogId(Long blog_id) {
        return null;
    }
}
