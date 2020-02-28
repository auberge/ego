package com.ego.manage.service;

import com.ego.commons.pojo.EasyUITree;
import com.ego.commons.pojo.EgoResult;
import com.ego.pojo.TbContentCategory;

import java.util.List;

public interface TbContentCategoryService {
    /**
     * 查询所有类目并转换成easyUITree的属性要求
     * id
     */
    List<EasyUITree> showCatefory(long id);

    /**
     * 类目新增
     * category
     */
    EgoResult create(TbContentCategory category);

    /**
     * 类目修改/重命名
     * category
     */
    EgoResult update(TbContentCategory category);

    /**
     * 删除类目
     * category
     */
    EgoResult delete(TbContentCategory category);
}
