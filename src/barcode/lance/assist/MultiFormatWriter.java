package barcode.lance.assist;

import java.util.Hashtable;

import barcode.lance.common.BitMatrix;
import barcode.lance.qrcode.QRCodeWriter;
import barcode.lance.oned.Code128Writer;
import barcode.lance.oned.Code39Writer;
import barcode.lance.oned.EAN13Writer;
import barcode.lance.oned.EAN8Writer;
import barcode.lance.oned.ITFWriter;
import barcode.lance.oned.UPCAWriter;

/**
 * This is a factory class which finds the appropriate Writer subclass for the
 * BarcodeFormat requested and encodes the barcode with the supplied contents.
 * 
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class MultiFormatWriter implements Writer {

	public BitMatrix encode(String contents, BarcodeFormat format, int width,
			int height) throws WriterException {

		return encode(contents, format, width, height, null);
	}

	public BitMatrix encode(String contents, BarcodeFormat format, int width,
			int height, Hashtable hints) throws WriterException {

		Writer writer;
		if (format == BarcodeFormat.EAN_8) {
			writer = new EAN8Writer();
		} else if (format == BarcodeFormat.EAN_13) {
			writer = new EAN13Writer();
		} else if (format == BarcodeFormat.UPC_A) {
			writer = new UPCAWriter();
		} else if (format == BarcodeFormat.QR_CODE) {
			writer = new QRCodeWriter();
		} else if (format == BarcodeFormat.CODE_39) {
			writer = new Code39Writer();
		} else if (format == BarcodeFormat.CODE_128) {
			writer = new Code128Writer();
		} else if (format == BarcodeFormat.ITF) {
			writer = new ITFWriter();
		} else {
			throw new IllegalArgumentException(
					"No encoder available for format " + format);
		}
		return writer.encode(contents, format, width, height, hints);
	}

}
