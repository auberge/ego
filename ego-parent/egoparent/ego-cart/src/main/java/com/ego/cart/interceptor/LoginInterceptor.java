package com.ego.cart.interceptor;

import com.ego.commons.pojo.CookieUtils;
import com.ego.commons.pojo.EgoResult;
import com.ego.commons.utils.HttpClientUtil;
import com.ego.commons.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {
    @Value("${sign.url}")
    private String signUrl;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String token = CookieUtils.getCookieValue(httpServletRequest, "TT_TOKEN");
        if (token != null && !token.equals("")) {
            String json = HttpClientUtil.doPost("http://localhost:8084/user/token/" + token);
            EgoResult result = JsonUtils.jsonToPojo(json, EgoResult.class);
            if (result.getStatus() == 200) {
                return true;
            }
        }
        String num = httpServletRequest.getParameter("num");
        if (num != null && !num.equals("")) {
            httpServletResponse.sendRedirect("http://localhost:8084/user/showLogin?interUrl=" + httpServletRequest.getRequestURL() + "%3Fnum=" + num);
        } else {
            httpServletResponse.sendRedirect("http://localhost:8084/user/showLogin");
        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
