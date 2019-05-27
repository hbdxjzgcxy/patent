package com.xdtech.patent.action;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Lists;
import com.xdtech.patent.conf.AppConf;
import com.xdtech.patent.entity.ImportTemplete;
import com.xdtech.patent.entity.PatentDB;
import com.xdtech.patent.entity.User;
import com.xdtech.patent.model.TopicModel;
import com.xdtech.patent.reader.PatentReader;
import com.xdtech.patent.service.ModelService;
import com.xdtech.patent.service.TopicService;
import com.xdtech.patent.service.UserService;
import com.xdtech.search.client.XDIndexOperationSupport;
import com.xdtech.search.client.ws.IndexOperateServiceInterface;
import com.xdtech.search.client.ws.StringArray;
import com.xdtech.search.client.ws.XDCloudSearchException_Exception;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

/**
 * 专题控制类
 * 
 * @author Administrator
 *
 */
@RequestMapping("/topic")
@Controller
public class TopicAction extends BaseAction {

	/*
	 * 同步索引数据参数
	 */
	final String wsdl = AppConf.get().get("operation.servcie.wsdl", "http://127.0.0.1:8080/xdcloudsearch/service/search?wsdl");

	@Resource
	private TopicService topicService;
	@Resource
	private UserService userService;
	@Resource
	private ModelService modelService;

	@Autowired
	public void setTopicService(TopicService topicService) {
		this.topicService = topicService;
	}

	@RequestMapping("home")
	public ModelAndView home() {
		File directory = getUserBaseDir();
		List<PatentDB> dbs = topicService.findSpecificUserDB(getCurrentUser(), DEFAULT_ROLE);
		List<TopicModel> voList = new ArrayList<TopicModel>();
		for (PatentDB db : dbs) {
			TopicModel tv = new TopicModel();
			tv.setDb(db);

			tv.setLoginCount(userService.loginLogCount(db.getUser().getId()));
			tv.setLoginEndTime(userService.loginLog(db.getUser().getId()).getTime());
			tv.setPatentCount(db.getCount());

			File userfile = new File(directory, db.getName());
			if (userfile.exists()) {
				float size = FileUtils.sizeOfDirectory(userfile) / (1024 * 1024f);
				tv.setSpace(NumberFormat.getInstance().format(size));// +"MB"
			} else
				tv.setSpace("0");
			voList.add(tv);
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("sel", "topic");
		params.put("cur", "admin_center");
		params.put("voList", voList);
		List<ImportTemplete> cfg = modelService.findModleByUser(currentUser.getId());
		params.put("confList", cfg);
		return mv("content/topic", params);
	}

	/**
	 * 专题目录树展现
	 * 
	 * @param user
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/topicTree", produces = { "text/html;charset=UTF-8" })
	public @ResponseBody String directory() throws Exception {
		User user = getCurrentUser();
		if (user != null)
			return topicService.buildTreeData(user);

		return "";
	}

	/**
	 * 解析附件到上传目录
	 * 
	 * @param request
	 * @return
	 * @throws ZipException
	 * @throws Exception
	 */
	private List<File> resolveFile(MultipartFile[] fiels, String topicName) throws ZipException {
		List<File> files = new ArrayList<File>();
		File userBaseDir = getUserBaseDir();
		File topicDir = new File(userBaseDir, topicName);
		if (!topicDir.exists()) {
			topicDir.mkdirs();
		}
		for (MultipartFile multiFile : fiels) {
			if (!multiFile.isEmpty()) {
				String fileName = multiFile.getOriginalFilename();
				File file = new File(topicDir, fileName);
				try {
					FileUtils.copyInputStreamToFile(multiFile.getInputStream(), file);
				} catch (IOException e) {
					e.printStackTrace();
				}
				String extName = FilenameUtils.getExtension(fileName).toLowerCase();
				if ("xls".equals(extName) || "xlsx".equals(extName)) {
					files.add(file);
				} else if ("zip".equals(extName)) {
					files.addAll(extractExcelFile(new File(topicDir, "tmp"), file));
				}
			}
		}

		return files;
	}

	/**
	 * 把zip文件抽取到用户目录下面
	 * 
	 * @param userBaseDir
	 * @param zipFile
	 * @return
	 * @throws ZipException
	 */
	private List<File> extractExcelFile(File tmp, File zipFile) throws ZipException {
		if (!tmp.exists())
			tmp.mkdir();
		ZipFile zip = new ZipFile(zipFile);
		zip.extractAll(tmp.getPath());
		return selectFile(tmp, Lists.newArrayList());
	}

	private List<File> selectFile(File tmp, List<File> files) {
		for (File file : tmp.listFiles()) {
			if (file.isDirectory()) {
				selectFile(file, files);
			} else if (isMSExcleFile(file)) {
				files.add(file);
			}
		}
		return files;
	}

	/**
	 * 根据文件后缀名测试一个文件是不是微软的Excel文件
	 * 
	 * @param tfile
	 * @return
	 */
	private boolean isMSExcleFile(File tfile) {
		return tfile != null && tfile.isFile() && (tfile.getName().endsWith(".xls") || tfile.getName().endsWith(".xlsx"));
	}

	/**
	 * 创建专题
	 * 
	 * @param user
	 * @param req
	 * @return
	 * @throws ZipException
	 * @throws IOException
	 * @throws Exception
	 */
	@RequestMapping("/create")
	public ModelAndView create(HttpServletRequest request, MultipartFile[] uploadfile) throws ZipException {
		HttpSession session = request.getSession();
		TopicProgressInfo progressInfo = new TopicProgressInfo();
		session.setAttribute("echo", "true");
		session.setAttribute("progressInfo", progressInfo);
		//int uid = currentUser.getId();
		String templete = request.getParameter("templete");

		String topicName = request.getParameter("topicname");
		final List<File> files = Lists.newArrayList();
		try {
			files.addAll(resolveFile(uploadfile, topicName));
		} catch (ZipException e1) {
			e1.printStackTrace();
			throw new ZipException("上传的文件解压失败，请确认文件是正常!");
		}

		/*
		 * 获取动态字段 //TODO:要优化处理
		 */
		final List<String> fields = Lists.newArrayList();
		String dynamicFields[] = request.getParameterValues("dynaFiled");
		String dynamicCodes[] = request.getParameterValues("dynaFiledCode");
		if (dynamicFields.length > 0 && dynamicFields.length == dynamicCodes.length) {
			for (int i = 0; i < dynamicFields.length; i++) {
				if (StringUtils.isNotEmpty(dynamicFields[i]) && StringUtils.isNotEmpty(dynamicCodes[i]))
					fields.add(dynamicFields[i] + "!" + dynamicCodes[i]);
			}
		}

		User user = getCurrentUser();
		PatentDB pdb = new PatentDB();
		pdb.setName(topicName);
		pdb.setAlias(topicName);
		pdb.setUser(user);
		pdb.setCore(CORE_NAME);
		pdb.setStatus(0);
		pdb.setTopicName("");
		pdb.setUser(user);
		PatentDB patentDB = topicService.saveOrUpdate(pdb);
		progressInfo.setFileCount(files.size());
		for (File file : files) {
			try {
				String filename = file.getName();
				progressInfo.getFiles().add(filename);
				topicService.pack(patentDB.getId(), filename);
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
		}

		Executors.newSingleThreadExecutor().execute(new Runnable() {
			@Override
			public void run() {
				int count = patentDB.getCount();
				try {
					TimeUnit.SECONDS.sleep(1);
					for (File file : files) {
						try {
							PatentReader dataset = new PatentReader(currentUserName(),templete).read(file, fields, currentUser, topicName);
							progressInfo.setCurrent(file.getName());
							int size = push(dataset, CORE_NAME, progressInfo);
							count += size;
						} catch (IOException | RuntimeException | InvalidFormatException e) {
							e.printStackTrace();
						} finally {
							progressInfo.addOkCount();
						}
					}

				} catch (Exception e) {
					// e.printStackTrace();
				} finally {
					patentDB.setStatus(1);
					patentDB.setCount(count);
					patentDB.setTopicName(PatentReader.topicStatu);
					topicService.update(patentDB);
					// 判断是否是zip文件,暂且认为zip文件中压缩的文件数大约1
					File tmp = new File(new File(getUserBaseDir(), topicName), "tmp");
					try {
						if (tmp.exists())
							FileUtils.forceDelete(tmp);
					} catch (IOException e) {
						e.printStackTrace();
					}
					session.setAttribute("echo", "false"); //关闭消息显示
				}
			}
		});

		log("create topic", "创建专题");
		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("cur", "admin_center");
		return mv("redirect:home.html", params);
	}

	/**
	 * 修改专题名称
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/modify")
	public @ResponseBody String modify(HttpServletRequest request) {
		String msg = "success";
		String topicId = request.getParameter("id");
		String topicName = request.getParameter("name");
		PatentDB db = topicService.findById(PatentDB.class, Integer.parseInt(topicId));
		db.setAlias(topicName);
		topicService.update(db);
		return msg;
	}

	/**
	 * 删除专题
	 * 
	 * @param request
	 * @return
	 * @throws XDCloudSearchException_Exception
	 * @throws MalformedURLException
	 * @throws Exception
	 */
	@RequestMapping("/delete")
	public @ResponseBody String delete(@RequestParam(value = "id") String tId) throws MalformedURLException, XDCloudSearchException_Exception {
		String msg = "success";
		PatentDB db = topicService.findById(PatentDB.class, Integer.parseInt(tId));
		List<PatentDB> dbList = topicService.findSpecificUserDB(getCurrentUser(),DEFAULT_ROLE);
		//处理重复删除的问题（有待优化）
		int totle = 0;
		for (PatentDB pdb : dbList) {
			if(StringUtils.isNotEmpty(pdb.getTopicName()) && null !=pdb.getTopicName()){
				if(pdb.getId() != db.getId()){
					if(pdb.getTopicName().contains(db.getTopicName()) || db.getTopicName().contains(pdb.getTopicName())){
						totle++;
					}
				}
			}
		}
		topicService.deleteDb(Integer.parseInt(tId));
		//多个专题一套索引
		if(totle == 0){
			try {
				topicService.deleteIndex(CORE_NAME, db.getName(), getCurrentUser().getId());
			} catch (Exception e) {
			}
		}
		log("delete topic", "删除专题");
		return msg;
	}

	/**
	 * 更新专题节点名称
	 * 
	 * @param request
	 * @return
	 * @throws XDCloudSearchException_Exception
	 * @throws MalformedURLException
	 * @throws Exception
	 */
	@RequestMapping("/updateName")
	public @ResponseBody String updateDirectory(@RequestParam(value = "id") String id, @RequestParam(value = "node") String node) {
		topicService.updateDirectory(id, node);
		return "true";
	}

	/**
	 * 专题目录树拖拽排序
	 * 
	 * @param next
	 * @param node
	 * @param sort
	 * @return
	 */
	@RequestMapping("/sort")
	public @ResponseBody String reorder(@RequestParam(value = "ids") String ids) {
		if (StringUtils.isEmpty(ids))
			return "false";
		for (String obj : ids.split(",")) {
			if (!StringUtils.isEmpty(obj)) {
				String id = StringUtils.substring(obj, 0, StringUtils.lastIndexOf(obj, "_"));
				String sort = StringUtils.substring(obj, StringUtils.lastIndexOf(obj, "_") + 1);
				topicService.reorder(Integer.parseInt(StringUtils.substring(id, 1)), Integer.parseInt(sort));
			}
		}
		return "true";
	}

	private ModelAndView mv(String url, Map<String, Object> params) {
		ModelAndView mv = new ModelAndView(url);
		for (Object obj : params.keySet()) {
			mv.addObject("" + obj + "", params.get(obj));
		}
		return mv;
	}

	private static final int MAX_PUSH_COUNT = 100;

	public int push(PatentReader dataSet, String core, TopicProgressInfo progressInfo)
			throws MalformedURLException, XDCloudSearchException_Exception {
		XDIndexOperationSupport indexOpe = new XDIndexOperationSupport(wsdl);
		// indexOpe.getService().clean(core);// 调试使用
		IndexOperateServiceInterface indexService = indexOpe.getService();
		List<String> head = dataSet.getHead();

		List<StringArray> data = dataSet.getData();
		progressInfo.setCurrentSize(data.size());
		int start = 0;
		do {
			int toIndex = Math.min(start + MAX_PUSH_COUNT, data.size());
			indexService.pushAtCore(core, head, data.subList(start, toIndex), null, null);
			start = toIndex;
			progressInfo.setCurrentOK(toIndex);
		} while (start < data.size());
		indexService.commitAtCore(core);
		return data.size();
	}
}
