package com.vansuita.pickimage;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.vansuita.library.IPickResult;
import com.vansuita.library.PickImageDialog;
import com.vansuita.library.PickSetup;

public class MainActivity extends AppCompatActivity implements IPickResult {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViewById(R.id.test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickSetup p = new PickSetup();
                PickImageDialog.on(MainActivity.this, p);
            }
        });

    }

    @Override
    public void onPickImageResult(Bitmap bitmap) {
        ((ImageView) findViewById(R.id.result_image)).setImageBitmap(bitmap);
    }
}
