package cn.e3mall.cart.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mall.pojo.TbItemExample.Criteria;

@Service
public class CartServiceImpl implements CartService {
	
	@Autowired
	private JedisClient jedisClient;
	
	@Autowired
	private TbItemMapper tbItemMapper;
	
	@Value("${REDIS_CART_PRE}")
	private String REDIS_CART_PRE;
	

	@Override
	public E3Result addCart(Long userId, Long itemId, Integer num) {

		Boolean hexists = jedisClient.hexists(REDIS_CART_PRE+":"+userId, itemId.toString());
		if(hexists)
		{
			String json = jedisClient.hget(REDIS_CART_PRE+":"+userId, itemId.toString());
			TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
			item.setNum(item.getNum()+num);
			jedisClient.hset(REDIS_CART_PRE+":"+userId, itemId.toString(), JsonUtils.objectToJson(item));
			return E3Result.ok();
		}
		
		TbItemExample example = new TbItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(itemId);
		List<TbItem> list = tbItemMapper.selectByExample(example);
		if(list.size()>0 && list != null)
		{
			
			TbItem item = list.get(0);
			item.setNum(num);
			item.setImage(item.getImage().split(",")[0]);
			jedisClient.hset(REDIS_CART_PRE+":"+userId, itemId.toString(), JsonUtils.objectToJson(item));
		}
		
		return E3Result.ok();
	}


	@Override
	public E3Result mergeCart(Long userId, List<TbItem> itemList) {
		for(TbItem item:itemList)
		{
			addCart(userId,item.getId(),item.getNum());
			
		}
		return E3Result.ok();
	}


	@Override
	public List<TbItem> getCartList(Long userId) {

		List<String> json = jedisClient.hvals(REDIS_CART_PRE+":"+userId);
		List<TbItem> itemList = new ArrayList<>();
		for(String string:json)
		{
			TbItem item = JsonUtils.jsonToPojo(string, TbItem.class);
			itemList.add(item);
		}
		return itemList;
	}
	
	@Override
	public E3Result updateCartNum(Long userId,Long itemId ,Integer num)
	{
		String json = jedisClient.hget(REDIS_CART_PRE+":"+userId, itemId.toString());
		TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
		item.setNum(num);
		jedisClient.hset(REDIS_CART_PRE+":"+userId, itemId.toString(), JsonUtils.objectToJson(item));
		return E3Result.ok();
	}


	@Override
	public E3Result deleteCartItem(Long userId,Long itemId) {
		jedisClient.hdel(REDIS_CART_PRE+":"+userId, itemId.toString());
		return E3Result.ok();
	}


	@Override
	public E3Result cleanCartItem(Long userId) {
		jedisClient.del(REDIS_CART_PRE+":"+userId);
		return E3Result.ok();
	}

}
