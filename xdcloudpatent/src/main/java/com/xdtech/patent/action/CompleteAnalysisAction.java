package com.xdtech.patent.action;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblBorders;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STBorder;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.xdtech.patent.ipc.IPCContext;
import com.xdtech.patent.model.GraphModel;
import com.xdtech.patent.reader.Util;
import com.xdtech.search.client.ws.FacetCount;
import com.xdtech.search.client.ws.FacetsResult;
import com.xdtech.search.client.ws.FieldFacetResult;
import com.xdtech.search.client.ws.PivotFacetResult;
import com.xdtech.search.client.ws.PivotResultSet;
import com.xdtech.search.client.ws.RangeFacetResult;
import com.xdtech.search.client.ws.SearchResult;
import com.xdtech.util.CustomXWPFDocument;
import com.xdtech.util.SVG2PNGTranscoder;

/**
 * 完整性分析
 * 
 * @author sunjp
 *
 */
@Controller
@SuppressWarnings({ "rawtypes", "unchecked" })
public class CompleteAnalysisAction extends AnalysisAction {

	private static final long serialVersionUID = 4750387284342395376L;

	/**
	 * 分析入口
	 */
	@RequestMapping("complete_analysis")
	public ModelAndView analysis(@ModelAttribute("analysiForm") SearchForm form) {
		form.setPageSize(1);
		form.setFacet(1);
		ModelAndView mv = doSearch(form);
		mv.setViewName("analysis/complete");
		mv.addObject("cur", "complete_analysis");
		return mv;
	}

	/**
	 * 文件导出
	 * 
	 * @param form
	 * @param response
	 * @param request
	 * @throws IOException
	 */
	@RequestMapping("/export")
	public void export(AnalysisForm form, HttpServletResponse response, HttpServletRequest request) throws IOException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		String firstName = sdf.format(new Date().getTime());
		String svg = request.getParameter("svg1");
		String svgg = request.getParameter("svg2");
		String webPath = Util.getWebPath();
		List<File> files = Lists.newArrayList();
		if (null != svgg && !"".equals(svgg)) {
			String path = webPath + "svg2.png";
			files.add(convertToPng(svgg, path));
		}
		if (null != svg && !"".equals(svg) && !"empty".equals(svg)) {
			String path = webPath + "svg1.png";
			files.add(convertToPng(svg, path));
		}
		GraphModel gm = createExportData(form);
		List<String> header = Lists.newArrayList();
		String[] field = form.getField();
		String xaxis = form.getXaxis();
		String fileName = null;
		HSSFWorkbook twb = null;
		if (!"".equals(field) && null != field) {
			if ("CNLX".equals(field[0])) {
				header.add("序号");
				header.add("专利类型");
				header.add("申请量");
				if (null == svg || "".equals(svg)) {
					fileName = firstName + "_专利类型统计表.xls";
					downExcel(createExcel(gm, header, "1"), response, fileName,request);
				} else {
					fileName = firstName + "_专利类型统计表.docx";
					downWord(createWord(gm, header, "1", null, files), response, fileName,request);
				}
			} else if ("LS".equals(field[0])) {
				header.add("序号");
				header.add("法律状态");
				header.add("申请量");
				if (null == svg || "".equals(svg)) {
					fileName = firstName + "_法律状态统计表.xls";
					downExcel(createExcel(gm, header, "1"), response, fileName,request);
				} else {
					fileName = firstName + "_法律状态统计表.docx";
					downWord(createWord(gm, header, "1", null, files), response, fileName,request);
				}
			} else if ("AD_Y_S".equals(field[0])) {
				if ("AU_S".equals(xaxis)) {
					form.setXaxis("PA_S");
					List fi = Lists.newArrayList();
					fi.add("AD_Y_S");
					form.setFields(fi);
					GraphModel tem = createExportData(form);
					header.add("名称");
					if (null == svg || "".equals(svg)) {
						twb = createExcel(tem, header, "2");
						fileName = firstName + "_申请人发明人趋势统计表.xls";
						downExcel(createExcel(gm, header, "2", twb), response, fileName,request);
					} else {
						CustomXWPFDocument txdoc = createWord(tem, header, "2", null, files);
						fileName = firstName + "_申请人发明人趋势统计表.docx";
						downWord(createWord(gm, header, "2", txdoc, files), response, fileName,request);
					}
				} else if ("IPC_B_S".equals(xaxis)) {
					header.add("分类");
					if (null == svg || "".equals(svg)) {
						fileName = firstName + "_技术趋势统计表.xls";
						downExcel(createExcel(gm, header, "2"), response, fileName,request);
					} else {
						fileName = firstName + "_技术趋势统计表.docx";
						downWord(createWord(gm, header, "2", null, files), response, fileName,request);
					}
				} else if ("AC".equals(xaxis)) {
					header.add("区域");
					if (null == svg || "".equals(svg)) {
						fileName = firstName + "_区域趋势统计表.xls";
						downExcel(createExcel(gm, header, "2"), response, fileName,request);
					} else {
						fileName = firstName + "_区域趋势统计表.docx";
						downWord(createWord(gm, header, "2", null, files), response, fileName,request);
					}
				} else if ("PA_S".equals(field[1])) {
					header.add("序号");
					header.add("申请日");
					header.add("专利申请数量");
					header.add("申请人数量");
					if (null == svg || "".equals(svg)) {
						fileName = firstName + "_技术生命周期统计表.xls";
						downExcel(createExcel(gm, header, "5"), response, fileName,request);
					} else {
						fileName = firstName + "_技术生命周期统计表.docx";
						downWord(createWord(gm, header, "5", null, files), response, fileName,request);
					}
				}
			} else if ("IPC_B_S".equals(field[0])) {
				if ("AU_S".equals(xaxis)) {
					form.setXaxis("PA_S");
					List fi = Lists.newArrayList();
					fi.add("IPC_B_S");
					form.setFields(fi);
					GraphModel tem = createExportData(form);
					header.add("申请人/分类号");
					if (null == svg || "".equals(svg)) {
						twb = createExcel(tem, header, "3");
						fileName = firstName + "_申请人发明人技术构成统计表.xls";
						header.set(0, "发明人/分类号");
						downExcel(createExcel(gm, header, "3", twb), response, fileName,request);
					} else {
						CustomXWPFDocument txdoc = createWord(tem, header, "3", null, files);
						fileName = firstName + "_申请人发明人技术构成统计表.docx";
						header.set(0, "发明人/分类号");
						downWord(createWord(gm, header, "3", txdoc, files), response, fileName,request);
					}
				} else if ("MIPC_B_S".equals(xaxis)) {
					header.add("分类");
					if (null == svg || "".equals(svg)) {
						fileName = firstName + "_技术组合统计表.xls";
						downExcel(createExcel(gm, header, "3"), response, fileName,request);
					} else {
						fileName = firstName + "_技术组合统计表.docx";
						downWord(createWord(gm, header, "3", null, files), response, fileName,request);
					}
				} else if ("AC".equals(xaxis)) {
					header.add("区域");
					if (null == svg || "".equals(svg)) {
						fileName = firstName + "_区域技术构成统计表.xls";
						downExcel(createExcel(gm, header, "3"), response, fileName,request);
					} else {
						fileName = firstName + "_区域技术构成统计表.docx";
						downWord(createWord(gm, header, "3", null, files), response, fileName,request);
					}
				} else if ("PA_S".equals(field[1])) {
					header.add("序号");
					header.add("分类");
					header.add("申请人");
					header.add("计数");
					String[] fi = new String[] { "IPC_B_S", "AU_S" };
					form.setField(fi);
					GraphModel tem = createExportData(form);
					if (null == svg || "".equals(svg)) {
						twb = createExcel(gm, header, "4");
						fileName = firstName + "_技术构成统计表.xls";
						header.set(2, "发明人");
						downExcel(createExcel(tem, header, "4", twb), response, fileName,request);
					} else {
						CustomXWPFDocument txdoc = createWord(gm, header, "4", null, files);
						fileName = firstName + "_技术构成统计表.docx";
						header.set(2, "发明人");
						downWord(createWord(tem, header, "4", txdoc, files), response, fileName,request);
					}
				} else if ("AC".equals(field[1])) {
					header.add("序号");
					header.add("分类");
					header.add("区域");
					header.add("计数");
					if (null == svg || "".equals(svg)) {
						fileName = firstName + "_技术构成区域分布统计表.xls";
						downExcel(createExcel(gm, header, "4"), response, fileName,request);
					} else {
						fileName = firstName + "_技术构成区域分布统计表.docx";
						downWord(createWord(gm, header, "4", null, files), response, fileName,request);
					}
				}
			} else if ("PA_S".equals(field[0])) {
				header.add("序号");
				header.add("申请人");
				header.add("申请人");
				header.add("合作次数");
				String[] fi = new String[] { "AU_S", "AU_S" };
				form.setField(fi);
				GraphModel tem = createExportData(form);
				if (null == svg || "".equals(svg)) {
					twb = createExcel(gm, header, "4");
					fileName = firstName + "_合作次数统计表.xls";
					header.set(1, "发明人");
					header.set(2, "发明人");
					downExcel(createExcel(tem, header, "4", twb), response, fileName,request);
				} else {
					CustomXWPFDocument txdoc = createWord(gm, header, "4", null, files);
					fileName = firstName + "_合作次数统计表.docx";
					header.set(1, "发明人");
					header.set(2, "发明人");
					downWord(createWord(tem, header, "4", txdoc, files), response, fileName,request);
				}
			} else if ("AC".equals(field[0])) {
				header.add("序号");
				header.add("区域");
				header.add("申请人");
				header.add("计数");
				String[] fi = new String[] { "AC", "AU_S" };
				form.setField(fi);
				GraphModel tem = createExportData(form);
				if (null == svg || "".equals(svg)) {
					twb = createExcel(gm, header, "4");
					fileName = firstName + "_区域专利权人统计表.xls";
					header.set(2, "发明人");
					downExcel(createExcel(tem, header, "4", twb), response, fileName,request);
				} else {
					CustomXWPFDocument txdoc = createWord(gm, header, "4", null, files);
					fileName = firstName + "_区域专利权人统计表.docx";
					downWord(createWord(tem, header, "4", txdoc, files), response, fileName,request);
				}
			}
		} else if ("AU_S".equals(xaxis)) {
			header.add("序号");
			header.add("申请人");
			header.add("申请量");
			form.setXaxis("PA_S");
			GraphModel dd = createExportData(form);
			if (null == svg || "".equals(svg)) {
				twb = createExcel(dd, header, "1");
				header.set(1, "发明人");
				fileName = firstName + "_申请人发明人统计表.xls";
				downExcel(createExcel(gm, header, "1", twb), response, fileName,request);
			} else {
				CustomXWPFDocument txdoc = createWord(dd, header, "1", null, files);
				header.set(1, "发明人");
				fileName = firstName + "_申请人发明人统计表.docx";
				downWord(createWord(gm, header, "1", txdoc, files), response, fileName,request);
			}
		} else if ("PD_Y_S".equals(xaxis)) {
			header.add("序号");
			header.add("申请日");
			header.add("申请量");
			form.setXaxis("AD_Y_S");
			form.setStart(-30);
			form.setDot(30);
			GraphModel dd = createExportData(form);
			if (null == svg || "".equals(svg)) {
				twb = createExcel(dd, header, "1");
				header.set(1, "公开日");
				fileName = firstName + "_申请日公开日统计表.xls";
				downExcel(createExcel(gm, header, "1", twb), response, fileName,request);
			} else {
				CustomXWPFDocument txdoc = createWord(dd, header, "1", null, files);
				header.set(1, "公开日");
				fileName = firstName + "_申请日公开日统计表.docx";
				downWord(createWord(gm, header, "1", txdoc, files), response, fileName,request);
			}
		} else if ("AC".equals(xaxis)) {
			header.add("序号");
			header.add("省份/国家/地区");
			header.add("申请量");
			if (null == svg || "".equals(svg)) {
				fileName = firstName + "_区域分布统计表.xls";
				downExcel(createExcel(gm, header, "1"), response, fileName,request);
			} else {
				fileName = firstName + "_区域分布统计表.docx";
				downWord(createWord(gm, header, "1", null, files), response, fileName,request);
			}
		}
	}

	/**
	 * 创建模板数据模型
	 * 
	 * @param form
	 * @return
	 */
	private GraphModel createExportData(AnalysisForm form) {
		GraphModel gm = new GraphModel();
		ModelAndView mv = doAnalysis(form);
		SearchResult result = (SearchResult) mv.getModelMap().get("search_result");
		FacetsResult faceResult = result.getFacets();
		String xAxis = form.getXaxis();
		if (StringUtils.isNotEmpty(xAxis)) {
			if (ArrayUtils.isEmpty(form.getField())) {
				List data = Lists.newArrayList();
				if ("AD_Y_S".equals(xAxis) || "PD_Y_S".equals(xAxis)) {
					RangeFacetResult rfr = faceResult.getRangeFacets().get(0);
					for (FacetCount fc : rfr.getResult()) {
						List datas = Lists.newArrayList();
						datas.add(fc.getValue());
						datas.add(fc.getCount());
						data.add(datas);
					}
				} else if ("AC".equals(xAxis)) {
					FieldFacetResult rfr = faceResult.getFieldFacets().get(0);
					for (FacetCount fc : rfr.getResult()) {
						List datas = Lists.newArrayList();
						String value = AC_CODE_MAP.get(fc.getValue());
						datas.add(value == null ? "未知" : value);
						datas.add(fc.getCount() + "");
						data.add(datas);
					}
				} else {
					FieldFacetResult rfr = faceResult.getFieldFacets().get(0);
					for (FacetCount fc : rfr.getResult()) {
						List datas = Lists.newArrayList();
						datas.add(fc.getValue());
						datas.add(fc.getCount() + "");
						data.add(datas);
					}
				}
				gm.setData(data);
			} else {
				if (("PA_S".equals(xAxis) || "AU_S".equals(xAxis) || "AC".equals(xAxis) || "MIPC_B_S".equals(xAxis))
						&& "IPC_B_S".equals(form.getField()[0])) {
					String[] arr = new String[] { "A", "B", "C", "D", "E", "F", "G", "H" };
					setBarGraphData(arr, gm, faceResult, xAxis);
				} else if (("PA_S".equals(xAxis) || "AU_S".equals(xAxis) || "AC".equals(xAxis)
						|| "IPC_B_S".equals(xAxis)) && "AD_Y_S".equals(form.getField()[0])) {
					String[] arr = getYear(10);
					setBrokenGraphData(arr, gm, faceResult, xAxis);
				} else {
					List data1 = Lists.newArrayList();
					List data2 = Lists.newArrayList();
					List data = Lists.newArrayList();
					List categories = Lists.newArrayList();
					PivotResultSet prs = faceResult.getPivotFacets().get(0);
					for (PivotFacetResult pfr : prs.getResultSet()) {
						categories.add(pfr.getValue());
						data1.add(pfr.getCount());
						data2.add(pfr.getPivots().size());
					}
					data.add(data1);
					data.add(data2);
					gm.setCategories(categories);
					gm.setData(data);
				}
			}
		} else {
			String[] field = form.getField();
			if (field.length == 1) {
				if ("AD_Y_S".equals(field[0]) || "PD_Y_S".equals(field[0])) {
					RangeFacetResult rfr = faceResult.getRangeFacets().get(0);
					List<Map<String, String>> list = Lists.newArrayList();
					rfr.getResult().forEach(e -> {
						Map<String, String> item = Maps.newHashMap();
						long count = e.getCount();
						String value = e.getValue();
						item.put("name", value);
						item.put("value", count + "");
						list.add(item);
					});
					gm.setData(list);
				} else {
					FieldFacetResult ffr = faceResult.getFieldFacets().get(0);
					if ("0".equals(form.getCtype())) {// 自定义图
						List<Map<String, String>> list = Lists.newArrayList();
						ffr.getResult().forEach(e -> {
							Map<String, String> item = Maps.newHashMap();
							long count = e.getCount();
							String value = e.getValue();
							String desc = "";
							if ("AC".equals(field[0])) {
								item.put("code", value);
								value = AC_CODE_MAP.get(value); // 替换成中文的国家名称
							} else if ("MIPC_DL_S".equals(field[0])) {
								desc = IPCContext.getDesc(value);
								item.put("desc", desc); // 附加一个 IPC_DL_S的一个描述
							}
							item.put("name", value);
							item.put("value", count + "");
							list.add(item);
						});
						gm.setData(list);
					} else {
						List<List<Object>> list = Lists.newArrayList();
						for (FacetCount fc : ffr.getResult()) {
							if ("".equals(fc.getValue())) {
								continue;
							}
							list.add(Lists.newArrayList(fc.getValue(), fc.getCount()));
						}
						gm.setData(list);
					}
				}
			} else {
				if (("AC".equals(field[0]) || "IPC_B_S".equals(field[0]) || "PA_S".equals(field[0])
						|| "AU_S".equals(field[0]))
						&& ("AC".equals(field[1]) || "PA_S".equals(field[1]) || "AU_S".equals(field[1]))) {
					setMultiFieldData(faceResult, field, gm);
				} else {
					List<Map<String, String>> list = Lists.newArrayList();
					PivotResultSet prs = faceResult.getPivotFacets().get(0);
					for (PivotFacetResult pfr : prs.getResultSet()) {
						Map<String, String> item = Maps.newHashMap();
						item.put("name", pfr.getValue());
						item.put("value", pfr.getCount() + "");
						item.put("count", pfr.getPivots().size() + "");
						list.add(item);
					}
					gm.setData(list);
				}
			}
		}
		return gm;
	}

	/**
	 * 创建word的导出模板（含图片的导出）
	 * 
	 * @param gm
	 * @param header
	 * @param type
	 * @param xdoc
	 * @param files
	 * @return
	 * @throws IOException
	 */
	private CustomXWPFDocument createWord(GraphModel gm, List header, String type, CustomXWPFDocument xdoc,
			List<File> files) throws IOException {
		List data = (List) gm.getData();
		List<String> title = gm.getCategories();
		List<String> names = gm.getName();
		FileInputStream fis = null;
		File file = null;
		XWPFTable table = null;
		if (null == xdoc || "".equals(xdoc)) {
			xdoc = new CustomXWPFDocument();
			if (!files.isEmpty()) {
				fis = new FileInputStream(files.get(0));
				file = new File(files.get(0).getPath());
			}
		} else {
			if (!files.isEmpty()) {
				fis = new FileInputStream(files.get(1));
				file = new File(files.get(1).getPath());
			}
		}
		if (!files.isEmpty()) {
			byte bytes[] = IOUtils.toByteArray(fis);
			try {
				xdoc.addPictureData(bytes, CustomXWPFDocument.PICTURE_TYPE_PNG);
			} catch (InvalidFormatException e) {
				e.printStackTrace();
			}
			XWPFParagraph xp = xdoc.createParagraph();
			xdoc.createPicture(xdoc.getAllPictures().size() - 1, 550, 300, xp);
			file.delete();
		}
		if ("1".equals(type) || "5".equals(type)) {
			table = xdoc.createTable(data.size(), header.size());
		} else if ("2".equals(type)) {
			table = xdoc.createTable(data.size(), title.size() + 1);
		} else if ("3".equals(type)) {
			table = xdoc.createTable(title.size(), names.size() + 1);
		} else if ("4".equals(type)) {
			int k = 0;
			for (int i = 0; i < data.size(); i++) {
				List st = (List) data.get(i);
				k = k + st.size();
			}
			table = xdoc.createTable(k, header.size());
		}
		table.setCellMargins(10, 10, 10, 10);
		table.setRowBandSize(0);
		// 获取表格边框
		CTTblBorders borders = table.getCTTbl().getTblPr().addNewTblBorders();
		// 设置表格无边框
		CTBorder hBorder = borders.addNewInsideH();
		hBorder.setVal(STBorder.Enum.forString("none"));
		hBorder.setSz(new BigInteger("1"));
		CTBorder vBorder = borders.addNewInsideV();
		vBorder.setVal(STBorder.Enum.forString("none"));
		vBorder.setSz(new BigInteger("1"));
		CTBorder lBorder = borders.addNewLeft();
		lBorder.setVal(STBorder.Enum.forString("none"));
		lBorder.setSz(new BigInteger("1"));
		CTBorder rBorder = borders.addNewRight();
		rBorder.setVal(STBorder.Enum.forString("none"));
		rBorder.setSz(new BigInteger("1"));
		CTBorder tBorder = borders.addNewTop();
		tBorder.setVal(STBorder.Enum.forString("none"));
		tBorder.setSz(new BigInteger("1"));
		CTBorder bBorder = borders.addNewBottom();
		bBorder.setVal(STBorder.Enum.forString("none"));
		bBorder.setSz(new BigInteger("1"));
		CTTbl ttbl = table.getCTTbl();
		CTTblPr tblPr = ttbl.getTblPr() == null ? ttbl.addNewTblPr() : ttbl.getTblPr();
		CTTblWidth tblWidth = tblPr.isSetTblW() ? tblPr.getTblW() : tblPr.addNewTblW();
		CTJc cTJc = tblPr.addNewJc();
		cTJc.setVal(STJc.Enum.forString("center"));
		tblWidth.setW(new BigInteger("9000"));
		tblWidth.setType(STTblWidth.DXA);
		XWPFTableRow firstRow = null;
		XWPFTableCell firstCell = null;
		if ("1".equals(type)) {
			for (int i = 0; i < data.size(); i++) {
				List st = (List) data.get(i);
				firstRow = table.getRow(i);
				firstRow.getCell(0).setText(i + 1 + "");
				firstRow.getCell(1).setText(st.get(0).toString());
				firstRow.getCell(2).setText(st.get(1).toString());
			}
			firstRow = table.insertNewTableRow(0);
			for (int i = 0; i < header.size(); i++) {
				firstCell = firstRow.addNewTableCell();
				firstCell.setText(header.get(i).toString());
				firstCell.setColor("CCCCCC");
			}
		} else if ("2".equals(type)) {
			for (int i = 0; i < data.size(); i++) {
				firstRow = table.getRow(i);
				firstRow.getCell(0).setText(names.get(i));
				List st = (List) data.get(i);
				for (int j = 0; j < st.size(); j++) {
					firstRow.getCell(j + 1).setText(st.get(j).toString());
				}
			}
			firstRow = table.insertNewTableRow(0);
			firstCell = firstRow.addNewTableCell();
			firstCell.setText(header.get(0).toString());
			firstCell.setColor("CCCCCC");
			for (int i = 0; i < title.size(); i++) {
				firstCell = firstRow.addNewTableCell();
				firstCell.setText(title.get(i));
				firstCell.setColor("CCCCCC");
			}
		} else if ("3".equals(type)) {
			for (int i = 0; i < title.size(); i++) {
				firstRow = table.getRow(i);
				firstRow.getCell(0).setText(title.get(i));
				for (int j = 0; j < data.size(); j++) {
					List st = (List) data.get(j);
					firstRow.getCell(j + 1).setText(st.get(i).toString());
				}
			}
			firstRow = table.insertNewTableRow(0);
			firstCell = firstRow.addNewTableCell();
			firstCell.setText(header.get(0).toString());
			firstCell.setColor("CCCCCC");
			for (int i = 0; i < names.size(); i++) {
				firstCell = firstRow.addNewTableCell();
				firstCell.setText(names.get(i));
				firstCell.setColor("CCCCCC");
			}
		} else if ("4".equals(type)) {
			int k = 0;
			for (int i = 0; i < data.size(); i++) {
				List st = (List) data.get(i);
				for (int j = 0; j < st.size(); j++) {
					Map o = (Map) st.get(j);
					for (Object s : o.keySet()) {
						firstRow = table.getRow(k);
						firstRow.getCell(0).setText(k + 1 + "");
						firstRow.getCell(1).setText(title.get(i));
						firstRow.getCell(2).setText(s.toString());
						firstRow.getCell(3).setText(o.get(s).toString());
						k++;
					}
				}
			}
			firstRow = table.insertNewTableRow(0);
			for (int i = 0; i < header.size(); i++) {
				firstCell = firstRow.addNewTableCell();
				firstCell.setText(header.get(i).toString());
				firstCell.setColor("CCCCCC");
			}
		} else if ("5".equals(type)) {
			for (int i = 0; i < data.size(); i++) {
				Map st = (Map) data.get(i);
				firstRow = table.getRow(i);
				firstRow.getCell(0).setText(i + 1 + "");
				firstRow.getCell(1).setText(st.get("name").toString());
				firstRow.getCell(2).setText(st.get("value").toString());
				firstRow.getCell(3).setText(st.get("count").toString());
			}
			firstRow = table.insertNewTableRow(0);
			for (int i = 0; i < header.size(); i++) {
				firstCell = firstRow.addNewTableCell();
				firstCell.setText(header.get(i).toString());
				firstCell.setColor("CCCCCC");
			}
		}
		return xdoc;
	}

	/**
	 * 下载word文档
	 * 
	 * @param xdoc
	 * @param response
	 * @param fileName
	 * @throws IOException
	 */
	private void downWord(CustomXWPFDocument xdoc, HttpServletResponse response, String fileName,
			HttpServletRequest request) throws IOException {
		OutputStream output = response.getOutputStream();

		String userAgent = request.getHeader("User-Agent");
		String name = new String(fileName.getBytes(), "ISO8859-1");
		// 针对IE或者以IE为内核的浏览器：
		if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
			name = java.net.URLEncoder.encode(fileName, "UTF-8");
		}
		response.reset();
		response.setHeader("Content-disposition", "attachment; filename=" + name);
		response.setContentType("application/msword");
		xdoc.write(output);
		output.close();
	}

	/**
	 * 创建excel模板（重载）
	 * 
	 * @param gm
	 * @param header
	 * @param type
	 * @param twb
	 * @return
	 */
	private HSSFWorkbook createExcel(GraphModel gm, List header, String type, HSSFWorkbook twb) {
		List data = (List) gm.getData();
		List<String> title = gm.getCategories();
		List<String> names = gm.getName();
		HSSFSheet sheet = null;
		int lastRowNum = 0;
		HSSFRow row1 = null;
		if(twb == null){
			twb = new HSSFWorkbook();
			// 建立新的sheet对象（excel的表单）
			 sheet = twb.createSheet("xdtech");
			// 创建标题
			 row1 = sheet.createRow(0);
		}else{
			 sheet = twb.getSheet("xdtech");
			 lastRowNum = sheet.getLastRowNum() + 2;
			 row1 = sheet.createRow(lastRowNum);
		}
		if ("1".equals(type)) {
			for (int i = 0; i < header.size(); i++) {
				HSSFCell cell = row1.createCell(i);
				cell.setCellValue(header.get(i).toString());
			}
			for (int i = 1; i <= data.size(); i++) {
				List st = (List) data.get(i - 1);
				HSSFRow row = sheet.createRow(lastRowNum + i);
				row.createCell(0).setCellValue(i);
				row.createCell(1).setCellValue(st.get(0).toString());
				row.createCell(2).setCellValue(st.get(1).toString());
			}
		} else if ("2".equals(type)) {
			HSSFCell cell0 = row1.createCell(0);
			cell0.setCellValue(header.get(0).toString());
			for (int i = 1; i <= title.size(); i++) {
				HSSFCell cell = row1.createCell(i);
				cell.setCellValue(title.get(i - 1));
			}
			for (int j = 1; j <= data.size(); j++) {
				HSSFRow row = sheet.createRow(lastRowNum + j);
				row.createCell(0).setCellValue(names.get(j - 1));
				List st = (List) data.get(j - 1);
				for (int i = 1; i <= st.size(); i++) {
					row.createCell(i).setCellValue(st.get(i - 1).toString());
				}
			}
		} else if ("3".equals(type)) {
			HSSFCell cell0 = row1.createCell(0);
			cell0.setCellValue(header.get(0).toString());
			for (int i = 1; i <= names.size(); i++) {
				HSSFCell cell = row1.createCell(i);
				cell.setCellValue(names.get(i - 1));
			}
			for (int j = 1; j <= title.size(); j++) {
				HSSFRow row = sheet.createRow(lastRowNum + j);
				row.createCell(0).setCellValue(title.get(j - 1));
				for (int i = 1; i <= data.size(); i++) {
					List st = (List) data.get(i - 1);
					row.createCell(i).setCellValue(st.get(j - 1).toString());
				}
			}
		} else if ("4".equals(type)) {
			for (int i = 0; i < header.size(); i++) {
				HSSFCell cell = row1.createCell(i);
				cell.setCellValue(header.get(i).toString());
			}
			int k = 0;
			for (int i = 0; i < data.size(); i++) {
				List st = (List) data.get(i);
				for (int j = 0; j < st.size(); j++) {
					Map o = (Map) st.get(j);
					for (Object s : o.keySet()) {
						HSSFRow row = sheet.createRow(k + 1 + lastRowNum);
						row.createCell(0).setCellValue(k + 1);
						row.createCell(1).setCellValue(title.get(i));
						row.createCell(2).setCellValue(s.toString());
						row.createCell(3).setCellValue(o.get(s).toString());
						k++;
					}
				}
			}
		}
		return twb;
	}

	/**
	 * 创建excel模板
	 * 
	 * @param gm
	 * @param header
	 * @param type
	 * @return
	 * @throws IOException
	 */
	public HSSFWorkbook createExcel(GraphModel gm, List header, String type) throws IOException {
		// 创建HSSFWorkbook对象(excel的文档对象)
		List data = (List) gm.getData();
		List<String> title = gm.getCategories();
		List<String> names = gm.getName();
		HSSFWorkbook wb = new HSSFWorkbook();
		// 建立新的sheet对象（excel的表单）
		HSSFSheet sheet = wb.createSheet("xdtech");
		// 创建标题
		HSSFRow row1 = sheet.createRow(0);
		if ("1".equals(type)) {
			// 创建单元格并设置单元格内容
			for (int i = 0; i < header.size(); i++) {
				HSSFCell cell = row1.createCell(i);
				cell.setCellValue(header.get(i).toString());
			}
			for (int i = 1; i <= data.size(); i++) {
				List st = (List) data.get(i - 1);
				HSSFRow row = sheet.createRow(i);
				row.createCell(0).setCellValue(i);
				row.createCell(1).setCellValue(st.get(0).toString());
				row.createCell(2).setCellValue(st.get(1).toString());
			}
		} else if ("2".equals(type)) {
			HSSFCell cell0 = row1.createCell(0);
			cell0.setCellValue(header.get(0).toString());
			for (int i = 1; i <= title.size(); i++) {
				HSSFCell cell = row1.createCell(i);
				cell.setCellValue(title.get(i - 1));
			}
			for (int j = 1; j <= data.size(); j++) {
				HSSFRow row = sheet.createRow(j);
				row.createCell(0).setCellValue(names.get(j - 1));
				List st = (List) data.get(j - 1);
				for (int i = 1; i <= st.size(); i++) {
					row.createCell(i).setCellValue(st.get(i - 1).toString());
				}
			}
		} else if ("3".equals(type)) {
			HSSFCell cell0 = row1.createCell(0);
			cell0.setCellValue(header.get(0).toString());
			for (int i = 1; i <= names.size(); i++) {
				HSSFCell cell = row1.createCell(i);
				cell.setCellValue(names.get(i - 1));
			}
			for (int j = 1; j <= title.size(); j++) {
				HSSFRow row = sheet.createRow(j);
				row.createCell(0).setCellValue(title.get(j - 1));
				for (int i = 1; i <= data.size(); i++) {
					List st = (List) data.get(i - 1);
					row.createCell(i).setCellValue(st.get(j - 1).toString());
				}
			}
		} else if ("4".equals(type)) {
			for (int i = 0; i < header.size(); i++) {
				HSSFCell cell = row1.createCell(i);
				cell.setCellValue(header.get(i).toString());
			}
			int k = 0;
			for (int i = 0; i < data.size(); i++) {
				List st = (List) data.get(i);
				for (int j = 0; j < st.size(); j++) {
					Map o = (Map) st.get(j);
					for (Object s : o.keySet()) {
						HSSFRow row = sheet.createRow(k + 1);
						row.createCell(0).setCellValue(k + 1);
						row.createCell(1).setCellValue(title.get(i));
						row.createCell(2).setCellValue(s.toString());
						row.createCell(3).setCellValue(o.get(s).toString());
						k++;
					}
				}
			}
		} else if ("5".equals(type)) {
			for (int i = 0; i < header.size(); i++) {
				HSSFCell cell = row1.createCell(i);
				cell.setCellValue(header.get(i).toString());
			}
			for (int i = 0; i < data.size(); i++) {
				Map st = (Map) data.get(i);
				HSSFRow row = sheet.createRow(i + 1);
				row.createCell(0).setCellValue(i + 1);
				row.createCell(1).setCellValue(st.get("name").toString());
				row.createCell(2).setCellValue(st.get("value").toString());
				row.createCell(3).setCellValue(st.get("count").toString());
			}
		}
		return wb;
	}

	/**
	 * 下载excel模板数据
	 * 
	 * @param wb
	 * @param response
	 * @param fileName
	 * @throws IOException
	 */
	private void downExcel(Workbook wb, HttpServletResponse response, String fileName,HttpServletRequest request) throws IOException {
		OutputStream output = response.getOutputStream();
		
		String userAgent = request.getHeader("User-Agent");
		String name = new String(fileName.getBytes(), "ISO8859-1");
		// 针对IE或者以IE为内核的浏览器：
		if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
			name = java.net.URLEncoder.encode(fileName, "UTF-8");
		}
		response.reset();
		response.setHeader("Content-disposition", "inline; filename=" + name);
		response.setContentType("application/msexcel");
		wb.write(output);
		output.close();
	};

	/**
	 * svg数据转为png图片
	 * 
	 * @param svgCode
	 * @param pngFilePath
	 * @return
	 */
	private File convertToPng(String svgCode, String pngFilePath) {
		File file = new File(pngFilePath);
		FileOutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(file);
			convertToPng(svgCode, outputStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return file;
	}

	/**
	 * svg格式数据转为png图片
	 * 
	 * @param svgCode
	 * @param outputStream
	 */
	private void convertToPng(String svgCode, OutputStream outputStream) {
		try {
			byte[] bytes = svgCode.getBytes("UTF-8");
			SVG2PNGTranscoder t = new SVG2PNGTranscoder();
			TranscoderInput input = new TranscoderInput(new ByteArrayInputStream(bytes));
			TranscoderOutput output = new TranscoderOutput(outputStream);
			t.transcode(input, output);
			outputStream.flush();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (TranscoderException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 导出完整分析报告内容
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("export_all")
	public void exportAll(HttpServletRequest request,HttpServletResponse response) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		String firstName = sdf.format(new Date().getTime());
		String type = request.getParameter("type");
		String fields = request.getParameter("fields");
		HSSFWorkbook twb =null;
		CustomXWPFDocument tword = null;
		String webPath = Util.getWebPath();
		List<File> files = Lists.newArrayList();
		String[] fieldsArr = fields.split(",");
		for(int i =0;i<fieldsArr.length;i++){
			if("e".equals(type)){
				if(i == 0){
					twb = getExportExcelData(fieldsArr[i],null);
				}else{
					twb = getExportExcelData(fieldsArr[i], twb);
				}
			}else{
				String svg = request.getParameter("svg_"+fieldsArr[i]);
				if(!"".equals(svg)&& null !=svg){
					String path = webPath + "svg.png";
					files.add(convertToPng(svg, path));
				}
				if(i ==0){
					tword = getExportWordData(fieldsArr[i], null, files);
				}else{
					tword = getExportWordData(fieldsArr[i], tword, files);
				}
			}
		}
		if("e".equals(type)){
			downExcel(twb, response, firstName+"_完整分析报告.xls", request);
		}else{
			downWord(tword, response, firstName+"_完整分析报告.docx",request);
		}
	}
	/**
	 * 获取导出word数据
	 * @param field
	 * @param cfd
	 * @param files
	 * @return
	 * @throws IOException
	 */
	private CustomXWPFDocument getExportWordData(String field,CustomXWPFDocument cfd,List<File> files) throws IOException{
		GraphModel gm = createExportData(getFormByField(field));
		List<String> header = Lists.newArrayList();
		CustomXWPFDocument tword = null;
		if("zllx".equals(field)){
			header.add("序号");
			header.add("专利类型");
			header.add("申请量");
			tword = createWord(gm, header, "1", cfd, files);
		}
		if("flzt".equals(field)){
			header.add("序号");
			header.add("法律状态");
			header.add("申请量");
			tword =createWord(gm, header, "1", cfd, files);
		}
		if("fmrqs".equals(field) || "sqrqs".equals(field)){
			header.add("名称");
			tword =createWord(gm, header, "2", cfd, files);
		}
		if("jsqs".equals(field)){
			header.add("分类");
			tword =createWord(gm, header, "2", cfd, files);
		}
		if("qyqs".equals(field)){
			header.add("区域");
			tword =createWord(gm, header, "2", cfd, files);
		}
		if("sxt".equals(field)){
			header.add("序号");
			header.add("申请日");
			header.add("专利申请数量");
			header.add("申请人数量");
			tword =createWord(gm, header, "5", cfd, files);
		}
		if("sqrgc".equals(field)){
			header.add("申请人/分类号");
			tword =createWord(gm, header, "3", cfd, files);
		}
		if("fmrgc".equals(field)){
			header.add("发明人/分类号");
			tword =createWord(gm, header, "3", cfd, files);
		}
		if("jszh".equals(field)){
			header.add("分类");
			tword =createWord(gm, header, "3", cfd, files);
		}
		if("qyjs".equals(field)){
			header.add("区域");
			tword =createWord(gm, header, "3", cfd, files);
		}
		if("jsgcsqr".equals(field)){
			header.add("序号");
			header.add("分类");
			header.add("申请人");
			header.add("计数");
			tword =createWord(gm, header, "4", cfd, files);
		}
		if("jsgcfmr".equals(field)){
			header.add("序号");
			header.add("分类");
			header.add("发明人");
			header.add("计数");
			tword =createWord(gm, header, "4", cfd, files);
		}
		if("jsgcqy".equals(field)){
			header.add("序号");
			header.add("分类");
			header.add("区域");
			header.add("计数");
			tword =createWord(gm, header, "4", cfd, files);
		}
		if("sqrhz".equals(field)){
			header.add("序号");
			header.add("申请人");
			header.add("申请人");
			header.add("合作次数");
			tword =createWord(gm, header, "4", cfd, files);
		}
		if("fmrhz".equals(field)){
			header.add("序号");
			header.add("发明人");
			header.add("发明人");
			header.add("合作次数");
			tword =createWord(gm, header, "4", cfd, files);
		}
		if("qysqr".equals(field)){
			header.add("序号");
			header.add("区域");
			header.add("申请人");
			header.add("计数");
			tword =createWord(gm, header, "4", cfd, files);
		}
		if("qyfmr".equals(field)){
			header.add("序号");
			header.add("区域");
			header.add("发明人");
			header.add("计数");
			tword =createWord(gm, header, "4", cfd, files);
		}
		if("sqren".equals(field)){
			header.add("序号");
			header.add("申请人");
			header.add("申请量");
			tword =createWord(gm, header, "1", cfd, files);
		}
		if("fmr".equals(field)){
			header.add("序号");
			header.add("发明人");
			header.add("申请量");
			tword =createWord(gm, header, "1", cfd, files);
		}
		if("sqr".equals(field)){
			header.add("序号");
			header.add("申请日");
			header.add("申请量");
			tword =createWord(gm, header, "1", cfd, files);
		}
		if("gkr".equals(field)){
			header.add("序号");
			header.add("公开日");
			header.add("申请量");
			tword =createWord(gm, header, "1", cfd, files);
		}
		if("qypm".equals(field)){
			header.add("序号");
			header.add("省份/国家/地区");
			header.add("申请量");
			tword =createWord(gm, header, "1", cfd, files);
		}
		return tword;
	}
	/**
	 * 获取excel导出数据
	 * @param field
	 * @param twb
	 * @return
	 * @throws Exception
	 */
	private HSSFWorkbook getExportExcelData(String field,HSSFWorkbook twb) throws Exception{
		GraphModel gm = createExportData(getFormByField(field));
		List<String> header = Lists.newArrayList();
		HSSFWorkbook wb = null;
		if("zllx".equals(field)){
			header.add("序号");
			header.add("专利类型");
			header.add("申请量");
			wb = createExcel(gm, header, "1",twb);
		}
		if("flzt".equals(field)){
			header.add("序号");
			header.add("法律状态");
			header.add("申请量");
			wb =createExcel(gm, header, "1",twb);
		}
		if("fmrqs".equals(field) || "sqrqs".equals(field)){
			header.add("名称");
			wb =createExcel(gm, header, "2",twb);
		}
		if("jsqs".equals(field)){
			header.add("分类");
			wb =createExcel(gm, header, "2",twb);
		}
		if("qyqs".equals(field)){
			header.add("区域");
			wb =createExcel(gm, header, "2",twb);
		}
		if("sxt".equals(field)){
			header.add("序号");
			header.add("申请日");
			header.add("专利申请数量");
			header.add("申请人数量");
			wb =createExcel(gm, header, "5",twb);
		}
		if("sqrgc".equals(field)){
			header.add("申请人/分类号");
			wb =createExcel(gm, header, "3",twb);
		}
		if("fmrgc".equals(field)){
			header.add("发明人/分类号");
			wb =createExcel(gm, header, "3",twb);
		}
		if("jszh".equals(field)){
			header.add("分类");
			wb =createExcel(gm, header, "3",twb);
		}
		if("qyjs".equals(field)){
			header.add("区域");
			wb =createExcel(gm, header, "3",twb);
		}
		if("jsgcsqr".equals(field)){
			header.add("序号");
			header.add("分类");
			header.add("申请人");
			header.add("计数");
			wb =createExcel(gm, header, "4",twb);
		}
		if("jsgcfmr".equals(field)){
			header.add("序号");
			header.add("分类");
			header.add("发明人");
			header.add("计数");
			wb =createExcel(gm, header, "4",twb);
		}
		if("jsgcqy".equals(field)){
			header.add("序号");
			header.add("分类");
			header.add("区域");
			header.add("计数");
			wb =createExcel(gm, header, "4",twb);
		}
		if("sqrhz".equals(field)){
			header.add("序号");
			header.add("申请人");
			header.add("申请人");
			header.add("合作次数");
			wb =createExcel(gm, header, "4",twb);
		}
		if("fmrhz".equals(field)){
			header.add("序号");
			header.add("发明人");
			header.add("发明人");
			header.add("合作次数");
			wb =createExcel(gm, header, "4",twb);
		}
		if("qysqr".equals(field)){
			header.add("序号");
			header.add("区域");
			header.add("申请人");
			header.add("计数");
			wb =createExcel(gm, header, "4",twb);
		}
		if("qyfmr".equals(field)){
			header.add("序号");
			header.add("区域");
			header.add("发明人");
			header.add("计数");
			wb =createExcel(gm, header, "4",twb);
		}
		if("sqren".equals(field)){
			header.add("序号");
			header.add("申请人");
			header.add("申请量");
			wb =createExcel(gm, header, "1",twb);
		}
		if("fmr".equals(field)){
			header.add("序号");
			header.add("发明人");
			header.add("申请量");
			wb =createExcel(gm, header, "1",twb);
		}
		if("sqr".equals(field)){
			header.add("序号");
			header.add("申请日");
			header.add("申请量");
			wb =createExcel(gm, header, "1",twb);
		}
		if("gkr".equals(field)){
			header.add("序号");
			header.add("公开日");
			header.add("申请量");
			wb =createExcel(gm, header, "1",twb);
		}
		if("qypm".equals(field)){
			header.add("序号");
			header.add("省份/国家/地区");
			header.add("申请量");
			wb =createExcel(gm, header, "1",twb);
		}
		return wb;
	}
	
	/**
	 * 根据字段获取分析表单数据
	 * @param field
	 * @return
	 */
	private AnalysisForm getFormByField(String field){
		AnalysisForm af = new AnalysisForm();
		if("zllx".equals(field)){
			af.setField(new String[]{"CNLX"});
		}
		if("flzt".equals(field)){
			af.setField(new String[]{"LS"});
		}
		if("sqr".equals(field)){
			af.setXaxis("AD_Y_S");
			af.setStart(-30);
			af.setDot(30);
		}
		if("gkr".equals(field)){
			af.setXaxis("PD_Y_S");
			af.setStart(-30);
			af.setDot(30);
		}
		if("sxt".equals(field)){
			af.setField(new String[]{"AD_Y_S","PA_S"});
			af.setSort("index");
			RangeSearch rs = new RangeSearch();
			rs.setField("AD_Y_S");
			rs.setStart("1987");
			af.setRs(Arrays.asList(rs));;
		}
		if("sqren".equals(field)){
			af.setXaxis("PA_S");
			af.setLimit(20);
		}
		if("sqrqs".equals(field)){
			af.setField(new String[]{"AD_Y_S"});
			af.setXaxis("PA_S");
		}
		if("sqrgc".equals(field)){
			af.setField(new String[]{"IPC_B_S"});
			af.setXaxis("PA_S");
		}
		if("sqrhz".equals(field)){
			af.setField(new String[]{"PA_S","PA_S"});
		}
		if("fmr".equals(field)){
			af.setXaxis("AU_S");
			af.setLimit(20);
		}
		if("fmrqs".equals(field)){
			af.setField(new String[]{"AD_Y_S"});
			af.setXaxis("AU_S");
		}
		if("fmrgc".equals(field)){
			af.setField(new String[]{"IPC_B_S"});
			af.setXaxis("AU_S");
		}
		if("fmrhz".equals(field)){
			af.setField(new String[]{"AU_S","AU_S"});
		}
		if("jsqs".equals(field)){
			af.setField(new String[]{"AD_Y_S"});
			af.setXaxis("IPC_B_S");
		}
		if("jsgcsqr".equals(field)){
			af.setField(new String[]{"IPC_B_S","PA_S"});
		}
		if("jsgcfmr".equals(field)){
			af.setField(new String[]{"IPC_B_S","AU_S"});
		}
		if("jsgcqy".equals(field)){
			af.setField(new String[]{"IPC_B_S","AC"});
		}
		if("jszh".equals(field)){
			af.setField(new String[]{"IPC_B_S"});
			af.setXaxis("MIPC_B_S");
		}
		if("qypm".equals(field)){
			af.setXaxis("AC");
			af.setLimit(20);
		}
		if("qyqs".equals(field)){
			af.setField(new String[]{"AD_Y_S"});
			af.setXaxis("AC");
		}
		if("qyjs".equals(field)){
			af.setField(new String[]{"IPC_B_S"});
			af.setXaxis("AC");
		}
		if("qysqr".equals(field)){
			af.setField(new String[]{"AC","PA_S"});
		}
		if("qyfmr".equals(field)){
			af.setField(new String[]{"AC","AU_S"});
		}
		return af;
	}
}
