package com.xdtech.patent.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xdtech.patent.dao.BaseDAO;

/**
 * CommonService
 * 
 * @author Chang Fei
 */
@Service
@Transactional(rollbackFor = Exception.class,propagation=Propagation.REQUIRED)
public class CommonService {

	/*** DAO */
	@Autowired(required = true)
	private BaseDAO dao;

	private BaseDAO getDao() {
		return dao;
	}

	/**
	 * 添加
	 * 
	 * @param <T>
	 * @param obj
	 */
	@Transactional()
	public <T> void save(T obj) {
		getDao().save(obj);
	}

	/**
	 * 添加
	 * 
	 * @param <T>
	 * @param obj
	 */
	public <T> void saveOrUpdate(T obj) {
		getDao().saveOrUpdate(obj);
	}

	/**
	 * 批量添加
	 * 
	 * @param <T>
	 * @param list
	 */
	public <T> void saveAll(Collection<T> list) {
		getDao().saveOrUpdateAll(list);
	}

	/**
	 * 删除对象
	 * 
	 * @param obj
	 */
	public void delete(Object obj) {
		getDao().delete(obj);
	}

	/**
	 * 根据指定的ID删除对象
	 * 
	 * @param <T>
	 * @param id
	 * @param clz
	 */
	public <T> void delete(Serializable id, Class<T> clz) {
		Object obj = getDao().findById(clz, id);
		getDao().delete(obj);
	}

	/**
	 * 根据 指定的多个ID删除对象
	 * 
	 * @param <T>
	 * @param ids
	 * @param clz
	 */
	public <T> void deleteAll(Class<T> clz, String ids[]) {
		if (ids != null && ids.length > 0) {
			String queryString = "from " + clz.getName() + " where id in(";
			for (String id : ids) {
				queryString += "'" + id + "',";
			}
			queryString = queryString.substring(0, queryString.length() - 1) + ")";
			List<T> list = find(queryString);
			if (list.size() > 0) {
				deleteAll(list);
			}
		}
	}

	/**
	 * 删除多个对象
	 * 
	 * @param <T>
	 * @param list
	 */
	public <T> void deleteAll(Collection<T> list) {
		getDao().deleteAll(list);
	}

	protected void updateByQuery(String queryString) {
		updateByQuery(queryString, new Object[] {});
	}

	protected void updateByQuery(String queryString, Object... params) {
		getDao().update(queryString, params);
	}

	/**
	 * 更新对象
	 * 
	 * @param obj
	 */
	public void update(Object obj) {
		getDao().update(obj);
	}

	/**
	 * 通过查询语句查询
	 * 
	 * @param <T>
	 * @param queryString
	 * @return
	 */
	protected <T> List<T> find(String queryString) {
		return getDao().findList(queryString);
	}

	/**
	 * 通过查询语句和一个或多个参数查询
	 * 
	 * @param <T>
	 * @param queryString
	 * @param params
	 * @return
	 */
	protected <T> List<T> find(String queryString, Object... params) {
		return getDao().findList(queryString, params);
	}

	/**
	 * 通过name名称来查找Core
	 * 
	 * @param name
	 * @return
	 */
	public <T> T findByName(String name, Class<T> klass) {
		if (name != null && !name.isEmpty()) {
			String queryString = "from " + klass.getName() + " where name=?";
			List<T> cores = find(queryString, name);
			if (cores.size() == 1)
				return cores.get(0);
		}
		return null;
	}

	/**
	 * 查询全部
	 * 
	 * @param <T>
	 * @param clz
	 * @return
	 */
	public <T> List<T> findAll(Class<T> clz) {
		return getDao().findAll(clz);
	}

	/**
	 * 通过ID查询
	 * 
	 * @param <T>
	 * @param clz
	 * @param id
	 * @return
	 */
	public <T> T findById(Class<?> clz, Serializable id) {
		T pvale = getDao().findById(clz, id);
		return pvale;
	}

	/**
	 * 分页查询
	 * 
	 * @param <T>
	 * @param queryString
	 * @param page
	 * @param rows
	 * @return
	 */
	public <T> List<T> findByPage(String queryString, int page, int rows) {
		return getDao().findList(queryString, (page - 1) * rows, rows);
	}

	public <T> List<T> findByPage(String queryString, int page, int rows, Object... params) {
		return getDao().findList(queryString, (page - 1) * rows, rows, params);
	}

	public <T> List<T> findByCriteria(DetachedCriteria deCriteria, int fistResult, int maxResult) {
		return getDao().findByCriteria(deCriteria, fistResult, maxResult);
	}

	/**
	 * 查询总数
	 * 
	 * @param clz
	 * @return
	 */
	public Long getCount(Class<?> clz) {
		String queryString = "select count(*) from " + clz.getName();
		return getCount(queryString, new Object[] {});
	}

	/**
	 * 查询总数
	 * 
	 * @param clz
	 * @return
	 */
	public Long getCount(String queryString, Object... params) {
		List result = null;
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		result = getDao().findList(queryString, params);
		if(result == null || result.isEmpty())
			return 0L;
		else 
			return (Long)result.get(0);
	}

	public <T> T get(Class<T> clazz, Serializable id) {
		return dao.get(clazz, id);
	}

	/**
	 * 查询数据库设置
	 * 
	 * @author yuhao
	 * @return
	 */

	public <T> List<T> findAll(Class<T> klass, Serializable ids[]) {
		StringBuffer queryString = new StringBuffer("from  " + klass.getName() + " where id in(");
		for (int i = 0; i < ids.length; i++) {
			if (i > 0)
				queryString.append(",");
			queryString.append("?");
		}
		queryString.append(")");
		List<T> list = find(queryString.toString(), ids);
		return list;
	}

	/**
	 * 查询数据库设置
	 * 
	 * @author yuhao
	 * @return
	 */

	public <T> List<T> findNotAll(Class<T> klass, Serializable ids[]) {
		StringBuffer queryString = new StringBuffer("from  " + klass.getName() + " where id not in(");
		for (int i = 0; i < ids.length; i++) {
			if (i > 0)
				queryString.append(",");
			queryString.append("?");
		}
		queryString.append(")");
		List<T> list = find(queryString.toString(), ids);
		return list;
	}

	/**
	 * 合并更新
	 * 
	 * @param <T>
	 * @param obj
	 */
	public <T> void merge(T obj) {
		getDao().merge(obj);
	}

	public <T> void execNativeSQL(List<String> sqls, List<List<Object>> params) throws Exception {
		getDao().execNativeSQL(sqls, params);
	}

	public <T> void execNativeSQL(String sql, List<Object> params) throws Exception {
		getDao().execNativeSQL(sql, params);
	}

	public <T> void execNativeSQL(String sql) throws Exception {
		getDao().execNativeSQL(sql);
	}
	
	public List findByProperty(Class clz,String field,Object args){
		String queryString = "from " + clz.getName() + " where "+field+"=?";
		return find(queryString, args);
	}
}
