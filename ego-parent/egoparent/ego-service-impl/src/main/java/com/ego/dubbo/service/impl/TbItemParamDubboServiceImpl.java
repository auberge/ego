package com.ego.dubbo.service.impl;

import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.dubbo.service.TbItemParamDubboService;
import com.ego.mapper.TbItemParamMapper;
import com.ego.pojo.TbItemParam;
import com.ego.pojo.TbItemParamExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import javax.annotation.Resource;
import java.util.List;

public class TbItemParamDubboServiceImpl implements TbItemParamDubboService {
    @Resource
    private TbItemParamMapper tbItemParamMapper;

    @Override
    public EasyUIDataGrid showPage(int page, int rows) {
        //设置分页条件
        PageHelper.startPage(page, rows);
        //设置查询的sql语句
        //xxxExample()设置了什么，相当于sql中where从句中添加条件

        //如果表中有一个或一个以上的列是text类型，生成的方法xxxxWithBolbs（）
        //如果使用xxxxxWithBolbs（）查询结果中包含带有text类的列，如果没有使用withBolbs（）方法不带有text类型
        List<TbItemParam> list = tbItemParamMapper.selectByExampleWithBLOBs(new TbItemParamExample());
        //根据程序员自己编写sql语句结合分页插件产生最终结果，封装到PageInfo中
        PageInfo<TbItemParam> info = new PageInfo<>(list);

        //设置方法返回结果
        EasyUIDataGrid dataGrid = new EasyUIDataGrid();
        dataGrid.setRows(info.getList());
        dataGrid.setTotal(info.getTotal());
        return dataGrid;
    }

    @Override
    public int delByIds(String ids) throws Exception {
        int index = 0;
        String[] idStr = ids.split(",");
        for (String id : idStr) {
            index += tbItemParamMapper.deleteByPrimaryKey(Long.parseLong(id));
        }
        if (index==idStr.length){
            return 1;
        }else {
            throw new Exception("删除失败，原因：数据可能不存在");
        }
    }

    @Override
    public TbItemParam selByCatId(long catId) {
        TbItemParamExample example = new TbItemParamExample();
        example.createCriteria().andItemCatIdEqualTo(catId);
        List<TbItemParam> list = tbItemParamMapper.selectByExampleWithBLOBs(example);
        if (list!=null&&list.size()>0)
            //要求每个类目只能有一个模板
            return list.get(0);
        return null;
    }

    @Override
    public int insParam(TbItemParam param) {
        return tbItemParamMapper.insertSelective(param);
    }
}
