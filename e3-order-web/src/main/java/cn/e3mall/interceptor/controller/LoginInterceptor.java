package cn.e3mall.interceptor.controller;

import java.lang.ProcessBuilder.Redirect;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;

public class LoginInterceptor implements HandlerInterceptor {
	
	@Value("${COOKIE_TOKEN_KEY}")
	private String COOKIE_TOKEN_KEY;
	
	@Value("${LOGIN_URL}")
	private String LOGIN_URL;
	
	@Value("${TT_CART}")
	private String TT_CART;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private CartService cartService;
	
	

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// TODO 自动生成的方法存根
		String token = CookieUtils.getCookieValue(request, COOKIE_TOKEN_KEY);
		if(StringUtils.isBlank(token))
		{
			String url = request.getRequestURI();
			response.sendRedirect(LOGIN_URL+"?redirectUrl="+url);
			return false;
		}
		E3Result e3Result = tokenService.getUserByToken(token);
		if(e3Result.getStatus() != 200)
		{
			String url = request.getRequestURI();
			response.sendRedirect(LOGIN_URL+"?redirectUrl="+url);
			return false;
		}
		TbUser user = (TbUser) e3Result.getData();
		request.setAttribute("user", user);
		String json = CookieUtils.getCookieValue(request, "TT_CART");
		if(StringUtils.isNoneBlank(json))
		{
			
			List<TbItem> itemList = JsonUtils.jsonToList(json, TbItem.class);
			cartService.mergeCart(user.getId(), itemList);
		}
		
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO 自动生成的方法存根
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO 自动生成的方法存根
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}

}
