package com.ego.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.commons.pojo.EasyUITree;
import com.ego.commons.pojo.EgoResult;
import com.ego.commons.utils.IDUtils;
import com.ego.dubbo.service.TbContentCategoryDubboService;
import com.ego.manage.service.TbContentCategoryService;
import com.ego.pojo.TbContentCategory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TbContentCategoryServiceImpl implements TbContentCategoryService {
    @Reference
    private TbContentCategoryDubboService tbContentCategoryDubboServiceImpl;

    @Override
    public List<EasyUITree> showCatefory(long id) {
        List<EasyUITree> treeList = new ArrayList<>();
        List<TbContentCategory> list = tbContentCategoryDubboServiceImpl.selByParentId(id);
        for (TbContentCategory category : list) {
            EasyUITree tree = new EasyUITree();
            tree.setId(category.getId());
            tree.setState(category.getIsParent() ? "closed" : "open");
            tree.setText(category.getName());
            treeList.add(tree);
        }
        return treeList;
    }

    @Override
    public EgoResult create(TbContentCategory category) {
        EgoResult result = new EgoResult();
        //判断当前节点名称是否已经存在
        List<TbContentCategory> children = tbContentCategoryDubboServiceImpl.selByParentId(category.getParentId());
        for (TbContentCategory child : children) {
            if (child.getName().equals(category.getName())) {
                result.setData("该分类名称已存在");
                return result;
            }
        }
        long id = IDUtils.genItemId();
        Date date = new Date();
        category.setCreated(date);
        category.setUpdated(date);
        category.setStatus(1);
        category.setId(id);
        category.setSortOrder(1);
        category.setIsParent(false);
        int index = tbContentCategoryDubboServiceImpl.insTbContentCategory(category);
        if (index > 0) {
            TbContentCategory parent = new TbContentCategory();
            parent.setId(category.getParentId());
            parent.setIsParent(true);
            parent.setUpdated(date);
            index += tbContentCategoryDubboServiceImpl.updTbContentCategory(parent);
        }
        if (index == 2) {
            Map<String, Long> map = new HashMap<>();
            map.put("id", id);
            result.setStatus(200);
            result.setData(map);
        }
        return result;
    }

    @Override
    public EgoResult update(TbContentCategory category) {
        EgoResult result = new EgoResult();
        Date date = new Date();
        category.setUpdated(date);
        TbContentCategory contentCategory = tbContentCategoryDubboServiceImpl.selById(category.getId());
        List<TbContentCategory> children = tbContentCategoryDubboServiceImpl.selByParentId(contentCategory.getParentId());
        for (TbContentCategory child : children) {
            if (child.getName().equals(category.getName())) {
                result.setData("该分类名称已存在");
                return result;
            }
        }
        int index = tbContentCategoryDubboServiceImpl.updTbContentCategory(category);
        if (index > 0) {
            result.setStatus(200);
        }
        return result;
    }

    @Override
    public EgoResult delete(TbContentCategory category) {
        EgoResult result = new EgoResult();
        Date date = new Date();
        category.setStatus(2);
        category.setUpdated(date);
        int index = tbContentCategoryDubboServiceImpl.updTbContentCategory(category);
        if (index > 0) {
            TbContentCategory contentCategory = tbContentCategoryDubboServiceImpl.selById(category.getId());
            List<TbContentCategory> list = tbContentCategoryDubboServiceImpl.selByParentId(contentCategory.getParentId());
            if (list == null || list.size() == 0) {
                TbContentCategory parent = new TbContentCategory();
                parent.setId(contentCategory.getParentId());
                parent.setIsParent(false);
                parent.setUpdated(date);
                index += tbContentCategoryDubboServiceImpl.updTbContentCategory(parent);
                if (index > 1) {
                    result.setStatus(200);
                }
            } else {
                result.setStatus(200);
            }
        }
        return result;
    }
}
