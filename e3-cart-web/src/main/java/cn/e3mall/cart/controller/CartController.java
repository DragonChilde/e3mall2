package cn.e3mall.cart.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.service.ItemService;

@Controller
public class CartController {
	
	@Value("${TT_CART}")
	private String TT_CART;
	
	@Autowired
	private ItemService itemService;
	
	@Value("${CART_EXPIRE}")
	private Integer CART_EXPIRE;
	
	@Autowired
	private CartService cartService;
	
	@RequestMapping(value="/cart/add/{itemId}")
	public String addCartItem(@PathVariable Long itemId, @RequestParam(defaultValue="1")Integer num,HttpServletRequest request,HttpServletResponse response)
	{
		
		TbUser user = (TbUser) request.getAttribute("user");
		if(user!=null)
		{
			cartService.addCart(user.getId(), itemId, num);
			return "cartSuccess";
		}
		
		List<TbItem> list = getItemListByCookie(request);
		Boolean hasItem  = false;
		for(TbItem item:list)
		{
			if(item.getId() == itemId.longValue())
			{
				item.setNum(num+item.getNum());
				hasItem = true;
				break;
			}
		}
		if(!hasItem)
		{
			TbItem item = itemService.getItemById(itemId);
			item.setNum(num);
			String images = item.getImage();
			if(StringUtils.isNotBlank(images))
			{
					String image = images.split(",")[0];
					item.setImage(image);
			}
			list.add(item);
		}
		CookieUtils.setCookie(request, response, TT_CART, JsonUtils.objectToJson(list), CART_EXPIRE, "utf-8");
		return "cartSuccess";
	}
	
	private List<TbItem> getItemListByCookie(HttpServletRequest request)
	{
		String json = CookieUtils.getCookieValue(request, TT_CART,true);
		if(StringUtils.isBlank(json))
		{
			return new ArrayList<>();
		}
		
		List<TbItem> list = JsonUtils.jsonToList(json, TbItem.class);
		return list;
	}
	
	@RequestMapping(value="/cart/cart")
	public String showCartList(HttpServletRequest request,HttpServletResponse response,Model model)
	{
		TbUser user = (TbUser) request.getAttribute("user");
		List<TbItem> list = getItemListByCookie(request);
		if(user!=null)
		{
			cartService.mergeCart(user.getId(), list);
			CookieUtils.deleteCookie(request, response, TT_CART);
			list = cartService.getCartList(user.getId());
		}
		
		model.addAttribute("cartList", list);
		return "cart";
	}
	
	@RequestMapping(value="/cart/update/num/{itemId}/{num}")
	@ResponseBody
	public E3Result updateCartNum(@PathVariable Long itemId,@PathVariable Integer num,HttpServletRequest request,HttpServletResponse response)
	{
		TbUser user = (TbUser) request.getAttribute("user");
		if(user!=null)
		{
			cartService.updateCartNum(user.getId(), itemId, num);
			return E3Result.ok();
		}
		
		List<TbItem> list = getItemListByCookie(request);
		for(TbItem item:list)
		{
			if(item.getId()==itemId.longValue())
			{
				item.setNum(num);
			}
		}
		CookieUtils.setCookie(request, response, TT_CART, JsonUtils.objectToJson(list), CART_EXPIRE, "utf-8");
		return E3Result.ok();
	}
	
	@RequestMapping(value="/cart/delete/{itemId}")
	public String delteCartItem(@PathVariable Long itemId,HttpServletRequest request,HttpServletResponse response)
	{
		TbUser user = (TbUser) request.getAttribute("user");
		if(user!=null) {
			
			cartService.deleteCartItem(user.getId(), itemId);
			return "redirect:/cart/cart.html";
		}
		
		List<TbItem> list = getItemListByCookie(request);
		for(TbItem item:list)
		{
			if(item.getId()==itemId.longValue())
			{
				list.remove(item);
				break;
			}
		}
		CookieUtils.setCookie(request, response, TT_CART, JsonUtils.objectToJson(list), CART_EXPIRE, "utf-8");
		return "redirect:/cart/cart.html";
	}
}
