package com.vansuita.pickimage.sample.act;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

public class SampleActivity extends BaseSampleActivity implements IPickResult /*, IPickClick*/ {



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* Set your layout and bind your views here */
        /* ... ... ... */
    }

    @Override
    protected void onImageViewClick() {
        PickSetup setup = new PickSetup();

        super.customize(setup);

     PickImageDialog.on(SampleActivity.this, setup);



        //Intent chooseImageIntent = ImagePicker.getPickImageIntent(this);
        //startActivityForResult(chooseImageIntent, PICK_IMAGE_ID);



        //If you don't have an Activity, you can set the FragmentManager
        /*PickImageDialog.on(getSupportFragmentManager(), setup, new IPickResult() {
            @Override
            public void onPickResult(PickResult r) {
                r.getBitmap();
                r.getError();
                r.getUri();
            }
        });*/

        //For overriding the click events you can do this
        /*PickImageDialog.on(getSupportFragmentManager(), setup).setOnClick(new IPickClick() {
            @Override
            public void onGalleryClick() {

            }

            @Override
            public void onCameraClick() {

            }
        });*/
    }

    @Override
    public void onPickResult(PickResult r) {
        if (r.getError() == null) {
            //If you want the Uri.
            //Mandatory to refresh image from Uri.
            //getImageView().setImageURI(null);

            //Setting the real returned image.
            //getImageView().setImageURI(r.getUri());

            //If you want the Bitmap.
            getImageView().setImageBitmap(r.getBitmap());

            //Image path
            //r.getPath();
        } else {
            //Handle possible errors
            //TODO: do what you have to do with r.getError();
            Toast.makeText(this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
        }

        scrollToTop();
    }

    /*
    If you use setOnClick(this), you have to implements this bellow methods

    @Override
    public void onGalleryClick() {
        Toast.makeText(this, "Implement your own functionality", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCameraClick() {
        Toast.makeText(this, "Implement your own functionality", Toast.LENGTH_LONG).show();
    }*/
}
