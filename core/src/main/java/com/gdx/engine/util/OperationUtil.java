package com.gdx.engine.util;

import org.apache.commons.lang3.StringUtils;

public class OperationUtil {

    public static boolean getBooleanValue(String stringValue, boolean value) {
        if (stringValue == null || stringValue.equals(StringUtils.EMPTY)) {
            return !value;
        } else {
            if (stringValue.equals("1")) {
                return true;
            } else {
                return Boolean.parseBoolean(stringValue);
            }
        }
    }
}
