package com.dbmigproj.builder.beans;

public class ConfigBean {

	private String srcDir = null;
	private String tgtDir = null;
	private String srcType = null;
	private String tgtType = null;
	private String srcProcFile = null;
	private String tgtProcFile = null;

	public ConfigBean() {

	}

	public String getSrcDir() {
		return srcDir;
	}

	public void setSrcDir(String srcDir) {
		this.srcDir = srcDir;
	}

	public String getTgtDir() {
		return tgtDir;
	}

	public void setTgtDir(String tgtDir) {
		this.tgtDir = tgtDir;
	}

	public String getSrcType() {
		return srcType;
	}

	public void setSrcType(String srcType) {
		this.srcType = srcType;
	}

	public String getTgtType() {
		return tgtType;
	}

	public void setTgtType(String tgtType) {
		this.tgtType = tgtType;
	}

	public String getSrcProcFile() {
		return srcProcFile;
	}

	public void setSrcProcFile(String srcProcFile) {
		this.srcProcFile = srcProcFile;
	}

	public String getTgtProcFile() {
		return tgtProcFile;
	}

	public void setTgtProcFile(String tgtProcFile) {
		this.tgtProcFile = tgtProcFile;
	}

	public String toString() {
		return "srcDir:" + srcDir + ", \ntgtDir:" + tgtDir + ", \nsrcType:" + srcType 
				+ ", tgtType:" + tgtType + ", srcProcFile:" + srcProcFile 
				+ ", tgtProcFile:" + tgtProcFile;
	}

}
