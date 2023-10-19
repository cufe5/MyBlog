package com.yujian.controller;

import com.yujian.domain.ResponseResult;
import com.yujian.domain.dto.AddArticleDto;
import com.yujian.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/content/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @PostMapping()
    public ResponseResult add(@RequestBody AddArticleDto addArticleDto){

        return articleService.add(addArticleDto);
    }
}
