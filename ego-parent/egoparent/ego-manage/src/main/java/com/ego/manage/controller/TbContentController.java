package com.ego.manage.controller;

import com.ego.manage.service.TbContentService;
import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.commons.pojo.EgoResult;
import com.ego.pojo.TbContent;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class TbContentController {
    @Resource
    private TbContentService tbContentServiceImpl;

    /**
     * 分页显示内容信息
     * categoryId,page,rows
     */
    @RequestMapping("content/query/list")
    @ResponseBody
    public EasyUIDataGrid showContent(long categoryId, int page, int rows) {
        return tbContentServiceImpl.showContent(categoryId, page, rows);
    }

    /**
     * 内容新增
     * content
     */
    @RequestMapping("content/save")
    @ResponseBody
    public EgoResult save(TbContent content) {
        EgoResult result = new EgoResult();
        int index = tbContentServiceImpl.save(content);
        if (index > 0) {
            result.setStatus(200);
        }
        return result;
    }
    @RequestMapping("content/delete")
    @ResponseBody
    public EgoResult delete(String ids) {
        EgoResult result = new EgoResult();
        try {
            int index = tbContentServiceImpl.delete(ids);
            if (index == 1) {
                result.setStatus(200);
            }
        } catch (Exception e) {
            result.setData(e.getMessage());
        }
        return result;
    }
}
