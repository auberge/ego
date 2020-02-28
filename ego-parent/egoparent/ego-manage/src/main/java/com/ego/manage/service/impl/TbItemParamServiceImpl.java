package com.ego.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.manage.pojo.TbItemParamChild;
import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.commons.pojo.EgoResult;
import com.ego.dubbo.service.TbItemCatDubboService;
import com.ego.dubbo.service.TbItemParamDubboService;
import com.ego.manage.service.TbItemParamService;
import com.ego.pojo.TbItemParam;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TbItemParamServiceImpl implements TbItemParamService {
    @Reference
    private TbItemParamDubboService tbItemParamDubboServiceImpl;

    @Reference
    private TbItemCatDubboService tbItemCatDubboServiceImpl;

    @Override
    public EasyUIDataGrid showPage(int page, int rows) {
        EasyUIDataGrid dataGrid = tbItemParamDubboServiceImpl.showPage(page, rows);
        List<TbItemParam> list = (List<TbItemParam>) dataGrid.getRows();
        List<TbItemParamChild> childList = new ArrayList<>();
        for (TbItemParam param : list) {
            TbItemParamChild child = new TbItemParamChild();

            child.setId(param.getId());
            child.setItemCatId(param.getItemCatId());
            child.setParamData(param.getParamData());
            child.setItemCatName(tbItemCatDubboServiceImpl.selById(child.getItemCatId()).getName());
            child.setCreated(param.getCreated());
            child.setUpdated(param.getUpdated());
            childList.add(child);
        }
        dataGrid.setRows(childList);
        return dataGrid;
    }

    @Override
    public int delete(String ids) throws Exception {
        return tbItemParamDubboServiceImpl.delByIds(ids);
    }

    @Override
    public EgoResult showParam(long catId) {
        EgoResult result=new EgoResult();
        TbItemParam param = tbItemParamDubboServiceImpl.selByCatId(catId);
        if (param!=null){
            result.setStatus(200);
            result.setData(param);
        }
        return result;
    }

    @Override
    public EgoResult save(TbItemParam param) {
        EgoResult result=new EgoResult();
        Date date=new Date();
        param.setCreated(date);
        param.setUpdated(date);
        int index = tbItemParamDubboServiceImpl.insParam(param);
        if (index>0){
            result.setStatus(200);
        }
        return result;
    }
}
