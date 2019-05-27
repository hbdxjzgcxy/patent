package com.xdtech.patent.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * HibernateDAO
 * 
 * @author Chang Fei
 */
@Repository
@SuppressWarnings({ "unchecked", "rawtypes" })
public class BaseHibernateDAO implements BaseDAO {

	@Autowired(required = true)
	private SessionFactory factory;

	private Session getSession() {
		return factory.getCurrentSession();
	}

	public <T> T findById(Class<?> clz, Serializable id) {
		return (T) getSession().get(clz, id);
	}

	public <T> T loadById(Class<?> clz, Serializable id) {
		T entity = (T) getSession().load(clz, id);
		return entity;
	}

	public <T> List<T> findAll(Class<?> clz) {
		String hql = "from " + clz.getName();
		List<T> list = getSession().createQuery(hql).list();
		return list;
	}

	public <T> T save(T entity) {
		try {
			getSession().save(entity);
			flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entity;
	}

	public void update(Object entity) {
		getSession().update(entity);
		flush();
	}

	public void update(Object entity, LockMode lock) {
		getSession().update(entity);
		flush();
	}

	public <T> void saveOrUpdate(Object entities) {
		getSession().saveOrUpdate(entities);
		flush();
	}

	public <T> void saveOrUpdateAll(Collection<?> entities) {
		getSession().saveOrUpdate(entities);
		flush();
	}

	public void delete(Object entity) {
		getSession().delete(entity);
		flush();
	}

	public void delete(Object entity, LockMode lock) {
		getSession().delete(entity);
		flush();
	}

	public void deleteById(Class<?> clz, Serializable id) {
		this.delete(this.loadById(clz, id));
		flush();
	}

	public void deleteAll(Collection<?> entities) {
		entities.forEach(e -> {
			getSession().delete(e);
		});
		flush();
	}

	public void merge(Object entity) {
		getSession().merge(entity);
		flush();
	}

	public <T> T get(final String hsql) {
		return (T) getSession().createQuery(hsql).uniqueResult();
	}

	public <T> T get(Class<T> clazz, Serializable id) {
		return (T) getSession().get(clazz, id);
	}

	public List get(String hql, Object... values) {
		Query query = getSession().createQuery(hql);
		for (int i = 0; i < values.length; i++) {
			query.setParameter(i, values[i]);
		}
		return query.list();
	}

	public <T> List<T> findList(String hsql) {
		List list = getSession().createQuery(hsql).list();
		return (List<T>) list;
	}

	public List findList(final String hql, final int start, final int number, final Object... values) {
		Query query = getSession().createQuery(hql);
		query.setFirstResult(start);
		query.setMaxResults(number);
		if (values != null && values.length > 0) {
			for (int i = 0; i < values.length; i++) {
				query.setParameter(i, values[i]);
			}
		}
		List list = query.list();
		return list;

	}

	public List findList(final String hql, final int start, final int number) {
		Query query = getSession().createQuery(hql);
		query.setFirstResult(start);
		query.setMaxResults(number);
		List list = query.list();
		return list;

	}

	private List find(Query q, Object... values) {
		if (values != null && values.length > 0) {
			for (int i = 0; i < values.length; i++) {
				q.setParameter(i, values[i]);
			}
		}
		return q.list();
	}

	public List findList(String queryString, Object... values) {
		Query query = getSession().createQuery(queryString);
		return find(query, values);
	}

	public List findByNamedQuery(String queryName) {
		Query query = getSession().getNamedQuery(queryName);
		return query.list();
	}

	public <T> List<T> findByNamedQuery(String queryName, Object... values) {
		Query q = getSession().getNamedQuery(queryName);
		return (List<T>) find(q, values);
	}

	public <T> List<T> findByNamedQuery(final String queryName, final String[] paramNames, final Object[] values) {
		Query query = getSession().getNamedQuery(queryName);
		for (int i = 0; i < paramNames.length; i++) {
			query.setParameter(paramNames[i], values[i]);
		}
		return query.list();
	}

	public <T> Iterator<T> iterate(final String hql) {
		return getSession().createQuery(hql).iterate();
	}

	public <T> Iterator<T> iterate(final String hql, final Object... values) {
		Query query = getSession().createQuery(hql);
		for (int i = 0; i < values.length; i++) {
			query.setParameter(i, values[i]);
		}
		return query.iterate();

	}

	public DetachedCriteria createDetachedCriteria(Class clz) {
		return DetachedCriteria.forClass(clz);
	}

	public Criteria createCriteria(final Class<?> clz) {
		return createDetachedCriteria(clz).getExecutableCriteria(getSession());
	}

	public List findByCriteria(final DetachedCriteria criteria) {
		return criteria.getExecutableCriteria(getSession()).list();
	}

	public <T> List<T> findByCriteria(final DetachedCriteria criteria, final int firstResult, final int maxResults) {
		return criteria.getExecutableCriteria(getSession()).setFirstResult(firstResult).setMaxResults(maxResults)
				.list();

	}

	public <T> List<T> findByCriteria(final Class<?> clz, final Criterion... criterion) {
		Criteria crit = getSession().createCriteria(clz);
		for (Criterion c : criterion) {
			if (c != null) {
				crit.add(c);
			}
		}
		List list = crit.list();
		return list;

	}

	public Integer getRowCount(DetachedCriteria criteria) {
		criteria.setProjection(Projections.rowCount());
		List list = this.findByCriteria(criteria, 0, 1);
		return (Integer) list.get(0);

	}

	public Object getStatValue(DetachedCriteria criteria, String propertyName, String StatName) {
		if (StatName.toLowerCase().equals("max"))
			criteria.setProjection(Projections.max(propertyName));
		else if (StatName.toLowerCase().equals("min"))
			criteria.setProjection(Projections.min(propertyName));
		else if (StatName.toLowerCase().equals("avg"))
			criteria.setProjection(Projections.avg(propertyName));
		else if (StatName.toLowerCase().equals("sum"))
			criteria.setProjection(Projections.sum(propertyName));
		else
			return null;
		List list = this.findByCriteria(criteria, 0, 1);
		return list.get(0);
	}

	public int update(final String hql, final Object... values) {
		Query query = getSession().createQuery(hql);
		for (int i = 0; i < values.length; i++) {
			query.setParameter(i, values[i]);
		}
		return query.executeUpdate();
	};

	@Override
	public <T> void execNativeSQL(final List<String> sqls, final List<List<Object>> params) throws Exception {
		if (sqls == null || sqls.size() == 0) {
			throw new Exception("无效的SQL语句。");
		}
		if (params != null && params.size() > 0 && sqls.size() != params.size()) {
			throw new Exception("params size 和SQLS 的长度不一致!");
		}
		getSession().doWork(new Work() {
			public void execute(Connection connection) throws SQLException {
				String DB_OEM = connection.getMetaData().getDatabaseProductName();
				connection.setAutoCommit(false);
				PreparedStatement pstmt = null;
				String sql = "";
				List<Object> cparams = null;
				for (int i = 0; i < sqls.size(); i++) {
					sql = sqls.get(i);
					pstmt = connection.prepareStatement(sql);
					cparams = params.get(i);
					if (cparams != null) {
						pstmt = connection.prepareStatement(sql);
						for (int k = 0; k < cparams.size(); k++) {
							Object value = cparams.get(k);
							// derby null val bug.
							if (value == null && DB_OEM.equals("Apache Derby")) {
								pstmt.setNull(k + 1, Types.VARCHAR);
							} else {
								pstmt.setObject(k + 1, value);
							}

						}
					}
					pstmt.execute();
				}
				connection.commit();

			}
		});
	}

	@Override
	public <T> void execNativeSQL(String sql, List<Object> params) throws Exception {
		List<String> sqls = new ArrayList<String>();
		sqls.add(sql);
		List<List<Object>> gparams = new ArrayList<List<Object>>();
		gparams.add(params);
		execNativeSQL(sqls, gparams);
	}

	@Override
	public <T> void execNativeSQL(String sql) throws Exception {
		List<String> sqls = new ArrayList<String>();
		sqls.add(sql);
		execNativeSQL(sqls, null);
	};

	public void flush() {
		//getSession().flush();
	}

}
