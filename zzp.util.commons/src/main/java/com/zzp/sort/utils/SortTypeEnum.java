package com.zzp.sort.utils;

/**
 * 排序类型
 * @author karyzeng
 * @since 2019.07.26
 */
public enum SortTypeEnum {

    ASC("ASC", "升序"),
    DESC("DESC", "降序");

    private String code;

    private String name;

    private SortTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }}
