package barcode.lance.client.handler;

import android.app.Activity;
import barcode.lance.result.GeoParsedResult;
import barcode.lance.result.ParsedResult;
import static barcode.lance.client.common.StringContant.*;

/**
 * Handles geographic coordinates (typically encoded as geo: URLs).
 * 
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class GeoResultHandler extends ResultHandler {
	private String title = HandlerTitles.GEO_TITLE;
	private static final String[] buttons = { SHOW_MAP, GET_ADDR };

	public GeoResultHandler(Activity activity, ParsedResult result) {
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
		GeoParsedResult geoResult = (GeoParsedResult) getResult();
		switch (index) {
		case 0:
			openMap(geoResult.getGeoURI());
			break;
		case 1:
			getDirections(geoResult.getLatitude(), geoResult.getLongitude());
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
