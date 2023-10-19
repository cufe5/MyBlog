package com.yujian.controller;

import com.yujian.domain.ResponseResult;
import com.yujian.domain.dto.TagListDto;
import com.yujian.domain.entity.Tag;
import com.yujian.domain.vo.PageVo;
import com.yujian.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/content/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("/list")
    public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize, TagListDto tagListDto){
        return tagService.pageTagList(pageNum,pageSize,tagListDto);
    }

    @PostMapping()
    public ResponseResult addTag(@RequestBody TagListDto tagListDto){
        return tagService.addTagList(tagListDto);
    }

    @DeleteMapping("/{ids}")
    public ResponseResult deleteTag(@PathVariable("ids") List<Long> ids){
        return tagService.deleteTag(ids);
    }

    @GetMapping("/{id}")
    public ResponseResult selectTag(@PathVariable("id") Long id){
        return tagService.selectTag(id);
    }

    @PutMapping()
    public ResponseResult updateTag(@RequestBody Tag tag){
        return tagService.updateTag(tag);
    }

    @GetMapping("/listAllTag")
    public ResponseResult listAllTag(){
        return tagService.listAllTag();
    }
}
