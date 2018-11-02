package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.search.service.SearchService;

@Controller
public class SearchController {

	@Autowired
	private SearchService searchService;
	
	@RequestMapping("/index/item/import")
	@ResponseBody
	public E3Result importItemIndex()
	{
		return searchService.getSearchItemListForAll();
	}
}
