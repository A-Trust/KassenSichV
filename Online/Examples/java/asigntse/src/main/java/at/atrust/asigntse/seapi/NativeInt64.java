package at.atrust.asigntse.seapi;

import com.sun.jna.IntegerType;

public class NativeInt64 extends IntegerType {

	private static final long serialVersionUID = 1L;
	/** Size of a native int, in bytes. */
	public static final int SIZE = 8;

	/** Create a zero-valued NativeInt. */
	public NativeInt64() {
		this(0);
	}

	/** Create a NativeInt with the given value. */
	public NativeInt64(int value) {
		this(value, false);
	}

	/** Create a NativeInt with the given value, optionally unsigned. */
	public NativeInt64(int value, boolean unsigned) {
		super(SIZE, value, unsigned);
	}
}
