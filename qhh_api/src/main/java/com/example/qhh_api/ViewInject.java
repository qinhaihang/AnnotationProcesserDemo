package com.example.qhh_api;

/**
 * @author qinhaihang_vendor
 * @version $Rev$
 * @time 2019/3/6 20:24
 * @des
 * @packgename com.example.qhh_api
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes
 */
public interface ViewInject<T> {
    void inject(T t, Object object);
}
