package barcode.lance.result;

import barcode.lance.assist.Result;

/**
 * Implements the "MATMSG" email message entry format.
 * 
 * Supported keys: TO, SUB, BODY
 * 
 * @author Sean Owen
 */
final class EmailDoCoMoResultParser extends AbstractDoCoMoResultParser {

	private static final char[] ATEXT_SYMBOLS = { '@', '.', '!', '#', '$', '%',
			'&', '\'', '*', '+', '-', '/', '=', '?', '^', '_', '`', '{', '|',
			'}', '~' };

	public static EmailAddressParsedResult parse(Result result) {
		String rawText = result.getText();
		if (rawText == null || !rawText.startsWith("MATMSG:")) {
			return null;
		}
		String[] rawTo = matchDoCoMoPrefixedField("TO:", rawText, true);
		if (rawTo == null) {
			return null;
		}
		String to = rawTo[0];
		if (!isBasicallyValidEmailAddress(to)) {
			return null;
		}
		String subject = matchSingleDoCoMoPrefixedField("SUB:", rawText, false);
		String body = matchSingleDoCoMoPrefixedField("BODY:", rawText, false);
		return new EmailAddressParsedResult(to, subject, body, "mailto:" + to);
	}

	/**
	 * This implements only the most basic checking for an email address's
	 * validity -- that it contains an '@' contains no characters disallowed by
	 * RFC 2822. This is an overly lenient definition of validity. We want to
	 * generally be lenient here since this class is only intended to
	 * encapsulate what's in a barcode, not "judge" it.
	 */
	static boolean isBasicallyValidEmailAddress(String email) {
		if (email == null) {
			return false;
		}
		boolean atFound = false;
		for (int i = 0; i < email.length(); i++) {
			char c = email.charAt(i);
			if ((c < 'a' || c > 'z') && (c < 'A' || c > 'Z')
					&& (c < '0' || c > '9') && !isAtextSymbol(c)) {
				return false;
			}
			if (c == '@') {
				if (atFound) {
					return false;
				}
				atFound = true;
			}
		}
		return atFound;
	}

	private static boolean isAtextSymbol(char c) {
		for (int i = 0; i < ATEXT_SYMBOLS.length; i++) {
			if (c == ATEXT_SYMBOLS[i]) {
				return true;
			}
		}
		return false;
	}

}