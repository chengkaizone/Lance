package barcode.lance.common;

import java.util.Vector;

import barcode.lance.qrcode.decoder.ErrorCorrectionLevel;

/**
 * <p>
 * Encapsulates the result of decoding a matrix of bits. This typically applies
 * to 2D barcode formats. For now it contains the raw bytes obtained, as well as
 * a String interpretation of those bytes, if applicable.
 * </p>
 * 
 * @author Sean Owen
 */
public final class DecoderResult {

	private final byte[] rawBytes;
	private final String text;
	private final Vector byteSegments;
	private final ErrorCorrectionLevel ecLevel;

	public DecoderResult(byte[] rawBytes, String text, Vector byteSegments,
			ErrorCorrectionLevel ecLevel) {
		if (rawBytes == null && text == null) {
			throw new IllegalArgumentException();
		}
		this.rawBytes = rawBytes;
		this.text = text;
		this.byteSegments = byteSegments;
		this.ecLevel = ecLevel;
	}

	public byte[] getRawBytes() {
		return rawBytes;
	}

	public String getText() {
		return text;
	}

	public Vector getByteSegments() {
		return byteSegments;
	}

	public ErrorCorrectionLevel getECLevel() {
		return ecLevel;
	}

}