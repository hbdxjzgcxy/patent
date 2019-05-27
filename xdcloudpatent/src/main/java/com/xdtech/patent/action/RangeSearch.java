package com.xdtech.patent.action;

import org.apache.commons.lang.StringUtils;

/**
 *范围检索辅助类。
 *
 * @author changfei
 *
 */
public class RangeSearch {
	private String field;
	private String start;
	private String end;
	private String type;

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		if (StringUtils.isEmpty(field) || (StringUtils.isEmpty(start) && StringUtils.isEmpty(end))) {
			return null;
		} else {
			if ("date".equals(type)) {
				if (!StringUtils.isEmpty(start)) {
					start = start.replace("-", "");
					//start = StringUtils.rightPad(start, 17, "0");
				}

				if (!StringUtils.isEmpty(end)) {
					end = end.replace("-", "");
					//end = StringUtils.rightPad(end, 17, "0");
				}
			}

			if (!StringUtils.isEmpty(start) && !StringUtils.isEmpty(end)) {
				return field + ">=" + start + " AND " + field + "<=" + end;
			} else if (StringUtils.isEmpty(end)) {
				return field + ">=" + start;
			} else {
				return field + "<=" + end;
			}
		}
	}

	/**
	 * 转换实例
	 * <blockquote><pre>
	 *  setField("XX");
	 *  setStart("1978"); 
	 *  setEnd("1980");  
	 *  return  XX>=1978 AND XX<=1980
	 * 
	 *  setField("XX");
	 *  setStart("1978-01-04"); 
	 *  setEnd("1982-01-05");  
	 *  setType("date") //设置值类型
	 *  return  XX>=19780104000000000 AND XX<=19820105000000000
	 *  
	 *  setField("XX");
	 *  setStart("1978-01-04"); 
	 *  setType("date") //设置值类型
	 *  return  XX>=19780104000000000
	 * 
	 *
	 * @return
	 */
	public String getQuery() {
		return toString();
	}

	public static void main(String[] args) {
		RangeSearch PD = new RangeSearch();
		PD.setField("PD");
		PD.setStart("1970-08-01");
		PD.setEnd("1980-08-01");
		PD.setType("date");
		System.out.println(PD.toString());
	}

}
