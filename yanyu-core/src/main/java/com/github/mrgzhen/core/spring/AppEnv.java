package com.github.mrgzhen.core.spring;

import com.github.mrgzhen.core.util.AssertUtil;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;

/**
 * @author yanyu
 */
public abstract class AppEnv {

    static Environment _sharedEnv = null;

    public static boolean has(String key) {
        AssertUtil.isNotNull(_sharedEnv,"异常");
        return _sharedEnv.containsProperty(key);
    }

    public static String getString(String key) {
        AssertUtil.isNotNull(_sharedEnv, "异常");
        return _sharedEnv.getProperty(key);
    }

    public static String getString(String key, String defval) {
        AssertUtil.isNotNull(_sharedEnv, "异常");
        return _sharedEnv.getProperty(key, defval);
    }

    public static int getInt(String key, int defval) {
        return Integer.parseInt(getString(key, String.valueOf(defval)).trim());
    }

    public static String[] getStringArray(String key) {
        return getStringArray(key, "\\s*[,;]\\s*");
    }

    public static String[] getStringArray(String key, String regexp) {
        return getString(key, "").split(regexp);
    }

    public static int[] getIntArray(String key) {
        return getIntArray(key, "\\s*[,;]\\s*");
    }

    public static int[] getIntArray(String key, String regexp) {
        String[] items = getStringArray(key, regexp);

        int index = 0;
        int[] result = new int[items.length];
        for (String item : items) {
            result[(index++)] = Integer.parseInt(item.trim());
        }
        return result;
    }

    public static Class<?> getClass(String key) throws ClassNotFoundException {
        return Class.forName(getString(key).trim());
    }

    public static Class[] getClassArray(String key) throws ClassNotFoundException {
        return getClassArray(key, "\\s*[,;]\\s*");
    }

    public static Class[] getClassArray(String key, String regexp) throws ClassNotFoundException {
        String[] items = getStringArray(key, regexp);

        int index = 0;
        Class[] result = new Class[items.length];
        for (String item : items) {
            result[(index++)] = Class.forName(item.trim());
        }
        return result;
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(getString(key, "false"));
    }

    public static void setPropertySource(PropertySource<?> propertySource) {
        if (_sharedEnv instanceof ConfigurableEnvironment) {
            ConfigurableEnvironment _env = (ConfigurableEnvironment)_sharedEnv;
            if (!_env.getPropertySources().contains(propertySource.getName())) {
                _env.getPropertySources().addLast(propertySource);
            } else {
                _env.getPropertySources().replace(propertySource.getName(), propertySource);
            }
        }
    }

    public static boolean isLoaded() {
        return _sharedEnv != null;
    }
}
