package barcode.lance.oned.rss;

final class Pair extends DataCharacter {

	private final FinderPattern finderPattern;
	private int count;

	Pair(int value, int checksumPortion, FinderPattern finderPattern) {
		super(value, checksumPortion);
		this.finderPattern = finderPattern;
	}

	FinderPattern getFinderPattern() {
		return finderPattern;
	}

	int getCount() {
		return count;
	}

	void incrementCount() {
		count++;
	}

}