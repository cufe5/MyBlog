package com.yujian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yujian.domain.ResponseResult;
import com.yujian.domain.dto.AddArticleDto;
import com.yujian.domain.entity.Article;

import java.util.List;

public interface ArticleService extends IService<Article> {
    ResponseResult hotArticleList();

    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult getArticleDetail(Long id);


    ResponseResult updateViewCount(Long id);

    ResponseResult add(AddArticleDto addArticleDto);

    ResponseResult articleAllList(Integer pageNum, Integer pageSize, String title, String summary);

    ResponseResult getArticleById(Long id);

    ResponseResult updateArticle(AddArticleDto articleDto);

    ResponseResult deleteArticleList(List<Long> ids);
}
