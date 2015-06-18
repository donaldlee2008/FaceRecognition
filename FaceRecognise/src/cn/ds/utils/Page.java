/**
 * Project             :FaceRecognise project
 * Comments            :页面信息封装类
 * Version             :1.0
 * Modification history: number | time |   why  |  who
 * 1 | 2013-4-20 | 创建 | jxm 
 */
package cn.ds.utils;


public class Page {
	private int pageNow;//当前页码
	private int pageSize;//每页的数据条数
	private int	pageCount;//页数
	private int rowCount;//总的数据条数
	private int startNum;
	private int endNUm;
public Page(int pNow,int pSize){
	this.pageNow=pNow;
	this.pageSize=pSize;
}
public int getPageNow() {
	return pageNow;
}
public void setPageNow(int pageNow) {
	this.pageNow = pageNow;
}
public int getPageSize() {
	return pageSize;
}
public void setPageSize(int pageSize) {
	this.pageSize = pageSize;
}
public int getPageCount() {
	pageCount=(rowCount-1)/pageSize+1;
	return pageCount;
}
public void setPageCount(int pageCount) {
	this.pageCount = pageCount;
}
public int getRowCount() {
	return rowCount;
}
public void setRowCount(int rowCount) {
	this.rowCount = rowCount;
}
public int getStartNum() {
	 startNum = pageSize * (pageNow - 1) +1;
	 return startNum;
}
public int getEndNUm() {
	//return endNUm = pageNow < getPageCount() ? pageSize * pageNow : rowCount - pageSize * (getPageCount()-1);
	return endNUm = pageNow < getPageCount() ? pageSize * pageNow : rowCount;
}
public int getNowPageSize(){
	if(pageNow < pageCount){
		return pageSize;
	}else{
		return rowCount % pageSize;
	}
}
}
