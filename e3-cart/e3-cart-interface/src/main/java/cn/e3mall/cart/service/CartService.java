package cn.e3mall.cart.service;

import cn.e3mall.common.utils.E3Result;

public interface CartService {

	public E3Result addCart(Long userId,Long itemId ,Integer num);
}
