package com.ego.dubbo.service;

import com.ego.pojo.TbItemDesc;

public interface TbItemDescDubboService {
    /**
     * 新增TbItemDesc
     * itemDesc
     */
    int insDesc(TbItemDesc itemDesc);

    /**
     * 根据主键查询商品描述对象
     * */
    TbItemDesc selByItemId(long itemId);
}
