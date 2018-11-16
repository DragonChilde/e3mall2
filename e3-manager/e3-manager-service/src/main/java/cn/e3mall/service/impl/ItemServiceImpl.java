package cn.e3mall.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.common.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.IDUtils;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.mapper.TbItemMapper;

import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mall.pojo.TbItemExample.Criteria;
import cn.e3mall.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService {
	
	@Autowired
	private TbItemMapper itemMapper;
	
	@Autowired
	private TbItemDescMapper itemDescMapper;
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	@Autowired
	private Destination topicDestination;
	
	@Value("${REDIS_ITEM_PRE}")
	private String REDIS_ITEM_PRE;
	
	@Value("${ITEM_CACHE_EXPIRE}")
	private Integer ITEM_CACHE_EXPIRE;
	
	@Autowired
	private JedisClient jedisClient;

	@Override
	public TbItem getItemById(Long id) {
		try {
			String json = jedisClient.get(REDIS_ITEM_PRE+":"+id+":BASE");
			if(StringUtils.isNotBlank(json))
			{
				TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
				return tbItem;
			}
		} catch(Exception e)
		{
			e.printStackTrace();
		}
		TbItemExample example = new TbItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(id);
		List<TbItem> list = itemMapper.selectByExample(example);
		if(list != null && list.size() >0)
		{
			TbItem item = list.get(0);
			try {
				jedisClient.set(REDIS_ITEM_PRE+":"+id+":BASE",JsonUtils.objectToJson(item));
				jedisClient.expire(REDIS_ITEM_PRE+":"+id+":BASE", ITEM_CACHE_EXPIRE);
			} catch(Exception e)
			{
				e.printStackTrace();
			}
			return list.get(0);
		}
		return null;
	}

	@Override
	public EasyUIDataGridResult getItemList(int page, int rows) {
		PageHelper.startPage(page, rows);
		TbItemExample itemExample = new TbItemExample();
		List<TbItem> itemList = itemMapper.selectByExample(itemExample);
		PageInfo<TbItem> pageInfo = new PageInfo<>(itemList);
		Long total = pageInfo.getTotal();
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setTotal(total);
		result.setRows(itemList);
		return result;
	}

	@Override
	public E3Result addItem(TbItem item, String desc) {
		Long itemId = IDUtils.genItemId();
		item.setId(itemId);
		item.setStatus((byte) 1);
		item.setCreated(new Date());
		item.setUpdated(new Date());
		itemMapper.insert(item);
		
		TbItemDesc itemDesc = new TbItemDesc();
		itemDesc.setItemId(itemId);
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(new Date());
		itemDesc.setUpdated(new Date());
		itemDescMapper.insert(itemDesc);
		E3Result result = E3Result.ok();
		
		jmsTemplate.send(topicDestination, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage message = session.createTextMessage(itemId+"");
				return message;
			}
		});
		return result;
	}

	@Override
	public TbItemDesc getItemDescById(long id) {		
		try {
			String json = jedisClient.get(REDIS_ITEM_PRE+":"+id+":DESC");
			if(StringUtils.isNotBlank(json))
			{
				TbItemDesc tbItemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
				return tbItemDesc;
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		TbItemDesc tbItemDesc = itemDescMapper.selectByPrimaryKey(id);
		try {
			jedisClient.set(REDIS_ITEM_PRE+":"+id+":DESC", JsonUtils.objectToJson(tbItemDesc));
			jedisClient.expire(REDIS_ITEM_PRE+":"+id+":DESC", ITEM_CACHE_EXPIRE);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return tbItemDesc;
	}

}
