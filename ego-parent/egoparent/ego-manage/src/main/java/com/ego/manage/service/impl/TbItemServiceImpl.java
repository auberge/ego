package com.ego.manage.service.impl;


import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.commons.utils.HttpClientUtil;
import com.ego.commons.utils.IDUtils;
import com.ego.commons.utils.JsonUtils;
import com.ego.dubbo.service.TbItemDescDubboService;
import com.ego.dubbo.service.TbItemDubboService;
import com.ego.manage.service.TbItemService;
import com.ego.pojo.TbItem;
import com.ego.pojo.TbItemDesc;
import com.ego.pojo.TbItemParamItem;
import com.ego.redis.dao.JedisDao;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class TbItemServiceImpl implements TbItemService {
    @Reference
    private TbItemDubboService tbItemDubboServiceimpl;
    @Reference
    private TbItemDescDubboService tbItemDescDubboServiceImpl;
    @Resource
    private JedisDao jedisDaoImpl;
    @Value("${search.url}")
    private String url;
    @Value("${redis.item.key}")
    private String itemKey;

    @Override
    public EasyUIDataGrid show(int page, int rows) {
        return tbItemDubboServiceimpl.show(page, rows);
    }

    @Override
    public int update(String ids, byte status) {
        TbItem item = new TbItem();
        String[] idsStr = ids.split(",");
        int index = 0;
        for (String id : idsStr) {
            item.setId(Long.parseLong(id));
            item.setStatus(status);
            index += tbItemDubboServiceimpl.updItemStatus(item);
            if (status == 2 || status == 3) {
                System.out.println(itemKey + id);
                jedisDaoImpl.del(itemKey + id);
            }
        }
        if (index == idsStr.length) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public int save(TbItem item, String desc) {
        long id = IDUtils.genItemId();
        item.setId(id);
        Date date = new Date();
        item.setCreated(date);
        item.setUpdated(date);
        item.setStatus((byte) 1);
        int index = tbItemDubboServiceimpl.insTbItem(item);
        if (index > 0) {
            TbItemDesc itemDesc = new TbItemDesc();
            itemDesc.setItemDesc(desc);
            itemDesc.setItemId(id);
            itemDesc.setCreated(date);
            itemDesc.setUpdated(date);
            index += tbItemDescDubboServiceImpl.insDesc(itemDesc);
        }
        if (index == 2) {
            return 1;
        }
        return 0;
    }

    @Override
    public int saveTrans(TbItem item, String desc, String itemParam) throws Exception {
        final TbItem itemFinal = item;
        final String descFinal = desc;

        long id = IDUtils.genItemId();
        item.setId(id);
        Date date = new Date();
        item.setCreated(date);
        item.setUpdated(date);
        item.setStatus((byte) 1);

        TbItemDesc itemDesc = new TbItemDesc();
        itemDesc.setItemDesc(desc);
        itemDesc.setItemId(id);
        itemDesc.setCreated(date);
        itemDesc.setUpdated(date);

        TbItemParamItem paramItem = new TbItemParamItem();
        paramItem.setCreated(date);
        paramItem.setUpdated(date);
        paramItem.setItemId(id);
        paramItem.setParamData(itemParam);

        new Thread() {
            @Override
            public void run() {
                Map<String, Object> map = new HashMap<>();
                map.put("item", itemFinal);
                map.put("desc", descFinal);
                HttpClientUtil.doPostJson(url, JsonUtils.objectToJson(map));
            }
        }.start();

        int index = tbItemDubboServiceimpl.insTbItemDesc(item, itemDesc, paramItem);
        System.out.println(url);

        return index;
    }
}
