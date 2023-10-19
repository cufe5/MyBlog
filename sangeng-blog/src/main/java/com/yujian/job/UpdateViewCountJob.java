package com.yujian.job;

import com.yujian.domain.entity.Article;
import com.yujian.service.ArticleService;
import com.yujian.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.yujian.constants.SystemConstants.ARTICLE_VIEW_COUNT;

@Component
public class UpdateViewCountJob {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ArticleService articleService;


    @Scheduled(cron = "0/300 * * * * ?")
    public void updateViewCount(){
        //获取redis的浏览量
        Map<String, Integer> viewCountMap = redisCache.getCacheMap(ARTICLE_VIEW_COUNT);

        List<Article> articles = viewCountMap.entrySet()
                .stream().map(entry -> new Article(Long.valueOf(entry.getKey()), entry.getValue().longValue()))
                .collect(Collectors.toList());

        //更新到数据库中

        System.out.println("数据更新成功");
        articleService.updateBatchById(articles);

    }
}
