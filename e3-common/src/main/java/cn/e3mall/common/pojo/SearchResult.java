package cn.e3mall.common.pojo;

import java.io.Serializable;
import java.util.List;

public class SearchResult implements Serializable{
	private List<SearchItem> itemLists;
	private int totalPages;
	private int recourdCount;
	public List<SearchItem> getItemLists() {
		return itemLists;
	}
	public void setItemLists(List<SearchItem> itemLists) {
		this.itemLists = itemLists;
	}
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	public int getRecourdCount() {
		return recourdCount;
	}
	public void setRecourdCount(int recourdCount) {
		this.recourdCount = recourdCount;
	}
}
