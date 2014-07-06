package barcode.lance.oned.rss.expanded.decoders;

/**
 * @author Pablo Orduña, University of Deusto (pablo.orduna@deusto.es)
 * @author Eduardo Castillejo, University of Deusto
 *         (eduardo.castillejo@deusto.es)
 */
final class BlockParsedResult {

	private final DecodedInformation decodedInformation;
	private final boolean finished;

	BlockParsedResult() {
		this.finished = true;
		this.decodedInformation = null;
	}

	BlockParsedResult(boolean finished) {
		this.finished = finished;
		this.decodedInformation = null;
	}

	BlockParsedResult(DecodedInformation information, boolean finished) {
		this.finished = finished;
		this.decodedInformation = information;
	}

	DecodedInformation getDecodedInformation() {
		return this.decodedInformation;
	}

	boolean isFinished() {
		return this.finished;
	}
}
