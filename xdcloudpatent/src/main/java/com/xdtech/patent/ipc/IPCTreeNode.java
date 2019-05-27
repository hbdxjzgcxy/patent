package com.xdtech.patent.ipc;

import java.util.List;

public class IPCTreeNode {
	String code;
	String desc;
	List<IPCTreeNode> children;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public List<IPCTreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<IPCTreeNode> children) {
		this.children = children;
	}
	
	

	public IPCTreeNode() {
		super();
	}
	
	

	public IPCTreeNode(String code, String desc, List<IPCTreeNode> children) {
		super();
		this.code = code;
		this.desc = desc;
		this.children = children;
	}

	@Override
	public String toString() {
		return code + ":" + desc;
	}

}
