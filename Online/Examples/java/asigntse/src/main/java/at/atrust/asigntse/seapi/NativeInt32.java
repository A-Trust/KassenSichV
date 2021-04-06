package at.atrust.asigntse.seapi;

import com.sun.jna.IntegerType;

public class NativeInt32 extends IntegerType {

	private static final long serialVersionUID = 1L;
	/** Size of a native int, in bytes. */
	public static final int SIZE = 4;

	/** Create a zero-valued NativeInt. */
	public NativeInt32() {
		this(0);
	}

	/** Create a NativeInt with the given value. */
	public NativeInt32(int value) {
		this(value, false);
	}

	/** Create a NativeInt with the given value, optionally unsigned. */
	public NativeInt32(int value, boolean unsigned) {
		super(SIZE, value, unsigned);
	}
}
