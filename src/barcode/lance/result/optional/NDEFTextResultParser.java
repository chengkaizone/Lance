package barcode.lance.result.optional;

import barcode.lance.assist.Result;
import barcode.lance.result.TextParsedResult;

/**
 * Recognizes an NDEF message that encodes text according to the
 * "Text Record Type Definition" specification.
 * 
 * @author Sean Owen
 */
final class NDEFTextResultParser extends AbstractNDEFResultParser {

	public static TextParsedResult parse(Result result) {
		byte[] bytes = result.getRawBytes();
		if (bytes == null) {
			return null;
		}
		NDEFRecord ndefRecord = NDEFRecord.readRecord(bytes, 0);
		if (ndefRecord == null || !ndefRecord.isMessageBegin()
				|| !ndefRecord.isMessageEnd()) {
			return null;
		}
		if (!ndefRecord.getType().equals(NDEFRecord.TEXT_WELL_KNOWN_TYPE)) {
			return null;
		}
		String[] languageText = decodeTextPayload(ndefRecord.getPayload());
		return new TextParsedResult(languageText[0], languageText[1]);
	}

	static String[] decodeTextPayload(byte[] payload) {
		byte statusByte = payload[0];
		boolean isUTF16 = (statusByte & 0x80) != 0;
		int languageLength = statusByte & 0x1F;
		// language is always ASCII-encoded:
		String language = bytesToString(payload, 1, languageLength, "US-ASCII");
		String encoding = isUTF16 ? "UTF-16" : "UTF8";
		String text = bytesToString(payload, 1 + languageLength, payload.length
				- languageLength - 1, encoding);
		return new String[] { language, text };
	}

}