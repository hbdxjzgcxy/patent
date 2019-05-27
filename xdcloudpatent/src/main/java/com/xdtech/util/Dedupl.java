package com.xdtech.util;

import java.util.List;


/**
 * 文档去重管理接口,记录已经索引的文档
 * @author Chang Fei
 *
 */
public interface Dedupl {
	/**
	 * 保存记录
	 * @param docNo
	 * @return
	 */
	public boolean saveRecord(String md5Key,String md5Value);
	
	/**
	 * 判断记录是否存在
	 * @param idMessage
	 * @return
	 */
	public boolean hasRecord(String idMessage);
	
	public String getRecord(String md5Key);
	
	/**
	 * 清楚记录
	 * @param idMessage
	 * @return
	 */
	public boolean clearRecord(String idMessage);
	
	/**
	 * 清空以所有记录
	 * @return
	 */
	public boolean clear();
	
	
	public boolean remove(List<String> md5Keys);
	
	/**
	 * 文档同步
	 */
	public void flush();
}
