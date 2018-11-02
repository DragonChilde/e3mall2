package cn.e3mall.search.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.common.pojo.SearchResult;
import cn.e3mall.search.service.SolrService;

@Repository
public class SearchDao {

	@Autowired
	private SolrClient solrClient;
	
	public SearchResult getSolrSearchItem(SolrQuery query) throws Exception
	{
		
		QueryResponse queryResponse = solrClient.query(query);
		SolrDocumentList results = queryResponse.getResults();
		
		long totals = results.getNumFound();
		Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
		List<SearchItem> lists = new ArrayList<>();
		for(SolrDocument solrDocument:results)
		{
			SearchItem searchItem = new SearchItem();
			searchItem.setId((String) solrDocument.get("id"));
			searchItem.setImage((String) solrDocument.get("item_image"));
			searchItem.setPrice((Long) solrDocument.get("item_price"));
			searchItem.setSellPoint((String) solrDocument.get("item_sell_point"));
			searchItem.setCategoryName((String) solrDocument.get("item_category_name"));
			List<String> highlightingList = highlighting.get(solrDocument.get("id")).get("item_title");
			String title = "";
			if(highlightingList.size() >0 && highlightingList != null)
			{
				 title = highlightingList.get(0);
			} else {
				title = (String) solrDocument.get("item_title");
			}
			searchItem.setTitle(title);
			lists.add(searchItem);
		}
		SearchResult searchResult = new SearchResult();
		searchResult.setItemLists(lists);
		searchResult.setRecourdCount((int) totals);
		return searchResult;
	}
}
