package barcode.lance.result;

/**
 * @author Sean Owen
 */
public final class SMSParsedResult extends ParsedResult {

	private final String[] numbers;
	private final String[] vias;
	private final String subject;
	private final String body;

	public SMSParsedResult(String number, String via, String subject,
			String body) {
		super(ParsedResultType.SMS);
		this.numbers = new String[] { number };
		this.vias = new String[] { via };
		this.subject = subject;
		this.body = body;
	}

	public SMSParsedResult(String[] numbers, String[] vias, String subject,
			String body) {
		super(ParsedResultType.SMS);
		this.numbers = numbers;
		this.vias = vias;
		this.subject = subject;
		this.body = body;
	}

	public String getSMSURI() {
		StringBuffer result = new StringBuffer();
		result.append("sms:");
		boolean first = true;
		for (int i = 0; i < numbers.length; i++) {
			if (first) {
				first = false;
			} else {
				result.append(',');
			}
			result.append(numbers[i]);
			if (vias[i] != null) {
				result.append(";via=");
				result.append(vias[i]);
			}
		}
		boolean hasBody = body != null;
		boolean hasSubject = subject != null;
		if (hasBody || hasSubject) {
			result.append('?');
			if (hasBody) {
				result.append("body=");
				result.append(body);
			}
			if (hasSubject) {
				if (hasBody) {
					result.append('&');
				}
				result.append("subject=");
				result.append(subject);
			}
		}
		return result.toString();
	}

	public String[] getNumbers() {
		return numbers;
	}

	public String[] getVias() {
		return vias;
	}

	public String getSubject() {
		return subject;
	}

	public String getBody() {
		return body;
	}

	public String getDisplayResult() {
		StringBuffer result = new StringBuffer(100);
		maybeAppend(numbers, result);
		maybeAppend(subject, result);
		maybeAppend(body, result);
		return result.toString();
	}

}