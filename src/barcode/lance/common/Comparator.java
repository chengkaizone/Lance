package barcode.lance.common;

/**
 * This is merely a clone of <code>Comparator</code> since it is not available
 * in CLDC 1.1 / MIDP 2.0.
 */
public interface Comparator {

	public int compare(Object o1, Object o2);

}