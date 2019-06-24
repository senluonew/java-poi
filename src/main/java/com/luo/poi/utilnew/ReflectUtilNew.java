package com.luo.poi.utilnew;



import com.luo.poi.util.ConvertUtil;

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
public class ReflectUtilNew {

    /**
     * 获取{@link Annotation}注解的Map
     * @param type
     * @return
     */
    public static Map<String, List<Annotation>> getFieldAnnoMap(Class<?> type){
        Map<String, List<Annotation>> fieldAnnoMap = new LinkedHashMap<>();
        List<Field> fieldList = getFields(type);
//        fieldList.forEach(field -> {
//            fieldAnnoMap.put(field.getName(), Arrays.asList(field.getAnnotations()));
//        });      //1.8
        return fieldAnnoMap;
    }

    public static <T> Map<String, T> getFieldAnnoMap(Class<?> type, Class<T> annotationClass){
        Map<String, T> fieldAnnoMap = new LinkedHashMap<>();
        List<Field> fieldList = getFields(type);
        Class annotationClassMid = annotationClass;
//        fieldList.forEach(field -> {
//            fieldAnnoMap.put(field.getName(), (T) field.getAnnotation(annotationClassMid));
//        });      //1.8
        return fieldAnnoMap;
    }

    public static List<List<Annotation>> getFieldAnnoList(Class<?> type) {
        List<List<Annotation>> fieldAnnoList = new ArrayList<>();
        List<Field> fieldList = getFields(type);
//        fieldList.forEach(field -> {
//            fieldAnnoList.add(Arrays.asList(field.getAnnotations()));
//        });      //1.8
        return fieldAnnoList;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static List<Field> getFields(Class<?> type) {
        List<Field> fieldList = new ArrayList<>();
        do {
            fieldList.addAll(Arrays.asList(type.getDeclaredFields()));
        } while ((type = type.getSuperclass()) != Object.class);
        return fieldList;
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

    public static Field getField(String name, Class<?> type) {
        Field field = null;
        while (type != Object.class) {
            try {
                return type.getDeclaredField(name);
            } catch (NoSuchFieldException e) {
                type = type.getSuperclass();
            }
        }
        return field;
    }

    public static Method getGetter(Class srcClass, String fieldName) {
        try {
            String getter = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            return srcClass.getMethod(getter);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    public static Method getSetter(Class srcClass, String fieldName) {
        try {
            Method getter = getGetter(srcClass, fieldName);
            return getSetter(srcClass, fieldName, getter.getReturnType());
        } catch (Exception e) {
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

    /**
     * 获取指定位置的泛型
     * @param srcClass
     * @param index
     * @return
     */
    public static <T> Class getGenericType(Class<T> srcClass, int index) {
        if (srcClass == null) {
            return null;
        }

        Type type = srcClass.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Type[] types = ((ParameterizedType) type).getActualTypeArguments();
            for (Type type1 : types) {
            }
        }
        return null;
    }

    /**
     * 获取所有的泛型Class
     * @param srcClass
     * @return
     */
    public static Class[] getGenericTypes(Class srcClass) {
        if (srcClass == null) {
            return null;
        }

        Type[] types = ((ParameterizedType)srcClass.getGenericSuperclass()).getActualTypeArguments();
        Class[] desClasses = new Class[types.length];
        for (int i = 0; i < types.length; i++) {
            desClasses[i] = (Class)types[i];
        }
        return desClasses;
    }

    public static Class getGenericType(Method method, int index) {
        method.getGenericParameterTypes();
        return null;
    }

    public static Object getFieldValue(Object srcObject, String fieldName) {
        try {
            Class srcClass = srcObject.getClass();
            Method getter = getGetter(srcClass, fieldName);
            if (getter != null) {
                return getter.invoke(srcObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map<String,Object> getFieldValue(Object src) {
        Class srcClass = src.getClass();
        Map<String, Object> fieldVlues = new HashMap<>();
//        getFieldNames(srcClass).forEach(fieldName -> {
//            try {
//                Method getter = getGetter(srcClass, fieldName);
//                if (getter != null) {
//                    fieldVlues.put(fieldName, getter.invoke(src));
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });      //1.8
        return fieldVlues;
    }

    public static void setFieldValue(Object srcObject, String fieldName, Object value) {
        try {
            Class srcClass = srcObject.getClass();
            Method setter = getSetter(srcClass, fieldName);
            if (setter != null) {
                Class clazz = setter.getParameterTypes()[0];
                setter.invoke(srcObject, ConvertUtil.toValue(value, clazz));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 是否为原始数据类型
     * @param srcClass
     * @return
     */
    public static boolean isPrimitiveType(Class<?> srcClass) {
        if (boolean.class == srcClass || Boolean.class == srcClass ||
                byte.class == srcClass || Byte.class == srcClass ||
                short.class == srcClass || Short.class == srcClass ||
                int.class == srcClass || Integer.class == srcClass ||
                long.class == srcClass || Long.class == srcClass ||
                float.class == srcClass || Float.class == srcClass ||
                double.class == srcClass || Double.class == srcClass) {
            return true;
        }
        return false;
    }

    public static boolean isString(Class<?> srcClass) {
        if (char.class == srcClass || Character.class == srcClass ||
                CharSequence.class.isAssignableFrom(srcClass)) {
            return true;
        }
        return false;
    }

    /**
     * 是否为原子数据类型
     * @param srcClass
     * @return
     */
    public static boolean isAtomicNumber(Class srcClass) {
        if (AtomicBoolean.class == srcClass || AtomicInteger.class == srcClass || AtomicLong.class == srcClass) {
            return true;
        }
        return false;
    }

    public static Class[] getGenericClass(Field field) {
        try {
            ParameterizedType type = (ParameterizedType) field.getGenericType();
            List<Class> classList = new ArrayList<>();
            for (Type type1 : type.getActualTypeArguments()) {
                if (type1 instanceof Class) {
                    classList.add((Class)type1);
                }
            }
            return classList.toArray(new Class[classList.size()]);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Class[] getGenericClass(String fieldName, Class type) {
        return getGenericClass(getField(fieldName, type));
    }

    public static void main(String[] args) {
        testGetGenericClass();
    }

    private static void testGetGenericClass() {
        List<String> list = new ArrayList<>();
    }
}
