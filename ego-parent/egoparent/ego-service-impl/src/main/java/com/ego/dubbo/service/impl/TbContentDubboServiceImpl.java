package com.ego.dubbo.service.impl;

import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.dubbo.service.TbContentDubboService;
import com.ego.mapper.TbContentMapper;
import com.ego.pojo.TbContent;
import com.ego.pojo.TbContentExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import javax.annotation.Resource;
import java.util.List;

public class TbContentDubboServiceImpl implements TbContentDubboService {
    @Resource
    private TbContentMapper tbContentMapper;
    @Override
    public EasyUIDataGrid selContentByPage(long categoryId, int page, int rows) {
        PageHelper.startPage(page,rows);
        TbContentExample example=new TbContentExample();
        if (categoryId!=0){
            example.createCriteria().andCategoryIdEqualTo(categoryId);
        }
        List<TbContent> list = tbContentMapper.selectByExampleWithBLOBs(example);
        PageInfo<TbContent> info=new PageInfo<>(list);
        EasyUIDataGrid dataGrid=new EasyUIDataGrid();
        dataGrid.setTotal(info.getTotal());
        dataGrid.setRows(info.getList());
        return dataGrid;
    }

    @Override
    public int insContent(TbContent content) {
        return tbContentMapper.insertSelective(content);
    }

    @Override
    public List<TbContent> selByCount(int count, boolean isSort,long categoryId) {
        TbContentExample example = new TbContentExample();
        if (isSort) {
            //排序
            example.setOrderByClause("updated desc");
        }
        if (count != 0) {
            PageHelper.startPage(1, count);
            example.createCriteria().andCategoryIdEqualTo(categoryId);
            List<TbContent> list = tbContentMapper.selectByExampleWithBLOBs(example);
            PageInfo<TbContent> info = new PageInfo<>(list);
            return info.getList();
        } else {
            return tbContentMapper.selectByExampleWithBLOBs(example);
        }
    }

    @Override
    public int delContentById(String ids) throws Exception {
        int index=0;
        String[] idStr = ids.split(",");
        for (String id : idStr) {
            index += tbContentMapper.deleteByPrimaryKey(Long.parseLong(id));
        }
        if (index==idStr.length){
            return 1;
        }else{
            throw new Exception("删除失败，原因：数据可能不存在");
        }
    }
}
