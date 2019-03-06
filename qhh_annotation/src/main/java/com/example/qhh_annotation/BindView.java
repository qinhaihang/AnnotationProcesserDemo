package com.example.qhh_annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author qinhaihang_vendor
 * @version $Rev$
 * @time 2019/3/6 19:30
 * @des
 * @packgename com.example.qhh_annotation
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.CLASS)
public @interface BindView {
    int value();
}
