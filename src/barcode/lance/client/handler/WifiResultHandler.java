package barcode.lance.client.handler;

import android.app.Activity;
import barcode.lance.result.ParsedResult;
import barcode.lance.result.WifiParsedResult;
import static barcode.lance.client.common.StringContant.*;

/**
 * Handles address book entries.
 * 
 * @author viki@google.com (Vikram Aggarwal)
 */
public final class WifiResultHandler extends ResultHandler {
	private String title = HandlerTitles.WIFI_TITLE;
	private final Activity parent;

	public WifiResultHandler(Activity activity, ParsedResult result) {
		super(activity, result);
		parent = activity;
	}

	@Override
	public int getButtonCount() {
		// We just need one button, and that is to configure the wireless. This
		// could change in the future.
		return 1;
	}

	@Override
	public String getButtonText(int index) {
		if (index == 0) {
			return CONNECT_NETWORK;
		}
		throw new ArrayIndexOutOfBoundsException();
	}

	@Override
	public void handleButtonPress(int index) {
		// Get the underlying wifi config
		WifiParsedResult wifiResult = (WifiParsedResult) getResult();
		if (index == 0) {
			wifiConnect(wifiResult);
		}
	}

	// Display the name of the network and the network type to the user.
	@Override
	public CharSequence getDisplayContents() {
		WifiParsedResult wifiResult = (WifiParsedResult) getResult();
		StringBuffer contents = new StringBuffer(50);
		String wifiLabel = NET_SSID;
		ParsedResult.maybeAppend(wifiLabel + '\n' + wifiResult.getSsid(),
				contents);
		String typeLabel = TYPE;
		ParsedResult.maybeAppend(
				typeLabel + '\n' + wifiResult.getNetworkEncryption(), contents);
		return contents.toString();
	}

	@Override
	public String getDisplayTitle() {
		return title;
	}

	@Override
	public void setTitle(String title) {
		this.title = title;
	}
}