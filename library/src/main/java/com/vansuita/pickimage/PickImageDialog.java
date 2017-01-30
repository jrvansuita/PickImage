package com.vansuita.pickimage;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.listeners.IPickClick;
import com.vansuita.pickimage.listeners.IPickResult;

import static android.app.Activity.RESULT_OK;
import static com.vansuita.pickimage.R.layout.dialog;
import static com.vansuita.pickimage.Util.tempUri;


/**
 * Created by jrvansuita on 01/11/16.
 */

public class PickImageDialog extends DialogFragment {

    public static PickImageDialog newInstance(PickSetup setup) {
        if (setup == null)
            throw new Error("Not defined a PickSetup - .on() method");

        PickImageDialog frag = new PickImageDialog();
        Bundle args = new Bundle();
        args.putSerializable(SETUP_TAG, setup);
        frag.setArguments(args);
        return frag;
    }

    public static PickImageDialog on(FragmentManager fm, PickSetup setup, IPickResult pickResult) {
        PickImageDialog d = PickImageDialog.newInstance(setup);
        d.setOnPickResult(pickResult);
        d.show(fm, "dialog");
        return d;
    }

    public static PickImageDialog on(FragmentManager fm, PickSetup setup) {
        return on(fm, setup, null);
    }

    public static PickImageDialog on(FragmentManager fm, String applicationID, IPickResult pickResult) {
        return on(fm, new PickSetup(applicationID), pickResult);
    }

    public static PickImageDialog on(FragmentManager fm, String applicationID) {
        return on(fm, applicationID, null);
    }

    public static PickImageDialog on(FragmentActivity activity, PickSetup setup) {
        return on(activity.getSupportFragmentManager(), setup);
    }

    public static PickImageDialog on(FragmentActivity activity, String applicationID) {
        return on(activity, new PickSetup(applicationID));
    }


    private static final String SETUP_TAG = "SETUP_TAG";
    private static final int FROM_CAMERA = 1;
    private static final int FROM_GALLERY = 2;

    private CardView card;
    private LinearLayout llButtons;
    private TextView tvTitle;
    private TextView tvCamera;
    private TextView tvGallery;
    private TextView tvCancel;
    private TextView tvProgress;

    private View vFirstLayer;
    private View vSecondLayer;

    private IPickResult onPickResult;

    public PickImageDialog setOnPickResult(IPickResult onPickResult) {
        this.onPickResult = onPickResult;
        return this;
    }

    private IPickClick onClick;

    public PickImageDialog setOnClick(IPickClick onClick) {
        this.onClick = onClick;
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(dialog, null, false);

        bindView(view);
        setUp();
        bindListeners();

        requestPermissions();

        return view;
    }

    private PickSetup setup;

    private void setUp() {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setup = (PickSetup) getArguments().getSerializable(SETUP_TAG);

        boolean showCamera = EPickTypes.CAMERA.inside(setup.getPickTypes());
        boolean showGallery = EPickTypes.GALLERY.inside(setup.getPickTypes());

        if (setup.getBackgroundColor() != android.R.color.white) {
            card.setCardBackgroundColor(setup.getBackgroundColor());

            if (showCamera)
                Util.background(tvCamera, Util.getAdaptiveRippleDrawable(setup.getBackgroundColor()));

            if (showGallery)
                Util.background(tvGallery, Util.getAdaptiveRippleDrawable(setup.getBackgroundColor()));
        }

        tvTitle.setTextColor(setup.getTitleColor());

        if (setup.getButtonTextColor() != 0) {
            tvCamera.setTextColor(setup.getButtonTextColor());
            tvGallery.setTextColor(setup.getButtonTextColor());
        }

        if (setup.getProgressTextColor() != 0)
            tvProgress.setTextColor(setup.getProgressTextColor());

        if (setup.getCancelTextColor() != 0)
            tvCancel.setTextColor(setup.getCancelTextColor());

        if (setup.getCameraButtonText() != null)
            tvCamera.setText(setup.getCameraButtonText());

        if (setup.getGalleryButtonText() != null)
            tvGallery.setText(setup.getGalleryButtonText());

        tvCancel.setText(setup.getCancelText());
        tvTitle.setText(setup.getTitle());
        tvProgress.setText(setup.getProgressText());

        visibleProgress(false);

        Util.gone(tvCamera, !showCamera);
        Util.gone(tvGallery, !showGallery);

        llButtons.setOrientation(setup.getButtonOrientation() == LinearLayoutCompat.HORIZONTAL ? LinearLayout.HORIZONTAL : LinearLayout.VERTICAL);

        Util.setIcon(tvCamera, setup.getCameraIcon(), setup.getIconGravity());
        Util.setIcon(tvGallery, setup.getGalleryIcon(), setup.getIconGravity());


        Util.setDimAmount(setup.getDimAmount(), getDialog());

        onAttaching(getActivity());
    }

    public void onAttaching(Context context) {

        if (onClick == null && context instanceof IPickClick)
            onClick = (IPickClick) context;

        if (onPickResult == null && context instanceof IPickResult)
            onPickResult = (IPickResult) context;
    }


    private void bindView(View v) {
        card = (CardView) v.findViewById(R.id.card);
        llButtons = (LinearLayout) v.findViewById(R.id.buttons_holder);
        tvTitle = (TextView) v.findViewById(R.id.title);
        tvCamera = (TextView) v.findViewById(R.id.camera);
        tvGallery = (TextView) v.findViewById(R.id.gallery);
        tvCancel = (TextView) v.findViewById(R.id.cancel);
        tvProgress = (TextView) v.findViewById(R.id.loading_text);

        vFirstLayer = v.findViewById(R.id.first_layer);
        vSecondLayer = v.findViewById(R.id.second_layer);
    }

    private void bindListeners() {
        tvCancel.setOnClickListener(listener);
        tvCamera.setOnClickListener(listener);
        tvGallery.setOnClickListener(listener);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.cancel) {
                dismiss();

            } else if (view.getId() == R.id.camera) {
                if (onClick != null) {
                    onClick.onCameraClick();
                } else {
                    Util.launchCamera(PickImageDialog.this, setup.getAuthority(), FROM_CAMERA);
                }

            } else if (view.getId() == R.id.gallery) {
                if (onClick != null) {
                    onClick.onGalleryClick();
                } else {
                    Util.launchGalery(PickImageDialog.this, FROM_GALLERY);
                }

            }
        }
    };


    private void visibleProgress(boolean show) {
        Util.gone(vFirstLayer, show);
        Util.gone(vSecondLayer, !show);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            visibleProgress(true);
            new AsyncResult(requestCode).execute(data);
        } else {
            dismiss();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                dismissAllowingStateLoss();
                break;
            }
        }
    }

    public boolean requestPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if ((getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) &&
                    getActivity().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA}, 1);
                return false;
            }
        }

        return true;
    }


    private class AsyncResult extends AsyncTask<Intent, Void, PickResult> {

        private int requestCode;

        public AsyncResult(int requestCode) {
            this.requestCode = requestCode;
        }

        @Override
        protected PickResult doInBackground(Intent... intents) {

            PickResult result = new PickResult();

            try {
                Intent data = intents[0];

                Bitmap bitmap = null;

                if (requestCode == FROM_CAMERA) {
                    bitmap = Util.decodeUri(tempUri(), getContext(), setup.getImageSize());

                    if (setup.isFlipped())
                        bitmap = Util.flip(bitmap);

                } else if (requestCode == FROM_GALLERY) {
                    bitmap = Util.decodeUri(data.getData(), getContext(), setup.getImageSize());
                }

                result.setBitmap(bitmap);

                Uri uri = null;
                if (requestCode == FROM_CAMERA) {
                    uri = tempUri();
                    result.setPath(uri.getPath());
                } else if (requestCode == FROM_GALLERY) {
                    uri = data.getData();
                    result.setPath(Util.getRealPathFromURI(getContext(), uri));
                }

                result.setUri(uri);

            } catch (Exception e) {
                result.setError(e);
            } finally {
                return result;
            }
        }


        @Override
        protected void onPostExecute(PickResult r) {
            if (onPickResult != null) {
                onPickResult.onPickResult(r);
            }

            dismiss();
        }
    }
}