package cn.e3mall.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.rpc.Result;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.content.service.ContentCategoryService;
import cn.e3mall.mapper.TbContentCategoryMapper;
import cn.e3mall.pojo.TbContentCategory;
import cn.e3mall.pojo.TbContentCategoryExample;
import cn.e3mall.pojo.TbContentCategoryExample.Criteria;


@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {
	
	@Autowired
	private TbContentCategoryMapper contentCategoryMapper;

	@Override
	public List<EasyUITreeNode> getContentCategory(Long parentId) {
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbContentCategory> lists= contentCategoryMapper.selectByExample(example);
		List<EasyUITreeNode> nodeList = new ArrayList<>();
		
		for(TbContentCategory list:lists)
		{
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(list.getId());
			node.setText(list.getName());
			node.setState(list.getIsParent()?"closed":"open");
			nodeList.add(node);
		}
		return nodeList;
	}

	@Override
	public E3Result addContentCategory(Long parentId, String name) {
		TbContentCategory contentCategory = new TbContentCategory();
		contentCategory.setParentId(parentId);
		contentCategory.setName(name);
		contentCategory.setStatus(1);
		contentCategory.setSortOrder(1);
		contentCategory.setCreated(new Date());
		contentCategory.setUpdated(new Date());
		contentCategory.setIsParent(false);
		contentCategoryMapper.insert(contentCategory);
		TbContentCategory parent = contentCategoryMapper.selectByPrimaryKey(parentId);
		if(!parent.getIsParent())
		{
			parent.setIsParent(true);
			TbContentCategoryExample example = new TbContentCategoryExample();
			Criteria criteria = example.createCriteria();
			criteria.andIdEqualTo(parentId);
			contentCategoryMapper.updateByExample(parent, example);
		}

		return E3Result.ok(contentCategory);
	}

}
