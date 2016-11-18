package com.vansuita.library;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import static android.app.Activity.RESULT_OK;
import static com.vansuita.library.R.layout.dialog;

/**
 * Created by jrvansuita on 01/11/16.
 */

public class PickImageDialog extends DialogFragment {

    public static PickImageDialog newInstance(PickSetup setup) {
        PickImageDialog frag = new PickImageDialog();
        Bundle args = new Bundle();
        args.putSerializable(SETUP_TAG, setup);
        frag.setArguments(args);
        return frag;
    }

    public static PickImageDialog on(AppCompatActivity activity, PickSetup setup) {
        PickImageDialog d = PickImageDialog.newInstance(setup);
        d.show(activity.getSupportFragmentManager(), "dialog");
        return d;
    }

    private static final String SETUP_TAG = "SETUP_TAG";
    private static final int FROM_CAMERA = 1;
    private static final int FROM_GALLERY = 2;

    private CardView cvRoot;
    private TextView tvTitle;
    private TextView tvCamera;
    private TextView tvGallery;
    private TextView tvCancel;

    private IPickResult.IPickResultBitmap bitmapListener;
    private IPickResult.IPickResultUri uriListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            if (context instanceof IPickResult.IPickResultBitmap)
                bitmapListener = (IPickResult.IPickResultBitmap) context;

            if (context instanceof IPickResult.IPickResultUri)
                uriListener = (IPickResult.IPickResultUri) context;


        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement IPickResult.?");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        cvRoot = (CardView) inflater.inflate(dialog, null, false);

        bindView();
        setUp();
        bindListeners();

        return cvRoot;
    }

    private PickSetup setup;

    private void setUp() {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setup = (PickSetup) getArguments().getSerializable(SETUP_TAG);

        cvRoot.setCardBackgroundColor(setup.getBackgroundColor());
        tvTitle.setTextColor(setup.getTitleColor());
        tvCamera.setTextColor(setup.getOptionsColor());
        tvGallery.setTextColor(setup.getOptionsColor());

        tvTitle.setText(setup.getTitle());

        setDimAcount(setup.getDimAmount());
    }


    private void bindView() {
        tvTitle = (TextView) cvRoot.findViewById(R.id.title);
        tvCamera = (TextView) cvRoot.findViewById(R.id.camera);
        tvGallery = (TextView) cvRoot.findViewById(R.id.gallery);
        tvCancel = (TextView) cvRoot.findViewById(R.id.cancel);
    }

    private void setDimAcount(float dim) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            getDialog().getWindow().setDimAmount(dim);
        } else {
            WindowManager.LayoutParams lp = getDialog().getWindow().getAttributes();
            lp.dimAmount = dim;
            getDialog().getWindow().setAttributes(lp);
        }
    }

    private void bindListeners() {
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        tvCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, FROM_CAMERA);
                }
            }
        });

        tvGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, FROM_GALLERY);
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            dismiss();

            if (bitmapListener != null) {
                try {
                    Bitmap bitmap = null;

                    if (requestCode == FROM_CAMERA) {
                        bitmap = (Bitmap) data.getExtras().get("data");

                    } else if (requestCode == FROM_GALLERY) {
                        bitmap = Util.decodeUri(data.getData(), getActivity());
                    }

                    if (setup.isFlipped())
                        bitmap = Util.flip(bitmap);

                    bitmapListener.onPickImageResult(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (uriListener != null) {
                uriListener.onPickImageResult(data.getData());
            }
        }
    }
}
