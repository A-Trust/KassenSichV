package at.atrust.asigntse.seapi;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;

public class NativeUtil {
	
	private static final String ZONE_ID_UTC = "UTC"; //$NON-NLS-1$
	
	public static ByteBuffer getAsciiByteBuffer(String s) {
		if(s == null || s.isEmpty()) {
			byte[] eba= {};
			return ByteBuffer.wrap(eba);
		}
		byte[] ba = s.getBytes(StandardCharsets.UTF_8);
		return ByteBuffer.wrap(ba);
	}
	
	public static ByteBuffer getByteArrayByteBuffer(byte[] ba) {
		if(ba == null) {
			// byte[] eba= {};
			// return ByteBuffer.wrap(eba);
			return null;
		}
		return ByteBuffer.wrap(ba);
	}
	
	public static NativeInt32 getBufferLen(ByteBuffer bb) {
		NativeInt32 uidLength = new NativeInt32(0);	
		if(bb != null) {
			uidLength = new NativeInt32(bb.capacity());	
		}
		return uidLength;
	}
	
	public static long getTimeStruct(ZonedDateTime zdt) {
		
		ZonedDateTime zdtUtc  = ZonedDateTime.ofInstant(zdt.toInstant(), ZoneId.of(ZONE_ID_UTC));
		/*
		Memory m = new Memory(36);
		m.setInt(0, zdtUtc.getSecond());	                // ("tm_sec", c_int),
		m.setInt(4, zdtUtc.getMinute());                    // ("tm_min", c_int),
		m.setInt(8, zdtUtc.getHour());                      // ("tm_hour", c_int),
		m.setInt(12, zdtUtc.getDayOfMonth());               // ("tm_mday", c_int),
		m.setInt(16, zdtUtc.getMonthValue() - MONTH_OFFSET);// ("tm_mon", c_int),
		m.setInt(20, zdtUtc.getYear() - YEAR_OFFSET);       // ("tm_year", c_int),
		m.setInt(24, zdtUtc.getDayOfWeek().getValue()  );   // ("tm_wday", c_int), tm_wday  day of week [0,6] (Sunday = 0)
		m.setInt(28, zdtUtc.getDayOfYear());                // ("tm_yday", c_int), tm_yday  day of year [0,365]
		m.setInt(32, 0);                                    // ("tm_isdst", c_int), tm_isdst daylight savings flag
		// https://stackoverflow.com/questions/36031757/interpretation-of-tm-isdst-field-in-struct-tm/36047676
		// set to 1 to indicate DST
		// set to 0 to indicate standard time
		// set to -1 to indicate DST is not known
		*/
		return zdtUtc.toEpochSecond();
	}
	
	public static ZonedDateTime getTimeFromStruct(NativeInt64ByReference l) {
		/*
		ZonedDateTime zdt = ZonedDateTime.of(
				m.getInt(20) + YEAR_OFFSET,
				m.getInt(16) + MONTH_OFFSET,
				m.getInt(12),
				m.getInt(8),
				m.getInt(4),
				m.getInt(0),
                0,
                ZoneId.of(ZONE_ID_UTC));
                */
		Instant i = Instant.ofEpochSecond(l.getValue());
		return ZonedDateTime.ofInstant(i, ZoneId.of(ZONE_ID_UTC) );
	}
}
