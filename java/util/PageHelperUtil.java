package com.post.util;

import com.github.pagehelper.Page;

import java.util.List;

/**
 * @author Barkley.Chang
 * @className:PageHelperUtil
 * @description: TODO
 * @date 2021-03-19 08:47
 */
public final class PageHelperUtil {
    private PageHelperUtil() {
    }

    /**
     * 利用PageHelper插件进行手工分页请求
     *
     * @param pageNum  指定页码
     * @param pageSize 指定每页记录数
     * @param list     待分页集合
     * @param <T>      集合类型
     * @return 分页后的集合
     */
    public static <T> List<T> pageRequest(int pageNum, int pageSize, List<T> list) {
        // 根据pageNum和pageSize构建Page类
        Page<T> page = new Page<T>(pageNum, pageSize);
        // 设置page对象的总记录数属性
        page.setTotal(list.size());
        // 计算分页的开始和结束索引
        int startIndex = (pageNum - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, list.size());
        // 从待分页的集合获取需要展示内容添加到page对象
        page.addAll(list.subList(startIndex, endIndex));
        // 返回分页后的集合
        return page.getResult();
    }
}