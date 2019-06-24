package com.luo.poi.util;

import java.lang.annotation.*;

/**
 * @author pudding
 * @version 1.0
 * @design
 * @date 2018\3\25 0025/22:45.
 * @see
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Title {

    String value() default "";

    /**
     * 字段是否不为空
     * @return
     */
    boolean isNotNull() default false;

    /**
     * 对应的序列位置
     * @return
     */
    int index() default 10;
}
