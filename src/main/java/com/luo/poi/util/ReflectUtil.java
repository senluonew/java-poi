package com.luo.poi.util;



import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author pudding
 * @version 1.0
 * @design
 * @date 2018\3\25 0025/22:49.
 * @see
 */
public class ReflectUtil {


    public static <T> Map<String, T> getFieldAnnoMap(Class<?> type, Class<T> annotationClass){
        Map<String, T> fieldAnnoMap = new LinkedHashMap<>();
        List<Field> fieldList = getFields(type);
        Class annotationClassMid = annotationClass;
        fieldList.forEach(field -> {
            if(Objects.nonNull(field.getAnnotation(annotationClassMid))) {
                fieldAnnoMap.put(field.getName(), (T) field.getAnnotation(annotationClassMid));
            }
        });
        return fieldAnnoMap;
    }

    public static List<Field> getFields(Class<?> type) {
        List<Field> fieldList = new ArrayList<>();
        do {
            fieldList.addAll(Arrays.asList(type.getDeclaredFields()));
        } while ((type = type.getSuperclass()) != Object.class);
        return fieldList;
    }


    public static Method getGetter(Class srcClass, String fieldName) {
        try {
            String getter = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            return srcClass.getMethod(getter);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    public static Method getSetter(Class srcClass, String fieldName, Class... parameterType) {
        try {
            String setter = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            return srcClass.getMethod(setter, parameterType);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    public static Map<String,Object> getFieldValue(Object src) {
        Class srcClass = src.getClass();
        Map<String, Object> fieldVlues = new HashMap<>();
        getFieldNames(srcClass).forEach(fieldName -> {
            try {
                Method getter = getGetter(srcClass, fieldName);
                if (getter != null) {
                    fieldVlues.put(fieldName, getter.invoke(src));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return fieldVlues;
    }

    public static List<String> getFieldNames(Class<?> type) {
        List<String> fieldNameList = new ArrayList<>();
        do {
            for (Field field : type.getDeclaredFields()) {
                fieldNameList.add(field.getName());
            }
        } while ((type = type.getSuperclass()) != Object.class);
        return fieldNameList;
    }

}
