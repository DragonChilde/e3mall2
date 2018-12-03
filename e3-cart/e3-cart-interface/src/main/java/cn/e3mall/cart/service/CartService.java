package cn.e3mall.cart.service;

import java.util.List;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbItem;

public interface CartService {

	public E3Result addCart(Long userId,Long itemId ,Integer num);
	public E3Result mergeCart(Long userId , List<TbItem> itemList);
	public List<TbItem> getCartList(Long userId);
	public E3Result updateCartNum(Long userId,Long itemId ,Integer num);
	public E3Result deleteCartItem(Long userId,Long itemId);
	 E3Result cleanCartItem(Long userId);
}
