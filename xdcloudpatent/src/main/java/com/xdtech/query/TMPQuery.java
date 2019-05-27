package com.xdtech.query;

/**
 * 临时Query。为避免在QueryParser.jj文件中写入过多的java代码，在QueryParser解析完查询语句之后，
 * 由转换程序将lucene的query转换成临时Query
 * 
 * @author zhangjianbing@msn.com
 */
public class TMPQuery {
	/**权重*/
	private float boost;

	public float getBoost() {
		return boost;
	}

	public void setBoost(float boost) {
		this.boost = boost;
	}
}
