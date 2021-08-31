package com.logigear.crm.admins.util;

import java.util.UUID;

public class IDGeneratorUtil {

    /**
     * Get the random 32 characters from UUID generating process.
     *
     * @return The generated 32 characters of UUID string
     * @author bang.ngo
     **/
    public static String getRandom32CharUUIDString() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
