package com.vansuita.pickimage;

import java.util.Arrays;

/**
 * Created by jrvansuita on 25/11/16.
 */

public enum EPickTypes {
    CAMERA(), GALERY();

    public boolean inside(EPickTypes[] array) {
        return Arrays.asList(array).contains(this);
    }
}
