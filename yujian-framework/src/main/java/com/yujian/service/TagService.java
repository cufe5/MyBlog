package com.yujian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yujian.domain.ResponseResult;
import com.yujian.domain.dto.TagListDto;
import com.yujian.domain.entity.Tag;

import java.util.List;


/**
 * 标签(Tag)表服务接口
 *
 * @author makejava
 * @since 2023-09-09 11:13:36
 */
public interface TagService extends IService<Tag> {

    ResponseResult pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto);

    ResponseResult addTagList(TagListDto tagListDto);

    ResponseResult deleteTag(List<Long> ids);

    ResponseResult selectTag(Long id);

    ResponseResult updateTag(Tag tag);

    ResponseResult listAllTag();
}

