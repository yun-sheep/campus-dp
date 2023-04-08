package com.example.campusdianping.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.campusdianping.common.domian.Result;
import com.example.campusdianping.entity.follow.Follow;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
public interface IFollowService extends IService<Follow> {

    Result follow(Long followUserId, Boolean isFollow);

    Result isFollow(Long followUserId);

    Result followCommons(Long id);
}
