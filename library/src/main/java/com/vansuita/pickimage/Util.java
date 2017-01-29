package com.vansuita.pickimage;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.FileProvider;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.vansuita.pickimage.bean.PickResult;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by jrvansuita on 01/11/16.
 */

public class Util {

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void background(View v, Drawable d) {
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            v.setBackgroundDrawable(d);
        } else {
            v.setBackground(d);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void background(View v, Bitmap b) {
        background(v, new BitmapDrawable(v.getResources(), b));
    }

    public static void launchGalery(DialogFragment frag, int code) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        frag.startActivityForResult(intent, code);
    }


    public static void launchCamera(DialogFragment frag, String authority, int code) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(frag.getActivity().getPackageManager()) != null) {

            Uri uri = FileProvider.getUriForFile(frag.getContext(),
                    authority,
                    tempFile());

            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            frag.startActivityForResult(intent, code);
        }
    }


    public static File tempFile() {
        File dir = new File(Environment.getExternalStorageDirectory(), PickResult.class.getSimpleName());
        dir.mkdirs();


        //Here I have to create a better name for the file.
        //Also, always have to clear the directory to keep only one image.
        return new File(dir, "temp.jpg");
    }

    public static Uri tempUri() {
        return Uri.fromFile(tempFile());
    }

    public static Bitmap flip(Bitmap bitmap) {
        Matrix m = new Matrix();
        m.preScale(-1, 1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, false);
    }

    public static Bitmap decodeUri(Uri selectedImage, Context context, int requiredSize) throws FileNotFoundException {
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImage), null, o);

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < requiredSize || height_tmp / 2 < requiredSize)
                break;

            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImage), null, o2);
    }


    public static void setDimAmount(float dim, Dialog dialog) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            dialog.getWindow().setDimAmount(dim);
        } else {
            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
            lp.dimAmount = dim;
            dialog.getWindow().setAttributes(lp);
        }
    }

    public static void gone(View v, boolean gone) {
        if (gone) {
            v.setVisibility(View.GONE);
        } else {
            v.setVisibility(View.VISIBLE);
        }
    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            return contentUri.getPath();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


    public static void setIcon(TextView tv, int icon, int gravity) {
        int left;
        int right = 0;
        int bottom = 0;
        int top = 0;

        if (gravity > 0) {
            left = gravity == Gravity.LEFT ? icon : 0;
            right = gravity == Gravity.RIGHT ? icon : 0;
            bottom = gravity == Gravity.BOTTOM ? icon : 0;
            top = gravity == Gravity.TOP ? icon : 0;
        } else {
            left = icon;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            tv.setCompoundDrawablesRelativeWithIntrinsicBounds(left, top, right, bottom);
        } else {
            tv.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
        }

        if (bottom + top != 0) {
            tv.setGravity(Gravity.CENTER);
        }
    }

}