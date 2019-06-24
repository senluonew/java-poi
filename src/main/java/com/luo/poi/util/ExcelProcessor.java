package com.luo.poi.util;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author pudding
 * @version 1.0
 * @design
 * @date 2018\3\31 0031/12:28.
 * @see
 */
public class ExcelProcessor {

    protected static final String XLS = ".xls";

    protected static final String XLSX = ".xlsx";

    /**
     * 获取数据的标题信息；当对象的属相有{@link Title}注解时，使用注解值，否者使用属性名称
     *
     * @param srcClass
     * @param fieldList
     * @return
     */
    protected static <E> List<String> parseTitle(Class<E> srcClass, List<String> fieldList) {
        List<String> titleList = new ArrayList<>();
        Map<String, Title> fieldAnnoMap = ReflectUtil.getFieldAnnoMap(srcClass, Title.class);
        sort(fieldAnnoMap).forEach(fieldName -> {
            fieldList.add(fieldName);
            Title title = fieldAnnoMap.get(fieldName);
            if (title != null && (title.value().length() > 0)) {
                titleList.add(title.value());
            } else {
                titleList.add(fieldName);
            }
        });
        return titleList;
    }

    private static List<String> sort(Map<String, Title> fieldAnnoMap) {
        List<String> fieldList = new ArrayList<>();
        fieldAnnoMap.forEach((field, anno) -> {
            fieldList.add(field);
        });

        Collections.sort(fieldList, (o1, o2) -> {
            Title title1 = fieldAnnoMap.get(o1);
            Title title2 = fieldAnnoMap.get(o2);
            if (title1 == null && title2 == null) {
                return 0;
            } else if (title1 == null) {
                return -1;
            } else if (title2 == null) {
                return 1;
            }
            return title1.index() - title2.index();
        });

        return fieldList;
    }


    /**
     * 获取单个数据的标题信息；当对象的属相有{@link Title}注解时，使用注解值，否者使用属性名称
     *
     * @param srcClass
     * @param fieldName
     * @return
     */
    protected static <E> Title parseSingleTitle(Class<E> srcClass, String fieldName) {
        Map<String, Title> fieldAnnoMap = ReflectUtil.getFieldAnnoMap(srcClass, Title.class);
        return fieldAnnoMap.get(fieldName);
    }
}
