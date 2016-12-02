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
    private int progressTextColor;
    private String progressText;
    private String cancelText;
    private int optionsColor;
    private float dimAmount;
    private boolean flip;
    private int imageSize;
    private EPickTypes[] pickTypes;


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


    public EPickTypes[] getPickTypes() {
        return pickTypes;
    }

    public PickSetup setPickTypes(EPickTypes... pickTypes) {
        this.pickTypes = pickTypes;
        return this;
    }

    public int getImageSize() {
        return imageSize;
    }

    public PickSetup setImageSize(int imageSize) {
        this.imageSize = imageSize;
        return this;
    }

    public boolean isFlipped() {
        return flip;
    }

    public PickSetup setFlip(boolean flip) {
        this.flip = flip;
        return this;
    }


    public int getProgressTextColor() {
        return progressTextColor;
    }

    public PickSetup setProgressTextColor(int progressTextColor) {
        this.progressTextColor = progressTextColor;
        return this;
    }

    public String getProgressText() {
        return progressText;
    }

    public PickSetup setProgressText(String progressText) {
        this.progressText = progressText;
        return this;
    }

    public PickSetup() {
        setTitle("Choose")
                .setBackgroundColor(Color.WHITE)
                .setTitleColor(Color.DKGRAY)
                .setDimAmount(0.3f)
                .setFlip(false)
                .setCancelText("Cancel")
                .setImageSize(300)
                .setPickTypes(EPickTypes.CAMERA, EPickTypes.GALERY)
                .setProgressText("Loading...");

    }


}
