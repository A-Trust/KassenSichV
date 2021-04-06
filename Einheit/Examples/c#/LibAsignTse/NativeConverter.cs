using System;
using System.Runtime.InteropServices;

namespace LibAsignTse
{
    public class NativeConverter
    {
        public static Int64 CharStarStarToInt(ref IntPtr in_ptr, ulong in_length)
        {
            Int64 ret = 0;
            byte[] arrayRes = new byte[in_length];
            if (in_length > 0)
            {
                Marshal.Copy(in_ptr, arrayRes, 0, (int)in_length);
                ret = BitConverter.ToInt64(arrayRes, 0);
            }
            return ret;

        }
        public static string CharStarStarToB64String(ref IntPtr in_ptr, ulong in_length)
        {
            string ret = "";
            byte[] arrayRes = new byte[in_length];
            if (in_length > 0)
            {
                Marshal.Copy(in_ptr, arrayRes, 0, (int)in_length);
                ret = System.Convert.ToBase64String(arrayRes);
            }
            return ret;
        }

        public static string CharStarStarToString(ref IntPtr in_ptr, ulong in_length)
        {
            byte[] arrayRes = new byte[in_length];
            if (in_length > 0)
            {
                Marshal.Copy(in_ptr, arrayRes, 0, (int)in_length);
            }
            return System.Text.Encoding.UTF8.GetString(arrayRes);
        }
        public static byte[] CharStarStarToByteArray(ref IntPtr in_ptr, ulong in_length)
        {
            byte[] arrayRes = new byte[in_length];
            if (in_length > 0)
            {
                Marshal.Copy(in_ptr, arrayRes, 0, (int)in_length);
            }
            return arrayRes;
        }

        public static Int32[] CharStarStarToInt32Array(ref IntPtr in_ptr, ulong in_length)
        {
            Int32[] arrayRes = new Int32[in_length];
            if (in_length > 0)
            {
                Marshal.Copy(in_ptr, arrayRes, 0, (int)in_length);
            }
            return arrayRes;
        }

        public static DateTime UnixTimeStampToDateTime(Int64 unixTimeStamp)
        {
            // Unix timestamp is seconds past epoch
            System.DateTime dtDateTime = new DateTime(1970, 1, 1, 0, 0, 0, 0, System.DateTimeKind.Utc);
            dtDateTime = dtDateTime.AddSeconds(unixTimeStamp).ToLocalTime();
            return dtDateTime;
        }
        public static Int64 DateTimeUtcToUnixTimeStamp(DateTime dt)
        {
            Int64 unixTimestamp = (Int64)(dt.Subtract(new DateTime(1970, 1, 1))).TotalSeconds;
            return unixTimestamp;
        }
    }
}
