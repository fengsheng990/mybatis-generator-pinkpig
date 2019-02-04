package com.pinkpig.mybatis.component;

import java.io.Serializable;

/**
 * 简单分页对象，适用于接口服务
 * 
 * @author fengsheng
 *
 */
public class SimplePage implements Serializable {

	private static final long serialVersionUID = 235019386370822983L;

	/**
	 * 当前页，默认1
	 */
	private int pageNum = 1;

	/**
	 * 分页大小，默认10条
	 */
	private int pageSize = 10;

	/**
	 * 是否返回总记录数，默认true
	 */
	private boolean returnCount = true;

	/**
	 * 总记录数
	 */
	private int count;

	public SimplePage() {
	}

	public SimplePage(int pageNum, int pageSize) {
		this.pageNum = pageNum;
		this.pageSize = pageSize;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public boolean isReturnCount() {
		return returnCount;
	}

	public void setReturnCount(boolean returnCount) {
		this.returnCount = returnCount;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getOffset() {
		return (this.pageNum - 1) * this.pageSize;
	}

	public int getLimit() {
		return getOffset() + this.pageSize;
	}
}