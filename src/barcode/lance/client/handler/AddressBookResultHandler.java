package barcode.lance.client.handler;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.telephony.PhoneNumberUtils;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import barcode.lance.result.AddressBookParsedResult;
import barcode.lance.result.ParsedResult;
import static barcode.lance.client.common.StringContant.*;

/**
 * Handles address book entries.
 * 
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class AddressBookResultHandler extends ResultHandler {
	private String title = HandlerTitles.ADDRESS_TITLE;
	private static final DateFormat[] DATE_FORMATS = {
			new SimpleDateFormat("yyyyMMdd"),
			new SimpleDateFormat("yyyyMMdd'T'HHmmss"),
			new SimpleDateFormat("yyyy-MM-dd"),
			new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"), };

	private final boolean[] fields;

	private int buttonCount;
	private final String[] btnTxt = { ADD_CONTACT, SHOW_MAP, DIALING,
			SEND_EMAIL };

	// This takes all the work out of figuring out which buttons/actions should
	// be in which
	// positions, based on which fields are present in this barcode.
	private int mapIndexToAction(int index) {
		if (index < buttonCount) {
			int count = -1;
			for (int x = 0; x < MAX_BUTTON_COUNT; x++) {
				if (fields[x]) {
					count++;
				}
				if (count == index) {
					return x;
				}
			}
		}
		return -1;
	}

	public AddressBookResultHandler(Activity activity, ParsedResult result) {
		super(activity, result);
		AddressBookParsedResult addressResult = (AddressBookParsedResult) result;
		String[] addresses = addressResult.getAddresses();
		boolean hasAddress = addresses != null && addresses.length > 0
				&& addresses[0].length() > 0;
		String[] phoneNumbers = addressResult.getPhoneNumbers();
		boolean hasPhoneNumber = phoneNumbers != null
				&& phoneNumbers.length > 0;
		String[] emails = addressResult.getEmails();
		boolean hasEmailAddress = emails != null && emails.length > 0;

		fields = new boolean[MAX_BUTTON_COUNT];
		fields[0] = true; // Add contact is always available
		fields[1] = hasAddress;
		fields[2] = hasPhoneNumber;
		fields[3] = hasEmailAddress;

		buttonCount = 0;
		for (int x = 0; x < MAX_BUTTON_COUNT; x++) {
			if (fields[x]) {
				buttonCount++;
			}
		}
	}

	@Override
	public int getButtonCount() {
		return buttonCount;
	}

	@Override
	public String getButtonText(int index) {
		int action = mapIndexToAction(index);
		switch (action) {
		case 0:
			return btnTxt[action];
		case 1:
			return btnTxt[action];
		case 2:
			return btnTxt[action];
		case 3:
			return btnTxt[action];
		default:
			throw new ArrayIndexOutOfBoundsException();
		}
	}

	@Override
	public void handleButtonPress(int index) {
		AddressBookParsedResult addressResult = (AddressBookParsedResult) getResult();
		String[] addresses = addressResult.getAddresses();
		String address1 = addresses == null || addresses.length < 1 ? null
				: addresses[0];
		int action = mapIndexToAction(index);
		switch (action) {
		case 0:
			addContact(addressResult.getNames(),
					addressResult.getPhoneNumbers(), addressResult.getEmails(),
					addressResult.getNote(), address1, addressResult.getOrg(),
					addressResult.getTitle());
			break;
		case 1:
			String[] names = addressResult.getNames();
			String title = names != null ? names[0] : null;
			searchMap(address1, title);
			break;
		case 2:
			dialPhone(addressResult.getPhoneNumbers()[0]);
			break;
		case 3:
			sendEmail(addressResult.getEmails()[0], null, null);
			break;
		default:
			break;
		}
	}

	private static Date parseDate(String s) {
		for (DateFormat currentFomat : DATE_FORMATS) {
			synchronized (currentFomat) {
				currentFomat.setLenient(false);
				Date result = currentFomat.parse(s, new ParsePosition(0));
				if (result != null) {
					return result;
				}
			}
		}
		return null;
	}

	// Overriden so we can hyphenate phone numbers, format birthdays, and bold
	// the name.
	@Override
	public CharSequence getDisplayContents() {
		AddressBookParsedResult result = (AddressBookParsedResult) getResult();
		StringBuffer contents = new StringBuffer(100);
		ParsedResult.maybeAppend(result.getNames(), contents);
		int namesLength = contents.length();

		String pronunciation = result.getPronunciation();
		if (pronunciation != null && pronunciation.length() > 0) {
			contents.append("\n(");
			contents.append(pronunciation);
			contents.append(')');
		}

		ParsedResult.maybeAppend(result.getTitle(), contents);
		ParsedResult.maybeAppend(result.getOrg(), contents);
		ParsedResult.maybeAppend(result.getAddresses(), contents);
		String[] numbers = result.getPhoneNumbers();
		if (numbers != null) {
			for (String number : numbers) {
				ParsedResult.maybeAppend(PhoneNumberUtils.formatNumber(number),
						contents);
			}
		}
		ParsedResult.maybeAppend(result.getEmails(), contents);
		ParsedResult.maybeAppend(result.getURL(), contents);

		String birthday = result.getBirthday();
		if (birthday != null && birthday.length() > 0) {
			Date date = parseDate(birthday);
			if (date != null) {
				ParsedResult.maybeAppend(
						DateFormat.getDateInstance().format(date.getTime()),
						contents);
			}
		}
		ParsedResult.maybeAppend(result.getNote(), contents);

		if (namesLength > 0) {
			// Bold the full name to make it stand out a bit.
			Spannable styled = new SpannableString(contents.toString());
			styled.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0,
					namesLength, 0);
			return styled;
		} else {
			return contents.toString();
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
