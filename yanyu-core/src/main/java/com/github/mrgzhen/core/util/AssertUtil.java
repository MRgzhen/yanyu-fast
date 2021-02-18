package com.github.mrgzhen.core.util;

import cn.hutool.core.util.ObjectUtil;
import com.github.mrgzhen.core.exception.GeneralException;
import com.github.mrgzhen.core.exception.ServiceException;

/**
 * @author yanyu
 */
public class AssertUtil {

    public static void isNotNull(Object obj,String message) {
        isTrue(
                ObjectUtil.isNotEmpty(obj), message);
    }

    public static void isNull(Object obj,String message) {
        isTrue(ObjectUtil.isEmpty(obj), message);
    }

    public static void equals(Object obj,Object obj1, String message) {
        isTrue(ObjectUtil.equal(obj,obj1),message);
    }

    public static void notEquals(Object obj,Object obj1, String message) {
        isTrue(ObjectUtil.notEqual(obj,obj1),message);
    }

    public static void isTrue(boolean expression,String message) {
        if(!expression) {
            throw new ServiceException(message);
        }
    }


}
