package com.xdtech.patent.service;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.xdtech.SysProperties;
import com.xdtech.patent.action.SearchForm;
import com.xdtech.patent.entity.Log;
import com.xdtech.patent.entity.Role;
import com.xdtech.patent.entity.SearchHistory;
import com.xdtech.patent.entity.User;
import com.xdtech.patent.entity.UserFavoritor;
import com.xdtech.util.RegexUtils;


@Service("userService")
public class UserService extends CommonService {

	/**
	 * 获取用户
	 * @param loginPara(username/phone/email)
	 * @return
	 */
	public User getUser(String loginPara) {
		StringBuilder queryString = new StringBuilder("FROM User ");

		if (RegexUtils.isEmail(loginPara))
			queryString.append("WHERE email='" + loginPara + "'");
		else if (RegexUtils.isPhoneNumber(loginPara))
			queryString.append("WHERE phone='" + loginPara + "'");
		else
			queryString.append("WHERE username='" + loginPara + "'");

		List<User> users = find(queryString.toString());

		if (users.isEmpty())
			return null;
		else
			return users.get(0);
	}

	/**
	 * 当前用户的子用户
	 * @param pid
	 * @return
	 */
	public List<User> getChildUsers(Integer pid) {
		if (pid == 0)
			return find("from User WHERE pid != 0 ");
		else
			return find("from User WHERE pid = " + pid);
	}

	public Role getDefaultRole() {
		return (Role) findByProperty(Role.class, "code", SysProperties.ROLE_COMPANY).get(0);
	}

	/**
	 * 当前用户的搜索历史
	 * @param uid
	 * @param searchForm
	 * @return
	 */
	public List<SearchHistory> getSearchRecoreds(int uid, SearchForm model) {
		int limit = model.getPageSize();
		int start = (model.getPageNo() - 1) * limit;
		DetachedCriteria dc = DetachedCriteria.forClass(SearchHistory.class);
		dc.add(Restrictions.eq("user", uid));
		if (!StringUtils.isEmpty(model.getKwd()))
			dc.add(Restrictions.like("word", model.getKwd()));
		dc.addOrder(Order.desc("time"));
		return findByCriteria(dc, start, limit);
	}

	/**
	 * 当前用户的搜索历史
	 * @param uid
	 * @param searchForm
	 * @return
	 */
	public Long getSearchRecoredCount(int uid, SearchForm model) {
		long count = 0;
		if (StringUtils.isNotEmpty(model.getKwd())) {
			count = getCount("select count(id) from SearchHistory where user=? and word like ? ", new Object[] { uid, "%" + model.getKwd() });
		} else {
			count = getCount("select count(id) from SearchHistory where user=? ", new Object[] { uid });
		}
		return count;
	}
	
	/**
	 * 系统用户的日志数量
	 * @param uid
	 * @param searchForm
	 * @return
	 */
	public Long logCount(int uid, SearchForm model) {
		long count = 0;
		if(uid > 0){
			count = getCount("select count(id) from Log where user.id=? ", new Object[] { uid });
		}else{
			count = getCount("select count(id) from Log ");
		}
		return count;
	}

	/**
	 * 用户登录历史
	 * @param uid
	 * @param searchForm
	 * @return
	 */
	public Log loginLog(int uid) {
		List<Log> list = find("FROM Log where user = " + uid + " ORDER BY time DESC");
		if (list == null || list.isEmpty())
			return new Log();
		else {
			if (list.size() > 1)
				return (Log) list.get(1);
			else
				return (Log) list.get(0);
		}
	}

	/**
	 * 用户登录历史次数
	 * @param uid
	 * @param searchForm
	 * @return
	 */
	public int loginLogCount(int uid) {
		Long count = (Long)find("SELECT count(id) FROM Log h WHERE user=" + uid).listIterator().next();
		return count!=null? count.intValue():0;
	}

	/**
	 * 当前用户的收藏夹组
	 * @param pid
	 * @return
	 */
	public List<String> favoritesGroup(int uid) {
		return find("SELECT t.folder FROM UserFavoritor t WHERE t.user = " + uid + " GROUP BY t.folder");
	}

	/**
	 * 当前用户的收藏夹组
	 * @param pid
	 * @return
	 */
	public List<UserFavoritor> favorites(int uid, String folder) {
		return find("FROM UserFavoritor WHERE user = " + uid + " AND folder = '" + folder + "'");
	}

	/**
	 * 当前用户的收藏夹
	 * @param pid
	 * @return
	 */
	public Map<String, List<UserFavoritor>> favorites(int uid) {
		Map<String, List<UserFavoritor>> map = new LinkedHashMap<String, List<UserFavoritor>>();
		List<String> folders = favoritesGroup(uid);
		for (String folder : folders) {
			List<UserFavoritor> tmp = findByProperty(UserFavoritor.class, "folder", folder);
			map.put(folder, tmp);
		}
		return map;
	}

	public List<Log> logs(int id, SearchForm model) {
		int limit = model.getPageSize();
		int start = (model.getPageNo() - 1) * limit;
		DetachedCriteria dc = DetachedCriteria.forClass(Log.class);
//		dc.add(Restrictions.eq("user", id));
		dc.addOrder(Order.desc("time"));
		return findByCriteria(dc, start, limit);
	}
	public <T> void delete(Serializable id, Class<T> clz){
		List<Log> list = find("FROM Log WHERE user = " + id);
		if(list !=null && !list.isEmpty()){
			super.deleteAll(list);
		}
		super.delete(id, User.class);
	}
}
