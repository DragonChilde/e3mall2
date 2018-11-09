import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.e3mall.common.pojo.SearchItem;

public class TestSolrj {
	private List<String> list;

	@Test
	public void addDoc() throws Exception
	{
		String urlString = "http://localhost:8080/solr/core";
		SolrClient solr = new HttpSolrClient.Builder(urlString).build();
		SolrInputDocument document = new SolrInputDocument();
		document.addField("id","doc01");
		document.addField("item_title", "测试");
		document.addField("item_price", "200");
		solr.add(document);
		solr.commit();
	}
	
	@Test
	public void deleteDoc() throws Exception
	{
		String urlString = "http://localhost:8080/solr/core";
		SolrClient solr = new HttpSolrClient.Builder(urlString).build();

		solr.deleteById("doc01");
		
		solr.commit();
	}
	
	@Test
	public void query() throws Exception
	{
		String urlString = "http://localhost:8080/solr/core";
		SolrClient solr = new HttpSolrClient.Builder(urlString).build();
		SolrQuery query = new SolrQuery();
		query.set("q", "*:*");
		QueryResponse queryResponse = solr.query(query);
		SolrDocumentList lists = queryResponse.getResults();
		System.out.println("total:"+lists.getNumFound());
		for(SolrDocument list:lists)
		{
			System.out.println(list.get("id"));
			System.out.println(list.get("item_title"));
		}
		
		
	}
	
	@Test
	public void queryComplex() throws Exception
	{
		String urlString = "http://localhost:8080/solr/core";
		SolrClient solr = new HttpSolrClient.Builder(urlString).build();
		SolrQuery query = new SolrQuery();
		query.set("q", "华为");
		query.setStart(1);
		query.setRows(2);
		query.set("df","item_title");
		query.setHighlight(true);
		query.addHighlightField("item_title");
		QueryResponse response = solr.query(query);
		SolrDocumentList solrDocumentList = response.getResults();
		
		long num = solrDocumentList.getNumFound();
		System.out.println(num);
		Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
		for(SolrDocument document:solrDocumentList)
		{
			list = highlighting.get(document.get("id")).get("item_title");
			String title = "";
			if(list.size()>0 && list != null)
			{
				 title = list.get(0);
			} else {
				 title = (String) document.get("item_title");
			}
			System.out.println(document.get("id"));
	
			System.out.println(title);
		}
	}
	
	@Test 
	public void querySolrTest() throws Exception
	{
		ClassPathXmlApplicationContext ac  = new ClassPathXmlApplicationContext("classpath:spring//applicationContext-solr.xml");
		SolrClient solrClient = (SolrClient) ac.getBean("httpSolrClient");
		SolrQuery solrQuery = new SolrQuery();
		solrQuery.setQuery("华为");
		solrQuery.setStart(1);
		solrQuery.setRows(60);
		solrQuery.set("df","item_title");
		solrQuery.setHighlight(true);
		solrQuery.addHighlightField("item_title");
		solrQuery.setHighlightSimplePre("<em style=\"color:red\">");
		solrQuery.setHighlightSimplePost("</em>");
		QueryResponse queryResponse = solrClient.query(solrQuery);
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
		System.out.println(lists);
	}
	
	@Test
	public void testStringSplit()
	{
		String a ="1,2,3,4,6";
		String[] b = a.split(",");
		for(int i=0;i < b.length;i++)
		{
			System.out.println(b[i]);
			
		}
		
	}
}
