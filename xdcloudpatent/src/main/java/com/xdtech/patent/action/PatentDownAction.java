package com.xdtech.patent.action;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.reflect.MethodUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Maps;
import com.xdtech.parser.ParseResult;
import com.xdtech.parser.PatentParser;
import com.xdtech.patent.entity.ImportTemplete;
import com.xdtech.patent.reader.ImportCfg;
import com.xdtech.patent.service.ModelService;
import com.xdtech.search.client.QueryObjectBuilder;
import com.xdtech.search.client.ws.BooleanQuery;
import com.xdtech.util.JsonUtil;
import com.xdtech.util.ResourceUtil;

@Controller
@RequestMapping("/patentDown")
public class PatentDownAction extends AbstractBaseSeachAction {

	@Autowired
	private ModelService modelService;

	/**
	 * 
	 * @param docNo
	 * @param kwd
	 * @param batch
	 *            批量标识 0 1
	 * @return
	 */
	@RequestMapping("/downpage")
	public ModelAndView downpage(@RequestParam(value = "docNo") String docNo, @RequestParam(value = "kwd") String kwd,
			@RequestParam(value = "batch") String batch,@RequestParam(value = "path") String path,
			@RequestParam(value = "tName") String tName) {
		List<ImportTemplete> modelList = modelService.findModleByUser(currentUser.getId());
		Map<String, ImportCfg> temp = null;
		try {
			temp = ImportCfg.getDefault();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String, String> fields = parseTemplate(temp);
		return new ModelAndView("/patent_download").addObject("docNo", docNo).addObject("kwd", kwd)
				.addObject("batch", batch).addObject("modelList", modelList).addObject("mapFields", fields)
				.addObject("path", path).addObject("tName", tName);
	}

	@RequestMapping("/fields")
	public @ResponseBody String changeField(HttpServletRequest request) {
		String value = request.getParameter("value");
		Map<String, ImportCfg> cfg = getTemplateByName(value);
		Map<String, String> fields = parseTemplate(cfg);
		return JsonUtil.toJsonStr(fields);
	}

	/**
	 * 根据模板名称获取模板
	 * 
	 * @param name
	 * @return
	 */
	public Map<String, ImportCfg> getTemplateByName(String name) {
		Map<String, ImportCfg> cfg = null;
		try {
			if (StringUtils.isEmpty(name)) {
				cfg = ImportCfg.getDefault();
			} else {
				String templeteDir = ResourceUtil.getUserTemplateDir(currentUserName());
				File tpl = new File(templeteDir, name);
				cfg = ImportCfg.get(tpl.getAbsolutePath());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cfg;
	}

	/**
	 * 解析模板，获取字段
	 * 
	 * @param template
	 * @return
	 */
	public Map<String, String> parseTemplate(Map<String, ImportCfg> template) {
		Map<String, String> fields = Maps.newHashMap();
		for (String str : template.keySet()) {
			String value = template.get(str).getColumn().get(0);
			fields.put(str, value);
		}
		return fields;
	}

	/**
	 * 下载专利
	 * 
	 * @param params
	 *            检索参数
	 * @param downtype
	 *            下载方式 all/range/cur&汽车&1-10/0/0
	 * @param fields
	 *            选中项
	 * @param filetype
	 *            下载文件类型
	 * @param word
	 *            检索关键词
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/download")
	public void download(@ModelAttribute("params") SearchForm model, @RequestParam(value = "downtype") String downtype,
			@RequestParam(value = "field") String field, @RequestParam(value = "filetype") String filetype,
			@RequestParam(value = "word") String word, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		ModelAndView mv = new ModelAndView();
		String downType = StringUtils.substringBefore(downtype, "&");
		String searchWord = StringUtils.substringBetween(downtype, "&", "&");
		String[] fields = field.split(",");
		String name = request.getParameter("template");
		Map<String, ImportCfg> temp = getTemplateByName(name);
		Map<String, String> templateFields = parseTemplate(temp);
		String[] values = getValueByFields(fields, templateFields);
		List<SearchBean> beans = null;
		if ("cur".equals(downType) || "batch".equals(downType)) {// itself&docNo
			if (StringUtils.indexOf(searchWord, ",") != -1) {
				StringBuilder sb = new StringBuilder();
				for (String no : searchWord.split(",")) {
					sb.append(" docNo=" + getDocNo(currentUser.getId(), no));
				}
				model.setKwd(sb.toString());
			} else {
				model.setKwd(" docNo=" + getDocNo(currentUser.getId(), searchWord));
			}
			mv = doSearch(model);
			beans = (List<SearchBean>) mv.getModelMap().get("s_list");
		} else if ("range".equals(downType)) {// range/all&word TI=xxAND
			int start = 0, rows = 0;
			if ("range".equals(downType)) {
				String range = StringUtils.substring(downtype, StringUtils.lastIndexOf(downtype, "&") + 1,
						downtype.length());
				start = Integer.parseInt(StringUtils.substringBefore(range, "-"));
				rows = Integer.parseInt(StringUtils.substringAfter(range, "-"));
				model.setPageNo(1);
				model.setPageSize(rows);
				model.setKwd(word);
			}
			mv = doSearch(model);
			beans = (List<SearchBean>) mv.getModelMap().get("s_list");
			if (start > beans.size()) {
				start = 1;
			}
			if (rows > beans.size()) {
				rows = beans.size();
			}
			beans = beans.subList(start - 1, rows);
		}
		Map<String, List<String>> excelMap = new LinkedHashMap<String, List<String>>();
		for (SearchBean bean : beans) {
			List<String> excelRow = new LinkedList<String>();
			for (String f : fields) {
				try {
					if ("EXT_DISPLAY_1".equals(f)) {
						f = "ExtValue1";
						String key = (String) MethodUtils.invokeMethod(bean, "getExtKey1", null);
						values = replaceValue(values, "EXT_DISPLAY_1", key);
					}
					if ("EXT_DISPLAY_2".equals(f)) {
						f = "ExtValue2";
						String key = (String) MethodUtils.invokeMethod(bean, "getExtKey2", null);
						values = replaceValue(values, "EXT_DISPLAY_2", key);
					}
					if ("EXT_DISPLAY_3".equals(f)) {
						f = "ExtValue3";
						String key = (String) MethodUtils.invokeMethod(bean, "getExtKey3", null);
						values = replaceValue(values, "EXT_DISPLAY_3", key);
					}

					String value = (String) MethodUtils.invokeMethod(bean, "get" + f, null);
					excelRow.add(value);
				} catch (Exception e) {
					// No such accessible method: getIAN() on object:
					// com.xdtech.patent.action.SearchBean
					excelRow.add("");
				}
			}

			excelMap.put(bean.getDocNo(), excelRow);
		}
		log("patent download", "下载专利");
		if ("xls".equals(filetype)) {
			downExcel(excel("sheet1", values, excelMap), response, filetype);
		} else {// txt download
			downTxt(excelMap, values, response);
		}
	}
	/**
	 * 替换数组中的某一个元素
	 * @param values
	 * @param oldKey
	 * @param newKey
	 * @return
	 */
	private String[] replaceValue(String[] values, String oldKey, String newKey) {
		for(int i=0;i<values.length;i++){
			if(oldKey.equals(values[i])){
				values[i] = newKey;
			}
		}
		return values;
	}

	/**
	 * 根据模板的field 获取模板value
	 * 
	 * @param fields
	 * @param templateFields
	 */
	private String[] getValueByFields(String[] fields, Map<String, String> templateFields) {
		String[] values = new String[fields.length];
		for (int i = 0; i < fields.length; i++) {
			for (String key : templateFields.keySet()) {
				if (fields[i].equals(key)) {
					values[i] = templateFields.get(key);
				}
			}
			if ("EXT_DISPLAY_1".equals(fields[i]))
				values[i] = "EXT_DISPLAY_1";
			if ("EXT_DISPLAY_2".equals(fields[i]))
				values[i] = "EXT_DISPLAY_2";
			if ("EXT_DISPLAY_3".equals(fields[i]))
				values[i] = "EXT_DISPLAY_3";
		}
		return values;
	}

	private Workbook excel(String sheetName, String[] header, Map<String, List<String>> data) {
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(sheetName);
		HSSFRow row = sheet.createRow((int) 0);
		HSSFFont font = wb.createFont();
		font.setFontHeightInPoints((short) 13);
		font.setFontName("黑体");
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setFont(font);

		for (int i = 0; i < header.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellValue(header[i]);
			cell.setCellStyle(style);
			sheet.autoSizeColumn(i);
			sheet.setColumnWidth(i, 20 * 256);
		}

		int i = 1;
		for (String key : data.keySet()) {
			row = sheet.createRow(i);
			row.setHeightInPoints(20.00f);
			int j = 0;
			for (String cellVal : data.get(key)) {
				HSSFCell cell = row.createCell(j);
				cell.setCellValue(cellVal);
				j++;
			}
			i++;
		}
		return wb;
	}

	private void downExcel(Workbook wb, HttpServletResponse response, String type) throws IOException {
		SimpleDateFormat myFmt = new SimpleDateFormat("yyyyMMdd_HHmmss");
		response.setHeader("Pragma", "public");
		response.setHeader("Cache-Control", "max-age=0");
		if ("xls".equals(type)) {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment; filename=" + myFmt.format(new Date()) + ".xls");
		}
		OutputStream ouputStream = response.getOutputStream();
		wb.write(ouputStream);
		ouputStream.flush();
		ouputStream.close();
	}

	private void downTxt(Map<String, List<String>> map, String[] header, HttpServletResponse response)
			throws IOException {
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd_HHmmss");
		OutputStream os = response.getOutputStream();
		response.setHeader("Pragma", "public");
		response.setHeader("Cache-Control", "max-age=0");
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=" + fmt.format(new Date()) + ".txt");
		for (String key : map.keySet()) {
			int i = 0;
			for (String val : map.get(key)) {
				os.write(("[" + header[i] + "]" + val + "\n").getBytes());
				i++;
			}
			os.write("\n\n".getBytes());
		}
		os.flush();
		os.close();
	}

	/*
	 * @Override protected void setupReturnField(QueryObjectBuilder builder) {
	 * super.setupReturnField(builder); //TODO 初始化下载字段 String[] fields = new
	 * String[] {}; for (String field : fields) { builder.addReturnField(field,
	 * false, Integer.MAX_VALUE); } }
	 */
	@Override
	public void setupQuery(SearchForm model, ModelAndView mv, QueryObjectBuilder builder) {
		String qstr = model.getKwd();
		if(StringUtils.isNotEmpty(model.getKwd())){
			if (!kwdIsQuery(model.getKwd())) {
				qstr = defaultQuery(model.getKwd());
			} else {
				qstr = model.getKwd();
			}
		}else{
			StringBuilder queryStr = new StringBuilder();
			if (!StringUtils.isEmpty(model.gettName()) && !"all".equals(model.gettName())) {
				queryStr.append("T_NAME").append("=(").append(model.gettName()).append(")");
			} else {
				if (queryStr.length() == 0) {
					queryStr.append("UID=").append(currentUser.getId());
				}
			}
			qstr += queryStr.toString();
		}
		BooleanQuery bq = null;
		ParseResult parseResult = PatentParser.parse(qstr, "TI");
		bq = parseResult.getQuery();
		builder.addCore(CORE_NAME);
		builder.setQuery(bq);
	}

	/**
	 * 设置检索结果要返回的字段
	 * 
	 * @param builder
	 */
	protected void setupReturnField(final QueryObjectBuilder builder) {
		builder.addReturnField("TI", true, hl(0)).addReturnField("AB", true, hl(0)).addReturnField("LS", false, hl(0))
				.addReturnField("PC", false, 0).addReturnField("AN", false, hl(0)).addReturnField("PA", true, hl(0))
				.addReturnField("AD", false, 0).addReturnField("PN", false, 0).addReturnField("PD", false, 0)
				.addReturnField("ADDR", false, 0).addReturnField("IPC", true, 0).addReturnField("AGT", false, 0)
				.addReturnField("AGC", false, 0).addReturnField("AU", false, 0).addReturnField("LS", false, 0)
				.addReturnField("CLM", false, 0).addReturnField("FT", false, 0).addReturnField("PFN", false, 0)
				.addReturnField("LSN", false, 0).addReturnField("LSE", false, 0).addReturnField("PR", true, 0)
				.addReturnField("EXT_DISPLAY_1", false, 0).addReturnField("EXT_DISPLAY_2", false, 0)
				.addReturnField("EXT_DISPLAY_3", false, 0).addReturnField("PFN", false, 0)
				.addReturnField("CDN", false, 0).addReturnField("CTN", false, 0).addReturnField("CPC", false, 0)
				.addReturnField("PRC", false, 0).addReturnField("PRD", false, 0).addReturnField("PRN", false, 0);

	}
}
