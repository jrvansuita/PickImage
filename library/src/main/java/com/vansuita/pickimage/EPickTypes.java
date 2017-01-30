package com.vansuita.pickimage;

import java.util.Arrays;

/**
 * Created by jrvansuita on 25/11/16.
 */

public enum EPickTypes {
    CAMERA(), GALLERY();

    public boolean inside(EPickTypes[] array) {
        return Arrays.asList(array).contains(this);
    }

    public static EPickTypes[] fromInt(int val) {
        if (val > values().length - 1){
            return new EPickTypes[] {CAMERA, GALLERY};
        }else{
            return new EPickTypes[] {values()[val]};
        }
    }
}
