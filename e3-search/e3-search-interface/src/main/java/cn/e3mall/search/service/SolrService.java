package cn.e3mall.search.service;

import java.util.List;

import cn.e3mall.common.pojo.SearchResult;

public interface SolrService {

	public SearchResult getSolrSearchItem(String keywors,int page,int rows) throws Exception;
}
