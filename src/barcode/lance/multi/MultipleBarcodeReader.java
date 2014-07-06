package barcode.lance.multi;

import java.util.Hashtable;

import barcode.lance.assist.BinaryBitmap;
import barcode.lance.assist.NotFoundException;
import barcode.lance.assist.Result;

/**
 * Implementation of this interface attempt to read several barcodes from one
 * image.
 * 
 * @see com.google.zxing.Reader
 * @author Sean Owen
 */
public interface MultipleBarcodeReader {

	public Result[] decodeMultiple(BinaryBitmap image) throws NotFoundException;

	public Result[] decodeMultiple(BinaryBitmap image, Hashtable hints)
			throws NotFoundException;

}