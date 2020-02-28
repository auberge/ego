package com.ego.sign.service;

import com.ego.commons.pojo.EgoResult;
import com.ego.pojo.TbUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface TbUserService {
    /**
     * 登录
     */
    EgoResult login(TbUser user, HttpServletRequest request, HttpServletResponse response);

    /**
     * 根据token查询用户信息
     */
    EgoResult getUserInfoByToken(String token);

    /**
     * 退出
     */
    EgoResult logout(String token, HttpServletRequest request, HttpServletResponse response);
}
