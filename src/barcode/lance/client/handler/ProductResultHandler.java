package barcode.lance.client.handler;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import barcode.lance.assist.Result;
import barcode.lance.result.ParsedResult;
import barcode.lance.result.ProductParsedResult;
import static barcode.lance.client.common.StringContant.*;

/**
 * Handles generic products which are not books.
 * 
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class ProductResultHandler extends ResultHandler {
	private String title = HandlerTitles.PRODUCT_TITLE;
	private static final String[] buttons = { SEARCH_PRODUCT, SEARCH_WEBPAGE,
			SEARCH_CUSTOM };

	public ProductResultHandler(Activity activity, ParsedResult result,
			Result rawResult) {
		super(activity, result, rawResult);
		// showGoogleShopperButton(new View.OnClickListener() {
		// public void onClick(View view) {
		// ProductParsedResult productResult = (ProductParsedResult)
		// getResult();
		// openGoogleShopper(productResult.getNormalizedProductID());
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
				ProductParsedResult productResult = (ProductParsedResult) getResult();
				switch (index) {
				case 0:
					openProductSearch(productResult.getNormalizedProductID());
					break;
				case 1:
					webSearch(productResult.getNormalizedProductID());
					break;
				case 2:
					openURL(fillInCustomSearchURL(productResult
							.getNormalizedProductID()));
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
