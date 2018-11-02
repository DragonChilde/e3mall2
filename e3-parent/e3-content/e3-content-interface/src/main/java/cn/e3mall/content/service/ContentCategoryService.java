package cn.e3mall.content.service;

import java.util.List;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.common.utils.E3Result;

public interface ContentCategoryService {

	public List<EasyUITreeNode> getContentCategory(Long parentId);

	public E3Result addContentCategory(Long parentId, String name);
}
