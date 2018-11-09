package cn.e3mall.search.service.impl;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.search.mapper.ItemMapper;
import cn.e3mall.search.service.SearchService;

@Service
public class SearchServiceImpl implements SearchService {
	
	@Autowired
	private ItemMapper itemMapper;
	
	@Autowired
	private SolrClient slorClient;

	@Override
	public E3Result getSearchItemListForAll() {
		
		
		List<SearchItem> lists = itemMapper.getSearchItem();
		try {
			for(SearchItem list:lists)
			{
				SolrInputDocument document = new SolrInputDocument();
				document.addField("id", list.getId());
				document.addField("item_title", list.getTitle());
				document.addField("item_sell_point", list.getSellPoint());
				document.addField("item_price", list.getPrice());
				document.addField("item_image", list.getImage());
				document.addField("item_category_name", list.getCategoryName());
				slorClient.add(document);
			}
			slorClient.commit();
			return E3Result.ok();
		}catch(Exception e)
		{
			e.printStackTrace();
			return E3Result.build(500,"error");
		}
		
	}



	@Override
	public E3Result importSearchItemListById(Long itemId) {
		SearchItem searchItem = itemMapper.importSearchItemById(itemId);
		SolrInputDocument document = new SolrInputDocument();
		document.addField("id", searchItem.getId());
		document.addField("item_title", searchItem.getTitle());
		document.addField("item_sell_point", searchItem.getSellPoint());
		document.addField("item_price", searchItem.getPrice());
		document.addField("item_image", searchItem.getImage());
		document.addField("item_category_name", searchItem.getCategoryName());
		try {
			slorClient.add(document);
			slorClient.commit();
		} catch (SolrServerException | IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return null;
	}
	
	

}
