package com.xdtech.patent.service;

import java.io.File;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import com.xdtech.patent.entity.ImportTemplete;

@Service("modelService")
@SuppressWarnings("unchecked")
public class ModelService extends CommonService{
	/**
	 * 保存模板
	 * @param conf
	 * @return
	 */
	public void saveOrUpdate(ImportTemplete conf){
		String hql = "from  " +ImportTemplete.class.getName()+"  where profileName=?";
		List<ImportTemplete> cfg = find(hql, new Object[] { conf.getProfileName()});
		ImportTemplete importCfg = null;
		if(CollectionUtils.isNotEmpty(cfg)){
			importCfg = cfg.get(0);
			//update(conf);
		}else{
			importCfg = conf;
			save(importCfg);
		}
	}
	
	/**
	 * 指定用户下的模板列表
	 * 
	 * @param user
	 * @param defaultRole
	 * @return
	 */
	public List<ImportTemplete> findModleByUser(int id) {
		String queryString = "from " +ImportTemplete.class.getName()+" where uid=?";
		List<ImportTemplete> list = find(queryString, new Object[]{id+""});
		return list;
	}
	/**
	 * 删除模板并删除xml文件
	 * @param cfg
	 */
	public void delete(ImportTemplete cfg,String path){
		super.delete(cfg);
		if(path !=null){
			File file = new File(path);
			file.delete();
		}
	}	
}
