package com.github.mrgzhen.core.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 * @author yanyu
 */
public class StringUtil {

    private static String EMPTY = "";
    private static String HAT = "^";

    private static Pattern linePattern = Pattern.compile("_(\\w)");

    /**
     * 下划线转换成驼峰
     */
    public static String lineToCamel(String str) {
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 实体首字母大写
     *
     */
    public static String capitalFirst(String name) {
        if (StringUtils.isNotBlank(name)) {
            return name.substring(0, 1).toUpperCase() + name.substring(1);
        }
        return EMPTY;
    }

    /**
     * 去掉指定的前缀
     */
    public static String removePrefix(String name, String... prefix) {
        if (StringUtils.isBlank(name)) {
            return EMPTY;
        }
        if (null != prefix) {
            // 判断是否有匹配的前缀，然后截取前缀
            // 删除前缀
            return Arrays.stream(prefix).filter(pf -> name.toLowerCase()
                    .matches(HAT + pf.toLowerCase() + ".*"))
                    .findFirst().map(pf -> name.substring(pf.length())).orElse(name);
        }
        return name;
    }

    /**
     * 空或null 返回默认值""
     */
    public static String defaultIfBlack(String str) {
        return StringUtils.defaultIfEmpty(str, EMPTY);
    }
}
