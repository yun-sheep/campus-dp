package com.example.campusdianping.common.utils.quartz;

import com.example.campusdianping.service.Impl.LikeTodbServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description 点赞定时任务
 * @auther j2-yizhiyang
 * @date 2023/4/19 15:25
 */
@Slf4j
public class LikeTask extends QuartzJobBean {
    @Resource
    private LikeTodbServiceImpl likeTodbService;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("LikeTask-------- {}", sdf.format(new Date()));

        //将 Redis 里的点赞信息同步到数据库里
        likeTodbService.LikeCountTodb();
    }
}
