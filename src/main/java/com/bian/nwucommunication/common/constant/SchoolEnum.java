package com.bian.nwucommunication.common.constant;


public enum SchoolEnum {
    NWU(1,"西北大学"),
    XAUT(2,"西安理工大学"),
    XI_DIAN(3,"西安电子科技大学"),
    XIDIAN(5, "西安电子科技大学"),
    XJTU(6, "西安交通大学"),
    NWPU(7, "西北工业大学"),
    XJUK(9, "西安建筑科技大学");

    private int code;

    private String name;


    SchoolEnum(int code,String name) {
        this.code = code;
        this.name = name;
    }

    // 获取代码的方法
    public int getCode() {
        return code;
    }

    // 获取名字的方法
    public String getName() {
        return name;
    }

    public static int getCodeByName(String name) {
        for (SchoolEnum school : SchoolEnum.values()) {
            if (school.getName().equals(name)) {
                return school.getCode();
            }
        }
        return 0;
    }

    public static String getNameByCode(int code) {
        for (SchoolEnum school : SchoolEnum.values()) {
            if (school.getCode() == (code)) {
                return school.getName();
            }
        }
        return "未知";
    }
}
