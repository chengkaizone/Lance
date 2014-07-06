package barcode.lance.result;

/**
 * @author Sean Owen
 */
public final class TelParsedResult extends ParsedResult {

	private final String number;
	private final String telURI;
	private final String title;

	public TelParsedResult(String number, String telURI, String title) {
		super(ParsedResultType.TEL);
		this.number = number;
		this.telURI = telURI;
		this.title = title;
	}

	public String getNumber() {
		return number;
	}

	public String getTelURI() {
		return telURI;
	}

	public String getTitle() {
		return title;
	}

	public String getDisplayResult() {
		StringBuffer result = new StringBuffer(20);
		maybeAppend(number, result);
		maybeAppend(title, result);
		return result.toString();
	}

}