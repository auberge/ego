package com.ego.search.controller;

import com.ego.search.service.TbItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
public class TbItemController {
    @Resource
    private TbItemService tbItemServiceImpl;

    /**
     * 初始化数据
     */
    @RequestMapping(value = "solr/init", produces = "text/html;charset=utf-8")
    @ResponseBody
    public String init() {
        long start = System.currentTimeMillis();
        try {
            tbItemServiceImpl.init();
            long end = System.currentTimeMillis();
            return "初始化总时间" + (end - start) / 1000 + "秒";
        } catch (Exception e) {
            e.printStackTrace();
            return "初始化失败！";
        }
    }

    /**
     * 搜索功能
     */
    @RequestMapping("search.html")
    public String search(Model model, String q, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "12") int rows) {
        try {
            q = new String(q.getBytes("iso-8859-1"), "utf-8");
            Map<String, Object> map = tbItemServiceImpl.selByQuery(q, page, rows);
            model.addAttribute("query", q);
            model.addAttribute("itemList", map.get("itemList"));
            model.addAttribute("totalPages", map.get("totalPages"));
            model.addAttribute("page", page);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "search";
    }

    /**
     * 新增，返回值为1成功
     */
    @RequestMapping("solr/add")
    @ResponseBody
    public int add(@RequestBody Map<String, Object> map) {
        System.out.println(map);
        try {
            return tbItemServiceImpl.add((LinkedHashMap) map.get("item"), map.get("desc").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
