package org.lance.pak;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

/**
 * J2SE pak压缩和读取工具---能提取到文件系统中
 * 
 * @author lance
 * 
 */
public class PakWriter {
	public final static String SIGNATURE = "012345";
	public final static String UUID = "0123456789";

	public PakWriter() {
	}

	/**
	 * 返回文件长度
	 * 
	 * @param filePath
	 *            文件路径
	 * @return 文件长度
	 */
	private long getFileSize(String filePath) {
		File file = new File(filePath);
		return file.length();
	}

	/**
	 * 返回文件名
	 * 
	 * @param filePath
	 *            文件路径
	 * @return 文件名
	 */

	private String getFileName(String filePath) {
		File file = new File(filePath);
		return file.getName();
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
	 * 计算文件位移
	 * 
	 * @param fileIndex
	 *            文件序号
	 * @param lastFileOffset
	 *            上一个文件位移
	 * @return　文件在pak文件中的位移
	 */
	private long workOutNextOffset(long sourceFileSize, long lastFileOffset) {
		return lastFileOffset + sourceFileSize;
	}

	/**
	 * 生成文件table
	 * 
	 * @param sourceFileName
	 *            源文件名
	 * @param sourceFileSize
	 *            源文件长度
	 * @param currentFileOffset
	 *            当前文件位移
	 * @return 生成的PakFileTable对象
	 */
	private PakTable generateFileTable(String sourceFileName,
			long sourceFileSize, long currentFileOffset) {
		PakTable ft = new PakTable();
		ft.setFileName(sourceFileName.toCharArray());
		ft.setFileSize(sourceFileSize);
		ft.setOffSet(currentFileOffset);
		return ft;
	}

	/**
	 * 将char字符数组写入到DataOutputStream中
	 * 
	 * @param toWriteCharArray
	 *            被写入的char数组
	 * @param dos
	 *            DataOutputStream
	 * @throws Exception
	 */
	private void writeCharArray(char[] toWriteCharArray, DataOutputStream dos)
			throws Exception {
		for (int i = 0; i < toWriteCharArray.length; dos
				.writeChar(toWriteCharArray[i]), i++)
			;
	}

	/**
	 * 使用文件头中的密码对数据进行加密
	 * 
	 * @param buff
	 *            被加密的数据
	 * @param buffLength
	 *            数据的长度
	 * @param header
	 *            文件头
	 */
	private void encryptBuff(byte[] buff, int buffLength, PakHeader header) {
		for (int i = 0; i < buffLength; i++) {
			switch (header.getCipherAction()) {
			case PakHeader.ADDITION_CIPHERACTION:
				buff[i] += header.getCipherValue();
				break;
			case PakHeader.SUBTRACT_CIHOERACTION:
				buff[i] -= header.getCipherValue();
				break;
			}
		}
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
	 * 制作Pak文件
	 * 
	 * @param sourceFilePath
	 *            源文件路径数组
	 * @param destinateFilePath
	 *            目的文件路径（Pak文件）
	 * @param cipherAction
	 *            密码行为
	 * @param cipherValue
	 *            密码
	 * @throws Exception
	 */
	public void makePakFile(String[] sourceFilePath, String destinateFilePath,
			PakHeader header) throws Exception {
		PakTable[] fileTable = new PakTable[sourceFilePath.length];
		// 计算文件位移起始点
		long fileOffset = workOutOffsetStart(header);
		// 逐个建立文件table
		for (int i = 0; i < sourceFilePath.length; i++) {
			String sourceFileName = getFileName(sourceFilePath[i]);
			long sourceFileSize = getFileSize(sourceFilePath[i]);
			PakTable ft = generateFileTable(sourceFileName, sourceFileSize,
					fileOffset);
			// 计算下一个文件位移
			fileOffset = workOutNextOffset(sourceFileSize, fileOffset);
			fileTable[i] = ft;
		}
		// 写入文件头
		File wFile = new File(destinateFilePath);
		FileOutputStream fos = new FileOutputStream(wFile);
		DataOutputStream dos = new DataOutputStream(fos);
		writeCharArray(header.getSignature(), dos);
		dos.writeFloat(header.getVersion());
		dos.writeLong(header.getNumFileTableEntries());
		dos.writeByte(header.getCipherAction());
		dos.writeByte(header.getCipherValue());
		writeCharArray(header.getUniqueID(), dos);
		dos.writeLong(header.getReserved());
		// 写入文件table
		for (int i = 0; i < fileTable.length; i++) {
			writeCharArray(fileTable[i].getFileName(), dos);
			dos.writeLong(fileTable[i].getFileSize());
			dos.writeLong(fileTable[i].getOffSet());
		}
		// 写入文件数据
		for (int i = 0; i < fileTable.length; i++) {
			File ftFile = new File(sourceFilePath[i]);
			FileInputStream ftFis = new FileInputStream(ftFile);
			DataInputStream ftDis = new DataInputStream(ftFis);
			byte[] buff = new byte[256];
			int readLength = 0;
			while ((readLength = ftDis.read(buff)) != -1) {
				encryptBuff(buff, readLength, header);
				dos.write(buff, 0, readLength);
			}
			ftDis.close();
			ftFis.close();
		}
		dos.close();
	}

	/**
	 * 创建pak压缩文件
	 * 
	 * @param files
	 * @param destinateFilePath
	 * @param header
	 * @throws Exception
	 */
	public void makePakFile(ArrayList<File> files, String destinateFilePath,
			PakHeader header) throws Exception {
		PakTable[] fileTable = new PakTable[files.size()];
		// 计算文件位移起始点
		long fileOffset = workOutOffsetStart(header);
		// 逐个建立文件table
		for (int i = 0; i < files.size(); i++) {
			File file = files.get(i);
			PakTable ft = generateFileTable(file.getName(), file.length(),
					fileOffset);
			// 计算下一个文件位移
			fileOffset = workOutNextOffset(file.length(), fileOffset);
			fileTable[i] = ft;
		}
		// 写入文件头
		File wFile = new File(destinateFilePath);
		FileOutputStream fos = new FileOutputStream(wFile);
		DataOutputStream dos = new DataOutputStream(fos);
		writeCharArray(header.getSignature(), dos);
		dos.writeFloat(header.getVersion());
		dos.writeLong(header.getNumFileTableEntries());
		dos.writeByte(header.getCipherAction());
		dos.writeByte(header.getCipherValue());
		writeCharArray(header.getUniqueID(), dos);
		dos.writeLong(header.getReserved());
		// 写入文件table
		for (int i = 0; i < fileTable.length; i++) {
			writeCharArray(fileTable[i].getFileName(), dos);
			dos.writeLong(fileTable[i].getFileSize());
			dos.writeLong(fileTable[i].getOffSet());
		}
		// 写入文件数据
		for (int i = 0; i < fileTable.length; i++) {
			File ftFile = files.get(i);
			FileInputStream ftFis = new FileInputStream(ftFile);
			DataInputStream ftDis = new DataInputStream(ftFis);
			byte[] buff = new byte[256];
			int readLength = 0;
			while ((readLength = ftDis.read(buff)) != -1) {
				encryptBuff(buff, readLength, header);
				dos.write(buff, 0, readLength);
			}
			ftDis.close();
			ftFis.close();
		}
		dos.close();
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
			return fileBuff;
		}
	}

	/**
	 * 将buffer中的内容写入到文件
	 * 
	 * @param fileBuff
	 *            保存文件内容的buffer
	 * @param fileName
	 *            文件名
	 * @param extractDir
	 *            文件导出目录
	 * @throws Exception
	 */
	private void writeFileFromByteBuffer(byte[] fileBuff, String fileName,
			String extractDir) throws Exception {
		String extractFilePath = extractDir + fileName;
		File wFile = new File(extractFilePath);
		FileOutputStream fos = new FileOutputStream(wFile);
		DataOutputStream dos = new DataOutputStream(fos);
		dos.write(fileBuff);
		dos.close();
		fos.close();
	}

	/**
	 * 从pak文件中取出指定的文件到byte数组，如果需要的话可以将byte数组写为文件
	 * 
	 * @param pakFilePath
	 *            pak文件路径绝对路径
	 * @param extractFileName
	 *            pak文件中将要被取出的文件名
	 * @param writeFile
	 *            是否需要将byte数组写为文件
	 * @param extractDir
	 *            如果需要的话可以将byte数组写为文件，extractDir为取出数据被写的目录文件
	 * @return byte数组
	 * @throws Exception
	 */
	public byte[] extractFileFromAbsolutePath(String pakFilePath,
			String extractFileName, boolean writeFile, String extractDir)
			throws Exception {
		File rFile = new File(pakFilePath);
		FileInputStream fis = new FileInputStream(rFile);
		DataInputStream dis = new DataInputStream(fis);
		PakHeader header = readHeader(dis);
		PakTable[] fileTable = readFileTable(dis,
				(int) header.getNumFileTableEntries());
		boolean find = false;
		int fileIndex = 0;
		for (int i = 0; i < fileTable.length; i++) {
			String fileName = new String(fileTable[i].getFileName()).trim();
			if (fileName.equals(extractFileName)) {
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
			if (writeFile) {
				writeFileFromByteBuffer(buff, extractFileName, extractDir);
			} else {
				dis.close();
				fis.close();
			}
			return buff;
		}
	}

	/**
	 * 提取文件从相对路径中
	 * 
	 * @param pakFilePath
	 * @param extractFileName
	 * @param writeFile
	 * @param extractDir
	 * @return
	 * @throws Exception
	 */
	public byte[] extractFileFromRelativePath(String pakFilePath,
			String extractFileName, boolean writeFile, String extractDir)
			throws Exception {
		InputStream fis = this.getClass().getResourceAsStream(pakFilePath);
		DataInputStream dis = new DataInputStream(fis);
		PakHeader header = readHeader(dis);
		PakTable[] fileTable = readFileTable(dis,
				(int) header.getNumFileTableEntries());
		boolean find = false;
		int fileIndex = 0;
		for (int i = 0; i < fileTable.length; i++) {
			String fileName = new String(fileTable[i].getFileName()).trim();
			if (fileName.equals(extractFileName)) {
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
			if (writeFile) {
				writeFileFromByteBuffer(buff, extractFileName, extractDir);
			} else {
				dis.close();
				fis.close();
			}
			return buff;
		}
	}

	/**
	 * 从流中提取指定文件名的资源
	 * 
	 * @param stream
	 * @param extractFileName
	 * @param writeFile
	 * @param extractDir
	 * @return
	 * @throws Exception
	 */
	public byte[] extractFileFromStream(InputStream stream,
			String extractFileName, boolean writeFile, String extractDir)
			throws Exception {
		DataInputStream dis = new DataInputStream(stream);
		PakHeader header = readHeader(dis);
		PakTable[] fileTable = readFileTable(dis,
				(int) header.getNumFileTableEntries());
		boolean find = false;
		int fileIndex = 0;
		for (int i = 0; i < fileTable.length; i++) {
			String fileName = new String(fileTable[i].getFileName()).trim();
			if (fileName.equals(extractFileName)) {
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
			if (writeFile) {
				writeFileFromByteBuffer(buff, extractFileName, extractDir);
			} else {
				dis.close();
				stream.close();
			}
			return buff;
		}
	}

	/**
	 * 从绝对路径中提取资源
	 * 
	 * @param pakFilePath
	 * @return
	 * @throws Exception
	 */
	public Vector showPakFromAbsolutePath(String pakFilePath) throws Exception {
		FileInputStream fis = new FileInputStream(new File(pakFilePath));
		DataInputStream dis = new DataInputStream(fis);
		PakHeader header = readHeader(dis);
		PakTable[] fileTable = readFileTable(dis,
				(int) header.getNumFileTableEntries());
		Vector result = new Vector();
		result.add(header);
		result.add(fileTable);
		return result;
	}

	/**
	 * 从pak文件中取出指定的Pak文件的信息
	 * 
	 * @param pakFilePath
	 *            pak在项目中的相对路径
	 * @return 装载文件头和文件table数组的Vector
	 * @throws Exception
	 */
	public Vector showPakFromRelativePath(String pakFilePath) throws Exception {
		InputStream fis = this.getClass().getResourceAsStream(pakFilePath);
		DataInputStream dis = new DataInputStream(fis);
		PakHeader header = readHeader(dis);
		PakTable[] fileTable = readFileTable(dis,
				(int) header.getNumFileTableEntries());
		Vector result = new Vector();
		result.add(header);
		result.add(fileTable);
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
		result.add(header);
		result.add(fileTable);
		return result;
	}

	/**
	 * 创建pak到指定文件
	 * 
	 * @param imagesDir
	 *            图片目录
	 * @param suffixName
	 *            后缀名
	 * @param dstFileName
	 *            目标文件
	 */
	public static void createPakToFile(String imagesDir, String ext,
			String dstFileName) {
		try {
			PakWriter writer = new PakWriter();
			// 构造文件头
			char[] signature = new char[PakHeader.SIGNATURE_LENGTH];
			signature = new String(SIGNATURE).toCharArray();
			char[] uniqueID = new char[PakHeader.UNIQUEID_LENGTH];
			uniqueID = new String(UUID).toCharArray();
			PakHeader header = new PakHeader();
			header.setSignature(signature);

			header.setCipherAction((byte) PakHeader.ADDITION_CIPHERACTION);
			header.setCipherValue((byte) 0x0f);
			header.setUniqueID(uniqueID);
			header.setVersion(1.0f);
			header.setReserved(0L);
			ArrayList<File> frr = listFile(new File(imagesDir), ext);
			header.setNumFileTableEntries(frr.size());
			String extractFilePath = dstFileName;
			// 创建压缩文件
			writer.makePakFile(frr, extractFilePath, header);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 迭代指定目录下的所有文件
	 * 
	 * @param dir
	 * @return
	 */
	public static ArrayList<File> listAllFile(File dir) {
		File[] files = dir.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.isFile();
			}
		});
		return new ArrayList<File>(Arrays.asList(files));
	}

	/**
	 * 迭代指定目录下指定后缀名的文件
	 * 
	 * @param dir
	 * @param ext
	 * @return
	 */
	public static ArrayList<File> listFile(File dir, final String ext) {
		File[] files = dir.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				String name = pathname.getName();
				boolean bool = name.endsWith(ext.toLowerCase())
						| name.endsWith(ext.toUpperCase());
				return pathname.isFile() && bool;
			}
		});
		return new ArrayList<File>(Arrays.asList(files));
	}

	/**
	 * 从文件系统绝对路径中提取资源
	 * 
	 * @param relaPath
	 * @param restoreDir
	 */
	public static void extractFromAbsoluteToDir(String absoPath,
			String restoreDir) {
		try {
			PakWriter writer = new PakWriter();
			// 构造文件头
			char[] signature = new char[PakHeader.SIGNATURE_LENGTH];
			signature = new String(SIGNATURE).toCharArray();
			char[] uniqueID = new char[PakHeader.UNIQUEID_LENGTH];
			uniqueID = new String(UUID).toCharArray();
			PakHeader header = new PakHeader();
			header.setSignature(signature);

			header.setCipherAction((byte) PakHeader.ADDITION_CIPHERACTION);
			header.setCipherValue((byte) 0x0f);
			header.setUniqueID(uniqueID);
			header.setVersion(1.0f);
			header.setReserved(0L);
			Vector pakInfo = writer.showPakFromAbsolutePath(absoPath);
			header = (PakHeader) pakInfo.elementAt(0);
			PakTable[] fileTable = (PakTable[]) pakInfo.elementAt(1);
			/*
			 * for (int i = 0; i < fileTable.length; i++) {
			 * System.out.println("文件table[" + i + "]:" + fileTable[i]); }
			 */
			String restoreFileName = null;
			byte[] fileBuff = null;
			for (int i = 0; i < fileTable.length; i++) {
				restoreFileName = new String(fileTable[i].getFileName()).trim();
				/* System.out.println("从Pak文件中取出" + restoreFileName + "文件..."); */
				fileBuff = writer.extractFileFromAbsolutePath(absoPath,
						restoreFileName, true, restoreDir);
				/*
				 * System.out.println("从Pak文件中取出" + restoreFileName + "文件保存在" +
				 * restoreDir + "目录");
				 */
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 提取文件到指定目录
	 * 
	 * @param extractFilePath
	 *            文件路径
	 * @param restoreDir
	 *            恢复目录
	 */
	public static void extractFromRelativeToDir(String extractFilePath,
			String restoreDir) {
		try {
			PakWriter writer = new PakWriter();
			// 构造文件头
			char[] signature = new char[PakHeader.SIGNATURE_LENGTH];
			signature = new String(SIGNATURE).toCharArray();
			char[] uniqueID = new char[PakHeader.UNIQUEID_LENGTH];
			uniqueID = new String(UUID).toCharArray();
			PakHeader header = new PakHeader();
			header.setSignature(signature);

			header.setCipherAction((byte) PakHeader.ADDITION_CIPHERACTION);
			header.setCipherValue((byte) 0x0f);
			header.setUniqueID(uniqueID);
			header.setVersion(1.0f);
			header.setReserved(0L);
			Vector pakInfo = writer.showPakFromRelativePath(extractFilePath);
			header = (PakHeader) pakInfo.elementAt(0);
			PakTable[] fileTable = (PakTable[]) pakInfo.elementAt(1);
			/*
			 * for (int i = 0; i < fileTable.length; i++) {
			 * System.out.println("文件table[" + i + "]:" + fileTable[i]); }
			 */
			String restoreFileName = null;
			byte[] fileBuff = null;
			for (int i = 0; i < fileTable.length; i++) {
				restoreFileName = new String(fileTable[i].getFileName()).trim();
				/* System.out.println("从Pak文件中取出" + restoreFileName + "文件..."); */
				fileBuff = writer.extractFileFromRelativePath(extractFilePath,
						restoreFileName, true, restoreDir);
				/*
				 * System.out.println("从Pak文件中取出" + restoreFileName + "文件保存在" +
				 * restoreDir + "目录");
				 */
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}