package com.ego.sign.controller;

import com.ego.commons.pojo.EgoResult;
import com.ego.pojo.TbUser;
import com.ego.sign.service.TbUserService;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class TbUserController {
    @Resource
    private TbUserService tbUserServiceImpl;

    /**
     * 显示登陆
     */
    @RequestMapping("user/showLogin")
    public String showLogin(@RequestHeader(value = "Referer", defaultValue = "") String url, String interUrl, Model model) {
        if (interUrl!=null&&!interUrl.equals("")) {
            model.addAttribute("redirect", interUrl);
        } else if (!url.equals("")) {
            model.addAttribute("redirect", url);
        }
        return "login";
    }

    /**
     * 登录
     */
    @RequestMapping("user/login")
    @ResponseBody
    public EgoResult login(TbUser user, HttpServletRequest request, HttpServletResponse response) {
        return tbUserServiceImpl.login(user, request, response);
    }

    /**
     * 通过token获取用户信息
     */
    @RequestMapping("user/token/{token}")
    @ResponseBody
    public Object getUserInfo(@PathVariable String token, String callback) {
        EgoResult result = tbUserServiceImpl.getUserInfoByToken(token);
        if (callback != null && !callback.equals("")) {
            MappingJacksonValue mjv = new MappingJacksonValue(result);
            mjv.setJsonpFunction(callback);
            return mjv;
        }
        return result;
    }

    /**
     * 退出登录
     */
    @RequestMapping("user/logout/{token}")
    @ResponseBody
    public Object logout(@PathVariable String token, String callback, HttpServletRequest request, HttpServletResponse response) {
        EgoResult result = tbUserServiceImpl.logout(token, request, response);
        if (callback != null && !callback.equals("")) {
            MappingJacksonValue mjv = new MappingJacksonValue(result);
            mjv.setJsonpFunction(callback);
            return mjv;
        }
        return result;
    }
}
