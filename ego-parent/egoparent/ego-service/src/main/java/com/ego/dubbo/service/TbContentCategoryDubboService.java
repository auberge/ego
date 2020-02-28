package com.ego.dubbo.service;

import com.ego.pojo.TbContentCategory;

import java.util.List;

public interface TbContentCategoryDubboService {
    /**
     * 根据父类目id查询子类目
     * id
     */
    List<TbContentCategory> selByParentId(long id);

    /**
     * 新增
     * category
     * */
    int insTbContentCategory(TbContentCategory category);

    /**
     * 根据id修改isParent属性
     * id,isParent
     * */
    int updTbContentCategory (TbContentCategory category);

    /**
     * 根据id查询内容类目详细信息
     * id
     * */
    TbContentCategory selById(long id);
}
