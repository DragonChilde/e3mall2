package cn.e3mall.search.service.impl;

import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.SearchResult;
import cn.e3mall.search.dao.SearchDao;
import cn.e3mall.search.service.SolrService;

@Service
public class SolrServiceImpl implements SolrService {
	
	@Autowired
	private SearchDao searchDao;

	@Override
	public SearchResult getSolrSearchItem(String keywors, int page, int rows) throws Exception {

		SolrQuery solrQuery = new SolrQuery();
		solrQuery.setQuery(keywors);
		solrQuery.setStart((page -1)*rows);
		solrQuery.setRows(rows);
		solrQuery.set("df","item_title");
		solrQuery.setHighlight(true);
		solrQuery.addHighlightField("item_title");
		solrQuery.setHighlightSimplePre("<em style=\"color:red\">");
		solrQuery.setHighlightSimplePost("</em>");
		SearchResult searchResult = searchDao.getSolrSearchItem(solrQuery);
		int totalPage = searchResult.getRecourdCount() / page;
		if(totalPage%rows >0)totalPage++;
		searchResult.setTotalPages(totalPage);
		
		return searchResult;
	}

}
