package at.atrust.asigntse.seapi;

import com.sun.jna.ptr.ByReference;

public class NativeInt32ByReference extends ByReference {

	public NativeInt32ByReference() {
		this(new NativeInt32(0));
	}

	public NativeInt32ByReference(NativeInt32 value) {
		super(NativeInt32.SIZE);
		setValue(value);
	}

	public void setValue(NativeInt32 value) {
		getPointer().setInt(0, value.intValue());
	}

	public int getValue() {
		return getPointer().getInt(0);
	}
}
