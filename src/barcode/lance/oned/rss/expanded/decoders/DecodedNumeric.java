package barcode.lance.oned.rss.expanded.decoders;

/**
 * @author Pablo Orduña, University of Deusto (pablo.orduna@deusto.es)
 * @author Eduardo Castillejo, University of Deusto
 *         (eduardo.castillejo@deusto.es)
 */
final class DecodedNumeric extends DecodedObject {

	private final int firstDigit;
	private final int secondDigit;

	static final int FNC1 = 10;

	DecodedNumeric(int newPosition, int firstDigit, int secondDigit) {
		super(newPosition);

		this.firstDigit = firstDigit;
		this.secondDigit = secondDigit;

		if (this.firstDigit < 0 || this.firstDigit > 10) {
			throw new IllegalArgumentException("Invalid firstDigit: "
					+ firstDigit);
		}

		if (this.secondDigit < 0 || this.secondDigit > 10) {
			throw new IllegalArgumentException("Invalid secondDigit: "
					+ secondDigit);
		}
	}

	int getFirstDigit() {
		return this.firstDigit;
	}

	int getSecondDigit() {
		return this.secondDigit;
	}

	int getValue() {
		return this.firstDigit * 10 + this.secondDigit;
	}

	boolean isFirstDigitFNC1() {
		return this.firstDigit == FNC1;
	}

	boolean isSecondDigitFNC1() {
		return this.secondDigit == FNC1;
	}

	boolean isAnyFNC1() {
		return this.firstDigit == FNC1 || this.secondDigit == FNC1;
	}

}
