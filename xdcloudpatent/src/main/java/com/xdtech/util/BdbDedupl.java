package com.xdtech.util;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sleepycat.je.Cursor;
import com.sleepycat.je.CursorConfig;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;

/**
 * 利用Berkeley DB 实现去重管理
 * 
 * @author Chang Fei
 */
public class BdbDedupl {

	/** 数据库实例 */
	private static Map<String, Dedupl> databasees = new HashMap<String, Dedupl>(10);

	/** 使用静态单例 */
	private static BdbDedupl dupl = new BdbDedupl();

	/**
	 * 获取指定路径下的去重库实例
	 * 
	 * @param path
	 *            索引存储路径
	 * @param create
	 *            不存在是否创建
	 * @return
	 */
	public static synchronized Dedupl get(String path, boolean create) {
		if (path == null) {
			return null;
		}
		Dedupl dedup = databasees.get(path);
		if (dedup == null) {
			if (create == true) {
				dedup = BdbDedupl.dupl.new BerkeleyDBImpl(path);
				databasees.put(path, dedup);
			}
		}
		return dedup;
	}

	// privted
	private BdbDedupl() {
	}

	/**
	 * flus所有数据库
	 */
	public synchronized void flushAll() {
		for (String key : databasees.keySet()) {
			databasees.get(key).flush();
		}
	}

	/**
	 * 用BerkeleyDB实现去重
	 * 
	 * @author Chang Fei
	 */
	class BerkeleyDBImpl implements Dedupl {
		private Logger log = LoggerFactory.getLogger(BdbDedupl.class);
		/** 数据库环境 */
		private Environment env = null;
		/** 去重库名称 */
		private String deduplDBName = "QC";
		/** 数据原文存储库 */
		private Database duplDB = null;
		/** 空值 */
		private byte[] val = "".intern().getBytes();

		public BerkeleyDBImpl(String path) {
			try {
				File file = new File(path + "/QC-INF");
				if (!file.exists())
					file.mkdirs();
				EnvironmentConfig envConf = new EnvironmentConfig();
				envConf.setAllowCreate(true);
				envConf.setTransactional(false);
				envConf.setCacheSize(30 * 1024 * 1024);
				envConf.setConfigParam("je.log.fileMax", "1000000000");
				this.env = new Environment(file, envConf);
				createdb();

			} catch (Exception de) {
				de.printStackTrace();
				log.info("BDB初始化失败，请确认文件目录存在，并且具有读写权限");
			}
		}

		/**
		 * 创建数据库
		 */
		private void createdb() {
			DatabaseConfig dbConf = new DatabaseConfig();
			dbConf.setAllowCreate(true);
			dbConf.setTransactional(false);
			dbConf.setSortedDuplicates(false);
			String database = deduplDBName;
			this.duplDB = env.openDatabase(null, database, dbConf);
		}

		/**
		 * 清空数据库
		 */
		public boolean clear() {
			//env.truncateDatabase(null, deduplDBName, false);
			CursorConfig ccfig = new CursorConfig();
			ccfig.setReadCommitted(true);
			//ccfig.setReadUncommitted(true);
			Cursor cursor = duplDB.openCursor(null, ccfig);
			 DatabaseEntry foundKey = new DatabaseEntry();
			 DatabaseEntry foundData = new DatabaseEntry(); 
			while(cursor.getNext(foundKey, foundData, LockMode.DEFAULT)==OperationStatus.SUCCESS){
				cursor.delete();
			}
			cursor.close();
			flush();
			return true;
		}

		public boolean clearRecord(String idMessage) {
			return false;
		}

		/**
		 * 缓存刷出
		 */
		public synchronized void flush() {
			try {
				if (env != null) {
					env.sync();
				}
			} catch (DatabaseException e) {
				log.info("数据已经同步完成，错误消息：" + e.getMessage());
			}
		}

		/**
		 * 保存记录，键为空值
		 */
		public boolean hasRecord(String idMessage) {
			DatabaseEntry key = new DatabaseEntry(idMessage.getBytes());
			DatabaseEntry value = new DatabaseEntry();
			OperationStatus result = duplDB.get(null, key, value, LockMode.DEFAULT);
			if (result.equals(OperationStatus.SUCCESS)) {
				return true;
			}
			return false;
		}

		/**
		 * 保存记录
		 */
		public synchronized boolean saveRecord(String md5Key, String md5Value) {
			DatabaseEntry key = new DatabaseEntry(md5Key.getBytes());
			DatabaseEntry value = null;
			if (md5Value == null) {
				value = new DatabaseEntry(val);
			} else {
				value = new DatabaseEntry(md5Value.getBytes());
			}
			try {
				duplDB.put(null, key, value);
			} catch (DatabaseException e) {

			}
			return hasRecord(md5Key);
		}

		/**
		 * 查询记录
		 */
		public String getRecord(String md5Key) {
			DatabaseEntry key = new DatabaseEntry(md5Key.getBytes());
			DatabaseEntry value = new DatabaseEntry();
			OperationStatus result = duplDB.get(null, key, value, LockMode.DEFAULT);
			if (result.equals(OperationStatus.SUCCESS)) {
				return new String(value.getData());
			}
			return null;
		}

		/**
		 * 删除记录
		 */
		public boolean remove(List<String> md5Keys) {
			if (md5Keys != null && md5Keys.size() > 0) {
				for (String key : md5Keys) {
					// System.out.println(hasRecord(key));
					DatabaseEntry entry = new DatabaseEntry(key.getBytes());
					duplDB.delete(null, entry);
				}
			}
			return true;
		}
	}

}
