package com.example.lib_annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Day：2020/12/1 4:34 PM
 *
 * @author zhanglei
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PagePath {
    /**
     * 页面id
     */
    String id();

    /**
     * 页面类型
     */
    PageType type();

    /**
     * 页面有多个id
     */
    boolean allowMultiMode();

}
