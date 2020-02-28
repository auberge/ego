package com.ego.manage.controller;

import com.ego.commons.pojo.EasyUITree;
import com.ego.commons.pojo.EgoResult;
import com.ego.manage.service.TbContentCategoryService;
import com.ego.pojo.TbContentCategory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class TbContentCategoryController {
    @Resource
    private TbContentCategoryService tbContentCategoryServiceImpl;

    /**
     * 查询显示内容类目
     * id
     */
    @RequestMapping("content/category/list")
    @ResponseBody
    public List<EasyUITree> showCategory(@RequestParam(defaultValue = "0") long id) {
        return tbContentCategoryServiceImpl.showCatefory(id);
    }

    /**
     * 新增内容类目
     * category
     */
    @RequestMapping("content/category/create")
    @ResponseBody
    public EgoResult create(TbContentCategory category) {
        return tbContentCategoryServiceImpl.create(category);
    }

    /**
     * 重命名类目名称
     * category
     */
    @RequestMapping("content/category/update")
    @ResponseBody
    public EgoResult update(TbContentCategory category) {
        return tbContentCategoryServiceImpl.update(category);
    }

    /**
     * 删除类目
     * category
     */
    @RequestMapping("content/category/delete")
    @ResponseBody
    public EgoResult delete(TbContentCategory category) {
        return tbContentCategoryServiceImpl.delete(category);
    }


}
