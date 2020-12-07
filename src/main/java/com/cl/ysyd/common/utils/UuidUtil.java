package com.cl.ysyd.common.utils;

import java.util.UUID;

/**
 * ClassName: UuidUtils.
 */
public final class UuidUtil {
    /**
     * <p>
     * Description: getUuid.
     * </p>
     *
     * @return String
     */
    public static final String getUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
