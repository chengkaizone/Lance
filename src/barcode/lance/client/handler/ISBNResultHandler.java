package barcode.lance.client.handler;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import barcode.lance.assist.Result;
import barcode.lance.result.ISBNParsedResult;
import barcode.lance.result.ParsedResult;
import static barcode.lance.client.common.StringContant.*;

/**
 * Handles books encoded by their ISBN values.
 * 
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class ISBNResultHandler extends ResultHandler {
	private String title = HandlerTitles.ISBN_TITLE;
	private static final String[] buttons = { SEARCH_PRODUCT, SEARCH_BOOKS,
			SEARCH_BOOK_CONTENT, SEARCH_CUSTOM };

	public ISBNResultHandler(Activity activity, ParsedResult result,
			Result rawResult) {
		super(activity, result, rawResult);
		// showGoogleShopperButton(new View.OnClickListener() {
		// public void onClick(View view) {
		// ISBNParsedResult isbnResult = (ISBNParsedResult) getResult();
		// openGoogleShopper(isbnResult.getISBN());
		// }
		// });
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
	public void handleButtonPress(final int index) {
		showNotOurResults(index, new AlertDialog.OnClickListener() {
			public void onClick(DialogInterface dialogInterface, int i) {
				ISBNParsedResult isbnResult = (ISBNParsedResult) getResult();
				switch (index) {
				case 0:
					openProductSearch(isbnResult.getISBN());
					break;
				case 1:
					openBookSearch(isbnResult.getISBN());
					break;
				case 2:
					searchBookContents(isbnResult.getISBN());
					break;
				case 3:
					openURL(fillInCustomSearchURL(isbnResult.getISBN()));
					break;
				}
			}
		});
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
