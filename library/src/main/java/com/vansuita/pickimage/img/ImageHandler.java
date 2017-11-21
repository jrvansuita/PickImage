package com.vansuita.pickimage.img;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;

import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.enums.EPickType;

import java.io.FileNotFoundException;

import static android.graphics.BitmapFactory.decodeStream;

/**
 * Created by jrvansuita build 07/02/17.
 */

public class ImageHandler {

    private Context context;
    private Uri uri;
    private EPickType provider;
    private PickSetup setup;

    public ImageHandler(Context context) {
        this.context = context;
    }

    public static ImageHandler with(Context context) {
        return new ImageHandler(context);
    }

    public ImageHandler uri(Uri uri) {
        this.uri = uri;
        return this;
    }

    public ImageHandler provider(EPickType provider) {
        this.provider = provider;
        return this;
    }

    public ImageHandler setup(PickSetup setup) {
        this.setup = setup;
        return this;
    }

    private Bitmap rotateIfNeeded(Bitmap bitmap) {
        int rotation;

        if (provider == EPickType.CAMERA) {
            rotation = getRotationFromCamera();
        } else {
            rotation = getRotationFromGallery();
        }

        return rotate(bitmap, rotation);
    }

    private int getRotationFromCamera() {
        int rotate = 0;
        try {

            ExifInterface exif = new ExifInterface(uri.getPath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
                default:
                    rotate = 0;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }

    private int getRotationFromGallery() {
        int result = 0;
        String[] columns = {MediaStore.Images.Media.ORIENTATION};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, columns, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int orientationColumnIndex = cursor.getColumnIndex(columns[0]);
                result = cursor.getInt(orientationColumnIndex);
            }
        } catch (Exception e) {

        } finally {
            if (cursor != null)
                cursor.close();
        }

        return result;
    }


    public Bitmap rotate(Bitmap bitmap, int degrees) {
        if (bitmap != null && degrees != 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(degrees);
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }

        return bitmap;
    }

    private Bitmap flip(Bitmap bitmap) {
        Matrix m = new Matrix();
        m.preScale(-1, 1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, false);
    }


    public Bitmap decode() throws FileNotFoundException {
        //Notify image changed
        // context.getContentResolver().notifyChange(uri, null);

        if (setup.getWidth() == 0 && setup.getHeight() == 0) {
            setup.setWidth(setup.getMaxSize());
            setup.setHeight(setup.getMaxSize());
        }

        Bitmap bitmap;

        if ((setup.getWidth() - setup.getHeight()) == 0) {
            bitmap = scaleDown();
        } else {
            bitmap = resize();
        }

        if (provider.equals(EPickType.CAMERA) && setup.isFlipped())
            bitmap = flip(bitmap);

        return rotateIfNeeded(bitmap);
    }

    public Uri getUri() {
        return uri;
    }

    public String getUriPath() {
        if (provider.equals(EPickType.CAMERA)) {
            return uri.getPath();
        } else {
            return getGalleryPath();
        }
    }

    private String getGalleryPath() {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(uri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            return uri.getPath();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private BitmapFactory.Options getOptions() throws FileNotFoundException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        decodeStream(context.getContentResolver().openInputStream(uri), null, options);

        int w = options.outWidth;
        int h = options.outHeight;
        int scale = 1;
        while (true) {
            if (w / 2 < setup.getWidth() || h / 2 < setup.getHeight())
                break;

            w /= 2;
            h /= 2;
            scale *= 2;
        }

        options = new BitmapFactory.Options();
        options.inSampleSize = scale;
        return options;
    }


    private Bitmap scaleDown() throws FileNotFoundException {
        return decodeStream(context.getContentResolver().openInputStream(uri), null, getOptions());
    }

    public Bitmap resize() throws FileNotFoundException {
        return Bitmap.createScaledBitmap(scaleDown(), setup.getWidth(), setup.getHeight(), false);
    }

}
