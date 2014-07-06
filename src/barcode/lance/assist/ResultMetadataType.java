package barcode.lance.assist;

import java.util.Hashtable;

public final class ResultMetadataType {

	// No, we can't use an enum here. J2ME doesn't support it.

	private static final Hashtable VALUES = new Hashtable();

	/**
	 * Unspecified, application-specific metadata. Maps to an unspecified
	 * {@link Object}.
	 */
	public static final ResultMetadataType OTHER = new ResultMetadataType(
			"OTHER");

	/**
	 * Denotes the likely approximate orientation of the barcode in the image.
	 * This value is given as degrees rotated clockwise from the normal, upright
	 * orientation. For example a 1D barcode which was found by reading
	 * top-to-bottom would be said to have orientation "90". This key maps to an
	 * {@link Integer} whose value is in the range [0,360).
	 */
	public static final ResultMetadataType ORIENTATION = new ResultMetadataType(
			"ORIENTATION");

	/**
	 * <p>
	 * 2D barcode formats typically encode text, but allow for a sort of 'byte
	 * mode' which is sometimes used to encode binary data. While {@link Result}
	 * makes available the complete raw bytes in the barcode for these formats,
	 * it does not offer the bytes from the byte segments alone.
	 * </p>
	 * 
	 * <p>
	 * This maps to a {@link java.util.Vector} of byte arrays corresponding to
	 * the raw bytes in the byte segments in the barcode, in order.
	 * </p>
	 */
	public static final ResultMetadataType BYTE_SEGMENTS = new ResultMetadataType(
			"BYTE_SEGMENTS");

	/**
	 * Error correction level used, if applicable. The value type depends on the
	 * format, but is typically a String.
	 */
	public static final ResultMetadataType ERROR_CORRECTION_LEVEL = new ResultMetadataType(
			"ERROR_CORRECTION_LEVEL");

	/**
	 * For some periodicals, indicates the issue number as an {@link Integer}.
	 */
	public static final ResultMetadataType ISSUE_NUMBER = new ResultMetadataType(
			"ISSUE_NUMBER");

	/**
	 * For some products, indicates the suggested retail price in the barcode as
	 * a formatted {@link String}.
	 */
	public static final ResultMetadataType SUGGESTED_PRICE = new ResultMetadataType(
			"SUGGESTED_PRICE");

	/**
	 * For some products, the possible country of manufacture as a
	 * {@link String} denoting the ISO country code. Some map to multiple
	 * possible countries, like "US/CA".
	 */
	public static final ResultMetadataType POSSIBLE_COUNTRY = new ResultMetadataType(
			"POSSIBLE_COUNTRY");

	private final String name;

	private ResultMetadataType(String name) {
		this.name = name;
		VALUES.put(name, this);
	}

	public String getName() {
		return name;
	}

	public String toString() {
		return name;
	}

	public static ResultMetadataType valueOf(String name) {
		if (name == null || name.length() == 0) {
			throw new IllegalArgumentException();
		}
		ResultMetadataType format = (ResultMetadataType) VALUES.get(name);
		if (format == null) {
			throw new IllegalArgumentException();
		}
		return format;
	}

}
