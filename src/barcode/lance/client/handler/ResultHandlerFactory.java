package barcode.lance.client.handler;

import android.app.Activity;
import barcode.lance.assist.Result;
import barcode.lance.result.ParsedResult;
import barcode.lance.result.ParsedResultType;
import barcode.lance.result.ResultParser;

/**
 * Manufactures Android-specific handlers based on the barcode content's type.
 * 
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class ResultHandlerFactory {
	private ResultHandlerFactory() {
	}

	public static ResultHandler makeResultHandler(Activity activity,
			Result rawResult) {
		ParsedResult result = parseResult(rawResult);
		ParsedResultType type = result.getType();
		if (type.equals(ParsedResultType.ADDRESSBOOK)) {
			return new AddressBookResultHandler(activity, result);
		} else if (type.equals(ParsedResultType.EMAIL_ADDRESS)) {
			return new EmailAddressResultHandler(activity, result);
		} else if (type.equals(ParsedResultType.PRODUCT)) {
			return new ProductResultHandler(activity, result, rawResult);
		} else if (type.equals(ParsedResultType.URI)) {
			return new URIResultHandler(activity, result);
		} else if (type.equals(ParsedResultType.WIFI)) {
			return new WifiResultHandler(activity, result);
		} else if (type.equals(ParsedResultType.TEXT)) {
			return new TextResultHandler(activity, result, rawResult);
		} else if (type.equals(ParsedResultType.GEO)) {
			return new GeoResultHandler(activity, result);
		} else if (type.equals(ParsedResultType.TEL)) {
			return new TelResultHandler(activity, result);
		} else if (type.equals(ParsedResultType.SMS)) {
			return new SMSResultHandler(activity, result);
		} else if (type.equals(ParsedResultType.CALENDAR)) {
			return new CalendarResultHandler(activity, result);
		} else if (type.equals(ParsedResultType.ISBN)) {
			return new ISBNResultHandler(activity, result, rawResult);
		} else {
			// The TextResultHandler is the fallthrough for unsupported formats.
			return new TextResultHandler(activity, result, rawResult);
		}
	}

	private static ParsedResult parseResult(Result rawResult) {
		return ResultParser.parseResult(rawResult);
	}
}
