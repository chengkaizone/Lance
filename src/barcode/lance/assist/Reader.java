package barcode.lance.assist;

import java.util.Hashtable;

public interface Reader {

	public Result decode(BinaryBitmap image) throws NotFoundException,
			ChecksumException, FormatException;

	public Result decode(BinaryBitmap image, Hashtable hints)
			throws NotFoundException, ChecksumException, FormatException;

	public void reset();

}