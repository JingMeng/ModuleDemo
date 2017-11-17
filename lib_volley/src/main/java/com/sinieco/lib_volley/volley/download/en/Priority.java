package com.sinieco.lib_volley.volley.download.en;

/**
 * @author BaiMeng on 2017/11/17.
 */

public enum  Priority {
    /**
     *
     */
    low(0),
    /**
     *
     */
    middle(1),
    /**
     *
     */
    high(2);

    private int value ;

    Priority(int value){
        this.value = value ;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public static Priority getInstance(int value)
    {
        for (Priority priority : Priority.values())
        {
            if (priority.getValue() == value)
            {
                return priority;
            }
        }
        return Priority.middle;
    }
}
