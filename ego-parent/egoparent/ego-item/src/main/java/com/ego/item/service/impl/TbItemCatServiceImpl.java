package com.ego.item.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.dubbo.service.TbItemCatDubboService;
import com.ego.item.pojo.PortalMenu;
import com.ego.item.pojo.PortalMenuNode;
import com.ego.item.service.TbItemCatService;
import com.ego.pojo.TbItemCat;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TbItemCatServiceImpl implements TbItemCatService {
    @Reference
    private TbItemCatDubboService tbItemCatDubboServiceImpl;

    @Override
    public PortalMenu showCatMenu() {
        //查询出所有的一级菜单
        List<TbItemCat> list = tbItemCatDubboServiceImpl.show(0);

        PortalMenu menu = new PortalMenu();
        menu.setData(selAllMenu(list));
        return menu;
    }

    /**
     * 最终返回结果所有查询到的结果
     */

    public List<Object> selAllMenu(List<TbItemCat> list) {
        List<Object> nodeList = new ArrayList<>();
        for (TbItemCat item : list) {
            if (item.getIsParent()) {
                PortalMenuNode node = new PortalMenuNode();
                node.setU("/products/" + item.getId() + ".html");
                node.setN("<a href='/products/" + item.getId() + ".html'>" + item.getName() + "</a>");
                node.setI(selAllMenu(tbItemCatDubboServiceImpl.show(item.getId())));
                nodeList.add(node);
            } else {
                nodeList.add("/products/" + item.getId() + ".html|" + item.getName());
            }
        }
        return nodeList;
    }
}
