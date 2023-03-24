package com.gdx.engine.util;

import com.badlogic.gdx.physics.box2d.BodyDef;
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

    public static BodyDef.BodyType getBodyTypeDef(boolean staticBodyType) {
        return staticBodyType ? BodyDef.BodyType.StaticBody : BodyDef.BodyType.DynamicBody;
    }

}
