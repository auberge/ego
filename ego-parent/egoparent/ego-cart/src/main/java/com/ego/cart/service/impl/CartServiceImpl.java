package com.ego.cart.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.cart.service.CartService;
import com.ego.commons.pojo.CookieUtils;
import com.ego.commons.pojo.EgoResult;
import com.ego.commons.pojo.TbItemChild;
import com.ego.commons.utils.HttpClientUtil;
import com.ego.commons.utils.JsonUtils;
import com.ego.dubbo.service.TbItemDubboService;
import com.ego.pojo.TbItem;
import com.ego.redis.dao.JedisDao;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Reference
    private TbItemDubboService tbItemDubboServiceImpl;
    @Resource
    private JedisDao jedisDaoImpl;
    @Value("${sign.url}")
    private String signUrl;
    @Value("${cart.key}")
    private String cartKey;

    @Override
    public void addCart(long id, int num, HttpServletRequest request) {
        //集合中存放购物车中所有商品
        List<TbItemChild> list = new ArrayList<>();
        String key = getToken(request);
        if (jedisDaoImpl.exists(key)) {
            String json = jedisDaoImpl.get(key);
            if (json != null && !json.equals("")) {
                list = JsonUtils.jsonToList(json, TbItemChild.class);
                for (TbItemChild child : list) {
                    if ((long)child.getId() == id) {
                        //购物车中有该商品
                        child.setNum(child.getNum() + num);
                        //重新添加到redis中
                        jedisDaoImpl.set(key, JsonUtils.objectToJson(list));
                        return;
                    }
                }
            }
        }
        TbItemChild child = getChild(id, num);
        list.add(child);
        jedisDaoImpl.set(key, JsonUtils.objectToJson(list));
    }

    @Override
    public List<TbItemChild> showCat(HttpServletRequest request) {
        String key = getToken(request);
        String json = jedisDaoImpl.get(key);
        return JsonUtils.jsonToList(json,TbItemChild.class);
    }

    @Override
    public EgoResult update(long id, int num,HttpServletRequest request) {
        String key = getToken(request);
        String json = jedisDaoImpl.get(key);
        List<TbItemChild> list = JsonUtils.jsonToList(json, TbItemChild.class);
        for (TbItemChild child:list) {
            if ((long)child.getId()==id){
                child.setNum(num);
            }
        }
        String ok = jedisDaoImpl.set(key, JsonUtils.objectToJson(list));
        EgoResult result=new EgoResult();
        if (ok.equals("OK")){
            result.setStatus(200);
        }
        return result;
    }

    @Override
    public EgoResult delete(long id, HttpServletRequest request) {
        String key = getToken(request);
        String json = jedisDaoImpl.get(key);
        List<TbItemChild> list = JsonUtils.jsonToList(json, TbItemChild.class);
        TbItemChild itemChild=new TbItemChild();
        for (TbItemChild child:list) {
            if ((long)child.getId()==id){
                itemChild=child;
            }
        }
        list.remove(itemChild);
        String ok = jedisDaoImpl.set(key, JsonUtils.objectToJson(list));
        EgoResult result=new EgoResult();
        if (ok.equals("OK")){
            result.setStatus(200);
        }
        return result;
    }

    private String getToken(HttpServletRequest request){
        String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
        String jsonUser = HttpClientUtil.doPost(signUrl + token);
        EgoResult result = JsonUtils.jsonToPojo(jsonUser, EgoResult.class);
        String key = cartKey + ((LinkedHashMap) result.getData()).get("username");
        return key;
    }

    private TbItemChild getChild(long id,int num){
        TbItem item = tbItemDubboServiceImpl.selById(id);
        TbItemChild child = new TbItemChild();
        child.setId(item.getId());
        child.setTitle(item.getTitle());
        child.setImages(item.getImage() == null || item.getImage().equals("") ? new String[1] : item.getImage().split(","));
        child.setNum(num);
        child.setPrice(item.getPrice());
        return child;
    }
}

