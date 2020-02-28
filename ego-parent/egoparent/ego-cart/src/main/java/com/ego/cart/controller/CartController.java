package com.ego.cart.controller;

import com.ego.cart.service.CartService;
import com.ego.commons.pojo.EgoResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
public class CartController {
    @Resource
    private CartService cartServiceImpl;

    /**
     * 添加购物车
     */
    @RequestMapping("cart/add/{id}.html")
    public String addCart(@PathVariable long id, int num, HttpServletRequest request) {
        cartServiceImpl.addCart(id, num, request);
        return "cartSuccess";
    }

    /**
     * 显示购物车
     */
    @RequestMapping("cart/cart.html")
    public String showCart(Model model, HttpServletRequest request) {
        model.addAttribute("cartList", cartServiceImpl.showCat(request));
        return "cart";
    }

    /**
     * 根据id修改购物车商品数量
     */
    @RequestMapping("cart/update/num/{id}/{num}.action")
    @ResponseBody
    public EgoResult update(@PathVariable long id, @PathVariable int num, HttpServletRequest request) {
        return cartServiceImpl.update(id, num, request);
    }

    /**
     * 根据id删除购物车商品
     */
    @RequestMapping("cart/delete/{id}.action")
    @ResponseBody
    public EgoResult update(@PathVariable long id, HttpServletRequest request) {
        return cartServiceImpl.delete(id, request);
    }
}
