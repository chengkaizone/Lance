package barcode.lance.client.handler;

import android.app.Activity;
import barcode.lance.result.EmailAddressParsedResult;
import barcode.lance.result.ParsedResult;
import static barcode.lance.client.common.StringContant.*;

/**
 * Handles email addresses.
 * 
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class EmailAddressResultHandler extends ResultHandler {
	private String title = HandlerTitles.EMAIL_TITLE;
	private static final String[] buttons = { SEND_EMAIL, ADD_CONTACT };

	public EmailAddressResultHandler(Activity activity, ParsedResult result) {
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
		EmailAddressParsedResult emailResult = (EmailAddressParsedResult) getResult();
		switch (index) {
		case 0:
			sendEmailFromUri(emailResult.getMailtoURI(),
					emailResult.getEmailAddress(), emailResult.getSubject(),
					emailResult.getBody());
			break;
		case 1:
			String[] addresses = new String[1];
			addresses[0] = emailResult.getEmailAddress();
			addContact(null, null, addresses, null, null, null, null);
			break;
		}
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
