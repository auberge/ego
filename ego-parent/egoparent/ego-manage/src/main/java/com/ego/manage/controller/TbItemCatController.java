package com.ego.manage.controller;

import com.ego.commons.pojo.EasyUITree;
import com.ego.manage.service.TbItemCatService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class TbItemCatController {
    @Resource
    private TbItemCatService tbItemCatServiceImpl;

    /**
     * 显示商品类目
     * id(默认值为0)
     */
    @RequestMapping("item/cat/list")
    @ResponseBody
    public List<EasyUITree> show(@RequestParam(defaultValue = "0") long id) {
        return tbItemCatServiceImpl.show(id);
    }
}
