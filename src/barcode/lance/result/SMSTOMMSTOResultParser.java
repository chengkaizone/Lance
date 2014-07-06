package barcode.lance.result;

import barcode.lance.assist.Result;

/**
 * <p>
 * Parses an "smsto:" URI result, whose format is not standardized but appears
 * to be like: <code>smsto:number(:body)</code>.
 * </p>
 * 
 * <p>
 * This actually also parses URIs starting with "smsto:", "mmsto:", "SMSTO:",
 * and "MMSTO:", and treats them all the same way, and effectively converts them
 * to an "sms:" URI for purposes of forwarding to the platform.
 * </p>
 * 
 * @author Sean Owen
 */
final class SMSTOMMSTOResultParser extends ResultParser {

	private SMSTOMMSTOResultParser() {
	}

	public static SMSParsedResult parse(Result result) {
		String rawText = result.getText();
		if (rawText == null) {
			return null;
		}
		if (!(rawText.startsWith("smsto:") || rawText.startsWith("SMSTO:")
				|| rawText.startsWith("mmsto:") || rawText.startsWith("MMSTO:"))) {
			return null;
		}
		// Thanks to dominik.wild for suggesting this enhancement to support
		// smsto:number:body URIs
		String number = rawText.substring(6);
		String body = null;
		int bodyStart = number.indexOf(':');
		if (bodyStart >= 0) {
			body = number.substring(bodyStart + 1);
			number = number.substring(0, bodyStart);
		}
		return new SMSParsedResult(number, null, null, body);
	}

}