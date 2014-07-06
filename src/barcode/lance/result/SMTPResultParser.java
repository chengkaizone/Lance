package barcode.lance.result;

import barcode.lance.assist.Result;

/**
 * <p>
 * Parses an "smtp:" URI result, whose format is not standardized but appears to
 * be like: <code>smtp(:subject(:body))</code>.
 * </p>
 * 
 * <p>
 * See http://code.google.com/p/zxing/issues/detail?id=536
 * </p>
 * 
 * @author Sean Owen
 */
final class SMTPResultParser {

	private SMTPResultParser() {
	}

	public static EmailAddressParsedResult parse(Result result) {
		String rawText = result.getText();
		if (rawText == null) {
			return null;
		}
		if (!(rawText.startsWith("smtp:") || rawText.startsWith("SMTP:"))) {
			return null;
		}
		String emailAddress = rawText.substring(5);
		String subject = null;
		String body = null;
		int colon = emailAddress.indexOf(':');
		if (colon >= 0) {
			subject = emailAddress.substring(colon + 1);
			emailAddress = emailAddress.substring(0, colon);
			colon = subject.indexOf(':');
			if (colon >= 0) {
				body = subject.substring(colon + 1);
				subject = subject.substring(0, colon);
			}
		}
		String mailtoURI = "mailto:" + emailAddress;
		return new EmailAddressParsedResult(emailAddress, subject, body,
				mailtoURI);
	}
}
