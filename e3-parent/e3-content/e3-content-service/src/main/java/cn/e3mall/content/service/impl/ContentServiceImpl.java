package cn.e3mall.content.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.dubbo.common.json.JSON;
import com.github.pagehelper.util.StringUtil;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.content.service.ContentService;
import cn.e3mall.mapper.TbContentMapper;
import cn.e3mall.pojo.TbContent;
import cn.e3mall.pojo.TbContentExample;
import cn.e3mall.pojo.TbContentExample.Criteria;

@Service
public class ContentServiceImpl implements ContentService {
	
	@Autowired
	private TbContentMapper contentMapper;
	
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${CONTENT_LIST}")
	private String CONTENT_LIST;

	@Override
	public E3Result addContent(TbContent content) {
		
		content.setCreated(new Date());
		content.setUpdated(new Date());
		contentMapper.insert(content);
		jedisClient.hdel(CONTENT_LIST, content.getCategoryId().toString());
		return E3Result.ok();
	}

	@Override
	public List<TbContent> getContentListByCid(Long CategoryId) {
		
		try {
			String jsonString = jedisClient.hget(CONTENT_LIST, CategoryId+"");
			if(StringUtil.isNotEmpty(jsonString))
			{
				List<TbContent> list = JsonUtils.jsonToList(jsonString, TbContent.class);
				return list;
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(CategoryId);
		List<TbContent> list = contentMapper.selectByExample(example);
		
		try {
			jedisClient.hset(CONTENT_LIST, CategoryId+"", JsonUtils.objectToJson(list));
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return list;
	}

}
