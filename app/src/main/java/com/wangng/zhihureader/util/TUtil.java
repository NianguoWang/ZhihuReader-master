package com.wangng.zhihureader.util;

import java.lang.reflect.ParameterizedType;

/**
 * Created by wng on 2016/12/31.
 */

public class TUtil {
    public static <T> T getT(Object o, int i) {
        try {
            /**
             * getGenericSuperclass() : 获得带有泛型的父类
             * ParameterizedType ： 参数化类型，即泛型
             * getActualTypeArguments()[] : 获取参数化类型的数组，泛型可能有多个
             */
            return ((Class<T>) ((ParameterizedType) (o.getClass()
                    .getGenericSuperclass())).getActualTypeArguments()[i])
                    .newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 获得类名className对应的Class对象
    public static Class<?> forName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
