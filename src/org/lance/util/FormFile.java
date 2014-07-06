package org.lance.util;

import java.io.InputStream;

/**
 * ±íÎÄ¼þ
 * 
 * @author lance
 * 
 */
public class FormFile {
	private String Formnames;
	private String contentType = "application/octet-stream";
	private byte[] data;
	private String fileName;
	private InputStream inStream;

	public FormFile() {
	}

	public FormFile(InputStream paramInputStream, String paramString1,
			String paramString2, String paramString3) {
		this.inStream = paramInputStream;
		this.fileName = paramString1;
		this.Formnames = paramString2;
		this.contentType = paramString3;
	}

	public FormFile(byte[] paramArrayOfByte, String paramString1,
			String paramString2, String paramString3) {
		this.data = paramArrayOfByte;
		this.fileName = paramString1;
		this.Formnames = paramString2;
		this.contentType = paramString3;
	}

	public String getContentType() {
		return this.contentType;
	}

	public byte[] getData() {
		return this.data;
	}

	public String getFileName() {
		return this.fileName;
	}

	public String getFormnames() {
		return this.Formnames;
	}

	public InputStream getInStream() {
		return this.inStream;
	}

	public void setContentType(String paramString) {
		this.contentType = paramString;
	}

	public void setData(byte[] paramArrayOfByte) {
		this.data = paramArrayOfByte;
	}

	public void setFileName(String paramString) {
		this.fileName = paramString;
	}

	public void setFormnames(String paramString) {
		this.Formnames = paramString;
	}

	public void setInStream(InputStream paramInputStream) {
		this.inStream = paramInputStream;
	}
}