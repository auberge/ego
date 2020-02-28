package com.ego.manage.service;

import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.pojo.TbItem;

public interface TbItemService {
    /**
     * 显示商品
     * page,rows
     */
    EasyUIDataGrid show(int page, int rows);

    /**
     * 批量修改商品状态
     * ids,status
     */
    int update(String ids, byte status);

    /**
     * 商品新增(no transaction)
     * item,desc
     */
    int save(TbItem item, String desc);

    /**
     * 商品新增
     * item,desc
     */
    int saveTrans(TbItem item, String desc, String ParamItem) throws Exception;
}
