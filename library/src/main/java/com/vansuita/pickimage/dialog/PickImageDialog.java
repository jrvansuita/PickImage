package com.vansuita.pickimage.dialog;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.keep.Keep;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickClick;
import com.vansuita.pickimage.listeners.IPickResult;
import com.vansuita.pickimage.resolver.IntentResolver;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import static androidx.appcompat.app.AppCompatActivity.RESULT_OK;


/**
 * Created by jrvansuita build 01/11/16.
 */

public class PickImageDialog extends PickImageBaseDialog {

    public static PickImageDialog newInstance(PickSetup setup) {
        PickImageDialog frag = new PickImageDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable(SETUP_TAG, setup);
        frag.setArguments(bundle);
        return frag;
    }

    public static PickImageDialog build(PickSetup setup, IPickResult pickResult) {
        PickImageDialog d = PickImageDialog.newInstance(setup);
        d.setOnPickResult(pickResult);
        return d;
    }

    public static PickImageDialog build(IPickResult pickResult) {
        return build(new PickSetup(), pickResult);
    }

    public static PickImageDialog build(PickSetup setup) {
        return build(setup, null);
    }

    public static PickImageDialog build() {
        return build();
    }

    public PickImageDialog show(FragmentActivity fragmentActivity) {
        return show(fragmentActivity.getSupportFragmentManager());
    }

    public PickImageDialog show(FragmentManager fragmentManager) {
        super.show(fragmentManager, DIALOG_FRAGMENT_TAG);

        return this;
    }

    @Override
    public void onCameraClick() {
        launchCamera();
    }

    @Override
    public void onGalleryClick() {
        launchGallery();
    }

    @Override
    public PickImageDialog setOnClick(IPickClick onClick) {
        return (PickImageDialog) super.setOnClick(onClick);
    }

    @Override
    public PickImageDialog setOnPickResult(IPickResult onPickResult) {
        return (PickImageDialog) super.setOnPickResult(onPickResult);
    }

    @Override
  public PickImageDialog setOnPickCancel(IPickCancel onPickCancel)
   {
    return (PickImageDialog) super.setOnPickCancel(onPickCancel);
   }


  @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentResolver.REQUESTER) {
            if (resultCode == RESULT_OK) {
                //Show progress
                showProgress(true);

                //Handle the image result async
                getAsyncResult().execute(data);
            } else {
                dismissAllowingStateLoss();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == IntentResolver.REQUESTER) {
            boolean granted = true;

            for (Integer i : grantResults)
                granted = granted && i == PackageManager.PERMISSION_GRANTED;

            if (granted) {
                if (!launchSystemDialog()) {
                    // See if the CAMERA permission is among the granted ones
                    int cameraIndex = -1;
                    for (int i = 0; i < permissions.length; i++) {
                         if (permissions[i].equals(Manifest.permission.CAMERA)) {
                            cameraIndex = i;
                             break;
                         }
                    }

                    if (cameraIndex == -1) {
                        launchGallery();
                    } else {
                        launchCamera();
                    }
                }
            } else {
                dismissAllowingStateLoss();

                if (grantResults.length > 1)
                    Keep.with(getActivity()).askedForPermission();
            }
        }
    }


   /* public static void forceDismiss(FragmentManager fm) {
        Fragment fragment = fm.findFragmentByTag(PickImageDialog.DIALOG_FRAGMENT_TAG);

        if (fragment != null) {
            DialogFragment dialog = (PickImageDialog) fragment;

            if (dialog.isVisible())
                dialog.dismiss();
        }
    }*/


}


