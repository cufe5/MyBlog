package com.yujian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yujian.domain.ResponseResult;
import com.yujian.domain.entity.Category;


/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2023-05-08 15:19:50
 */
public interface CategoryService extends IService<Category> {

    ResponseResult getListAllCategory();

    ResponseResult getCategoryList();
}

