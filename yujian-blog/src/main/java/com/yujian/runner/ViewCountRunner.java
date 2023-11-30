package com.yujian.runner;

import com.yujian.domain.entity.Article;
import com.yujian.mapper.ArticleMapper;
import com.yujian.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.yujian.constants.SystemConstants.ARTICLE_VIEW_COUNT;

@Component
public class ViewCountRunner implements CommandLineRunner {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private RedisCache redisCache;

    @Override
    public void run(String... args) throws Exception {
        //查询博客信息 id viewCount
        /**
         * 可以用lambda表达式简化
         */
        List<Article> articles = articleMapper.selectList(null);
        Map<String, Integer> viewCount = articles.stream()
                .collect(Collectors.toMap(new Function<Article, String>() {
                    @Override
                    public String apply(Article article) {
                        return article.getId().toString();
                    }
                }, new Function<Article, Integer>() {
                    @Override
                    public Integer apply(Article article) {
                        return article.getViewCount().intValue();
                    }
                }));
        //存储到redis中
        redisCache.setCacheMap(ARTICLE_VIEW_COUNT,viewCount);

    }
}
