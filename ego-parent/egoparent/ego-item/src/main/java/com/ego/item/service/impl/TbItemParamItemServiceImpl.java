package com.ego.item.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.item.pojo.ParamItem;
import com.ego.commons.utils.JsonUtils;
import com.ego.dubbo.service.TbItemParamItemDubboService;
import com.ego.item.service.TbItemParamItemService;
import com.ego.pojo.TbItemParamItem;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TbItemParamItemServiceImpl implements TbItemParamItemService {
    @Reference
    private TbItemParamItemDubboService tbItemParamItemDubboServiceImpl;

    @Override
    public String showParam(long itemId) {
        TbItemParamItem item = tbItemParamItemDubboServiceImpl.selByItemId(itemId);
        List<ParamItem> list = JsonUtils.jsonToList(item.getParamData(), ParamItem.class);
        System.out.println(list);
        StringBuffer sf = new StringBuffer();
        for (ParamItem paramItem : list) {
            sf.append("<table width='500'>");
            sf.append("<tr>");
            for (int i = 0; i < paramItem.getParams().size(); i++) {
                if (i == 0) {
                    sf.append("<tr>");
                    sf.append("<td align='right' width='30%'>" + paramItem.getGroup() + "</td>");
                    sf.append("<td align='right' width='30%'>" + paramItem.getParams().get(i).getK() + "</td>");
                    sf.append("<td align='left' width='30%'>" + paramItem.getParams().get(i).getV() + "</td>");
                    sf.append("</tr>");
                }else {
                    sf.append("<tr>");
                    sf.append("<td> </td>");
                    sf.append("<td align='right' width='30%'>" + paramItem.getParams().get(i).getK() + "</td>");
                    sf.append("<td align='left' width='30%'>" + paramItem.getParams().get(i).getV() + "</td>");
                    sf.append("</tr>");
                }
            }
            sf.append("</tr>");
            sf.append("</table");
            sf.append("<hr/>");
        }

        return sf.toString();
    }
}
