package com.yujian.controller;

import com.yujian.domain.ResponseResult;
import com.yujian.domain.dto.AddArticleDto;
import com.yujian.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/content/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @PostMapping()
    public ResponseResult add(@RequestBody AddArticleDto addArticleDto){

        return articleService.add(addArticleDto);
    }

    @GetMapping("/list")
    public ResponseResult articleAllList(Integer pageNum,Integer pageSize,String title,String summary){
        return articleService.articleAllList(pageNum,pageSize,title,summary);
    }

    @GetMapping("/{id}")
    public ResponseResult getArticleById(@PathVariable("id") Long id){
        return articleService.getArticleById(id);
    }

    @PutMapping()
    public ResponseResult updateArticle(@RequestBody AddArticleDto articleDto){
        return articleService.updateArticle(articleDto);
    }

    @DeleteMapping("/{ids}")
    public ResponseResult deleteArticleList(@PathVariable("ids") List<Long> ids){
        return articleService.deleteArticleList(ids);
    }
}
