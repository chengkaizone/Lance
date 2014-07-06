package barcode.lance.common;

/**
 * Superclass of classes encapsulating types ECIs, according to
 * "Extended Channel Interpretations" 5.3 of ISO 18004.
 * 
 * @author Sean Owen
 */
public abstract class ECI {

	private final int value;

	public ECI(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	/**
	 * @param value
	 *            ECI value
	 * @return {@link ECI} representing ECI of given value, or null if it is
	 *         legal but unsupported
	 * @throws IllegalArgumentException
	 *             if ECI value is invalid
	 */
	public static ECI getECIByValue(int value) {
		if (value < 0 || value > 999999) {
			throw new IllegalArgumentException("Bad ECI value: " + value);
		}
		if (value < 900) { // Character set ECIs use 000000 - 000899
			return CharacterSetECI.getCharacterSetECIByValue(value);
		}
		return null;
	}

}
