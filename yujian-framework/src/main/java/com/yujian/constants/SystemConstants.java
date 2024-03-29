package com.yujian.constants;

public class SystemConstants {
    /**
     * 文章是草稿
     */
    public static final int ARTICLE_STATUS_DRAFT = 1;
    /**
     * 文章是正常发布状态
     */
    public static final int ARTICLE_STATUS_NORMAL = 0;

    /**
     * 分类是正常状态
     */
    public static final String STATUS_NORMAL = "0";

    /**
     * 友链审核状态为通过
     */
    public static final String LINK_STATUS_NORMAL = "0";

    /**
     * 该评论为根评论
     */
    public static final Long ROOT_ID = -1L;

    /**
     * 评论类型为文章评论
     */
    public static final String ARTICLE_COMMENT = "0";

    /**
     * 评论类型为友链评论
     */
    public static final String LINK_COMMENT = "1";

    /**
     * 根据key值更新redis中对应文章的浏览量
     */
    public static final String ARTICLE_VIEW_COUNT = "article:viewCount";

    /**
     * 菜单和按钮
     */
    public static final String MENU = "C";
    public static final String BUTTON = "F";

    /**
     * 分类是正常可用状态
     */
    public static final String NORMAL = "0";

    public static final String ADMIN = "1";
}
