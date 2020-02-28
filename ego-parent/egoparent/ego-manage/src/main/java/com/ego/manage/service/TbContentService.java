package com.ego.manage.service;

import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.pojo.TbContent;

public interface TbContentService {
    /**
     * 分页显示内容信息
     * categoryId,page,rows
     */
    EasyUIDataGrid showContent(long categoryId, int page, int rows);

    /**
     * 新增内容
     * content
     */
    int save(TbContent content);

    /**
     * 批量删除内容
     */
    int delete(String ids) throws Exception;
}
