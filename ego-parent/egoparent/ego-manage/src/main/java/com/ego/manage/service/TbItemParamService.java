package com.ego.manage.service;

import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.commons.pojo.EgoResult;
import com.ego.pojo.TbItemParam;

public interface TbItemParamService {
    /**
     * 显示商品规格参数
     * page,rows
     */
    EasyUIDataGrid showPage(int page, int rows);

    /**
     * 批量删除规格参数
     * ids
     */
    int delete(String ids) throws Exception;

    /**
     * 根据类目查询模板信息
     * catId
     */
    EgoResult showParam(long catId);

    /**
     * 新增规格模板信息
     * param
     */
    EgoResult save(TbItemParam param);
}
