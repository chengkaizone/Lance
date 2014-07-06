package barcode.lance.result;

/**
 * @author Vikram Aggarwal
 */
public final class WifiParsedResult extends ParsedResult {
	private final String ssid;
	private final String networkEncryption;
	private final String password;

	public WifiParsedResult(String networkEncryption, String ssid,
			String password) {
		super(ParsedResultType.WIFI);
		this.ssid = ssid;
		this.networkEncryption = networkEncryption;
		this.password = password;
	}

	public String getSsid() {
		return ssid;
	}

	public String getNetworkEncryption() {
		return networkEncryption;
	}

	public String getPassword() {
		return password;
	}

	public String getDisplayResult() {
		StringBuffer result = new StringBuffer(80);
		maybeAppend(ssid, result);
		maybeAppend(networkEncryption, result);
		maybeAppend(password, result);
		return result.toString();
	}
}