package barcode.lance.result.optional;

import barcode.lance.assist.Result;
import barcode.lance.result.URIParsedResult;

/**
 * Recognizes an NDEF message that encodes a URI according to the
 * "URI Record Type Definition" specification.
 * 
 * @author Sean Owen
 */
final class NDEFURIResultParser extends AbstractNDEFResultParser {

	private static final String[] URI_PREFIXES = { null, "http://www.",
			"https://www.", "http://", "https://", "tel:", "mailto:",
			"ftp://anonymous:anonymous@", "ftp://ftp.", "ftps://", "sftp://",
			"smb://", "nfs://", "ftp://", "dav://", "news:", "telnet://",
			"imap:", "rtsp://", "urn:", "pop:", "sip:", "sips:", "tftp:",
			"btspp://", "btl2cap://", "btgoep://", "tcpobex://", "irdaobex://",
			"file://", "urn:epc:id:", "urn:epc:tag:", "urn:epc:pat:",
			"urn:epc:raw:", "urn:epc:", "urn:nfc:", };

	public static URIParsedResult parse(Result result) {
		byte[] bytes = result.getRawBytes();
		if (bytes == null) {
			return null;
		}
		NDEFRecord ndefRecord = NDEFRecord.readRecord(bytes, 0);
		if (ndefRecord == null || !ndefRecord.isMessageBegin()
				|| !ndefRecord.isMessageEnd()) {
			return null;
		}
		if (!ndefRecord.getType().equals(NDEFRecord.URI_WELL_KNOWN_TYPE)) {
			return null;
		}
		String fullURI = decodeURIPayload(ndefRecord.getPayload());
		return new URIParsedResult(fullURI, null);
	}

	static String decodeURIPayload(byte[] payload) {
		int identifierCode = payload[0] & 0xFF;
		String prefix = null;
		if (identifierCode < URI_PREFIXES.length) {
			prefix = URI_PREFIXES[identifierCode];
		}
		String restOfURI = bytesToString(payload, 1, payload.length - 1, "UTF8");
		return prefix == null ? restOfURI : prefix + restOfURI;
	}

}