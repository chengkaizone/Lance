package barcode.lance.multi.qrcode.detector;

import java.util.Hashtable;
import java.util.Vector;

import barcode.lance.assist.NotFoundException;
import barcode.lance.assist.ReaderException;
import barcode.lance.common.BitMatrix;
import barcode.lance.common.DetectorResult;
import barcode.lance.qrcode.detector.FinderPatternInfo;
import barcode.lance.qrcode.detector.QRDetector;

/**
 * <p>
 * Encapsulates logic that can detect one or more QR Codes in an image, even if
 * the QR Code is rotated or skewed, or partially obscured.
 * </p>
 * 
 * @author Sean Owen
 * @author Hannes Erven
 */
public final class MultiDetector extends QRDetector {

	private static final DetectorResult[] EMPTY_DETECTOR_RESULTS = new DetectorResult[0];

	public MultiDetector(BitMatrix image) {
		super(image);
	}

	public DetectorResult[] detectMulti(Hashtable hints)
			throws NotFoundException {
		BitMatrix image = getImage();
		MultiFinderPatternFinder finder = new MultiFinderPatternFinder(image);
		FinderPatternInfo[] info = finder.findMulti(hints);

		if (info == null || info.length == 0) {
			throw NotFoundException.getNotFoundInstance();
		}

		Vector result = new Vector();
		for (int i = 0; i < info.length; i++) {
			try {
				result.addElement(processFinderPatternInfo(info[i]));
			} catch (ReaderException e) {
				// ignore
			}
		}
		if (result.isEmpty()) {
			return EMPTY_DETECTOR_RESULTS;
		} else {
			DetectorResult[] resultArray = new DetectorResult[result.size()];
			for (int i = 0; i < result.size(); i++) {
				resultArray[i] = (DetectorResult) result.elementAt(i);
			}
			return resultArray;
		}
	}

}
