package barcode.lance.common.reedsolomon;

/**
 * <p>
 * This class contains utility methods for performing mathematical operations
 * over the Galois Fields. Operations use a given primitive polynomial in
 * calculations.
 * </p>
 * 
 * <p>
 * Throughout this package, elements of the GF are represented as an
 * <code>int</code> for convenience and speed (but at the cost of memory).
 * </p>
 * 
 * @author Sean Owen
 * @author David Olivier
 */
public final class GenericGF {

	public static final GenericGF AZTEC_DATA_12 = new GenericGF(0x1069, 4096); // x^12
																				// +
																				// x^6
																				// +
																				// x^5
																				// +
																				// x^3
																				// +
																				// 1
	public static final GenericGF AZTEC_DATA_10 = new GenericGF(0x409, 1024); // x^10
																				// +
																				// x^3
																				// +
																				// 1
	public static final GenericGF AZTEC_DATA_6 = new GenericGF(0x43, 64); // x^6
																			// +
																			// x
																			// +
																			// 1
	public static final GenericGF AZTEC_PARAM = new GenericGF(0x13, 16); // x^4
																			// +
																			// x
																			// +
																			// 1
	public static final GenericGF QR_CODE_FIELD_256 = new GenericGF(0x011D, 256); // x^8
																					// +
																					// x^4
																					// +
																					// x^3
																					// +
																					// x^2
																					// +
																					// 1
	public static final GenericGF DATA_MATRIX_FIELD_256 = new GenericGF(0x012D,
			256); // x^8 + x^5 + x^3 + x^2 + 1
	public static final GenericGF AZTEC_DATA_8 = DATA_MATRIX_FIELD_256;

	private static final int INITIALIZATION_THRESHOLD = 0;

	private int[] expTable;
	private int[] logTable;
	private GenericGFPoly zero;
	private GenericGFPoly one;
	private final int size;
	private final int primitive;
	private boolean initialized = false;

	/**
	 * Create a representation of GF(size) using the given primitive polynomial.
	 * 
	 * @param primitive
	 *            irreducible polynomial whose coefficients are represented by
	 *            the bits of an int, where the least-significant bit represents
	 *            the constant coefficient
	 */
	public GenericGF(int primitive, int size) {
		this.primitive = primitive;
		this.size = size;

		if (size <= INITIALIZATION_THRESHOLD) {
			initialize();
		}
	}

	private void initialize() {
		expTable = new int[size];
		logTable = new int[size];
		int x = 1;
		for (int i = 0; i < size; i++) {
			expTable[i] = x;
			x <<= 1; // x = x * 2; we're assuming the generator alpha is 2
			if (x >= size) {
				x ^= primitive;
				x &= size - 1;
			}
		}
		for (int i = 0; i < size - 1; i++) {
			logTable[expTable[i]] = i;
		}
		// logTable[0] == 0 but this should never be used
		zero = new GenericGFPoly(this, new int[] { 0 });
		one = new GenericGFPoly(this, new int[] { 1 });
		initialized = true;
	}

	private void checkInit() {
		if (!initialized) {
			initialize();
		}
	}

	GenericGFPoly getZero() {
		checkInit();

		return zero;
	}

	GenericGFPoly getOne() {
		checkInit();

		return one;
	}

	/**
	 * @return the monomial representing coefficient * x^degree
	 */
	GenericGFPoly buildMonomial(int degree, int coefficient) {
		checkInit();

		if (degree < 0) {
			throw new IllegalArgumentException();
		}
		if (coefficient == 0) {
			return zero;
		}
		int[] coefficients = new int[degree + 1];
		coefficients[0] = coefficient;
		return new GenericGFPoly(this, coefficients);
	}

	/**
	 * Implements both addition and subtraction -- they are the same in
	 * GF(size).
	 * 
	 * @return sum/difference of a and b
	 */
	static int addOrSubtract(int a, int b) {
		return a ^ b;
	}

	/**
	 * @return 2 to the power of a in GF(size)
	 */
	int exp(int a) {
		checkInit();

		return expTable[a];
	}

	/**
	 * @return base 2 log of a in GF(size)
	 */
	int log(int a) {
		checkInit();

		if (a == 0) {
			throw new IllegalArgumentException();
		}
		return logTable[a];
	}

	/**
	 * @return multiplicative inverse of a
	 */
	int inverse(int a) {
		checkInit();

		if (a == 0) {
			throw new ArithmeticException();
		}
		return expTable[size - logTable[a] - 1];
	}

	/**
	 * @param a
	 * @param b
	 * @return product of a and b in GF(size)
	 */
	int multiply(int a, int b) {
		checkInit();

		if (a == 0 || b == 0) {
			return 0;
		}

		if (a < 0 || b < 0 || a >= size || b >= size) {
			a++;
		}

		int logSum = logTable[a] + logTable[b];
		return expTable[(logSum % size) + logSum / size];
	}

	public int getSize() {
		return size;
	}

}
