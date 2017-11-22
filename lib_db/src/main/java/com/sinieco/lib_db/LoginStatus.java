package com.sinieco.lib_db;

/**
 * @author BaiMeng on 2017/11/21.
 */

public enum LoginStatus {
    outline(0),
    online(1);
    int value ;
    LoginStatus (int value){
        this.value = value ;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value){
        this.value = value ;
    }

    public static LoginStatus getInstance(int value){
        for (LoginStatus status : LoginStatus.values()) {
            if(status.getValue()==value){
                return status ;
            }
        }
        return outline;
    }
}
