package barcode.lance.result;

import barcode.lance.assist.Result;

/**
 * Parses a "tel:" URI result, which specifies a phone number.
 * 
 * @author Sean Owen
 */
final class TelResultParser extends ResultParser {

	private TelResultParser() {
	}

	public static TelParsedResult parse(Result result) {
		String rawText = result.getText();
		if (rawText == null
				|| (!rawText.startsWith("tel:") && !rawText.startsWith("TEL:"))) {
			return null;
		}
		// Normalize "TEL:" to "tel:"
		String telURI = rawText.startsWith("TEL:") ? "tel:"
				+ rawText.substring(4) : rawText;
		// Drop tel, query portion
		int queryStart = rawText.indexOf('?', 4);
		String number = queryStart < 0 ? rawText.substring(4) : rawText
				.substring(4, queryStart);
		return new TelParsedResult(number, telURI, null);
	}

}