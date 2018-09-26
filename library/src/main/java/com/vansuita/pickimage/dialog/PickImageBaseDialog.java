package com.vansuita.pickimage.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vansuita.pickimage.R;
import com.vansuita.pickimage.async.AsyncImageResult;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.enums.EPickType;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickClick;
import com.vansuita.pickimage.listeners.IPickResult;
import com.vansuita.pickimage.resolver.IntentResolver;
import com.vansuita.pickimage.util.Util;

import static com.vansuita.pickimage.R.layout.dialog;

/**
 * Created by jrvansuita on 08/02/17.
 */

public abstract class PickImageBaseDialog extends DialogFragment implements IPickClick {

    protected static final String SETUP_TAG = "SETUP_TAG";
    protected static final String RESOLVER_STATE_TAG = "resolverState";
    public static final String DIALOG_FRAGMENT_TAG = PickImageBaseDialog.class.getSimpleName();

    private PickSetup setup;
    private IntentResolver resolver;

    private boolean showCamera = true;
    private boolean showGallery = true;

    private CardView card;
    private LinearLayout llButtons;
    private TextView tvTitle;
    private TextView tvCamera;
    private TextView tvGallery;
    private TextView tvCancel;
    private TextView tvProgress;

    private View vFirstLayer;
    private View vSecondLayer;

    private Boolean validProviders = null;

    private IPickResult onPickResult;
    private IPickClick onClick;
    private IPickCancel onPickCancel;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(dialog, null, false);

        onAttaching();
        onInitialize(savedInstanceState);

        if (isValidProviders()) {
            onBindViewsHolders(view);

            if (!launchSystemDialog()) {
                onBindViews(view);
                onBindViewListeners();
                onSetup();
            }
        } else {
            return delayedDismiss();
        }


        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle resolverState = new Bundle();
        this.resolver.onSaveInstanceState(resolverState);

        outState.putBundle(RESOLVER_STATE_TAG, resolverState);
    }

    private void onAttaching() {
        if (onClick == null) {
            if (getActivity() instanceof IPickClick) {
                onClick = (IPickClick) getActivity();
            } else {
                onClick = this;
            }
        }

        if (onPickResult == null && getActivity() instanceof IPickResult)
            onPickResult = (IPickResult) getActivity();

        if (onPickCancel == null && getActivity() instanceof IPickCancel)
            onPickCancel = (IPickCancel) getActivity();
    }


    protected void onInitialize(Bundle savedInstanceState) {
        if (getDialog().getWindow() != null) {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        this.setup = (PickSetup) getArguments().getSerializable(SETUP_TAG);

        Bundle resolverState = null;
        if (savedInstanceState != null) {
            resolverState = savedInstanceState.getBundle(RESOLVER_STATE_TAG);
        }
        this.resolver = new IntentResolver((AppCompatActivity) getActivity(), setup, resolverState);
    }


    private boolean isValidProviders() {
        if (validProviders == null) {
            validProviders = true;

            showCamera = EPickType.CAMERA.inside(setup.getPickTypes()) && ((onClick == null) || (resolver.isCamerasAvailable() && !resolver.wasCameraPermissionDeniedForever()));
            showGallery = EPickType.GALLERY.inside(setup.getPickTypes());

            if (!(showCamera || showGallery)) {
                Error e = new Error(getString(R.string.no_providers));

                validProviders = false;

                if (onPickResult != null) {
                    onError(e);
                } else {
                    throw e;
                }
            }
        }

        return validProviders;
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

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.cancel) {
                if (onPickCancel != null) {
                    onPickCancel.onCancelClick();
                }
                dismiss();
            } else {
                if (view.getId() == R.id.camera) {
                    onClick.onCameraClick();
                } else if (view.getId() == R.id.gallery) {
                    onClick.onGalleryClick();
                }
            }
        }
    };


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

        showProgress(false);

        Util.gone(tvCamera, !showCamera);
        Util.gone(tvGallery, !showGallery);

        llButtons.setOrientation(setup.getButtonOrientation() == LinearLayoutCompat.HORIZONTAL ? LinearLayout.HORIZONTAL : LinearLayout.VERTICAL);

        Util.setIcon(tvCamera, setup.getCameraIcon(), setup.getIconGravity());
        Util.setIcon(tvGallery, setup.getGalleryIcon(), setup.getIconGravity());

        Util.setDimAmount(setup.getDimAmount(), getDialog());
    }


    protected void showProgress(boolean show) {
        Util.gone(card, false);
        Util.gone(vFirstLayer, show);
        Util.gone(vSecondLayer, !show);
    }


    protected void launchCamera() {
        if (resolver.requestCameraPermissions(this))
            resolver.launchCamera(this);
    }

    protected void launchGallery() {
        if (resolver.requestGalleryPermissions(this))
            resolver.launchGallery(this);
    }

    protected boolean launchSystemDialog() {
        if (setup.isSystemDialog()) {
            card.setVisibility(View.GONE);

            if (showCamera) {
                if (resolver.requestCameraPermissions(this))
                    resolver.launchSystemChooser(this);
            } else {
                resolver.launchSystemChooser(this);
            }

            return true;
        } else {
            return false;
        }
    }


    protected PickImageBaseDialog setOnPickResult(IPickResult onPickResult) {
        this.onPickResult = onPickResult;
        return this;
    }


    protected PickImageBaseDialog setOnClick(IPickClick onClick) {
        this.onClick = onClick;
        return this;
    }


    protected PickImageBaseDialog setOnPickCancel(IPickCancel onPickCancel) {
        this.onPickCancel = onPickCancel;
        return this;
    }


    protected AsyncImageResult getAsyncResult() {
        return new AsyncImageResult(resolver, setup).setOnFinish(new AsyncImageResult.OnFinish() {
            @Override
            public void onFinish(PickResult pickResult) {
                if (onPickResult != null)
                    onPickResult.onPickResult(pickResult);

                dismissAllowingStateLoss();
            }
        });
    }

    @Override
    public Context getContext() {
        Context context = super.getContext();

        if ((context == null) && (resolver != null))
            context = resolver.getActivity();

        return context;
    }

    private View delayedDismiss() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        }, 20);

        return new View(getContext());
    }

    protected void onError(Error e) {
        if (onPickResult != null) {
            onPickResult.onPickResult(new PickResult().setError(e));

            dismissAllowingStateLoss();
        }
    }
}
