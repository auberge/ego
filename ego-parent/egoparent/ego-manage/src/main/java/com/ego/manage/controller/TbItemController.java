package com.ego.manage.controller;

import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.commons.pojo.EgoResult;
import com.ego.manage.service.TbItemService;
import com.ego.pojo.TbItem;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class TbItemController {
    @Resource
    private TbItemService tbItemServiceimpl;

    /**
     * 分页显示商品
     */
    @RequestMapping("item/list")
    @ResponseBody
    public EasyUIDataGrid show(int page, int rows) {
        return tbItemServiceimpl.show(page, rows);
    }

    /**
     * 显示商品修改页面
     */
    @RequestMapping("rest/page/item-edit")
    public String showItemEdit() {
        return "item-edit";
    }

    /**
     * 批量删除商品
     * ids
     */
    @RequestMapping("rest/item/delete")
    @ResponseBody
    public EgoResult delete(String ids) {
        EgoResult egoResult = new EgoResult();
        int index = tbItemServiceimpl.update(ids, (byte) 3);
        if (index == 1) {
            egoResult.setStatus(200);
        }
        return egoResult;
    }

    /**
     * 批量上架商品
     * ids
     */
    @RequestMapping("rest/item/reshelf")
    @ResponseBody
    public EgoResult reshelf(String ids) {
        EgoResult egoResult = new EgoResult();
        int index = tbItemServiceimpl.update(ids, (byte) 1);
        if (index == 1) {
            egoResult.setStatus(200);
        }
        return egoResult;
    }

    /**
     * 批量下架商品
     * ids
     */
    @RequestMapping("rest/item/instock")
    @ResponseBody
    public EgoResult instock(String ids) {
        EgoResult egoResult = new EgoResult();
        int index = tbItemServiceimpl.update(ids, (byte) 2);
        if (index == 1) {
            egoResult.setStatus(200);
        }
        return egoResult;
    }

    /**
     * 商品新增
     * item,desc(描述）
     */
    @RequestMapping("item/save")
    @ResponseBody
    public EgoResult insert(TbItem item, String desc, String itemParams) {
        EgoResult result = new EgoResult();
        int index = 0;
        try {
            index = tbItemServiceimpl.saveTrans(item, desc, itemParams);
        } catch (Exception e) {
            //e.printStackTrace();
            result.setData(e.getMessage());
        }
        if (index == 1) {
            result.setStatus(200);
        }
        return result;
    }
}
