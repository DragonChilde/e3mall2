package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.SearchResult;
import cn.e3mall.search.service.SolrService;

@Controller
public class SearchController {
	
	@Autowired
	private SolrService solrService;

	@RequestMapping("/search")
	@ResponseBody
	public String getSearchList(String keyword,@RequestParam(defaultValue="1")Integer page,Integer rows,Model model) throws Exception
	{
		
		keyword = new String(keyword.getBytes("iso8859-1"),"UTF-8");
		SearchResult searchResult = solrService.getSolrSearchItem(keyword, page, rows);
		model.addAttribute("itemList",searchResult.getItemLists());
		model.addAttribute("totalPages", searchResult.getTotalPages());
		model.addAttribute("page", page);
		model.addAttribute("query", keyword);
		model.addAttribute("recourdCount", searchResult.getRecourdCount());
		return "search";
	}
}
