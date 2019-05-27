package com.xdtech.query;

import java.util.List;

/**
 * 临时Query的BooleanQuery
 * 
 * @author zhangjianbing@msn.com
 *
 */
public class TMPBooleanQuery extends TMPQuery {
	private List<TMPBooleanClause> querys;
	/** 权重 */
	private float boost = 1.0f;

	public List<TMPBooleanClause> getQuerys() {
		return querys;
	}

	public void setQuerys(List<TMPBooleanClause> querys) {
		this.querys = querys;
	}

	public float getBoost() {
		return boost;
	}

	public void setBoost(float boost) {
		this.boost = boost;
	}
}
