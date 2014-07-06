package barcode.lance.aztec;

import java.util.Hashtable;

import barcode.lance.aztec.decoder.AztecDecoder;
import barcode.lance.aztec.detector.AztecDetector;
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
import barcode.lance.assist.ResultPointCallback;
import barcode.lance.common.DecoderResult;

/**
 * This implementation can detect and decode Aztec codes in an image.
 * 
 * @author David Olivier
 */
public final class AztecReader implements Reader {

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
			FormatException {
		return decode(image, null);
	}

	public Result decode(BinaryBitmap image, Hashtable hints)
			throws NotFoundException, FormatException {

		AztecDetectorResult detectorResult = new AztecDetector(
				image.getBlackMatrix()).detect();
		ResultPoint[] points = detectorResult.getPoints();

		if (hints != null && detectorResult.getPoints() != null) {
			ResultPointCallback rpcb = (ResultPointCallback) hints
					.get(DecodeHintType.NEED_RESULT_POINT_CALLBACK);
			if (rpcb != null) {
				for (int i = 0; i < detectorResult.getPoints().length; i++) {
					rpcb.foundPossibleResultPoint(detectorResult.getPoints()[i]);
				}
			}
		}

		DecoderResult decoderResult = new AztecDecoder().decode(detectorResult);

		Result result = new Result(decoderResult.getText(),
				decoderResult.getRawBytes(), points, BarcodeFormat.AZTEC);

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

}