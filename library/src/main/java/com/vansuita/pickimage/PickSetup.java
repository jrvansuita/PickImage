package com.vansuita.pickimage;

import android.graphics.Color;

import java.io.Serializable;

/**
 * Created by jrvansuita on 01/11/16.
 */

public class PickSetup implements Serializable {

    private static final long serialVersionUID = 1L;

    private String title;
    private int backgroundColor;
    private int titleColor;
    private String cancelText;
    private int optionsColor;
    private float dimAmount;
    private boolean flip;
    private int imageSize;



    public String getCancelText() {
        return cancelText;
    }

    public PickSetup setCancelText(String text) {
        this.cancelText = text;
        return this;
    }
    public String getTitle() {
        return title;
    }

    public PickSetup setTitle(String title) {
        this.title = title;
        return this;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public PickSetup setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public int getTitleColor() {
        return titleColor;
    }

    public PickSetup setTitleColor(int titleColor) {
        this.titleColor = titleColor;
        return this;
    }

    public int getOptionsColor() {
        return optionsColor;
    }

    public PickSetup setOptionsColor(int optionsColor) {
        this.optionsColor = optionsColor;
        return this;
    }

    public float getDimAmount() {
        return dimAmount;
    }

    public PickSetup setDimAmount(float dimAmount) {
        this.dimAmount = dimAmount;
        return this;
    }

    public int getImageSize() {
        return imageSize;
    }

    public void setImageSize(int imageSize) {
        this.imageSize = imageSize;
    }

    public boolean isFlipped() {
        return flip;
    }

    public PickSetup setFlip(boolean flip) {
        this.flip = flip;
        return this;
    }

    public PickSetup() {
        this.title = "Choose";
        this.backgroundColor = Color.WHITE;
        this.titleColor = Color.DKGRAY;
        this.optionsColor = Color.GRAY;
        this.dimAmount = 0.3f;
        this.flip = false;
        this.cancelText = "Cancel";
        this.imageSize = 300;
    }


}
