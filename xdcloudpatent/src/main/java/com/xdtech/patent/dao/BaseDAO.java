package com.xdtech.patent.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;

/**
 * 数据库访问接口
 * 
 * @author Chang Fei
 */
public interface BaseDAO {


	public <T> T findById(Class<?> clz, Serializable id);

	public <T> T loadById(Class<?> clz, Serializable id);

	public <T> List<T> findAll(Class<?> clz);

	public <T> T save(T entity);

	public void update(Object entity);

	public void update(Object entity, LockMode lock);
	
	public <T> void saveOrUpdate(Object entity);

	public <T> void saveOrUpdateAll(Collection<?> entities);

	public void delete(Object entity);

	public void delete(Object entity, LockMode lock);

	public void deleteById(Class<?> clz, Serializable id);

	public void deleteAll(Collection<?> entities);

	public void merge(Object entity);

	public <T> T get(String hsql);
	
	public <T> T get(Class<T> clazz,Serializable id);

	public Object get(String hql, Object... values);

	public <T> List<T> findList(String hsql);

	public <T> List<T> findList(String hql, int start, int number, Object... values);

	public <T> List<T> findList(String hql, int start, int number);

	public <T> List<T> findList(String hql, Object... values);

	public <T> List<T> findByNamedQuery(String queryName);

	public <T> List<T> findByNamedQuery(String queryName, Object... values);

	public <T> List<T> findByNamedQuery(String queryName, String[] paramNames, Object[] values);

	public <T> Iterator<T> iterate(String hql);

	public <T> Iterator<T> iterate(String hql, Object... values);

	public DetachedCriteria createDetachedCriteria(Class<?> clz);

	public Criteria createCriteria(Class<?> clz);

	public <T> List<T> findByCriteria(DetachedCriteria criteria);

	public <T> List<T> findByCriteria(DetachedCriteria criteria, int firstResult, int maxResults);

	public <T> List<T> findByCriteria(Class<?> clz, Criterion... criterion);

	public Integer getRowCount(DetachedCriteria criteria);

	public Object getStatValue(DetachedCriteria criteria, String propertyName, String StatName);

	public int update(String hql, Object... values);

	public <T> void execNativeSQL(List<String> sqls, List<List<Object>> params) throws Exception;

	public <T> void execNativeSQL(String sql, List<Object> params) throws Exception;

	public <T> void execNativeSQL(String sql) throws Exception;
}