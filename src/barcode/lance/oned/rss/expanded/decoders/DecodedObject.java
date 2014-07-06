package barcode.lance.oned.rss.expanded.decoders;

/**
 * @author Pablo Orduña, University of Deusto (pablo.orduna@deusto.es)
 */
abstract class DecodedObject {

	protected final int newPosition;

	DecodedObject(int newPosition) {
		this.newPosition = newPosition;
	}

	int getNewPosition() {
		return this.newPosition;
	}

}
