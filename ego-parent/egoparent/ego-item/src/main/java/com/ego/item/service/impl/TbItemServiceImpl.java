package com.ego.item.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.commons.pojo.TbItemChild;
import com.ego.commons.utils.JsonUtils;
import com.ego.dubbo.service.TbItemDubboService;
import com.ego.item.service.TbItemService;
import com.ego.pojo.TbItem;
import com.ego.redis.dao.JedisDao;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class TbItemServiceImpl implements TbItemService {
    @Reference
    private TbItemDubboService tbItemDubboServiceImpl;
    @Resource
    private JedisDao jedisDaoImpl;
    @Value("${redis.item.key}")
    private String itemKey;

    @Override
    public TbItemChild show(long id) {
        String key = itemKey + id;
        if (jedisDaoImpl.exists(key)) {
            String json = jedisDaoImpl.get(key);
            if (json != null && !json.equals("")) {
                return JsonUtils.jsonToPojo(json, TbItemChild.class);
            }
        }
        TbItem item = tbItemDubboServiceImpl.selById(id);
        TbItemChild itemChild = new TbItemChild();
        itemChild.setId(item.getId());
        itemChild.setTitle(item.getTitle());
        itemChild.setPrice(item.getPrice());
        itemChild.setSellPoint(item.getSellPoint());
        itemChild.setImages(item.getImage() != null && !item.getImage().equals("") ? item.getImage().split(",") : new String[1]);
        //存到redis数据库中
        jedisDaoImpl.set(key, JsonUtils.objectToJson(itemChild));
        return itemChild;
    }
}
