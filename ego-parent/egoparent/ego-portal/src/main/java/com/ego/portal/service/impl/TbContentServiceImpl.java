package com.ego.portal.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.commons.utils.JsonUtils;
import com.ego.portal.service.TbContentService;
import com.ego.dubbo.service.TbContentDubboService;
import com.ego.pojo.TbContent;
import com.ego.redis.dao.JedisDao;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TbContentServiceImpl implements TbContentService {
    @Reference
    private TbContentDubboService tbContentDubboServiceImpl;
    @Resource
    private JedisDao jedisDaoImpl;
    @Value("${redis.bigPic.key}")
    private String key;

    @Override
    public String showBigPic() {
        if (jedisDaoImpl.exists(key)) {
            String value = jedisDaoImpl.get(key);
            if (value != null && !value.equals("")) {
                return value;
            }
        }
        List<TbContent> list = tbContentDubboServiceImpl.selByCount(6, true,89);
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (TbContent content : list) {
            Map<String, Object> map = new HashMap<>();
            map.put("srcB", content.getPic2());
            map.put("height", 240);
            map.put("alt", "图片未加载");
            map.put("width", 670);
            map.put("src", content.getPic());
            map.put("widthB", 500);
            map.put("href", content.getUrl());
            map.put("heightB", 240);
            mapList.add(map);
        }
        String listJson = JsonUtils.objectToJson(mapList);
        //把数据存储到redis中
        jedisDaoImpl.set(key, listJson);
        return listJson;
    }
}
