package com.xdtech.util;

import java.io.IOException;
import java.io.StringWriter;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

public class JsonUtil {
	public static String toJsonStr(Object obj) {
		StringWriter writer = new StringWriter();
		try {
			JsonFactory jsonFactory = new ObjectMapper().getJsonFactory();
			JsonGenerator generator = jsonFactory.createJsonGenerator(writer);
			generator.writeObject(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return writer.getBuffer().toString();
	}
}
