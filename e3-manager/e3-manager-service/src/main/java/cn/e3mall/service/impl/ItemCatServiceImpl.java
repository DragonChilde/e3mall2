package cn.e3mall.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.mapper.TbItemCatMapper;
import cn.e3mall.pojo.TbItemCat;
import cn.e3mall.pojo.TbItemCatExample;
import cn.e3mall.pojo.TbItemCatExample.Criteria;
import cn.e3mall.service.ItemCatService;

@Service
public class ItemCatServiceImpl implements ItemCatService{
	
	@Autowired
	private TbItemCatMapper itemCatMapper;

	@Override
	public List<EasyUITreeNode> getEasyUITreeNode(Long parentId) {

		TbItemCatExample itemCatExample = new TbItemCatExample();
		Criteria criteria = itemCatExample.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		
		List<TbItemCat> list = itemCatMapper.selectByExample(itemCatExample);
		List<EasyUITreeNode> lists = new ArrayList<>();
		for(TbItemCat itemCat:list)
		{
			EasyUITreeNode easyUITreeNode = new EasyUITreeNode();
			easyUITreeNode.setId(itemCat.getId());
			easyUITreeNode.setText(itemCat.getName());
			easyUITreeNode.setState(itemCat.getIsParent()?"closed":"open");
			lists.add(easyUITreeNode);
		}
		return lists;
	}

}
