package com.ego.manage.controller;

import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.commons.pojo.EgoResult;
import com.ego.manage.service.TbItemParamService;
import com.ego.pojo.TbItemParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class TbItemParamController {
    @Resource
    private TbItemParamService tbItemParamServiceimpl;

    /**
     * 规格参数分页显示
     * page,rows
     */
    @RequestMapping("item/param/list")
    @ResponseBody
    public EasyUIDataGrid showPage(int page, int rows) {
        return tbItemParamServiceimpl.showPage(page, rows);
    }

    /**
     * 批量删除规格参数
     * ids
     */
    @RequestMapping("item/param/delete")
    @ResponseBody
    public EgoResult delete(String ids) {
        EgoResult result = new EgoResult();
        try {
            int index = tbItemParamServiceimpl.delete(ids);
            if (index == 1) {
                result.setStatus(200);
            }
        } catch (Exception e) {
//            e.printStackTrace();
            result.setData(e.getMessage());
        }
        return result;
    }

    /**
     * 点击商品类目按钮显示添加分组按钮
     * 判断是否已经添加模板
     * catId
     * */
    @RequestMapping("item/param/query/itemcatid/{catId}")
    @ResponseBody
    public EgoResult show(@PathVariable long catId) {
        return tbItemParamServiceimpl.showParam(catId);
    }

    /**
     * 添加规格参数模板
     *
     * */
    @RequestMapping("item/param/save/{catId}")
    @ResponseBody
    public EgoResult save(TbItemParam param,@PathVariable long catId) {
        param.setItemCatId(catId);
        return tbItemParamServiceimpl.save(param);
    }
}
