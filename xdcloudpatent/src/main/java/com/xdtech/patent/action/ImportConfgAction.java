package com.xdtech.patent.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.common.collect.Lists;
import com.xdtech.patent.entity.ImportTemplete;
import com.xdtech.patent.entity.MapConfig;
import com.xdtech.patent.reader.ImportCfg;
import com.xdtech.patent.service.ModelService;
import com.xdtech.util.ResourceUtil;

/**
 * 专题库导入配置管理Action
 * 
 * @author coolBoy
 *
 */
@RequestMapping("/model")
@Controller
public class ImportConfgAction extends BaseAction {

	private ModelService modelService;

	@Autowired
	public void setModelService(ModelService modelService) {
		this.modelService = modelService;
	}

	/**
	 * 模板管理页面展示
	 * 
	 * @return
	 */
	@RequestMapping("show")
	public ModelAndView show() {
		ModelAndView mv = new ModelAndView("import/model");
		List<ImportTemplete> models = modelService.findModleByUser(currentUser.getId());
		mv.addObject("modelList", models);
		mv.addObject("sel", "model");
		mv.addObject("cur", "admin_center");
		return mv;
	}

	/**
	 * 模板管理新增模板
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("add")
	public ModelAndView add(HttpServletRequest request) {
		ImportTemplete templete = new ImportTemplete();
		String modelName = request.getParameter("modelName");
		String modelDesc = request.getParameter("modelDesc");
		String activity = request.getParameter("activity");
		templete.setActivity(activity == null ? false : true);
		templete.setCreateDate(new Date());
		templete.setProfileDesc(modelDesc);
		templete.setProfileName(modelName);
		String filename = buildTempleFileName(modelName);
		templete.setProfileUrl(filename);
		templete.setUid(currentUser.getId() + "");
		if (activity != null) {
			List<ImportTemplete> cfList = modelService.findModleByUser(currentUser.getId());
			for (ImportTemplete cf : cfList) {
				cf.setActivity(false);
				modelService.save(cf);
			}
		}
		modelService.save(templete);
		String path = getTempletePath(filename);
		createXML(request, path);
		return new ModelAndView("redirect:show.html");
	}

	/**
	 * 生成模板文件名称
	 * 
	 * @param templeteName
	 * @return
	 */
	private String buildTempleFileName(String templeteName) {
		String filename = templeteName + "_import_cfg.xml";
		return filename;
	}

	/**
	 * 获取模板的绝对路径
	 * 
	 * @param templeteFileName
	 * @return
	 */
	private String getTempletePath(String templeteFileName) {
		String userTemplateDir = ResourceUtil.getUserTemplateDir(currentUserName());
		File file = new File(userTemplateDir);
		file = new File(file, templeteFileName);
		return file.getPath();
	}

	/**
	 * 根据id删除模板
	 * 
	 * @param tId
	 * @return
	 */
	@RequestMapping("delete")
	public @ResponseBody String delete(@RequestParam(value = "id") String tId) {
		String msg = "success";
		ImportTemplete importCfg = modelService.findById(ImportTemplete.class, Integer.parseInt(tId));
		String path = getTempletePath(importCfg.getProfileUrl());
		modelService.delete(importCfg, path);
		return msg;
	}

	/**
	 * 设置默认的模板
	 * 
	 * @param tId
	 * @return
	 */
	@RequestMapping("default")
	public @ResponseBody String setDefault(@RequestParam(value = "id") String tId) {
		String msg = "success";
		ImportTemplete cfg = modelService.findById(ImportTemplete.class, Integer.parseInt(tId));
		List<ImportTemplete> cfList = modelService.findModleByUser(currentUser.getId());
		for (ImportTemplete cf : cfList) {
			cf.setActivity(false);
			modelService.save(cf);
		}
		cfg.setActivity(true);
		modelService.save(cfg);
		return msg;
	}

	/**
	 * 修改模板内容
	 * 
	 * @param tId
	 * @return
	 */
	@RequestMapping("tomodify")
	public ModelAndView toModify(@RequestParam(value = "id") String tId) {
		ModelAndView mv = new ModelAndView("modify_model");
		ImportTemplete cfg = modelService.findById(ImportTemplete.class, Integer.parseInt(tId));
		String profileName = cfg.getProfileName();
		String profileDesc = cfg.getProfileDesc();
		boolean activity = cfg.isActivity();
		Map<String, com.xdtech.patent.reader.ImportCfg> map = parseXML(cfg.getProfileUrl());
		mv.addObject("profileName", profileName);
		mv.addObject("profileDesc", profileDesc);
		mv.addObject("activity", activity);
		mv.addObject("id", tId);
		List<MapConfig> list = Lists.newArrayList();
		for (String key : map.keySet()) {
			if (key.equals("PA")) {
				mv.addObject("PA", getText("PA", map));
			} else if (key.equals("AN")) {
				mv.addObject("AN", getText("AN", map));
			} else if (key.equals("IPC")) {
				mv.addObject("IPC", getText("IPC", map));
			} else if (key.equals("PN")) {
				mv.addObject("PN", getText("PN", map));
			} else {
				list.add(getText(key, map));
			}
		}
		mv.addObject("mapList", list);
		return mv;
	}

	/**
	 * 根据路径解析xml文件
	 * 
	 * @param profileUrl
	 * @return
	 */
	public Map<String, ImportCfg> parseXML(String profileUrl) {
		Map<String, com.xdtech.patent.reader.ImportCfg> map = null;
		try {
			String path = ResourceUtil.getUserTemplateDir(currentUserName());
			File file = new File(path, profileUrl);
			map = com.xdtech.patent.reader.ImportCfg.get(file.getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 获取映射对象
	 * 
	 * @param key
	 * @param map
	 * @return
	 */
	private MapConfig getText(String key, Map<String, ImportCfg> map) {
		MapConfig mapc = new MapConfig();
		mapc.setIndexName(key);
		String excelName = map.get(key).getColumn().toString().replaceAll(",", "；");
		mapc.setExcelName(excelName.substring(1, excelName.length() - 1));
		String sep = Arrays.toString(map.get(key).getSplitChars()).replaceAll(",", "").replaceAll(" ", "");
		if (sep.equals("null")) {
			sep = "";
		} else {
			sep = sep.substring(1, sep.length() - 1);
		}
		mapc.setSepa(sep);
		return mapc;
	}

	/**
	 * 修改模板并将配置文件名称修改
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("modify")
	public @ResponseBody String modify(HttpServletRequest request) {
		String msg = "success";
		String id = request.getParameter("id");
		String modelName = request.getParameter("modelName");
		String modelDesc = request.getParameter("modelDesc");
		String activity = request.getParameter("activity");

		ImportTemplete conf = modelService.findById(ImportTemplete.class, Integer.parseInt(id));

		// 1.刪除模板文件
		File file = new File(getTempletePath(conf.getProfileUrl()));
		file.delete();

		// 2.更新数据库模板对应的记录
		String templeteFileName = buildTempleFileName(modelName);
		conf.setActivity(activity == null ? false : true);
		conf.setCreateDate(new Date());
		conf.setProfileDesc(modelDesc);
		conf.setProfileName(modelName);
		conf.setProfileUrl(templeteFileName);
		modelService.update(conf);

		// 3.生成新的模板文件
		createXML(request, getTempletePath(templeteFileName));
		return msg;
	}

	/**
	 * 下载模板
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("download")
	public void downLoadXML(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		try {
			File file = null;
			String filename = "import_cfg.xml";
			InputStream fis = ImportCfg.class.getResourceAsStream("/import_cfg.xml");
			if (!"0".equals(id)) {
				ImportTemplete conf = modelService.findById(ImportTemplete.class, Integer.parseInt(id));
				// path是指欲下载的文件的路径。
				file = new File(getTempletePath(conf.getProfileUrl()));
				// 取得文件名。
				filename = file.getName();
				fis = new BufferedInputStream(new FileInputStream(getTempletePath(conf.getProfileUrl())));
			}
			// 以流的形式下载文件。
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			// 清空response
			response.reset();
			String userAgent = request.getHeader("User-Agent");
			String name = new String(filename.getBytes(), "ISO8859-1");
			// 针对IE或者以IE为内核的浏览器：
			if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
				name = java.net.URLEncoder.encode(filename, "UTF-8");
			}
			// 设置response的Header
			response.addHeader("Content-Disposition", "attachment;filename=" + name);
			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/xml");
			toClient.write(buffer);
			toClient.flush();
			toClient.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 上传模板
	 * 
	 * @param request
	 * @param uploadfile
	 * @return
	 */
	@RequestMapping("upload")
	public ModelAndView uploadModel(HttpServletRequest request, MultipartFile[] uploadfile) {
		String id = request.getParameter("tid");
		ImportTemplete conf = modelService.findById(ImportTemplete.class, Integer.parseInt(id));
		for (MultipartFile mf : uploadfile) {
			if (!mf.isEmpty()) {
				if (conf != null) {
					File file = new File(getTempletePath(conf.getProfileUrl()));
					try {
						FileUtils.copyInputStreamToFile(mf.getInputStream(), file);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return new ModelAndView("redirect:show.html");
	}

	/**
	 * 创建生成xml文件
	 * 
	 * @param request
	 * @param modelName
	 */
	private void createXML(HttpServletRequest request, String path) {
		String[] indexNames = request.getParameterValues("indexName");
		String[] excNames = request.getParameterValues("EXCName");
		String[] separators = request.getParameterValues("separator");
		DocumentBuilder builder;
		try {
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document document = builder.newDocument();
			Element root = document.createElement("导入配置项");
			document.appendChild(root);
			for (int i = 0; i < indexNames.length; i++) {
				Element pageElt = document.createElement("字段映射");
				// 创建索引字段名称节点
				Element indexElt = document.createElement("索引字段名称");
				indexElt.setTextContent(indexNames[i]);
				pageElt.appendChild(indexElt);
				// 创建excle标题节点
				String[] spilExcName = excNames[i].split("；");
				for (int j = 0; j < spilExcName.length; j++) {
					if (StringUtils.isNotEmpty(spilExcName[j])) {
						Element excName = document.createElement("EXCEL标题");
						excName.setTextContent(spilExcName[j]);
						pageElt.appendChild(excName);
					}
				}
				// 创建多值分隔符节点
				if (StringUtils.isNotEmpty(separators[i])) {
					Element separElt = document.createElement("多值分隔符");
					separElt.setTextContent(separators[i]);
					pageElt.appendChild(separElt);
				}
				root.appendChild(pageElt);
			}
			// 将Document映射到文件
			BufferedWriter pw = null;
			try {
				Transformer transFormer = TransformerFactory.newInstance().newTransformer();
				// 设置输出结果
				document.setXmlStandalone(true);
				DOMSource domSource = new DOMSource(document);
				transFormer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
				transFormer.setOutputProperty(OutputKeys.INDENT, "yes");
				transFormer.setOutputProperty(OutputKeys.STANDALONE, "yes");
				pw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), "UTF-8"));
				StreamResult sr = new StreamResult(pw);
				transFormer.transform(domSource, sr);
			} catch (TransformerConfigurationException e) {
				e.printStackTrace();
			} catch (TransformerFactoryConfigurationError e) {
				e.printStackTrace();
			} catch (TransformerException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} finally {
				try {
					pw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}
}
