package com.xdtech.patent.reader;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

public class AbstractCellValProc {
	List<String> proc(String field, String cellVal,Map<String, ImportCfg> cfgs) {
		String PA_S = "";
		if (field != null) {
			String[] splitValues = Util.splitAndTrim(field, field,cfgs);
			PA_S = Util.jionArray(splitValues, ";");
		}
		return Lists.newArrayList(PA_S);

	}
}
