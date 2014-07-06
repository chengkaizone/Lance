package barcode.lance.result;

import java.util.Hashtable;

import barcode.lance.assist.Result;

/**
 * Represents a result that encodes an e-mail address, either as a plain address
 * like "joe@example.org" or a mailto: URL like "mailto:joe@example.org".
 * 
 * @author Sean Owen
 */
final class EmailAddressResultParser extends ResultParser {

	public static EmailAddressParsedResult parse(Result result) {
		String rawText = result.getText();
		if (rawText == null) {
			return null;
		}
		String emailAddress;
		if (rawText.startsWith("mailto:") || rawText.startsWith("MAILTO:")) {
			// If it starts with mailto:, assume it is definitely trying to be
			// an email address
			emailAddress = rawText.substring(7);
			int queryStart = emailAddress.indexOf('?');
			if (queryStart >= 0) {
				emailAddress = emailAddress.substring(0, queryStart);
			}
			Hashtable nameValues = parseNameValuePairs(rawText);
			String subject = null;
			String body = null;
			if (nameValues != null) {
				if (emailAddress.length() == 0) {
					emailAddress = (String) nameValues.get("to");
				}
				subject = (String) nameValues.get("subject");
				body = (String) nameValues.get("body");
			}
			return new EmailAddressParsedResult(emailAddress, subject, body,
					rawText);
		} else {
			if (!EmailDoCoMoResultParser.isBasicallyValidEmailAddress(rawText)) {
				return null;
			}
			emailAddress = rawText;
			return new EmailAddressParsedResult(emailAddress, null, null,
					"mailto:" + emailAddress);
		}
	}

}