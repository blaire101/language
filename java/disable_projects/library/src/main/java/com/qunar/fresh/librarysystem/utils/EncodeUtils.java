package com.qunar.fresh.librarysystem.utils;

import com.google.common.base.Strings;
import com.google.common.primitives.Ints;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

/**
 * Created with IntelliJ IDEA. User: he.chen Date: 14-4-18 Time: 上午1:19 To change this template use File | Settings |
 * File Templates.
 */
public class EncodeUtils {

    public static String encodeString(String str) {
        if (Strings.isNullOrEmpty(str)) {
            return "";
        }
        String encodeStr = Base64.encode(str.getBytes());
        return encodeStr;
    }

    public static String decodeString(String code) {
        if (Strings.isNullOrEmpty(code)) {
            return "";
        }
        String decodeStr = new String(Base64.decode(code));
        return decodeStr;
    }

    public static String encodeInt(int id) {
        String encodeStr = Base64.encode(Ints.toByteArray(id));
        return encodeStr;
    }

    public static int decodeInt(String code) {
        if (Strings.isNullOrEmpty(code)) {
            return 0;
        }
        int decodeInt = Ints.fromByteArray(Base64.decode(code));
        return decodeInt;
    }
}
