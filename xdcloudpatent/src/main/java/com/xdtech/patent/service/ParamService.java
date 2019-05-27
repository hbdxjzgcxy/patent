package com.xdtech.patent.service;
import java.io.File;
import java.net.URL;

import org.springframework.stereotype.Service;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.Dom4JDriver;
import com.xdtech.patent.action.ParamContext;
import com.xdtech.patent.entity.ParamInfo;
import com.xdtech.patent.service.CommonService;

@Service("paramService")
public class ParamService extends CommonService {

	public void initSystemParam() {
		XStream xst = new XStream(new Dom4JDriver());
		
		URL webRootPath = ParamService.class.getResource("/");
		//String classPath = webRootPath.getFile() +"WEB-INF" +  File.separator + "classes";
		String classPath = webRootPath.getFile();
		StringBuilder sb = new StringBuilder(classPath);
		sb.append(File.separator).append("data");
		sb.append(File.separator).append("init_param.xml");
		File file = new File(sb.toString());

		ParamInfo param = new ParamInfo();
		Object obj = xst.fromXML(file);
		if (obj != null) {
			param = (ParamInfo) obj;
			ParamInfo p = findParamById(param.getId());
			if (p == null) {
				addParam(param);
				ParamContext.setParamInfo(param);
			} else {
				ParamContext.setParamInfo(p);
			}
		}

	}

	public ParamInfo findParamById(String id) {
		return get(ParamInfo.class, id);
	}

	public void addParam(ParamInfo param) {
		save(param);
	}

	public void updateParam(ParamInfo param) {
		update(param);
	}
}
