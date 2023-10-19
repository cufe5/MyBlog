package com.yujian.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yujian.constants.SystemConstants;
import com.yujian.domain.ResponseResult;
import com.yujian.domain.dto.AddArticleDto;
import com.yujian.domain.entity.Article;
import com.yujian.domain.entity.ArticleTag;
import com.yujian.domain.entity.Category;
import com.yujian.domain.vo.ArticleDetailVo;
import com.yujian.domain.vo.ArticleListVo;
import com.yujian.domain.vo.HotArticleVo;
import com.yujian.domain.vo.PageVo;
import com.yujian.mapper.ArticleMapper;
import com.yujian.service.ArticleService;
import com.yujian.service.ArticleTagService;
import com.yujian.service.CategoryService;
import com.yujian.utils.BeanCopyUtils;
import com.yujian.utils.RedisCache;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.yujian.constants.SystemConstants.ARTICLE_VIEW_COUNT;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ArticleTagService articleTagService;
    @Override
    public ResponseResult hotArticleList() {
        //查询热门文章，封装成ResponseResult返回

        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //必须是正式文章
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        //按照浏览量进行排序
        queryWrapper.orderByDesc(Article::getViewCount);
        //最多只能查询10条
        Page<Article> page = new Page<>(1,10);
        page(page,queryWrapper);

        List<Article> articles = page.getRecords();

//        //bean拷贝
//        List<HotArticleVo> articleVos = new ArrayList<>();
//        for (Article article : articles) {
//            HotArticleVo vo = new HotArticleVo();
//            BeanUtils.copyProperties(article,vo);
//            articleVos.add(vo);
        List<HotArticleVo> vs = BeanCopyUtils.copyBeanList(articles, HotArticleVo.class);





        return ResponseResult.okResult(vs);
    }

    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {

        //查询条件
        LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //如果有categoryId 就要求查询时要和传入的相同
        lambdaQueryWrapper.eq(Objects.nonNull(categoryId)&&categoryId>0,Article::getCategoryId,categoryId);
        //状态是正式发布的
        lambdaQueryWrapper.eq(Article::getStatus,SystemConstants.ARTICLE_STATUS_NORMAL);
        lambdaQueryWrapper.orderByDesc(Article::getIsTop);
        //对isTop进行降序
        //分页查询
        Page<Article> page = new Page<>(pageNum, pageSize);
        page(page,lambdaQueryWrapper);

        List<Article> articles = page.getRecords();
        //查询categoryName
        articles.stream()
                .map(article -> article.setCategoryName(categoryService.getById(article.getCategoryId()).getName()))
                .collect(Collectors.toList());





        //articleId去查询articleName进行设置
//        for (Article article : articles) {
//            Category category = categoryService.getById(article.getCategoryId());
//            article.setCategoryName(category.getName());
//        }





        //封装查询结果
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(page.getRecords(), ArticleListVo.class);
        PageVo pageVo = new PageVo(articleListVos, page.getTotal());
        return ResponseResult.okResult(pageVo);
    }


    @Override
    public ResponseResult getArticleDetail(Long id) {
        //根据id查询文章
        Article article = getById(id);
        //从redis中获取viewCount
        Integer articleViewCount = redisCache.getCacheMapValue(ARTICLE_VIEW_COUNT, id.toString());
        article.setViewCount(articleViewCount.longValue());

        //转换成vo
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);

        //根据分类id查询分类名
        Long categoryId = articleDetailVo.getCategoryId();
        Category category = categoryService.getById(categoryId);
        if (category != null){
            articleDetailVo.setCategoryName(category.getName());
        }

        //封装响应返回
        return ResponseResult.okResult(articleDetailVo);
    }

    @Override
    public ResponseResult updateViewCount(Long id) {
        //更新redis中对应id文章的浏览量
        redisCache.incrementCacheMapValue(ARTICLE_VIEW_COUNT,id.toString(),1);
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult add(AddArticleDto addArticleDto) {
        Article article = BeanCopyUtils.copyBean(addArticleDto, Article.class);
        save(article);

        List<ArticleTag> articleTags = addArticleDto.getTags()
                .stream().map(tagId -> new ArticleTag(article.getId(), tagId)).collect(Collectors.toList());

        articleTagService.saveBatch(articleTags);

        return ResponseResult.okResult();
    }
}
