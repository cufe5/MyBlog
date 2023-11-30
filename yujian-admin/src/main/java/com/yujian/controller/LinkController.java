package com.yujian.controller;

import com.yujian.domain.ResponseResult;
import com.yujian.domain.entity.Link;
import com.yujian.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/link")
public class LinkController {
    @Autowired
    private LinkService linkService;
    @GetMapping("/list")
    public ResponseResult queryPage(Integer pageNum,Integer pageSize,String name,String status){
        return linkService.pageLink(pageNum,pageSize,name,status);
    }
    @PostMapping
    public ResponseResult addLink(@RequestBody Link link){
        linkService.save(link);
        return ResponseResult.okResult();
    }
    @GetMapping("/{id}")
    public ResponseResult queryById(@PathVariable("id") Long id){
        return ResponseResult.okResult(linkService.getById(id));
    }
    @PutMapping
    public ResponseResult modifyLink(@RequestBody Link link){
        linkService.updateById(link);
        return ResponseResult.okResult();
    }
    @DeleteMapping("/{id}")
    public ResponseResult deleteLink(@PathVariable("id") Long id){
        linkService.removeById(id);
        return ResponseResult.okResult();
    }
}
