package com.xdtech.patent.ipc;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.google.common.collect.Lists;
import com.xdtech.patent.conf.AppConf;
import com.xdtech.search.client.XDIndexOperationSupport;
import com.xdtech.search.client.ws.StringArray;
import com.xdtech.search.client.ws.XDCloudSearchException_Exception;

public class IPCContext {

	//根节点
	private static IPCTreeNode contextRoot = new IPCTreeNode("全部IPC", "IPCALL", Lists.newArrayList());

	/**
	 * 查询ipc分类节点信息
	 * @param ipccode
	 * @return
	 */
	public static IPCTreeNode get(String ipccode) {
		if (StringUtils.isNotEmpty(ipccode)) {
			ipccode = ipccode.trim().toUpperCase();
			return findNode(contextRoot, ipccode);
		}
		return null;
	}

	/**
	 * 查询ipc分类号对应的描述
	 * @param ipccode
	 * @return
	 */
	public static String getDesc(String ipccode) {
		IPCTreeNode entry = get(ipccode);
		if (entry != null)
			return entry.getDesc();
		return null;
	}

	/**
	 * 在节点下查找
	 * @param ipc
	 * @param ipccode
	 * @return
	 */
	private static IPCTreeNode findNode(IPCTreeNode ipc, String ipccode) {
		IPCTreeNode next = null;
		if (ipc.getCode().equals(ipccode)) {
			next = ipc;
		} else {
			for (IPCTreeNode pp : ipc.getChildren()) {
				if (pp.getCode().equals(ipccode)) {
					next = pp;
					break;
				} else if (ipccode.startsWith(pp.getCode())) { //
					next = findNode(pp, ipccode);
				} else {
					if (hasSamePrefix(pp.getCode(), ipccode, 5)) {
						for (IPCTreeNode sb : pp.getChildren()) {
							next = fineNextLevle(sb, ipccode);
							if (next != null) {
								break;
							}
						}
					}
				}
				if (next != null) {
					break;
				}
			}
		}
		return next;
	}

	/**
	 * 测试两个ipc分类号是否有相同的前缀
	 * @param code1
	 * @param code2
	 * @param startNum
	 * @return
	 */
	private static boolean hasSamePrefix(String code1, String code2, int startNum) {
		if (code1.length() > startNum && code2.length() > startNum) {
			String scode = code1.substring(0, startNum);
			String dcode = code2.substring(0, startNum);
			if (scode.equals(dcode)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 在子节点中查找
	 * @param sb
	 * @param ipccode
	 * @return
	 */
	private static IPCTreeNode fineNextLevle(IPCTreeNode sb, String ipccode) {
		if (sb.getCode().equals(ipccode)) {
			return sb;
		} else {
			for (IPCTreeNode cb : sb.getChildren()) {
				if (cb.getCode().equals(ipccode)) {
					return cb;
				} else {
					IPCTreeNode next = fineNextLevle(cb, ipccode);
					if (next != null) {
						return next;
					}
				}
			}
		}
		return null;
	}

	public static String category(String code) {
		if (StringUtils.isEmpty(code))
			return code;

		String category = null;
		if (code.length() == 1)
			category = "IPC_B_S";
		else if (code.length() == 3) {
			category = "IPC_DL_S";
		} else if (code.length() == 4) {
			category = "IPC_XL_S";
		} else if (code.length() == 5) {
			category = "IPC_DZ_S";
		} else {
			category = "IPC";
		}
		return category;
	}

	/**
	 * 初始化IPC检索上下文。
	 * @return ipc 树的根节点。
	 */
	public static IPCTreeNode init() {
		load(contextRoot, "ipc.xml");
		delayPush(20);
		return contextRoot;
	}

	/**
	 * 初始化IPC检索上下文不推送到搜索引擎。
	 * @return
	 */
	public static IPCTreeNode initNoPush() {
		load(contextRoot, "ipc.xml");
		return contextRoot;
	}

	/**
	 *加载xml形式的IPC数据
	 */
	private static void load(IPCTreeNode contextRoot2, String file) {
		InputStream is = IPCContext.class.getResourceAsStream("/com/xdtech/patent/ipc/" + file);
		SAXReader saxReader = new SAXReader();
		if (is != null) {
			try {

				Document document = saxReader.read(is);
				@SuppressWarnings("unchecked")
				List<Element> nodes = document.getRootElement().selectNodes("TreeNode");
				for (Element e : nodes) {
					loadNode(contextRoot2, e);
				}

			} catch (DocumentException e) {
				System.out.println(file);
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			System.out.println(file + "找不到");
		}
	}

	@SuppressWarnings("unchecked")
	private static void loadNode(IPCTreeNode parent, Element subNode) {
		String subfile = subNode.attributeValue("NodeXMLSrc");
		String title = subNode.attributeValue("Title");
		IPCTreeNode entry = newIPCEntrty(title);

		if (subfile != null && !subfile.isEmpty()) {
			load(entry, subfile);
		} else {
			List<Element> nodes = subNode.selectNodes("TreeNode");
			if (nodes != null && !nodes.isEmpty()) {
				for (Element e1 : nodes) {
					loadNode(entry, e1);
				}
			}
		}
		parent.getChildren().add(entry);
	}

	/**
	 * 延时推送到全文
	 * @param second 延时时长
	 */
	private static void delayPush(int second) {
		try {
			TimeUnit.SECONDS.sleep(second);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new Thread(() -> {
			try {
				Thread.sleep(20 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			push();
		}).start();
	}

	/**
	 *推送数据到全文
	 * @return
	 */
	private static String push() {
		XDIndexOperationSupport service = null;
		try {
			String wsdl = AppConf.get().get("operation.servcie.wsdl", "http://localhost:8080/xdcloudsearch/service/operate?wsdl");
			service = new XDIndexOperationSupport(wsdl);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (service != null) {
			List<StringArray> data = Lists.newArrayList();
			String[] codes = new String[] { "A", "B", "C", "D", "E", "F", "G", "H" };
			for (String code : codes) {
				IPCTreeNode entry = IPCContext.get(code);
				list(service, entry, data);
			}
			push(service, data);
			data.clear();
			push(service, data);
		}
		return "sucess!";
	}

	/**
	 * 由title创建一个IPCEntry
	 * @param title
	 * @return
	 */
	private static IPCTreeNode newIPCEntrty(String title) {
		IPCTreeNode entry = new IPCTreeNode();
		int spacChar = title.indexOf(" ");
		entry.setCode(title.substring(0, spacChar));
		entry.setDesc(title.substring(spacChar + 1).trim().replace("#", "."));
		entry.setChildren(Lists.newArrayList());
		return entry;
	}

	/**
	 * 遍历IPC树，1000条推送一次
	 * @param service
	 * @param entry
	 * @param data
	 */
	private static void list(XDIndexOperationSupport service, IPCTreeNode entry, List<StringArray> data) {
		if (data.size() >= 1000) {
			push(service, data);
			data.clear();
		}

		StringArray array = new StringArray();
		array.getItem().add(entry.getCode());
		array.getItem().add(entry.getCode());
		array.getItem().add(entry.getDesc());
		data.add(array);

		for (IPCTreeNode en : entry.getChildren()) {
			list(service, en, data);
		}
	}

	private static void push(XDIndexOperationSupport service, List<StringArray> data) {
		try {
			service.getService().commitAtCore("ipc");
			if (data.size() == 0) {
				service.getService().optimizeAtCore("ipc");
			} else {
				service.getService().pushAtCore("ipc", Lists.newArrayList("docNo", "code", "desc"), data, null, null);
			}
		} catch (XDCloudSearchException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
