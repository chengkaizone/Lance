package barcode.lance.result;

import barcode.lance.assist.Result;

/**
 * Parses a WIFI configuration string. Strings will be of the form:
 * WIFI:T:WPA;S:mynetwork;P:mypass;;
 * 
 * The fields can come in any order, and there should be tests to see if we can
 * parse them all correctly.
 * 
 * @author Vikram Aggarwal
 */
final class WifiResultParser extends ResultParser {

	private WifiResultParser() {
	}

	public static WifiParsedResult parse(Result result) {
		String rawText = result.getText();

		if (rawText == null || !rawText.startsWith("WIFI:")) {
			return null;
		}

		// Don't remove leading or trailing whitespace
		boolean trim = false;
		String ssid = matchSinglePrefixedField("S:", rawText, ';', trim);
		String pass = matchSinglePrefixedField("P:", rawText, ';', trim);
		String type = matchSinglePrefixedField("T:", rawText, ';', trim);

		return new WifiParsedResult(type, ssid, pass);
	}
}