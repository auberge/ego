package com.ego.sign.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.commons.pojo.CookieUtils;
import com.ego.commons.pojo.EgoResult;
import com.ego.commons.utils.JsonUtils;
import com.ego.dubbo.service.TbUserDubboService;
import com.ego.pojo.TbUser;
import com.ego.redis.dao.JedisDao;
import com.ego.sign.service.TbUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Service
public class TbUserServiceImpl implements TbUserService {
    @Reference
    private TbUserDubboService tbUserDubboServiceImpl;
    @Resource
    private JedisDao jedisDaoImpl;

    @Override
    public EgoResult login(TbUser user, HttpServletRequest request, HttpServletResponse response) {
        EgoResult result = new EgoResult();
        TbUser userSelect = tbUserDubboServiceImpl.selByUser(user);
        if (userSelect != null) {
            result.setStatus(200);
            //当用户登录成功后把用户信息放入到redis中
            String key = UUID.randomUUID().toString();
            jedisDaoImpl.set(key, JsonUtils.objectToJson(userSelect));
            //设置key的有效时间
            jedisDaoImpl.expire(key, 60 * 60 * 24 * 3);
            //产生cookie
            CookieUtils.setCookie(request, response, "TT_TOKEN", key, 60 * 60 * 24 * 3);
        } else {
            result.setMsg("用户名或密码错误");
        }
        return result;
    }

    @Override
    public EgoResult getUserInfoByToken(String token) {
        EgoResult result = new EgoResult();
        String json = jedisDaoImpl.get(token);
        if (json != null && !json.equals("")) {
            TbUser user = JsonUtils.jsonToPojo(json, TbUser.class);
            //把密码清空
            user.setPassword(null);
            result.setStatus(200);
            result.setMsg("OK");
            result.setData(user);
        }else{
            result.setMsg("获取失败");
        }
        return result;
    }

    @Override
    public EgoResult logout(String token, HttpServletRequest request, HttpServletResponse response) {
        EgoResult result=new EgoResult();
        jedisDaoImpl.del(token);
        CookieUtils.deleteCookie(request,response,"TT_TOKEN");
        result.setStatus(200);
        result.setMsg("OK");
        result.setData(null);
        return result;
    }
}
