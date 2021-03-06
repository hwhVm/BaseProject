package com.beini.util;

/**
 * Created by beini on 2017/2/9.
 */

public class ObjectUtil {

    public static Object createInstance(Class<?> className) {
        try {
            Class<?> c = Class.forName(className.getName());
            try {
                return c.newInstance();
            } catch (IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}
