package com.simon.common.code;

/**
 * @author simon
 * @create 2018-08-07
 **/

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 代码生成列实体
 * @author simon
 * @date  2018-08-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Column implements Serializable{
    private static final long serialVersionUID = 8051725962502921942L;
    /**
     * 属性注解
     */
    private String annotation;

    /**
     * 属性名
     */
    private String name;

    /**
     * 属性类型
     */
    private String type;

    /**
     * 属性注释
     */
    private String comment;
}