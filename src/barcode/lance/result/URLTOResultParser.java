package barcode.lance.result;

import barcode.lance.assist.Result;

/**
 * Parses the "URLTO" result format, which is of the form "URLTO:[title]:[url]".
 * This seems to be used sometimes, but I am not able to find documentation on
 * its origin or official format?
 * 
 * @author Sean Owen
 */
final class URLTOResultParser {

	private URLTOResultParser() {
	}

	public static URIParsedResult parse(Result result) {
		String rawText = result.getText();
		if (rawText == null
				|| (!rawText.startsWith("urlto:") && !rawText
						.startsWith("URLTO:"))) {
			return null;
		}
		int titleEnd = rawText.indexOf(':', 6);
		if (titleEnd < 0) {
			return null;
		}
		String title = titleEnd <= 6 ? null : rawText.substring(6, titleEnd);
		String uri = rawText.substring(titleEnd + 1);
		return new URIParsedResult(uri, title);
	}

}