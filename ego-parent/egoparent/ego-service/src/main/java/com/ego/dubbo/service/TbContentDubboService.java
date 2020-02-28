package com.ego.dubbo.service;

import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.pojo.TbContent;

import java.util.List;

public interface TbContentDubboService {
    /**
     * 分页显示查询
     * categoryId,page,rows
     */
    EasyUIDataGrid selContentByPage(long categoryId, int page, int rows);

    /**
     * 新增内容
     * content
     */
    int insContent(TbContent content);

    /**
     * 查询出最近的前n个内容
     * count,isSort,categoryId
     * */
    List<TbContent> selByCount(int count,boolean isSort,long categoryId);

    /**
     * 批量删除内容
     * */
    int delContentById(String ids) throws Exception;
}
