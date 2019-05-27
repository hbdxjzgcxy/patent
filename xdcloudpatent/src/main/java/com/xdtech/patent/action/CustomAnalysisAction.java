package com.xdtech.patent.action;

import java.awt.Color;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
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
import com.xdtech.patent.model.GraphModel;
import com.xdtech.patent.model.NamedData;
import com.xdtech.patent.model.PiePoint;
import com.xdtech.patent.reader.Util;
import com.xdtech.search.client.LOGIC;
import com.xdtech.search.client.QueryObjectBuilder;
import com.xdtech.search.client.ws.BooleanQuery;
import com.xdtech.search.client.ws.FacetCount;
import com.xdtech.search.client.ws.FacetsResult;
import com.xdtech.search.client.ws.FieldFacetResult;
import com.xdtech.search.client.ws.PivotFacetResult;
import com.xdtech.search.client.ws.PivotResultSet;
import com.xdtech.search.client.ws.QueryClause;
import com.xdtech.search.client.ws.SearchResult;
import com.xdtech.search.client.ws.TermQuery;
import com.xdtech.util.CustomXWPFDocument;
import com.xdtech.util.SVG2PNGTranscoder;

/**
 * 自定义分析
 * 
 * @author sunjp
 *
 */
@Controller
public class CustomAnalysisAction extends AnalysisAction {

	private static final long serialVersionUID = -5622164440799346599L;

	@RequestMapping("custom_analysis")
	public ModelAndView analysis(@ModelAttribute("analysiForm") SearchForm form) {
		form.setPageSize(1);
		form.setFacet(1);
		ModelAndView mv = doSearch(form);
		mv.setViewName("analysis/custom");
		mv.addObject("cur", "custom_analysis");
		return mv;
	}

	@RequestMapping("/graph2")
	public void graph2(HttpServletResponse response, AnalysisForm form) {
		ModelAndView mv = doAnalysis(form);
		SearchResult result = (SearchResult) mv.getModel().get("search_result");
		GraphModel model = buildeGraph(result, form);
		String json = toJsonStr(model);
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		try {
			response.getWriter().print(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private GraphModel buildeGraph(SearchResult result, AnalysisForm form) {
		if ("pie".equals(form.getCtype())) {
			return buildPieGraph(result, form);
		}
		GraphModel model = buildCommonGraph(result, form);
		return model;
	}

	private GraphModel buildCommonGraph(SearchResult result, AnalysisForm form) {
		GraphModel model = new GraphModel();
		List<String> nameList = Lists.newArrayList();
		nameList.add(getFieldDesc(form.getField()[0]));
		model.setName(nameList);
		List<String> category = Lists.newArrayList(); // 图标的 category
		List<Object> dataList = new ArrayList<Object>();
		List<FieldFacetResult> fieldFaces = result.getFacets().getFieldFacets();
		String baseField = form.getField()[0]; // 视角字段
		// 如果统计的第一个字段是“申请日-年”字段 或者“公开日-年字段”，需要设定好category
		if ("AD_Y_S".equals(baseField) || "PD_Y_S".equals(baseField)) {
			// 基于年份统计，需要计算category
			int point = 10;
			if (form.getLimit() != 0) {
				point = form.getLimit();
			}
			String[] years = getYear(point);
			category = Arrays.asList(years);
		}

		// 简单统计
		if (form.getPivot() == 0) {
			// 第一组数据
			List<FacetCount> fcs = fieldFaces.get(0).getResult();
			NamedData data = fillData(fcs, category, getFieldDesc(baseField));
			dataList.add(data);
			// 如果有对比数据，处理对比数据
			if (fieldFaces.size() > 1) {
				// 暂时只处理两组数据
				fcs = fieldFaces.get(1).getResult();
				data = fillData(fcs, category, getFieldDesc(form.getField()[1]));
				dataList.add(data);
				model.setData(dataList);
				model.setCategories(category);
			}
			model.setData(dataList);
			model.setCategories(category);
		}
		// 需要pivot(切边)统计
		else {
			PivotResultSet pivotResult = result.getFacets().getPivotFacets().get(0); // 只考虑切片一次
			List<PivotFacetResult> list = pivotResult.getResultSet();

			// 确定category
			if (category.isEmpty()) {
				category = Lists.newArrayList();
				for (PivotFacetResult s : list) {
					category.add(s.getValue());
				}
			}
			// 只统计切片数量
			if (form.getIsCount() == 1) {
				List<Object> data = Lists.newArrayList();
				for (int i = 0; i < category.size(); i++) {
					boolean find = false;
					for (PivotFacetResult s : list) {
						if (s.getValue().equals(category.get(i))) {
							find = data.add(s.getPivots().size());
							break;
						}
					}
					if (!find)
						data.add(0);
				}
				dataList.add(new NamedData(getFieldDesc(form.getField()[1]), data));
			}
			// 统计每个切片的专利数量
			else {
				// 确定切片的标题
				List<String> names = Lists.newArrayList();
				for (PivotFacetResult s : list) {
					for (PivotFacetResult s2 : s.getPivots()) {
						if (!names.contains(s2.getValue())) {
							names.add(s2.getValue());
						}
					}
				}
				names.sort(Comparator.comparing(str -> str));

				// 依据切片标题生成填充数据
				for (String name : names) {
					List<Object> data = Lists.newArrayList();
					for (PivotFacetResult s : list) {
						boolean find = false;
						for (PivotFacetResult s2 : s.getPivots()) {
							if (name.equals(s2.getValue())) {
								find = data.add(s2.getCount());
								break;
							}
						}
						if (!find) {
							data.add(0);
						}
					}
					for (int i = data.size(); i < category.size(); i++) {
						data.add(0);
					}
					dataList.add(new NamedData(name, data));
				}
			}
		}
		model.setData(dataList);
		model.setCategories(category);
		return model;
	}

	private GraphModel buildPieGraph(SearchResult result, AnalysisForm form) {
		GraphModel model = new GraphModel();
		List<Object> dataList = Lists.newArrayList();

		FacetsResult facets = result.getFacets();

		// 一维数据统计
		List<FieldFacetResult> fieldResult = facets.getFieldFacets();
		if (fieldResult.size() > 0) {
			List<Object> data = Lists.newArrayList();
			List<FacetCount> fis = fieldResult.get(0).getResult();
			for (FacetCount fc : fis) {
				PiePoint p = new PiePoint(fc.getValue(), fc.getCount());
				data.add(p);
			}
			NamedData namedData = new NamedData(getFieldDesc(form.getField()[0]), data);
			dataList.add(namedData);
		} else {

			// 一维数据统计（数据据切片）
			List<PivotResultSet> piList = facets.getPivotFacets();
			if (piList.size() > 0) {
				List<Object> list = Lists.newArrayList();
				List<Object> list2 = Lists.newArrayList();

				List<PivotFacetResult> resultSet = piList.get(0).getResultSet();
				if (form.getIsCount() == 1) {
					list = resultSet.stream().map(pf -> new PiePoint(pf.getValue(), pf.getPivots().size()))
							.collect(Collectors.toList());
				} else {
					for (int i = 0; i < resultSet.size(); i++) {
						PivotFacetResult pfResult = resultSet.get(i);
						String color = colors[i];
						PiePoint point = new PiePoint(pfResult.getValue(), pfResult.getCount(), color);
						list.add(point);

						List<PivotFacetResult> pivots = pfResult.getPivots();
						float count = pivots.size();
						for (int j = 0; j < count; j++) {
							PivotFacetResult pfResult2 = pivots.get(j);
							PiePoint point2 = new PiePoint(pfResult2.getValue(), pfResult2.getCount(),
									brighter(color, (j + 1) / count));
							list2.add(point2);
						}

					}
				}
				NamedData namedData = new NamedData(getFieldDesc(form.getField()[0]), list);
				dataList.add(namedData);
				if (form.getIsCount() == 0) {
					namedData = new NamedData(form.getField()[1], list2);
					dataList.add(namedData);
				}
			}

		}
		model.setData(dataList);
		return model;

	}

	/**
	 * 填充数据
	 * 
	 * @param fcs
	 * @param category
	 * @return
	 */
	private NamedData fillData(List<FacetCount> fcs, List<String> category, String dataName) {
		List<Object> data = Lists.newArrayList();
		boolean requireCgy;// 否需要添加categroy
		if (requireCgy = (category.isEmpty())) {
			// facet结果转换成统计图数据
			for (FacetCount fc : fcs) {
				if (requireCgy) {
					category.add(fc.getValue());
				}
				data.add(fc.getCount());
			}
		} else {
			for (String c : category) {
				boolean find = false;
				for (FacetCount fc : fcs) {
					if (c.equals(fc.getValue())) {
						find = data.add(fc.getCount());
						break;
					}
				}
				if (!find) {
					data.add(0);
				}
			}
		}
		return new NamedData(dataName, data);
	}

	@Override
	protected void setupGraphFacet(AnalysisForm form, QueryObjectBuilder builder) {
		String[] field = form.getField();
		int pivot = form.getPivot();
		if (field != null && field.length > 0) {
			if (pivot == 1 && field.length > 1) {
				builder.addPivotFacet("", field);
			} else {
				for (String f : field) {
					builder.addFieldFacet(f);
				}
			}
			if ("AD_Y_S".equals(field[0]) || "PD_Y_S".equals(field[0])) {
				BooleanQuery bq = new BooleanQuery();
				String[] years = getYear(form.getLimit());
				bq.getClasuses().add(
						newQueryClause(newRangQuery(field[0], years[0], years[form.getLimit() - 1], true), LOGIC.OR));
				builder.addFilter(bq);
			}
			// 时间为轴的时候不能直接设置limit，默认按count排序，未匹配数据会为0
			if (form.getLimit() > 0) {
				builder.addFacetParam("limit", field[0], form.getLimit() + "");
			}

			if (field.length == 2) {
				builder.addFacetParam("limit", field[1], Integer.MAX_VALUE + "");
			}
		} else {
			throw new IllegalArgumentException("至少需要一个统计字段");
		}
	}

	// 导出文件
	@RequestMapping("/export2")
	public void export(HttpServletRequest request, HttpServletResponse response) throws IOException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		String firstName = sdf.format(new Date().getTime());
		String svg = request.getParameter("svg");
		String data = request.getParameter("data");
		ObjectMapper om = new ObjectMapper();
		JsonNode node = om.readTree(data);
		GraphModel gm = om.convertValue(node, GraphModel.class);
		String fileName = firstName + gm.getName().get(0) + "统计图表.docx";
		String webPath = Util.getWebPath();
		String path = webPath + "svg.png";
		File file = convertToPng(svg, path);

		downWord(createWord(gm, file), response, fileName,request);
	}

	/**
	 * 创建word自定义
	 * 
	 * @param gm
	 * @param file
	 * @return
	 * @throws IOException
	 */
	private CustomXWPFDocument createWord(GraphModel gm, File file) throws IOException {
		List data = (List) gm.getData();
		List<String> title = gm.getCategories();
		List<String> name = gm.getName();
		XWPFTable table = null;
		CustomXWPFDocument xdoc = new CustomXWPFDocument();
		FileInputStream fis = new FileInputStream(file);
		File tem = new File(file.getPath());
		byte bytes[] = IOUtils.toByteArray(fis);
		try {
			xdoc.addPictureData(bytes, CustomXWPFDocument.PICTURE_TYPE_PNG);
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		}
		XWPFParagraph xp = xdoc.createParagraph();
		xdoc.createPicture(xdoc.getAllPictures().size() - 1, 550, 300, xp);
		file.delete();
		table = xdoc.createTable(title.size(), data.size() + 1);
		// 设置表格无边框
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
		for (int i = 0; i < title.size(); i++) {
			firstRow = table.getRow(i);
			firstRow.getCell(0).setText(title.get(i));
			for (int j = 0; j < data.size(); j++) {
				Map<String, List> map = (Map) data.get(j);
				firstRow.getCell(j + 1).setText(map.get("data").get(i) + "");
			}
		}
		firstRow = table.insertNewTableRow(0);
		firstCell = firstRow.addNewTableCell();
		firstCell.setText(name.get(0));
		firstCell.setColor("CCCCCC");
		for (int i = 0; i < data.size(); i++) {
			Map<String, List> map = (Map) data.get(i);
			firstCell = firstRow.addNewTableCell();
			firstCell.setText(map.get("name") + "");
			firstCell.setColor("CCCCCC");
		}

		return xdoc;
	}

	/**
	 * 导出word文件
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

	public File convertToPng(String svgCode, String pngFilePath) {
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

	private String getFieldDesc(String key) {
		String desc = "";
		switch (key) {
		case "AD_Y_S":
			desc = "申请年";
			break;
		case "AD_M_S":
			desc = "申请年(月)";
			break;
		case "CNLX":
			desc = "中国专利分类";
			break;
		case "PD_Y_S":
			desc = "公开年";
			break;
		case "PD_M_S":
			desc = "公开年(月)";
			break;
		case "MIPC_B_S":
			desc = "IPC大部";
			break;
		case "MIPC_DL_S":
			desc = "IPC大类";
			break;
		case "MIPC_DZ_S":
			desc = "IPC大组";
			break;
		case "MIPC_XL_S":
			desc = "IPC小类";
			break;
		case "CPC_B_S":
			desc = "CPC专利分类号部";
			break;
		case "CPC_DL_S":
			desc = "CPC专利分类号大类";
			break;
		case "CPC_DZ_S":
			desc = "CPC专利分类号大组";
			break;
		case "CPC_XL_S":
			desc = "CPC专利分类号小类";
			break;
		case "PA_S":
			desc = "申请人";
			break;
		case "AU_S":
			desc = "发明人";
			break;
		case "AC":
			desc = "申请人国别";
			break;
		case "PC":
			desc = "国省代码";
			break;
		case "AGC_S":
			desc = "代理机构";
			break;
		case "AGT_S":
			desc = "代理人";
			break;
		case "CTN_COUNT":
			desc = "引证次数";
			break;
		case "PFN_COUNT":
			desc = "同族专利数量";
			break;
		default:
			break;
		}
		return desc;
	}

	/**
	 * 颜色亮度增强
	 * 
	 * @param baseColor
	 * @return
	 */
	static String brighter(String baseColor, float brightness) {
		int r = Integer.parseInt(baseColor.substring(1, 3), 16);
		int g = Integer.parseInt(baseColor.substring(3, 5), 16);
		int b = Integer.parseInt(baseColor.substring(5, 7), 16);

		Color color = new Color(r, g, b);
		color = brighter(color, brightness);
		String red = Integer.toHexString(color.getRed());
		String green = Integer.toHexString(color.getGreen());
		String blue = Integer.toHexString(color.getBlue());
		red = StringUtils.leftPad(red, 2, "0");
		green = StringUtils.leftPad(green, 2, "0");
		blue = StringUtils.leftPad(blue, 2, "0");
		return "#" + red + green + blue;
	}

	public static Color brighter(Color color, float brightness) {
		int r = color.getRed();
		int g = color.getGreen();
		int b = color.getBlue();
		int alpha = color.getAlpha();

		brightness = brightness * .6f + 1f;

		return new Color(Math.min((int) (r * brightness), 255), Math.min((int) (g * brightness), 255),
				Math.min((int) (b * brightness), 255), alpha);
	}

	static String[] colors = new String[] { "#058DC7", "#50B432", "#ED561B", "#DDDF00", "#24CBE5", "#64E572", "#FF9655",
			"#FFF263", "#6AF9C4", "#7cb5ec", "#434348", "#90ed7d", "#f7a35c", "#8085e9", "#f15c80", "#e4d354",
			"#2b908f", "#f45b5b", "#91e8e1", "#2f7ed8", "#0d233a", "#8bbc21", "#910000", "#1aadce", "#492970",
			"#f28f43", "#77a1e5", "#c42525", "#a6c96a" };

}
