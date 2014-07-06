package barcode.lance.client.handler;

import android.app.Activity;
import android.telephony.PhoneNumberUtils;
import barcode.lance.result.ParsedResult;
import barcode.lance.result.TelParsedResult;
import static barcode.lance.client.common.StringContant.*;

/**
 * Offers relevant actions for telephone numbers.
 * 
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class TelResultHandler extends ResultHandler {
	private String title = HandlerTitles.TEL_TITLE;
	private static final String[] buttons = { DIALING, ADD_CONTACT };

	public TelResultHandler(Activity activity, ParsedResult result) {
		super(activity, result);
	}

	@Override
	public int getButtonCount() {
		return buttons.length;
	}

	@Override
	public String getButtonText(int index) {
		return buttons[index];
	}

	@Override
	public void handleButtonPress(int index) {
		TelParsedResult telResult = (TelParsedResult) getResult();
		switch (index) {
		case 0:
			dialPhoneFromUri(telResult.getTelURI());
			break;
		case 1:
			String[] numbers = new String[1];
			numbers[0] = telResult.getNumber();
			addContact(null, numbers, null, null, null, null, null);
			break;
		}
	}

	// Overriden so we can take advantage of Android's phone number hyphenation
	// routines.
	@Override
	public CharSequence getDisplayContents() {
		String contents = getResult().getDisplayResult();
		contents = contents.replace("\r", "");
		return PhoneNumberUtils.formatNumber(contents);
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
