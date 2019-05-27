package com.xdtech.patent.conf;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.jempbox.impl.XMLUtil;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

/**
 * 读取配置文件中的配置项
 * 
 * @author zhangjianbing@msn.com
 */
@SuppressWarnings("rawtypes")
public class AppConf {
	private static Logger LOG = Logger.getLogger(AppConf.class.getName());
	private static final AppConf DEFAULT = new AppConf();

	public static AppConf get() {
		return DEFAULT;
	}

	private List resourceNames = new ArrayList();
	private Properties properties;

	private ClassLoader classLoader = Thread.currentThread()
			.getContextClassLoader();

	public AppConf() {
		resourceNames.add("config.xml");
		resourceNames.add("backup.xml");
	}

	public synchronized void addConfResource(String name) {
		addConfResourceInternal(name);
	}

	public synchronized void addConfResource(File file) {
		addConfResourceInternal(file);
	}

	private synchronized void addConfResourceInternal(Object name) {
		resourceNames.add(resourceNames.size() - 1, name); // add second to last
		properties = null; // trigger reload
	}

	public String get(String name) {
		return getProps().getProperty(name);
	}

	public void set(String name, Object value) {
		getProps().setProperty(name, value.toString());
	}

	public String get(String name, String defaultValue) {
		return getProps().getProperty(name, defaultValue);
	}

	public int getInt(String name, int defaultValue) {
		String valueString = get(name);
		if (valueString == null)
			return defaultValue;
		try {
			return Integer.parseInt(valueString);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	public void setInt(String name, int value) {
		set(name, Integer.toString(value));
	}

	public long getLong(String name, long defaultValue) {
		String valueString = get(name);
		if (valueString == null)
			return defaultValue;
		try {
			return Long.parseLong(valueString);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	public float getFloat(String name, float defaultValue) {
		String valueString = get(name);
		if (valueString == null)
			return defaultValue;
		try {
			return Float.parseFloat(valueString);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	public boolean getBoolean(String name, boolean defaultValue) {
		String valueString = get(name);
		if ("true".equals(valueString))
			return true;
		else if ("false".equals(valueString))
			return false;
		else
			return defaultValue;
	}

	public String[] getStrings(String name) {
		String valueString = get(name);
		if (valueString == null)
			return null;
		StringTokenizer tokenizer = new StringTokenizer(valueString,
				", \t\n\r\f");
		List values = new ArrayList();
		while (tokenizer.hasMoreTokens()) {
			values.add(tokenizer.nextToken());
		}
		return (String[]) values.toArray(new String[values.size()]);
	}

	public Class getClass(String name, Class defaultValue) {
		String valueString = get(name);
		if (valueString == null)
			return defaultValue;
		try {
			return Class.forName(valueString);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public Class getClass(String propertyName, Class defaultValue, Class xface) {
		try {
			Class theClass = getClass(propertyName, defaultValue);
			if (theClass != null && !xface.isAssignableFrom(theClass))
				throw new RuntimeException(theClass + " not " + xface.getName());
			return theClass;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void setClass(String propertyName, Class theClass, Class xface) {
		if (!xface.isAssignableFrom(theClass))
			throw new RuntimeException(theClass + " not " + xface.getName());
		set(propertyName, theClass.getName());
	}

	public URL getResource(String name) {
		return classLoader.getResource(name);
	}

	public InputStream getConfResourceAsInputStream(String name) {
		try {
			URL url = getResource(name);

			if (url == null) {
				return null;
			} else {
				LOG.info("资源文件 " + name + " 位于 " + url);
			}

			return url.openStream();
		} catch (Exception e) {
			return null;
		}
	}

	public Reader getConfResourceAsReader(String name) {
		try {
			URL url = getResource(name);

			if (url == null) {
				LOG.info(name + " 未找到");
				return null;
			} else {
				LOG.info("资源文件 " + name + " 位于 " + url);
			}

			return new InputStreamReader(url.openStream());
		} catch (Exception e) {
			return null;
		}
	}

	private synchronized Properties getProps() {
		if (properties == null) {
			Properties defaults = new Properties();
			Properties newProps = new Properties(defaults);
			ListIterator i = resourceNames.listIterator();
			while (i.hasNext()) {
				if (i.nextIndex() == 0) { // load defaults
					loadResource(defaults, i.next(), false);
				} else if (i.nextIndex() == resourceNames.size() - 1) { // load
					// site
					loadResource(newProps, i.next(), true);
				} else { // load intermediate
					loadResource(newProps, i.next(), false);
				}
			}
			properties = newProps;
		}
		return properties;
	}

	private void loadResource(Properties properties, Object name,
			boolean quietFail) {
		try {
			Document doc = null;
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			

			if (name instanceof String) { // a CLASSPATH resource
				URL url = getResource((String) name);
				if (url != null) {
					// LOG.info("parsing " + url);
					doc = builder.parse(url.toString());
				}
			} else if (name instanceof File) { // a file resource
				File file = (File) name;
				if (file.exists()) {
					// LOG.info("parsing " + file);
					doc = builder.parse(file);
				}
			}

			if (doc == null) {
				if (quietFail)
					return;
				throw new RuntimeException(name + " 未找到");
			}

			Element root = doc.getDocumentElement();
			if (!"app-conf".equals(root.getTagName()))
				LOG.severe("bad conf file: top-level element not <app-conf>");
			NodeList props = root.getChildNodes();
			for (int i = 0; i < props.getLength(); i++) {
				Node propNode = props.item(i);
				if (!(propNode instanceof Element))
					continue;
				Element prop = (Element) propNode;
				if (!"property".equals(prop.getTagName()))
					LOG.warning("bad conf file: element not <property>");
				NodeList fields = prop.getChildNodes();
				String attr = null;
				String value = null;
				for (int j = 0; j < fields.getLength(); j++) {
					Node fieldNode = fields.item(j);
					if (!(fieldNode instanceof Element))
						continue;
					Element field = (Element) fieldNode;
					if ("name".equals(field.getTagName()))
						attr = ((Text) field.getFirstChild()).getData();
					if ("value".equals(field.getTagName())
							&& field.hasChildNodes())
						value = ((Text) field.getFirstChild()).getData();
				}
				if (attr != null && value != null)
					properties.setProperty(attr, value);
			}

		} catch (Exception e) {
			LOG.severe("error parsing conf file: " + e);
			throw new RuntimeException(e);
		}
	}
	
	
	
	public void persist(String resource,String key,String value){
		try {
			resource = AppContextUtils.getRealPath()+"/"+"WEB-INF/classes/"+resource;
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			Document doc = builder.parse(new File(resource));
			Element root = doc.getDocumentElement();
			NodeList props = root.getChildNodes();
			for (int i = 0; i < props.getLength(); i++) {
				Node propNode = props.item(i);
				if (!(propNode instanceof Element))
					continue;
				Element prop = (Element) propNode;
				if (!"property".equals(prop.getTagName()))
					LOG.warning("bad conf file: element not <property>");
				NodeList fields = prop.getChildNodes();
				String attr = null;
				boolean stop =false;
				for (int j = 0; j < fields.getLength(); j++) {
					Node fieldNode = fields.item(j);
					if (!(fieldNode instanceof Element))
						continue;
					Element field = (Element) fieldNode;
					String tagName = field.getTagName();
					if ("name".equals(tagName))
						attr = ((Text) field.getFirstChild()).getData();
					if(key.equals(attr)&&tagName.equals("value")){
						stop = true;
						field.getFirstChild().setTextContent(value);
						break;
					}
				}
				if(stop==true){
					XMLUtil.save(doc,resource, "utf-8");
				}
			}
		} catch (DOMException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}
}
