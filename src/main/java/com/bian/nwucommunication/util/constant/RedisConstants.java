package com.bian.nwucommunication.util.constant;

public class RedisConstants {
    public static final String LOGIN_CODE_KEY = "login:code:";
    public static final Long LOGIN_CODE_TTL = 2L;
    public static final String LOGIN_USER_KEY = "login:token:";
    public static final Long LOGIN_USER_TTL = 36L;

    public static final Long CACHE_NULL_TTL = 2L;

    public static final Long CACHE_All_School_TTL = 30L;
    public static final String CACHE_All_School_KEY = "allschool:";

    public static final Long CACHE_CODE_TTL = 30L;
    public static final String CACHE_CODE_KEY = "cache:code:";


    public static final Integer CACHE_SCANS_COUNT = 100;
}
