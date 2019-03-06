package com.example.qhh_api;

import android.app.Activity;

/**
 * @author qinhaihang_vendor
 * @version $Rev$
 * @time 2019/3/6 20:23
 * @des
 * @packgename com.example.qhh_api
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes
 */
public class QhhButterKnife {

    private static final String SUFFIX = "$$" + ViewInject.class.getSimpleName();

    public static void inject(Activity activity){
        inject(activity,activity);
    }

    public static void inject(Object host, Object root) {
        Class<?> clazz = host.getClass();

        //拼接类的全路径
        String proxyClassFullName = clazz.getName() + SUFFIX;

        //通过全路径获取 class类
        Class<?> proxyClazz;
        try {
            proxyClazz = Class.forName(proxyClassFullName);

            //通过newInstance生成实例，强转，调用代理类的inject方法
            ViewInject viewInject = (ViewInject) proxyClazz.newInstance();

            //调用生成类里面的inject方法，进行findViewById
            viewInject.inject(host,root);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e){
            e.printStackTrace();
        } catch (InstantiationException e){
            e.printStackTrace();
        }
    }
}
