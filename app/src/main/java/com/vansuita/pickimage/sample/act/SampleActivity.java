package com.vansuita.pickimage.sample.act;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.vansuita.pickimage.IPickResult;
import com.vansuita.pickimage.PickImageDialog;
import com.vansuita.pickimage.PickSetup;
import com.vansuita.pickimage.sample.R;

public class SampleActivity extends AppCompatActivity implements IPickResult.IPickError
        , IPickResult.IPickResultBitmap
        , IPickResult.IPickResultUri
        //, IPickResult.IPickClick
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_layout);


        findViewById(R.id.test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickSetup setup = new PickSetup();

                //setup.setBackgroundColor(yourColor);
                //setup.setTitle(yourTitle);
                //setup.setDimAmount(yourFloat);
                //setup.setTitleColor(yourColor);
                //setup.setFlip(true);
                //setup.setCancelText("Test");
                //setup.setImageSize(500);
                //setup.setPickTypes(EPickTypes.GALERY);

                PickImageDialog.on(SampleActivity.this, setup)
                        .setOnBitmapResult(new IPickResult.IPickResultBitmap() {
                            @Override
                            public void onPickImageResult(Bitmap bitmap) {
                                //TODO: getting the bitmap.
                            }
                        }).setOnBitmapResult(new IPickResult.IPickResultBitmap() {
                            @Override
                            public void onPickImageResult(Bitmap bitmap) {
                                 //TODO: getting the bitmap.
                        }
                        }).setOnError(new IPickResult.IPickError() {
                            @Override
                            public void onPickError(Exception e) {
                                //TODO: handle the error.
                        }
                        });
            }
        });

    }


    @Override
    public void onPickError(Exception e) {
        //TODO: handle the error.
    }


    @Override
    public void onPickImageResult(Bitmap bitmap) {
        ImageView imageView = ((ImageView) findViewById(R.id.result_image));

        imageView.setImageBitmap(bitmap);
    }

    @Override
    public void onPickImageResult(Uri bitmapUri) {
        ImageView imageView = ((ImageView) findViewById(R.id.result_image));

        //Mandatory to refresh image from Uri.
        imageView.setImageURI(null);

        //Setting the real returned image.
        imageView.setImageURI(bitmapUri);
    }


    /*@Override
    public void onGalleryClick() {
        Toast.makeText(this, "Implement your own functionality", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCameraClick() {
        Toast.makeText(this, "Implement your own functionality", Toast.LENGTH_LONG).show();
    }*/
}
