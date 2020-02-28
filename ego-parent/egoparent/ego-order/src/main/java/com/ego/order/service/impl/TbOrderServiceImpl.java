package com.ego.order.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.commons.pojo.CookieUtils;
import com.ego.commons.pojo.EgoResult;
import com.ego.commons.pojo.TbItemChild;
import com.ego.commons.utils.HttpClientUtil;
import com.ego.commons.utils.IDUtils;
import com.ego.commons.utils.JsonUtils;
import com.ego.dubbo.service.TbItemDubboService;
import com.ego.dubbo.service.TbOrderDubboService;
import com.ego.order.pojo.MyOrderParam;
import com.ego.order.service.TbOrderService;
import com.ego.pojo.TbItem;
import com.ego.pojo.TbOrder;
import com.ego.pojo.TbOrderItem;
import com.ego.pojo.TbOrderShipping;
import com.ego.redis.dao.JedisDao;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class TbOrderServiceImpl implements TbOrderService {
    @Reference
    private TbItemDubboService tbItemDubboServiceImpl;
    @Reference
    private TbOrderDubboService tbOrderDubboServiceImpl;
    @Resource
    private JedisDao jedisDaoImpl;
    @Value("${cart.key}")
    private String cartKey;
    @Value("${sign.url}")
    private String signUrl;


    @Override
    public List<TbItemChild> showOrderCart(List<Long> ids, HttpServletRequest request) {
        List<TbItemChild> newList = new ArrayList<>();
        String key = getToken(request);
        if (jedisDaoImpl.exists(key)) {
            String json = jedisDaoImpl.get(key);
            List<TbItemChild> list = JsonUtils.jsonToList(json, TbItemChild.class);
            if (list != null && !list.equals("")) {
                for (TbItemChild child : list) {
                    for (Long id : ids) {
                        if ((long) child.getId() == (long) id) {
                            //判断购买数量是否大于库存
                            TbItem item = tbItemDubboServiceImpl.selById(id);
                            if (item.getNum() >= child.getNum()) {
                                child.setEnough(true);
                            } else {
                                child.setEnough(false);
                            }
                            newList.add(child);
                        }
                    }
                }
            }
        }
        return newList;
    }

    @Override
    public EgoResult create(MyOrderParam param, HttpServletRequest request) {
        //订单数据
        TbOrder order = new TbOrder();
        order.setPayment(param.getPayment());
        order.setPaymentType(param.getPaymentType());
        long id = IDUtils.genItemId();
        order.setOrderId(id + "");
        Date date = new Date();
        order.setCreateTime(date);
        order.setUpdateTime(date);
        String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
        String jsonUser = HttpClientUtil.doPost(signUrl + token);
        EgoResult egoResult = JsonUtils.jsonToPojo(jsonUser, EgoResult.class);
        Map user = (LinkedHashMap) egoResult.getData();
        order.setUserId(Long.parseLong(user.get("id").toString()));
        order.setBuyerNick(user.get("username").toString());
        order.setBuyerRate(0);
        //订单-商品表
        for (TbOrderItem item : param.getOrderItems()) {
            item.setId(IDUtils.genItemId() + "");
            item.setOrderId(id + "");
        }
        //收货人信息
        TbOrderShipping shopping = param.getOrderShipping();
        shopping.setOrderId(id + "");
        shopping.setCreated(date);
        shopping.setUpdated(date);
        EgoResult result = new EgoResult();
        try {
            int index = tbOrderDubboServiceImpl.insOrder(order, param.getOrderItems(), shopping);
            if (index > 0) {
                result.setStatus(200);
                //删除购买的商品
                String json = jedisDaoImpl.get(cartKey + user.get("username"));
                List<TbItemChild> listCart = JsonUtils.jsonToList(json, TbItemChild.class);
                List<TbItemChild> listNew = new ArrayList<>();
                for (TbItemChild child : listCart) {
                    for (TbOrderItem item : param.getOrderItems()) {
                        if (child.getId().longValue() == Long.parseLong(item.getItemId())) {
                            listNew.add(child);
                        }
                    }
                }
                for (TbItemChild myNew:listNew){
                    listCart.remove(myNew);
                }
                jedisDaoImpl.set(cartKey + user.get("username"), JsonUtils.objectToJson(listCart));
            }
        } catch (Exception e) {
            result.setMsg(e.getMessage());
        }
        return result;
    }

    private String getToken(HttpServletRequest request) {
        String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
        String jsonUser = HttpClientUtil.doPost(signUrl + token);
        EgoResult result = JsonUtils.jsonToPojo(jsonUser, EgoResult.class);
        String key = cartKey + ((LinkedHashMap) result.getData()).get("username");
        return key;
    }
}
