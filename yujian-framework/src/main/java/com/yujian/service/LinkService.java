package com.yujian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yujian.domain.ResponseResult;
import com.yujian.domain.entity.Link;


/**
 * 友链(Link)表服务接口
 *
 * @author makejava
 * @since 2023-05-10 15:23:13
 */
public interface LinkService extends IService<Link> {

    ResponseResult getAllLink();

    ResponseResult pageLink(Integer pageNum, Integer pageSize, String name, String status);
}

