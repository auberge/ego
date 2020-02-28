package com.ego.dubbo.service;

import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.pojo.TbItem;
import com.ego.pojo.TbItemDesc;
import com.ego.pojo.TbItemParamItem;

import java.util.List;

public interface TbItemDubboService {

    /**
     * 商品分页查询
     * page，rows
     */
    EasyUIDataGrid show(int page, int rows);

    /**
     * 根据id修改状态
     * id，status
     */
    int updItemStatus(TbItem tbItem);

    /**
     * 新增TbItem
     * item
     */
    int insTbItem(TbItem item);

    /**
     * 新增包含商品表和商品描述表表
     * tbItem,tbItemDesc
     */
    int insTbItemDesc(TbItem tbItem, TbItemDesc tbItemDesc, TbItemParamItem paramItem) throws Exception;

    /**
     * 通过状态查询全部可用数据
     */
    List<TbItem> selAllByStatus(byte status);

    /**
     * 根据主键查询商品信息
     */
    TbItem selById(long id);
}
