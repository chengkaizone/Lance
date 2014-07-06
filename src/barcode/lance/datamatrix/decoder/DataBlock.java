package barcode.lance.datamatrix.decoder;

/**
 * <p>
 * Encapsulates a block of data within a Data Matrix Code. Data Matrix Codes may
 * split their data into multiple blocks, each of which is a unit of data and
 * error-correction codewords. Each is represented by an instance of this class.
 * </p>
 * 
 * @author bbrown@google.com (Brian Brown)
 */
final class DataBlock {

	private final int numDataCodewords;
	private final byte[] codewords;

	private DataBlock(int numDataCodewords, byte[] codewords) {
		this.numDataCodewords = numDataCodewords;
		this.codewords = codewords;
	}

	/**
	 * <p>
	 * When Data Matrix Codes use multiple data blocks, they actually interleave
	 * the bytes of each of them. That is, the first byte of data block 1 to n
	 * is written, then the second bytes, and so on. This method will separate
	 * the data into original blocks.
	 * </p>
	 * 
	 * @param rawCodewords
	 *            bytes as read directly from the Data Matrix Code
	 * @param version
	 *            version of the Data Matrix Code
	 * @return {@link DataBlock}s containing original bytes, "de-interleaved"
	 *         from representation in the Data Matrix Code
	 */
	static DataBlock[] getDataBlocks(byte[] rawCodewords, Version version) {
		// Figure out the number and size of data blocks used by this version
		Version.ECBlocks ecBlocks = version.getECBlocks();

		// First count the total number of data blocks
		int totalBlocks = 0;
		Version.ECB[] ecBlockArray = ecBlocks.getECBlocks();
		for (int i = 0; i < ecBlockArray.length; i++) {
			totalBlocks += ecBlockArray[i].getCount();
		}

		// Now establish DataBlocks of the appropriate size and number of data
		// codewords
		DataBlock[] result = new DataBlock[totalBlocks];
		int numResultBlocks = 0;
		for (int j = 0; j < ecBlockArray.length; j++) {
			Version.ECB ecBlock = ecBlockArray[j];
			for (int i = 0; i < ecBlock.getCount(); i++) {
				int numDataCodewords = ecBlock.getDataCodewords();
				int numBlockCodewords = ecBlocks.getECCodewords()
						+ numDataCodewords;
				result[numResultBlocks++] = new DataBlock(numDataCodewords,
						new byte[numBlockCodewords]);
			}
		}

		// All blocks have the same amount of data, except that the last n
		// (where n may be 0) have 1 less byte. Figure out where these start.
		// (bbrown): There is only one case where there is a difference for
		// Data Matrix for size 144
		int longerBlocksTotalCodewords = result[0].codewords.length;
		// int shorterBlocksTotalCodewords = longerBlocksTotalCodewords - 1;

		int longerBlocksNumDataCodewords = longerBlocksTotalCodewords
				- ecBlocks.getECCodewords();
		int shorterBlocksNumDataCodewords = longerBlocksNumDataCodewords - 1;
		// The last elements of result may be 1 element shorter for 144 matrix
		// first fill out as many elements as all of them have minus 1
		int rawCodewordsOffset = 0;
		for (int i = 0; i < shorterBlocksNumDataCodewords; i++) {
			for (int j = 0; j < numResultBlocks; j++) {
				result[j].codewords[i] = rawCodewords[rawCodewordsOffset++];
			}
		}

		// Fill out the last data block in the longer ones
		boolean specialVersion = version.getVersionNumber() == 24;
		int numLongerBlocks = specialVersion ? 8 : numResultBlocks;
		for (int j = 0; j < numLongerBlocks; j++) {
			result[j].codewords[longerBlocksNumDataCodewords - 1] = rawCodewords[rawCodewordsOffset++];
		}

		// Now add in error correction blocks
		int max = result[0].codewords.length;
		for (int i = longerBlocksNumDataCodewords; i < max; i++) {
			for (int j = 0; j < numResultBlocks; j++) {
				int iOffset = (specialVersion && j > 7) ? i - 1 : i;
				result[j].codewords[iOffset] = rawCodewords[rawCodewordsOffset++];
			}
		}

		if (rawCodewordsOffset != rawCodewords.length) {
			throw new IllegalArgumentException();
		}

		return result;
	}

	int getNumDataCodewords() {
		return numDataCodewords;
	}

	byte[] getCodewords() {
		return codewords;
	}

}
