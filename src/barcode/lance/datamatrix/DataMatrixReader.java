package barcode.lance.datamatrix;

import java.util.Hashtable;

import barcode.lance.assist.BarcodeFormat;
import barcode.lance.assist.BinaryBitmap;
import barcode.lance.assist.ChecksumException;
import barcode.lance.assist.DecodeHintType;
import barcode.lance.assist.FormatException;
import barcode.lance.assist.NotFoundException;
import barcode.lance.assist.Reader;
import barcode.lance.assist.Result;
import barcode.lance.assist.ResultMetadataType;
import barcode.lance.assist.ResultPoint;
import barcode.lance.common.BitMatrix;
import barcode.lance.common.DecoderResult;
import barcode.lance.common.DetectorResult;
import barcode.lance.datamatrix.decoder.DataDecoder;
import barcode.lance.datamatrix.detector.DataDetector;

/**
 * This implementation can detect and decode Data Matrix codes in an image.
 * 
 * @author bbrown@google.com (Brian Brown)
 */
public final class DataMatrixReader implements Reader {

	private static final ResultPoint[] NO_POINTS = new ResultPoint[0];

	private final DataDecoder decoder = new DataDecoder();

	/**
	 * Locates and decodes a Data Matrix code in an image.
	 * 
	 * @return a String representing the content encoded by the Data Matrix code
	 * @throws NotFoundException
	 *             if a Data Matrix code cannot be found
	 * @throws FormatException
	 *             if a Data Matrix code cannot be decoded
	 * @throws ChecksumException
	 *             if error correction fails
	 */
	public Result decode(BinaryBitmap image) throws NotFoundException,
			ChecksumException, FormatException {
		return decode(image, null);
	}

	public Result decode(BinaryBitmap image, Hashtable hints)
			throws NotFoundException, ChecksumException, FormatException {
		DecoderResult decoderResult;
		ResultPoint[] points;
		if (hints != null && hints.containsKey(DecodeHintType.PURE_BARCODE)) {
			BitMatrix bits = extractPureBits(image.getBlackMatrix());
			decoderResult = decoder.decode(bits);
			points = NO_POINTS;
		} else {
			DetectorResult detectorResult = new DataDetector(
					image.getBlackMatrix()).detect();
			decoderResult = decoder.decode(detectorResult.getBits());
			points = detectorResult.getPoints();
		}
		Result result = new Result(decoderResult.getText(),
				decoderResult.getRawBytes(), points, BarcodeFormat.DATA_MATRIX);
		if (decoderResult.getByteSegments() != null) {
			result.putMetadata(ResultMetadataType.BYTE_SEGMENTS,
					decoderResult.getByteSegments());
		}
		if (decoderResult.getECLevel() != null) {
			result.putMetadata(ResultMetadataType.ERROR_CORRECTION_LEVEL,
					decoderResult.getECLevel().toString());
		}
		return result;
	}

	public void reset() {
		// do nothing
	}

	/**
	 * This method detects a code in a "pure" image -- that is, pure monochrome
	 * image which contains only an unrotated, unskewed, image of a code, with
	 * some white border around it. This is a specialized method that works
	 * exceptionally fast in this special case.
	 * 
	 * @see barcode.lance.pdf417.google.zxing.pdf417.PDF417Reader#extractPureBits(BitMatrix)
	 * @see com.google.zxing.qrcode.QRCodeReader#extractPureBits(BitMatrix)
	 */
	private static BitMatrix extractPureBits(BitMatrix image)
			throws NotFoundException {

		int[] leftTopBlack = image.getTopLeftOnBit();
		int[] rightBottomBlack = image.getBottomRightOnBit();
		if (leftTopBlack == null || rightBottomBlack == null) {
			throw NotFoundException.getNotFoundInstance();
		}

		int moduleSize = moduleSize(leftTopBlack, image);

		int top = leftTopBlack[1];
		int bottom = rightBottomBlack[1];
		int left = leftTopBlack[0];
		int right = rightBottomBlack[0];

		int matrixWidth = (right - left + 1) / moduleSize;
		int matrixHeight = (bottom - top + 1) / moduleSize;
		if (matrixWidth == 0 || matrixHeight == 0) {
			throw NotFoundException.getNotFoundInstance();
		}

		// Push in the "border" by half the module width so that we start
		// sampling in the middle of the module. Just in case the image is a
		// little off, this will help recover.
		int nudge = moduleSize >> 1;
		top += nudge;
		left += nudge;

		// Now just read off the bits
		BitMatrix bits = new BitMatrix(matrixWidth, matrixHeight);
		for (int y = 0; y < matrixHeight; y++) {
			int iOffset = top + y * moduleSize;
			for (int x = 0; x < matrixWidth; x++) {
				if (image.get(left + x * moduleSize, iOffset)) {
					bits.set(x, y);
				}
			}
		}
		return bits;
	}

	private static int moduleSize(int[] leftTopBlack, BitMatrix image)
			throws NotFoundException {
		int width = image.getWidth();
		int x = leftTopBlack[0];
		int y = leftTopBlack[1];
		while (x < width && image.get(x, y)) {
			x++;
		}
		if (x == width) {
			throw NotFoundException.getNotFoundInstance();
		}

		int moduleSize = x - leftTopBlack[0];
		if (moduleSize == 0) {
			throw NotFoundException.getNotFoundInstance();
		}
		return moduleSize;
	}

}