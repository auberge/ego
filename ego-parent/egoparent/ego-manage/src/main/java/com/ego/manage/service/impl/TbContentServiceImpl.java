package com.ego.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.manage.service.TbContentService;
import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.commons.utils.JsonUtils;
import com.ego.dubbo.service.TbContentDubboService;
import com.ego.pojo.TbContent;
import com.ego.redis.dao.JedisDao;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class TbContentServiceImpl implements TbContentService {
    @Reference
    private TbContentDubboService tbContentDubboServiceImpl;
    @Resource
    private JedisDao jedisDaoImpl;
    @Value("${redis.bigPic.key}")
    private String key;


    @Override
    public EasyUIDataGrid showContent(long categoryId, int page, int rows) {
        return tbContentDubboServiceImpl.selContentByPage(categoryId, page, rows);
    }

    @Override
    public int save(TbContent content) {
        Date date = new Date();
        content.setCreated(date);
        content.setUpdated(date);

        boolean exists = jedisDaoImpl.exists(key);
        if (exists && content.getCategoryId() == 89) {
            String value = jedisDaoImpl.get(key);
            if (value != null && !value.equals("")) {
                List<HashMap> list = JsonUtils.jsonToList(value, HashMap.class);
                HashMap<String, Object> map = new HashMap<>();
                map.put("srcB", content.getPic2());
                map.put("height", 240);
                map.put("alt", "图片未加载");
                map.put("width", 670);
                map.put("src", content.getPic());
                map.put("widthB", 500);
                map.put("href", content.getUrl());
                map.put("heightB", 240);

                if (list.size() == 6) {
                    list.remove(5);
                }
                list.add(0, map);
                jedisDaoImpl.set(key, JsonUtils.objectToJson(list));
            }
        }
        return tbContentDubboServiceImpl.insContent(content);
    }

    @Override
    public int delete(String ids) throws Exception {
        return tbContentDubboServiceImpl.delContentById(ids);
    }
}
