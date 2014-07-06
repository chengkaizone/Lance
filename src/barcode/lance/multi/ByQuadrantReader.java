package barcode.lance.multi;

import java.util.Hashtable;

import barcode.lance.assist.BinaryBitmap;
import barcode.lance.assist.ChecksumException;
import barcode.lance.assist.FormatException;
import barcode.lance.assist.NotFoundException;
import barcode.lance.assist.Reader;
import barcode.lance.assist.Result;

/**
 * This class attempts to decode a barcode from an image, not by scanning the
 * whole image, but by scanning subsets of the image. This is important when
 * there may be multiple barcodes in an image, and detecting a barcode may find
 * parts of multiple barcode and fail to decode (e.g. QR Codes). Instead this
 * scans the four quadrants of the image -- and also the center 'quadrant' to
 * cover the case where a barcode is found in the center.
 * 
 * @see GenericMultipleBarcodeReader
 */
public final class ByQuadrantReader implements Reader {

	private final Reader delegate;

	public ByQuadrantReader(Reader delegate) {
		this.delegate = delegate;
	}

	public Result decode(BinaryBitmap image) throws NotFoundException,
			ChecksumException, FormatException {
		return decode(image, null);
	}

	public Result decode(BinaryBitmap image, Hashtable hints)
			throws NotFoundException, ChecksumException, FormatException {

		int width = image.getWidth();
		int height = image.getHeight();
		int halfWidth = width / 2;
		int halfHeight = height / 2;

		BinaryBitmap topLeft = image.crop(0, 0, halfWidth, halfHeight);
		try {
			return delegate.decode(topLeft, hints);
		} catch (NotFoundException re) {
			// continue
		}

		BinaryBitmap topRight = image.crop(halfWidth, 0, halfWidth, halfHeight);
		try {
			return delegate.decode(topRight, hints);
		} catch (NotFoundException re) {
			// continue
		}

		BinaryBitmap bottomLeft = image.crop(0, halfHeight, halfWidth,
				halfHeight);
		try {
			return delegate.decode(bottomLeft, hints);
		} catch (NotFoundException re) {
			// continue
		}

		BinaryBitmap bottomRight = image.crop(halfWidth, halfHeight, halfWidth,
				halfHeight);
		try {
			return delegate.decode(bottomRight, hints);
		} catch (NotFoundException re) {
			// continue
		}

		int quarterWidth = halfWidth / 2;
		int quarterHeight = halfHeight / 2;
		BinaryBitmap center = image.crop(quarterWidth, quarterHeight,
				halfWidth, halfHeight);
		return delegate.decode(center, hints);
	}

	public void reset() {
		delegate.reset();
	}

}
