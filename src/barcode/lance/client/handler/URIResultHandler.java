package barcode.lance.client.handler;

import android.app.Activity;
import barcode.lance.result.ParsedResult;
import barcode.lance.result.URIParsedResult;
import static barcode.lance.client.common.StringContant.*;

/**
 * Offers appropriate actions for URLS.
 * 
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class URIResultHandler extends ResultHandler {
	private String title = HandlerTitles.URI_TITLE;
	private static final String[] buttons = { OPEN_BROWSER, EMAIL_SHARE,
			SMS_SHARE, SEARCH_BOOK_CONTENT };

	public URIResultHandler(Activity activity, ParsedResult result) {
		super(activity, result);
	}

	@Override
	public int getButtonCount() {
		return isGoogleBooksURI() ? buttons.length : buttons.length - 1;
	}

	@Override
	public String getButtonText(int index) {
		return buttons[index];
	}

	@Override
	public void handleButtonPress(int index) {
		URIParsedResult uriResult = (URIParsedResult) getResult();
		String uri = uriResult.getURI();
		switch (index) {
		case 0:
			openURL(uri);
			break;
		case 1:
			shareByEmail(uri);
			break;
		case 2:
			shareBySMS(uri);
			break;
		case 3:
			searchBookContents(uri);
			break;
		}
	}

	@Override
	public String getDisplayTitle() {
		return title;
	}

	private boolean isGoogleBooksURI() {
		return ((URIParsedResult) getResult()).getURI().startsWith(
				"http://google.com/books?id=");
	}

	@Override
	public void setTitle(String title) {
		this.title = title;
	}

}
