package com.vansuita.pickimage.dialog;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
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

import com.vansuita.pickimage.R;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.enums.EPickType;
import com.vansuita.pickimage.keep.Keep;
import com.vansuita.pickimage.listeners.IPickClick;
import com.vansuita.pickimage.listeners.IPickResult;
import com.vansuita.pickimage.resolver.IntentResolver;
import com.vansuita.pickimage.util.Util;

import static android.app.Activity.RESULT_OK;
import static com.vansuita.pickimage.R.layout.dialog;


/**
 * Created by jrvansuita on 01/11/16.
 */

public class PickImageDialog extends DialogFragment {

    public static PickImageDialog newInstance(PickSetup setup) {
        if (setup == null)
            setup = new PickSetup();

        PickImageDialog frag = new PickImageDialog();
        Bundle args = new Bundle();
        args.putSerializable(SETUP_TAG, setup);
        frag.setArguments(args);
        return frag;
    }

    public static PickImageDialog on(FragmentManager fm, PickSetup setup, IPickResult pickResult) {
        PickImageDialog d = PickImageDialog.newInstance(setup);
        d.setOnPickResult(pickResult);
        d.show(fm, PickImageDialog.class.getSimpleName());
        return d;
    }

    public static PickImageDialog on(FragmentManager fm, PickSetup setup) {
        return on(fm, setup, null);
    }

    public static PickImageDialog on(FragmentManager fm, IPickResult pickResult) {
        return on(fm, null, pickResult);
    }

    public static PickImageDialog on(FragmentManager fm) {
        return on(fm, null, null);
    }

    public static PickImageDialog on(FragmentActivity activity, PickSetup setup, IPickResult pickResult) {
        return on(activity.getSupportFragmentManager(), setup, pickResult);
    }

    public static PickImageDialog on(FragmentActivity activity, PickSetup setup) {
        return on(activity, setup, null);
    }

    public static PickImageDialog on(FragmentActivity activity) {
        return on(activity, null);
    }

    private static final String SETUP_TAG = "SETUP_TAG";

    private CardView card;
    private LinearLayout llButtons;
    private TextView tvTitle;
    private TextView tvCamera;
    private TextView tvGallery;
    private TextView tvCancel;
    private TextView tvProgress;

    private View vFirstLayer;
    private View vSecondLayer;

    private IntentResolver resolver;

    private boolean validProvides;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(dialog, null, false);

        onAttaching();
        onInitialize();

        this.validProvides = validateProviders();

        if (validProvides) {
            onBindViewsHolders(view);

            if (!showSystemDialog()) {
                onBindViews(view);
                onBindViewListeners();
                onSetup();
            }
        } else {
            //Delayed dismiss...
            view = new View(getContext());
            delayedDismiss();
        }

        return view;
    }

    private PickSetup setup;

    private void onInitialize() {
        if (getDialog().getWindow() != null) {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        this.setup = (PickSetup) getArguments().getSerializable(SETUP_TAG);
        this.resolver = new IntentResolver(getActivity());
    }

    private boolean showSystemDialog() {
        if (setup.isSystemDialog()) {
            card.setVisibility(View.GONE);

            if (showCamera) {
                if (resolver.requestCameraPermissions(this))
                    resolver.launchSystemChooser(setup, this);
            } else {
                resolver.launchSystemChooser(setup, this);
            }

            return true;
        } else {
            return false;
        }
    }

    private void onBindViewsHolders(View v) {
        card = (CardView) v.findViewById(R.id.card);
        vFirstLayer = v.findViewById(R.id.first_layer);
        vSecondLayer = v.findViewById(R.id.second_layer);
    }

    private void onBindViews(View v) {
        llButtons = (LinearLayout) v.findViewById(R.id.buttons_holder);
        tvTitle = (TextView) v.findViewById(R.id.title);
        tvCamera = (TextView) v.findViewById(R.id.camera);
        tvGallery = (TextView) v.findViewById(R.id.gallery);
        tvCancel = (TextView) v.findViewById(R.id.cancel);
        tvProgress = (TextView) v.findViewById(R.id.loading_text);
    }


    private void onBindViewListeners() {
        tvCancel.setOnClickListener(listener);
        tvCamera.setOnClickListener(listener);
        tvGallery.setOnClickListener(listener);
    }


    private boolean showCamera = true;
    private boolean showGallery = true;

    private boolean validateProviders() {
        if (onClick == null) {
            showCamera = EPickType.CAMERA.inside(setup.getPickTypes()) && resolver.isCamerasAvailable() && !resolver.wasCameraPermissionDeniedForever();
            showGallery = EPickType.GALLERY.inside(setup.getPickTypes());
        }

        if (!(showCamera || showGallery)) {
            Error e = new Error(getString(R.string.no_providers));

            if (onPickResult != null) {
                onPickResult.onPickResult(new PickResult().setError(e));
                return false;
            } else {
                throw e;
            }
        }

        return true;
    }

    private void onSetup() {
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
    }

    public void onAttaching() {
        if (onClick == null && getActivity() instanceof IPickClick)
            onClick = (IPickClick) getActivity();

        if (onPickResult == null && getActivity() instanceof IPickResult)
            onPickResult = (IPickResult) getActivity();
    }

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

    private int returnAction = 0;

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.cancel) {
                dismiss();

            } else if (view.getId() == R.id.camera) {

                returnAction = view.getId();

                if (onClick != null) {
                    onClick.onCameraClick();
                } else {

                    if (resolver.requestCameraPermissions(PickImageDialog.this))
                        resolver.launchCamera(PickImageDialog.this);
                }

            } else if (view.getId() == R.id.gallery) {
                if (onClick != null) {
                    onClick.onGalleryClick();
                } else {
                    resolver.launchGallery(PickImageDialog.this);
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
        if (requestCode == IntentResolver.REQUESTER) {
            if (resultCode == RESULT_OK) {
                visibleProgress(true);
                new AsyncResult().execute(data);
            } else {
                if (PickImageDialog.this.isVisible())
                    dismiss();
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
                if (setup.isSystemDialog()) {
                    showSystemDialog();
                } else if (returnAction > 0) {
                    getView().findViewById(returnAction).performClick();
                }
            } else {

                dismissAllowingStateLoss();

                //if (resolver.wasCameraPermissionDeniedForever())
                //   Util.gone(tvCamera, true);
            }

            Keep.with(getActivity()).asked();
        }


    }


    private class AsyncResult extends AsyncTask<Intent, Void, PickResult> {

        @Override
        protected PickResult doInBackground(Intent... intents) {

            //Create a PickResult instance
            PickResult result = new PickResult();

            //Get the data intent from onActivityResult()
            Intent data = intents[0];

            //Define if it was pick from camera
            boolean fromCamera = resolver.fromCamera(data);

            try {
                //Create a Uri variable to keep the path of the image
                Uri uri;

                //If it was pick from camera
                if (fromCamera) {
                    //Camera has a default uri. All images took from camera will have the same uri
                    //The real file will always be updated with a new image
                    uri = resolver.cameraUri();

                    //Save the path on PickResult
                    result.setPath(uri.getPath());

                    //Or if it was pick from gallery
                } else {
                    //All of the gallery images has a unique uri, sent by the Intent
                    uri = data.getData();

                    //Getting the real path from a gallery uri is a little trick.
                    result.setPath(Util.getRealPathFromURI(getContext(), uri));
                }

                //Save the uri on PickResult
                result.setUri(uri);

                //Time to get the bitmap
                Bitmap bitmap = Util.decodeUri(result.getUri(), getContext(), setup.getImageSize());

                if (fromCamera) {
                    //If developer want to flip by default
                    if (setup.isFlipped())
                        bitmap = Util.flip(bitmap);

                }

                result.setBitmap(bitmap);
                return result;
            } catch (Exception e) {
                result.setError(e);
                return null;
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

    private void delayedDismiss() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dismissAllowingStateLoss();
            }
        }, 20);
    }


}
