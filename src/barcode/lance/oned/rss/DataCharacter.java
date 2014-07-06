package barcode.lance.oned.rss;

public class DataCharacter {

	private final int value;
	private final int checksumPortion;

	public DataCharacter(int value, int checksumPortion) {
		this.value = value;
		this.checksumPortion = checksumPortion;
	}

	public int getValue() {
		return value;
	}

	public int getChecksumPortion() {
		return checksumPortion;
	}

}