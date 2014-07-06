package barcode.lance.client.handler;

import android.app.Activity;
import android.telephony.PhoneNumberUtils;
import barcode.lance.result.ParsedResult;
import barcode.lance.result.SMSParsedResult;
import static barcode.lance.client.common.StringContant.*;
/**
 * Handles SMS addresses, offering a choice of composing a new SMS or MMS
 * message.
 * 
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class SMSResultHandler extends ResultHandler {
	private String title = HandlerTitles.SMS_TITLE;
	private static final String[] buttons = { SEND_SMS, SEND_MMS};

	public SMSResultHandler(Activity activity, ParsedResult result) {
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
		SMSParsedResult smsResult = (SMSParsedResult) getResult();
		switch (index) {
		case 0:
			// Don't know of a way yet to express a SENDTO intent with multiple
			// recipients
			sendSMS(smsResult.getNumbers()[0], smsResult.getBody());
			break;
		case 1:
			sendMMS(smsResult.getNumbers()[0], smsResult.getSubject(),
					smsResult.getBody());
			break;
		}
	}

	@Override
	public CharSequence getDisplayContents() {
		SMSParsedResult smsResult = (SMSParsedResult) getResult();
		StringBuffer contents = new StringBuffer(50);
		String[] rawNumbers = smsResult.getNumbers();
		String[] formattedNumbers = new String[rawNumbers.length];
		for (int i = 0; i < rawNumbers.length; i++) {
			formattedNumbers[i] = PhoneNumberUtils.formatNumber(rawNumbers[i]);
		}
		ParsedResult.maybeAppend(formattedNumbers, contents);
		ParsedResult.maybeAppend(smsResult.getSubject(), contents);
		ParsedResult.maybeAppend(smsResult.getBody(), contents);
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
