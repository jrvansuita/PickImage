package com.vansuita.pickimage.async;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;

import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.enums.EPickType;
import com.vansuita.pickimage.img.ImageHandler;
import com.vansuita.pickimage.resolver.IntentResolver;

import java.lang.ref.WeakReference;

/**
 * Created by jrvansuita on 08/02/17.
 */

public class AsyncImageResult extends AsyncTask<Intent, Void, PickResult> {

    private WeakReference<IntentResolver> weakIntentResolver;
    private WeakReference<PickSetup> weakSetup;


    public AsyncImageResult(Activity activity, PickSetup setup) {
        this.weakIntentResolver = new WeakReference<IntentResolver>(new IntentResolver(activity));
        this.weakSetup = new WeakReference<PickSetup>(setup);
    }


    @Override
    protected PickResult doInBackground(Intent... intents) {

        //Create a PickResult instance
        PickResult result = new PickResult();

        IntentResolver resolver = weakIntentResolver.get();

        if (resolver == null) {
            result.setError(new Error("Activity was destroyed, can't handle pick image result."));
            return result;
        }

        try {
            //Get the data intent from onActivityResult()
            Intent data = intents[0];

            //Define if it was pick from camera
            boolean fromCamera = resolver.fromCamera(data);

            //Instance of a helper class
            ImageHandler imageHandler = ImageHandler
                    .with(resolver.getActivity()).setup(weakSetup.get())
                    .provider(fromCamera ? EPickType.CAMERA : EPickType.GALLERY)
                    .uri(fromCamera ? resolver.cameraUri() : data.getData());

            //Setting uri and path for result
            result.setUri(imageHandler.getUri())
                    .setPath(imageHandler.getUriPath())
                    .setBitmap(imageHandler.decode());


            return result;
        } catch (Exception e) {
            result.setError(e);
            return result;
        }
    }


    @Override
    protected void onPostExecute(PickResult r) {
        if (weakOnFinish != null) {
            OnFinish onFinish = weakOnFinish.get();

            if (onFinish != null)
                onFinish.onFinish(r);
        }
    }

    public interface OnFinish {
        void onFinish(PickResult pickResult);
    }

    private WeakReference<OnFinish> weakOnFinish;

    public AsyncImageResult setOnFinish(OnFinish onFinish) {
        this.weakOnFinish = new WeakReference<OnFinish>(onFinish);
        return this;
    }
}
