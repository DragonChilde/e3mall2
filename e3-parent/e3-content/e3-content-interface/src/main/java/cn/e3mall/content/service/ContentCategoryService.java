package cn.e3mall.content.service;

import java.util.List;

import cn.e3mall.common.pojo.EasyUITreeNode;

public interface ContentCategoryService {

	public List<EasyUITreeNode> getContentCategory(Long parentId);
}
