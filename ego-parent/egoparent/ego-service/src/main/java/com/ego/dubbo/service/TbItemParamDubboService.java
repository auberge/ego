package com.ego.dubbo.service;

import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.pojo.TbItemParam;

public interface TbItemParamDubboService {
    /**
     * 分页查询数据
     * page,rows
     * return 当前页显示数据和总条数
     */
    EasyUIDataGrid showPage(int page, int rows);

    /**
     * 批量删除规格参数
     * ids
     */
    int delByIds(String ids) throws Exception;

    /**
     * 分局类目id查询参数模板
     * catId
     */
    TbItemParam selByCatId(long catId);

    /**
     * 新增规格参数（支持主键自增）
     * param
     */
    int insParam(TbItemParam param);
}
