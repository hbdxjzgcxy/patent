package com.xdtech.patent.service;

import java.io.File;
import java.net.MalformedURLException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.xdtech.SysProperties;
import com.xdtech.patent.conf.AppConf;
import com.xdtech.patent.entity.DBFolder;
import com.xdtech.patent.entity.ImportTemplete;
import com.xdtech.patent.entity.PatentDB;
import com.xdtech.patent.entity.User;
import com.xdtech.search.client.XDIndexOperationSupport;
import com.xdtech.search.client.ws.IndexOperateServiceInterface;
import com.xdtech.search.client.ws.XDCloudSearchException_Exception;
import com.xdtech.util.Dedupl;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

@Service("topicService")
@SuppressWarnings("unchecked")
public class TopicService extends CommonService {

	final String wsdl = AppConf.get().get("operation.servcie.wsdl", "http://127.0.0.1:8080/xdcloudsearch/service/search?wsdl");
	@Resource
	private ModelService modelService;
	/*
	 * 自定义ztree图标
	 */
	private final static String TOPIC_ICON_OPEN = "/xdcloudpatent/theme/default/images/folder_open.png";
	private final static String TOPIC_ICON_CLOSE = "/xdcloudpatent/theme/default/images/tree/style0/file_open.png";

	/**
	 * 构建专题目录树展现
	 * 
	 * @param User
	 * @return string(type:json)
	 */
	public String buildTreeData(User user) {

		List<PatentDB> dbList = null;
		if (SysProperties.ROLE_ADMIN.equals(user.getRole().getCode())) {
			dbList = findAll(PatentDB.class);
		} else {
			int tid = user.getId();
			if(user.getPid()!=0){
				tid = user.getPid();
			}
			dbList = findByProperty(PatentDB.class, "user.id", tid);
		}

		List<Map<String, Object>> lists = new LinkedList<Map<String, Object>>();

		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("id", "1");
		map.put("pId", "0");
		map.put("name", "专题");
		map.put("open", "true");
		map.put("icon", TOPIC_ICON_CLOSE);
		map.put("iconOpen", TOPIC_ICON_OPEN);
		map.put("iconClose", TOPIC_ICON_CLOSE);
		map.put("childOuter", false);
		lists.add(map);
		for (PatentDB db : dbList) {
			String dbStatus = "";
			if (db.getStatus() == 0)
				dbStatus = "(索引构建中)";

			map = new LinkedHashMap<String, Object>();
			map.put("id", "d" + db.getId());
			map.put("name", db.getAlias() + dbStatus);
			map.put("rname", db.getName());
			map.put("pId", 1);
			map.put("open", true);
			map.put("status", dbStatus);
			map.put("icon", TOPIC_ICON_CLOSE);
			map.put("iconOpen", TOPIC_ICON_OPEN);
			map.put("iconClose", TOPIC_ICON_CLOSE);
			map.put("childOuter", false);
			lists.add(map);

			if (db.getStatus() == 1) {
				List<DBFolder> folderList = find("from DBFolder WHERE db = " + db.getId() + " ORDER BY sort");
				for (DBFolder folder : folderList) {
					Map<String, Object> submap = new LinkedHashMap<String, Object>();
					submap.put("id", "f" + folder.getId());
					submap.put("name", folder.getAlias());
					submap.put("rname", folder.getName());
					if (folder.getLevel() == 1 && folder.getLeaf() <= 1)
						submap.put("pId", "d" + db.getId());
					else
						submap.put("pId", "f" + folder.getPid());
					//					submap.put("open", true);
					submap.put("iconOpen", TOPIC_ICON_OPEN);
					submap.put("icon", TOPIC_ICON_CLOSE);
					submap.put("iconClose", TOPIC_ICON_CLOSE);
					submap.put("childOuter", false);
					lists.add(submap);
				}
			}
		}
		net.sf.json.JSONArray jsonArray = net.sf.json.JSONArray.fromObject(lists);
		String json = jsonArray.toString();
		return json;
	}

	public PatentDB saveOrUpdate(PatentDB db) {
		String hql = "from PatentDB where name=? and user.id=?";
		//List<PatentDB> persistentObjs = findByProperty(PatentDB.class, "name", db.getName());
		List<PatentDB> persistentObjs = find(hql, new Object[] { db.getName(), db.getUser().getId() });
		PatentDB patentDB = null;
		if (CollectionUtils.isNotEmpty(persistentObjs)) {
			patentDB = persistentObjs.get(0);
			/*patentDB.setName(db.getName());
			patentDB.setAlias(db.getName());
			update(patentDB);*/
		} else {
			patentDB = db;
			save(patentDB);
		}
		return patentDB;
	}

	public void deleteDb(int pid) {
		List<DBFolder> folders = findByProperty(DBFolder.class, "db", pid);
		if (folders != null && !folders.isEmpty()) {
			deleteAll(folders);
		}
		delete(pid, PatentDB.class);
	}

	/**
	 * 索引数据删除程序
	 * 
	 * @param coreName
	 * @param tName
	 *            数据库名称
	 * @param uid
	 *            用户标识
	 * @throws MalformedURLException
	 * @throws XDCloudSearchException_Exception
	 */
	public void deleteIndex(String coreName, String tName, int uid) throws MalformedURLException, XDCloudSearchException_Exception {
		XDIndexOperationSupport indexOpe = new XDIndexOperationSupport(wsdl);
		IndexOperateServiceInterface indexService = indexOpe.getService();
		indexService.deleteByQueryStringInCore(coreName, "T_NAME:" + tName + " AND UID:" + uid);
		indexService.commitAtCore(coreName);
	}

	/**
	 * 数据打包(数据库)
	 * @param file
	 * @param dbId
	 * @param fileName
	 * @param set
	 */
	public void pack(int dbId, String fileName) {
		fileName = FilenameUtils.getBaseName(fileName);
		String[] nodes = fileName.split("-");//根据文件名称生成节点层级
		int nodeSize = nodes.length;
		int patentId = dbId;
		for (int i = 0; i < nodeSize; i++) {
			String name = nodes[i];
			if (StringUtils.isEmpty(name))
				continue;

			/*if (!set.add(name))
				continue;*/

			List<Integer> folders = find("SELECT id from DBFolder WHERE pid="+patentId+" and level=" + (i+1) + " AND name='" + name + "' AND db=" + dbId);
			if (folders.isEmpty()) {
				DBFolder folder = new DBFolder();
				folder.setDb(dbId);
				folder.setFilter("0");
				folder.setLevel(i + 1);
				folder.setName(name);
				folder.setAlias(name);
				if (i == nodeSize - 1)
					folder.setLeaf(1);
				folder.setParent(0);
				/*if (i == 0) {
					folder.setPid(dbId);
				} else {
					List<Integer> ids = find("SELECT id from DBFolder WHERE  db=" + dbId + " AND level= " + i + " AND name = '" + nodes[i - 1] + "'");
					if (!CollectionUtils.isEmpty(ids)) {
						Integer parentId = (Integer) ids.get(0);
						folder.setPid(parentId);
					}
				}*/
				folder.setPid(patentId);
				save(folder);
				patentId = folder.getId();
			} else {
				patentId = folders.get(0);
			}
		}
	}

	/**
	 * 处理专题名称字段为多值字段
	 * @param docNo
	 * @param tName
	 * @return
	 */
	static Dedupl dedupl = null;

	/**
	 * 专题库节点数量验证
	 * 
	 * @param id
	 * @param rows
	 *            limit count
	 * @return
	 */
	public boolean overstep(int id, int rows) {
		Long count = (Long) find("SELECT count(folder) FROM DBFolder folder,PatentDB db WHERE db.id = folder.db AND db.user=" + id).listIterator()
				.next();
		if (count + rows <= 10000)
			return true;
		return false;
	}

	public List<PatentDB> findDbByName(String name, int uid) {
		return find("FROM PatentDB WHERE name='" + name + "' AND user = " + uid);
	}

	public void updateDirectory(String id, String node) {
		String flag = StringUtils.substring(id, 0, 1);
		int key = Integer.parseInt(StringUtils.substring(id, 1));
		if ("d".equals(flag)) {
			PatentDB db = findById(PatentDB.class, key);
			db.setAlias(node);
			update(db);
		} else {
			DBFolder f = findById(DBFolder.class, key);
			f.setAlias(node);
			update(f);
		}
	}

	public void reorder(int id, int sort) {
		updateByQuery("UPDATE DBFolder SET sort=? WHERE id=?", new Integer[] { sort, id });
	}

	/**
	 * 指定用户下的专题列表
	 * 
	 * @param user
	 * @param defaultRole
	 * @return
	 */
	public List<PatentDB> findSpecificUserDB(User user, int defaultRole) {
		int id = user.getId();
		if (SysProperties.ROLE_COMPANY_CHILD.equals(user.getRole().getCode()))
			id = user.getPid();

		List<PatentDB> dbs = null;
		if (SysProperties.ROLE_ADMIN.equals(user.getUsername())) {
			dbs = findAll(PatentDB.class);
		} else {
			dbs = findByProperty(PatentDB.class, "user.id", id);
		}
		return dbs;
	}

	static void unzip(File zipFile, String dest, String passwd) throws ZipException {
		ZipFile zFile = new ZipFile(zipFile);
		//zFile.setFileNameCharset("GBK");// 设置文件名编码，在GBK系统中需要设置
		if (!zFile.isValidZipFile()) {
			throw new ZipException("压缩文件不合法,可能被损坏.");
		}

		File destDir = new File(dest);
		if (destDir.isDirectory() && !destDir.exists()) {
			destDir.mkdir();
		}
		zFile.extractAll(dest);
	}
	/**
	 * 获取模板名称
	 * @return
	 */
	public String getTemplete(String uid) {
		 String pathName  = null;
		//查询出所有的默认模板列表
		 String hql = "from ImportTemplete where activity =? and uid =?";
		List<ImportTemplete> confList = modelService.find(hql, new Object[] {true,uid});
		if(confList.size()>0){
			for(ImportTemplete cfg :confList){
				pathName = cfg.getProfileUrl();
			}
		}
		return pathName;
	}

}
