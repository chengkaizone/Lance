package barcode.lance.assist;

/**
 * Thrown when a barcode was successfully detected and decoded, but was not
 * returned because its checksum feature failed.
 * 
 * @author Sean Owen
 */
public final class ChecksumException extends ReaderException {

	private static final ChecksumException instance = new ChecksumException();

	private ChecksumException() {
		// do nothing
	}

	public static ChecksumException getChecksumInstance() {
		return instance;
	}

}