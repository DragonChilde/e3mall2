package cn.e3mall.search.service;

import cn.e3mall.common.utils.E3Result;

public interface SearchService {
	public E3Result getSearchItemListForAll();
	
	public E3Result importSearchItemListById(Long itemId);
}
