package com.vansuita.pickimage;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.vansuita.library.IPickResult;
import com.vansuita.library.PickImageDialog;
import com.vansuita.library.PickSetup;

public class MainActivity extends AppCompatActivity implements IPickResult.IPickResultBitmap
        , IPickResult.IPickResultUri
        //, IPickResult.IPickClick
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViewById(R.id.test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickSetup setup = new PickSetup();

                //setup.setBackgroundColor(yourColor);
                //setup.setTitle(yourTitle);
                //setup.setDimAmount(yourFloat);
                //setup.setTitleColor(yourColor);
                //setup.setFlip(true);

                PickImageDialog.on(MainActivity.this, setup);
            }
        });

    }

    @Override
    public void onPickImageResult(Bitmap bitmap) {
        ((ImageView) findViewById(R.id.result_image)).setImageBitmap(bitmap);
    }

    @Override
    public void onPickImageResult(Uri bitmap) {
        ((ImageView) findViewById(R.id.result_image)).setImageURI(bitmap);
    }


    /*@Override
    public void onGaleryClick() {
        Toast.makeText(this, "Implement your own functionality", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCameraClick() {
        Toast.makeText(this, "Implement your own functionality", Toast.LENGTH_LONG).show();
    }*/
}
