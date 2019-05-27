package com.xdtech.patent.reader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 专利导入配置文件
 * 
 * @author changfei
 *
 */
public class ImportCfg {
	/**
	 * 可能遇到的列名称
	 */
	private List<String> column = Lists.newArrayList();

	/**
	 * 索引字段的名称
	 */
	private String name;

	/**
	 * 多值字段分隔符
	 */
	private char splitChars[];

	/**
	 * 日期字段格式
	 */
	private String datePatetn;

	public ImportCfg(String name, List<String> column) {
		this.name = name;
		this.column = column;
	}

	public static Map<String, ImportCfg> getDefault() throws IOException, DocumentException {
		try (InputStream is = ImportCfg.class.getResourceAsStream("/import_cfg.xml");
				InputStreamReader inReader = new InputStreamReader(is, "UTF-8");) {
			return get(is);
		}
	}

	public static Map<String, ImportCfg> get(String templet) throws IOException, DocumentException {
		if (StringUtils.isEmpty(templet))
			return getDefault();
		try (InputStream is = new FileInputStream(templet); InputStreamReader inReader = new InputStreamReader(is, "UTF-8");) {
			return get(is);
		}

	}

	@SuppressWarnings({ "unchecked" })
	private static Map<String, ImportCfg> get(InputStream xml) throws IOException, DocumentException {
		Map<String, ImportCfg> cfgs = Maps.newHashMap();
		SAXReader reader = new SAXReader();
		// "/import_cfg.xml"
		InputStreamReader inReader = new InputStreamReader(xml, "UTF-8");
		Document doc = reader.read(inReader);
		@SuppressWarnings("rawtypes")
		List list = doc.selectNodes("/导入配置项/字段映射");
		list.forEach(e -> {
			Element fieldNode = (Element) e;
			String name = getText(fieldNode, "索引字段名称", true);
			List<String> columns = getTexts(fieldNode, "EXCEL标题", true);
			ImportCfg cfg = new ImportCfg(name, columns);
			cfg.setDatePatetn(getText(fieldNode, "日期格式", false));
			String splitStr = getText(fieldNode, "多值分隔符", false);
			if (StringUtils.isNotEmpty(splitStr)) {
				cfg.setSplitChars(splitStr.toCharArray());
			}
			cfgs.put(name, cfg);
		});
		return cfgs;
	}

	@SuppressWarnings({ "unchecked" })
	private static String getText(Element e, String name, boolean must) {
		String text = null;
		Element element = (Element) e;
		List<Element> nodes = element.selectNodes(name);
		if (!nodes.isEmpty()) {
			text = nodes.get(0).getText().trim();
		}
		if (StringUtils.isEmpty(text) && must)
			throw new IllegalArgumentException(name + "字段不能为空!");

		return text;
	}

	@SuppressWarnings({ "unchecked" })
	private static List<String> getTexts(Element e, String name, boolean must) {
		Element element = (Element) e;
		List<Element> nodes = element.selectNodes(name);
		List<String> list = Lists.newArrayList();
		for (Element n : nodes) {
			String text = n.getText();
			if (StringUtils.isNotEmpty(text)) {
				list.add(n.getText().trim());
			}
		}
		if (list.isEmpty() && must) {
			throw new IllegalArgumentException(name + "字段不能为空!");
		}
		return list;
	}

	public List<String> getColumn() {
		return column;
	}

	void setColumn(List<String> column) {
		this.column = column;
	}

	public String getName() {
		return name;
	}

	void setName(String name) {
		this.name = name;
	}

	public char[] getSplitChars() {
		return splitChars;
	}

	void setSplitChars(char splitChars[]) {
		this.splitChars = splitChars;
	}

	public String getDatePatetn() {
		return datePatetn;
	}

	void setDatePatetn(String datePatetn) {
		this.datePatetn = datePatetn;
	}

	public static void main(String[] args) {

	}

}
