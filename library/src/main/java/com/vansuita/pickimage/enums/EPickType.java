package com.vansuita.pickimage.enums;

import java.util.Arrays;

/**
 * Created by jrvansuita build 25/11/16.
 */

public enum EPickType {
    CAMERA, GALLERY;

    public boolean inside(EPickType[] array) {
        return Arrays.asList(array).contains(this);
    }

    public static EPickType[] fromInt(int val) {
        if (val > values().length - 1){
            return new EPickType[] {CAMERA, GALLERY};
        }else{
            return new EPickType[] {values()[val]};
        }
    }
}
