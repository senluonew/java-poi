package com.luo.poi.util;

/**
 * @author pudding
 * @version 1.0.0
 * @design
 * @date 2018/6/7.17:13
 * @see
 */
public class ConvertUtil {

    public static <T> T toPrimitiveValue(Object source, Class<T> type) {
        String strSource = source + "";
        if (int.class == type || Integer.class == type) {
            return (T) Integer.valueOf(strSource);
        } else if (double.class == type || Double.class == type) {
            return (T) Double.valueOf(strSource);
        } else if (String.class == type) {
            return (T) source;
        }
        throw new ClassCastException("class type is not primitive");
    }

    public static <T> T toValue(Object source, Class<T> type) {
        try {
            return toPrimitiveValue(source, type);
        } catch (Exception e) {
            return (T) source;
        }
    }
}
