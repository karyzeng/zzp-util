package com.zzp.caches.utils;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 超时缓存value
 * @Author Garyzeng
 * @since 2019.11.22
 **/
public class TimeValue extends TimeBaseValue implements Serializable {

    private static final long serialVersionUID = -1L;

    /**
     * 内容
     */
    private List<String> list;

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

}
