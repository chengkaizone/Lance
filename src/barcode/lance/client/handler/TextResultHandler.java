package barcode.lance.client.handler;

import android.app.Activity;
import barcode.lance.assist.Result;
import barcode.lance.result.ParsedResult;
import static barcode.lance.client.common.StringContant.*;
/**
 * This class handles TextParsedResult as well as unknown formats. It's the
 * fallback handler.
 * 
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class TextResultHandler extends ResultHandler {
	private String title = HandlerTitles.TEXT_TITLE;
	private static final String[] buttons = { SEARCH_WEBPAGE, EMAIL_SHARE,SMS_SHARE,
			SEARCH_CUSTOM };

	public TextResultHandler(Activity activity, ParsedResult result,
			Result rawResult) {
		super(activity, result, rawResult);
	}

	@Override
	public int getButtonCount() {
		return hasCustomProductSearch() ? buttons.length : buttons.length - 1;
	}

	@Override
	public String getButtonText(int index) {
		return buttons[index];
	}

	@Override
	public void handleButtonPress(int index) {
		String text = getResult().getDisplayResult();
		switch (index) {
		case 0:
			webSearch(text);
			break;
		case 1:
			shareByEmail(text);
			break;
		case 2:
			shareBySMS(text);
			break;
		case 3:
			openURL(fillInCustomSearchURL(text));
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
