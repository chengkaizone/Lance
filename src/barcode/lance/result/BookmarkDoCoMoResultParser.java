package barcode.lance.result;

import barcode.lance.assist.Result;

/**
 * @author Sean Owen
 */
final class BookmarkDoCoMoResultParser extends AbstractDoCoMoResultParser {

	private BookmarkDoCoMoResultParser() {
	}

	public static URIParsedResult parse(Result result) {
		String rawText = result.getText();
		if (rawText == null || !rawText.startsWith("MEBKM:")) {
			return null;
		}
		String title = matchSingleDoCoMoPrefixedField("TITLE:", rawText, true);
		String[] rawUri = matchDoCoMoPrefixedField("URL:", rawText, true);
		if (rawUri == null) {
			return null;
		}
		String uri = rawUri[0];
		if (!URIResultParser.isBasicallyValidURI(uri)) {
			return null;
		}
		return new URIParsedResult(uri, title);
	}

}