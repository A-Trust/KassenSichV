package at.atrust.asigntse.seapi;

import com.sun.jna.ptr.ByReference;

public class NativeInt64ByReference extends ByReference {

	public NativeInt64ByReference() {
		this(new NativeInt64(0));
	}

	public NativeInt64ByReference(NativeInt64 value) {
		super(NativeInt64.SIZE);
		setValue(value);
	}

	public void setValue(NativeInt64 value) {
		getPointer().setInt(0, value.intValue());
	}

	public long getValue() {
		return getPointer().getLong(0);
	}
}
