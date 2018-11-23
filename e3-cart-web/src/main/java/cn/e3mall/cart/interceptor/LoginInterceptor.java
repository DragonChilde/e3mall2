package cn.e3mall.cart.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;

import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;

public class LoginInterceptor implements HandlerInterceptor{
	
	//@Autowired
	//private TokenService tokenService;
	
	@Value("${USER_INFO}")
	private String USER_INFO;
	
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
	{
		
		String json = CookieUtils.getCookieValue(request, USER_INFO);
		if(StringUtils.isBlank(json))
		{
			return true;
		}
		/*
		E3Result e3Result = tokenService.getUserByToken(json);
		if(e3Result.getStatus() != 200)
		{
			return true;
		}
		TbUser user = (TbUser) e3Result.getData();
		request.setAttribute("user", user);*/
		return true;
		
	}
	
	public  void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) {
	}
	
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex){
	}
}
