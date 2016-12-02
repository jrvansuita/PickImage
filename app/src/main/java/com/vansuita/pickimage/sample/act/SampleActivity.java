package com.vansuita.pickimage.sample.act;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.vansuita.pickimage.PickImageDialog;
import com.vansuita.pickimage.PickSetup;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.listeners.IPickResult;
import com.vansuita.pickimage.sample.R;

public class SampleActivity extends AppCompatActivity implements IPickResult
        //, IPickClick
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
                //setup.setPickTypes(EPickTypes.GALERY, EPickTypes.CAMERA);
                //setup.setProgressText("Loading...");
                //setup.setProgressTextColor(Color.BLUE);


                PickImageDialog.on(SampleActivity.this, setup);


                //If you do not have an Activity, you can set the FragmentManager
                /*PickImageDialog.on(getSupportFragmentManager(), new IPickResult() {
                    @Override
                    public void onPickResult(PickResult r) {

                    }
                });*/


                //Setting the listeners on the constructor.
             /*   PickImageDialog.on(getSupportFragmentManager())
                        .setOnPickResult(new IPickResult() {
                            @Override
                            public void onPickResult(PickResult r) {

                            }
                        });*/

                //You can set the listerners like this.
                /*.setOnPickResult(new IPickResult() {
                    @Override
                    public void onPickResult(PickResult r) {
                        r.getBitmap();
                        r.getError();
                        r.getUri();

                    }
                }).setOnClick(new IPickClick() {
                    @Override
                    public void onGalleryClick() {

                    }

                    @Override
                    public void onCameraClick() {

                    }
                });*/
            }
        });

    }

    @Override
    public void onPickResult(PickResult r) {
        if (r.getError() == null) {
            ImageView imageView = ((ImageView) findViewById(R.id.result_image));

            //If you want the Bitmap.
            imageView.setImageBitmap(r.getBitmap());

            //If you want the Uri.
            //Mandatory to refresh image from Uri.
            imageView.setImageURI(null);

            //Setting the real returned image.
            imageView.setImageURI(r.getUri());
        } else {
            //Handle possible errors
            //TODO: do what you have to do with r.getError();
        }
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
