package com.yujian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yujian.domain.ResponseResult;
import com.yujian.domain.entity.Comment;


/**
 * 评论表(Comment)表服务接口
 *
 * @author makejava
 * @since 2023-06-21 15:20:04
 */
public interface CommentService extends IService<Comment> {

    ResponseResult commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize);

    ResponseResult addComment(Comment comment);
}


