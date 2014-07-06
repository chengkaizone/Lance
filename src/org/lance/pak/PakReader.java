package org.lance.pak;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Vector;

/**
 * J2ME Pak文件读取工具类 ---只能提取数据不能写入文件 功能：从Pak文件中取出png图片，构造byte数组（可以用来构造Image对象）
 * 可以减少安装包的空间大小
 * 
 * @author lance
 */
public class PakReader {
	public PakReader() {
	}

	/**
	 * 计算文件位移的起始点
	 * 
	 * @return 文件位移的起始点
	 */
	private long workOutOffsetStart(PakHeader header) {
		// 计算出文件头+文件table的长度
		return PakHeader.size() + header.getNumFileTableEntries()
				* PakTable.size();
	}

	/**
	 * 从DataInputStream读取char数组
	 * 
	 * @param dis
	 *            DataInputStream
	 * @param readLength
	 *            读取长度
	 * @return char数组
	 * @throws Exception
	 */
	private char[] readCharArray(DataInputStream dis, int readLength)
			throws Exception {
		char[] readCharArray = new char[readLength];
		for (int i = 0; i < readLength; i++) {
			readCharArray[i] = dis.readChar();
		}
		return readCharArray;
	}

	/**
	 * 从PAK文件中读取文件头
	 * 
	 * @param dis
	 *            DataInputStream
	 * @return PakHeader
	 * @throws Exception
	 */
	private PakHeader readHeader(DataInputStream dis) throws Exception {
		PakHeader header = new PakHeader();
		char[] signature = readCharArray(dis, PakHeader.SIGNATURE_LENGTH);
		header.setSignature(signature);
		header.setVersion(dis.readFloat());
		header.setNumFileTableEntries(dis.readLong());
		header.setCipherAction(dis.readByte());
		header.setCipherValue(dis.readByte());
		char[] uniqueID = readCharArray(dis, PakHeader.UNIQUEID_LENGTH);
		header.setUniqueID(uniqueID);
		header.setReserved(dis.readLong());
		return header;
	}

	/**
	 * 读取所有的文件table
	 * 
	 * @param dis
	 *            DataInputStream
	 * @param fileTableNumber
	 *            文件表总数
	 * @return 文件table数组
	 * @throws Exception
	 */
	private PakTable[] readFileTable(DataInputStream dis, int fileTableNumber)
			throws Exception {
		PakTable[] fileTable = new PakTable[fileTableNumber];
		for (int i = 0; i < fileTableNumber; i++) {
			PakTable ft = new PakTable();
			ft.setFileName(readCharArray(dis, PakTable.FILENAME_LENGTH));
			ft.setFileSize(dis.readLong());
			ft.setOffSet(dis.readLong());
			fileTable[i] = ft;
		}
		return fileTable;
	}

	/**
	 * 从pak文件读取文件到byte数组
	 * 
	 * @param dis
	 *            DataInputStream
	 * @param fileTable
	 *            PakFileTable
	 * @return byte数组
	 * @throws Exception
	 */
	private byte[] readFileFromPak(DataInputStream dis, PakHeader header,
			PakTable fileTable) throws Exception {
		dis.skip(fileTable.getOffSet() - workOutOffsetStart(header));
		//
		int fileLength = (int) fileTable.getFileSize();
		byte[] fileBuff = new byte[fileLength];
		int readLength = dis.read(fileBuff, 0, fileLength);
		if (readLength < fileLength) {
			System.out.println("读取数据长度不正确");
			return null;
		} else {
			decryptBuff(fileBuff, readLength, header);
		}
		return fileBuff;
	}

	/**
	 * 使用文件头中的密码对数据进行解密
	 * 
	 * @param buff
	 *            被解密的数据
	 * @param buffLength
	 *            数据的长度
	 * @param header
	 *            文件头
	 */
	private void decryptBuff(byte[] buff, int buffLength, PakHeader header) {
		for (int i = 0; i < buffLength; i++) {
			switch (header.getCipherAction()) {
			case PakHeader.ADDITION_CIPHERACTION:
				buff[i] -= header.getCipherValue();
				break;
			case PakHeader.SUBTRACT_CIHOERACTION:
				buff[i] += header.getCipherValue();
				break;
			}
		}
	}

	/**
	 * 从pak文件中取出指定的文件到byte数组
	 * 
	 * @param pakResourceURL
	 *            　pak文件在项目中的的相对路径
	 * @param extractResourceName
	 *            pak文件中将要被取出的文件名
	 * @return byte数组
	 * @throws Exception
	 */
	public byte[] extractResourceFromRelativePath(String pakResourcePath,
			String extractResourceName) throws Exception {
		InputStream is = this.getClass().getResourceAsStream(pakResourcePath);
		DataInputStream dis = new DataInputStream(is);
		PakHeader header = readHeader(dis);
		PakTable[] fileTable = readFileTable(dis,
				(int) header.getNumFileTableEntries());
		boolean find = false;
		int fileIndex = 0;
		for (int i = 0; i < fileTable.length; i++) {
			String fileName = new String(fileTable[i].getFileName()).trim();
			if (fileName.equals(extractResourceName)) {
				find = true;
				fileIndex = i;
				break;
			}
		}
		if (find == false) {
			System.out.println("没有找到指定的文件");
			return null;
		} else {
			byte[] buff = readFileFromPak(dis, header, fileTable[fileIndex]);
			return buff;
		}
	}

	/**
	 * 从pak中提取指定文件到byte数组
	 * 
	 * @param absoluteURL
	 *            pak文件绝对路径
	 * @param resName
	 *            资源名
	 * @return
	 * @throws Exception
	 */
	public byte[] extractResourceFromAbsolutePath(String absolutePath,
			String resName) throws Exception {
		InputStream is = new FileInputStream(absolutePath);
		DataInputStream dis = new DataInputStream(is);
		PakHeader header = readHeader(dis);
		PakTable[] fileTable = readFileTable(dis,
				(int) header.getNumFileTableEntries());
		boolean find = false;
		int fileIndex = 0;
		for (int i = 0; i < fileTable.length; i++) {
			String fileName = new String(fileTable[i].getFileName()).trim();
			if (fileName.equals(resName)) {
				find = true;
				fileIndex = i;
				break;
			}
		}
		if (find == false) {
			System.out.println("没有找到指定的文件");
			return null;
		} else {
			byte[] buff = readFileFromPak(dis, header, fileTable[fileIndex]);
			return buff;
		}
	}

	/**
	 * 从指定的流中提取指定文件名的的资源
	 * 
	 * @param stream
	 *            输入流
	 * @param resName
	 * @return
	 * @throws Exception
	 */
	public byte[] extractResourceFromStream(InputStream stream, String resName)
			throws Exception {
		DataInputStream dis = new DataInputStream(stream);
		PakHeader header = readHeader(dis);
		PakTable[] fileTable = readFileTable(dis,
				(int) header.getNumFileTableEntries());
		boolean find = false;
		int fileIndex = 0;
		for (int i = 0; i < fileTable.length; i++) {
			String fileName = new String(fileTable[i].getFileName()).trim();
			if (fileName.equals(resName)) {
				find = true;
				fileIndex = i;
				break;
			}
		}
		if (find == false) {
			System.out.println("没有找到指定的文件");
			return null;
		} else {
			byte[] buff = readFileFromPak(dis, header, fileTable[fileIndex]);
			return buff;
		}
	}

	/**
	 * 从pak文件中取出指定的Pak文件的信息 取出时第一个元素为pak文件头 第二个元素为文件PakTable数组
	 * 
	 * @param pakResourcePath
	 *            　pak文件资源路径---项目文件夹的相对路径
	 * @return 装载文件头和文件table数组的Vector
	 * @throws Exception
	 */
	public Vector showPakFromRelativePath(String pakRelativePath)
			throws Exception {
		InputStream is = this.getClass().getResourceAsStream(pakRelativePath);
		DataInputStream dis = new DataInputStream(is);
		PakHeader header = readHeader(dis);
		PakTable[] fileTable = readFileTable(dis,
				(int) header.getNumFileTableEntries());
		Vector result = new Vector();
		result.addElement(header);
		result.addElement(fileTable);
		return result;
	}

	/**
	 * 从pak文件中取出指定的Pak文件的信息 取出时第一个元素为pak文件头 第二个元素为文件PakTable数组
	 * 
	 * @param absolutePath
	 *            pak文件绝对路径
	 * @return
	 * @throws Exception
	 */
	public Vector showPakFromAbsolutePath(String pakAbsolutePath)
			throws Exception {
		InputStream is = new FileInputStream(pakAbsolutePath);
		DataInputStream dis = new DataInputStream(is);
		PakHeader header = readHeader(dis);
		PakTable[] fileTable = readFileTable(dis,
				(int) header.getNumFileTableEntries());
		Vector result = new Vector();
		result.addElement(header);
		result.addElement(fileTable);
		return result;
	}

	/**
	 * 从指定的流中获取数据
	 * 
	 * @param stream
	 * @return
	 * @throws Exception
	 */
	public Vector showPakFromStream(InputStream stream) throws Exception {
		DataInputStream dis = new DataInputStream(stream);
		PakHeader header = readHeader(dis);
		PakTable[] fileTable = readFileTable(dis,
				(int) header.getNumFileTableEntries());
		Vector result = new Vector();
		result.addElement(header);
		result.addElement(fileTable);
		return result;
	}

	/**
	 * 测试方法---是用项目中的文件
	 * 
	 * @param relaPath
	 *            相对路径
	 */
	public static void extractFileFromRelativePath(String relaPath) {
		try {
			PakReader reader = new PakReader();
			// 从Pak文件中取出所有的图片文件
			Vector pakInfo = reader.showPakFromRelativePath(relaPath);
			PakHeader header = (PakHeader) pakInfo.elementAt(0);
			/*
			 * System.out.println("Pak文件信息:"); System.out.println("文件头:");
			 * System.out.println(header);
			 */
			PakTable[] fileTable = (PakTable[]) pakInfo.elementAt(1);
			// System.out.println("文件数量："+fileTable.length);
			String restoreFileName = null;
			byte[] fileBuff = null;
			for (int i = 0; i < fileTable.length; i++) {
				restoreFileName = new String(fileTable[i].getFileName()).trim();
				fileBuff = reader.extractResourceFromRelativePath(relaPath,
						restoreFileName);
				System.out.println(restoreFileName + " 大小：" + fileBuff.length
						/ 1024f + "kb");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param absoPath
	 */
	public static void extractFileFromAbsolutePath(String absoPath) {
		try {
			PakReader reader = new PakReader();
			// 从Pak文件中取出所有的图片文件
			Vector pakInfo = reader.showPakFromAbsolutePath(absoPath);
			PakHeader header = (PakHeader) pakInfo.elementAt(0);
			/*
			 * System.out.println("Pak文件信息:"); System.out.println("文件头:");
			 * System.out.println(header);
			 */
			PakTable[] fileTable = (PakTable[]) pakInfo.elementAt(1);
			// System.out.println("文件数量："+fileTable.length);
			String restoreFileName = null;
			byte[] fileBuff = null;
			for (int i = 0; i < fileTable.length; i++) {
				restoreFileName = new String(fileTable[i].getFileName()).trim();
				fileBuff = reader.extractResourceFromAbsolutePath(absoPath,
						restoreFileName);
				System.out.println(restoreFileName + " 大小：" + fileBuff.length
						/ 1024f + "kb");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/*
	 * public static void main(String[] argv) throws Exception {
	 * extractFileFromAbsolutePath("c:/TEMP/wall.pak"); }
	 */
}