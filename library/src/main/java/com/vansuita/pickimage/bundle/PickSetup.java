package com.vansuita.pickimage.bundle;

import android.graphics.Color;
import androidx.annotation.IntDef;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.vansuita.pickimage.R;
import com.vansuita.pickimage.enums.EPickType;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by jrvansuita build 01/11/16.
 */

public class PickSetup implements Serializable {

    private static final long serialVersionUID = 1L;

    private String title;
    private int titleColor;

    private int backgroundColor;

    private String progressText;
    private int progressTextColor;

    private String cancelText;
    private int cancelTextColor;

    private int buttonTextColor;

    private float dimAmount;
    private boolean flip;

    private int maxSize = 300;
    private int width = 0;
    private int height = 0;

    private EPickType[] pickTypes;

    private int cameraIcon;
    private int galleryIcon;

    private String cameraButtonText;
    private String galleryButtonText;

    private boolean systemDialog;

    private boolean video;

    private boolean isCameraToPictures;

    @OrientationMode
    private int buttonOrientation;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({LinearLayout.VERTICAL, LinearLayout.HORIZONTAL})
    public @interface OrientationMode {
    }

    @IconGravity
    private int iconGravity;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({Gravity.LEFT, Gravity.BOTTOM, Gravity.RIGHT, Gravity.TOP})
    public @interface IconGravity {
    }

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

    public String getCameraButtonText() {
        return cameraButtonText;
    }

    public PickSetup setCameraButtonText(String cameraButtonText) {
        this.cameraButtonText = cameraButtonText;
        return this;
    }

    public String getGalleryButtonText() {
        return galleryButtonText;
    }

    public PickSetup setGalleryButtonText(String galleryButtonText) {
        this.galleryButtonText = galleryButtonText;
        return this;
    }

    public int getButtonTextColor() {
        return buttonTextColor;
    }

    public PickSetup setButtonTextColor(int buttonTextColor) {
        this.buttonTextColor = buttonTextColor;
        return this;
    }

    public float getDimAmount() {
        return dimAmount;
    }

    public PickSetup setDimAmount(float dimAmount) {
        this.dimAmount = dimAmount;
        return this;
    }


    public EPickType[] getPickTypes() {
        return pickTypes;
    }

    public PickSetup setPickTypes(EPickType... pickTypes) {
        this.pickTypes = pickTypes;
        return this;
    }

    public int getCancelTextColor() {
        return cancelTextColor;
    }

    public PickSetup setCancelTextColor(int cancelTextColor) {
        this.cancelTextColor = cancelTextColor;
        return this;
    }

    public int getWidth() {
        return width;
    }

    public PickSetup setWidth(int width) {
        this.width = width;
        return this;
    }

    public int getHeight() {
        return height;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public PickSetup setMaxSize(int maxSize) {
        this.maxSize = maxSize;
        return this;
    }

    public PickSetup setHeight(int height) {
        this.height = height;
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

    @OrientationMode
    public int getButtonOrientation() {
        return buttonOrientation;
    }

    public PickSetup setButtonOrientation(@OrientationMode int buttonOrientation) {
        this.buttonOrientation = buttonOrientation;
        return this;
    }

    public PickSetup setButtonOrientationInt(int buttonOrientation) {
        this.buttonOrientation = buttonOrientation;
        return this;
    }


    public int getCameraIcon() {
        return cameraIcon;
    }

    public PickSetup setCameraIcon(int cameraIcon) {
        this.cameraIcon = cameraIcon;
        return this;
    }

    public int getGalleryIcon() {
        return galleryIcon;
    }

    public PickSetup setGalleryIcon(int galleryIcon) {
        this.galleryIcon = galleryIcon;
        return this;
    }

    public boolean isSystemDialog() {
        return systemDialog;
    }

    public PickSetup setSystemDialog(boolean systemDialog) {
        this.systemDialog = systemDialog;
        return this;
    }

    public boolean isCameraToPictures() {
        return isCameraToPictures;
    }

    public PickSetup setCameraToPictures(boolean isCameraToPictures) {
        this.isCameraToPictures = isCameraToPictures;
        return this;
    }

    @IconGravity
    public int getIconGravity() {
        return iconGravity;
    }

    public PickSetup setIconGravity(@IconGravity int iconGravity) {
        this.iconGravity = iconGravity;
        return this;
    }

    public PickSetup setIconGravityInt(int iconGravity) {
        this.iconGravity = iconGravity;
        return this;
    }

    public boolean isVideo() {
        return video;
    }

    public PickSetup setVideo(boolean video){
        this.video = video;
        return this;
    }

    public PickSetup() {
        setTitle("Choose")
                .setBackgroundColor(Color.WHITE)
                .setTitleColor(Color.DKGRAY)
                .setDimAmount(0.3f)
                .setFlip(false)
                .setCancelText("Cancel")
                .setMaxSize(300)
                .setWidth(0)
                .setHeight(0)
                .setPickTypes(EPickType.CAMERA, EPickType.GALLERY)
                .setProgressText("Loading...")
                .setButtonOrientation(LinearLayout.VERTICAL)
                .setCameraIcon(R.drawable.camera)
                .setSystemDialog(false)
                .setCameraToPictures(true)
                .setGalleryIcon(R.drawable.gallery)
                .setVideo(false);
    }

}
