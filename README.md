
<!-- Library Logo -->
<img src="https://github.com/jrvansuita/PickImage/blob/master/app/src/main/res/mipmap-xxxhdpi/ic_launcher.png?raw=true" align="left" hspace="1" vspace="1">

<!-- JitPack integration -->
[![](https://jitpack.io/v/jrvansuita/PickImage.svg)](https://jitpack.io/#jrvansuita/PickImage)<!-- Android Arsenal -->
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-PickImage-green.svg?)](https://android-arsenal.com/details/1/4614)
[![ghit.me](https://ghit.me/badge.svg?repo=jrvansuita/PickImage)](https://ghit.me/repo/jrvansuita/PickImage)

<!-- License -->
<a target="_blank" href="/LICENSE.txt"><img src="http://img.shields.io/:License-MIT-yellow.svg" alt="MIT License" /></a><!-- Minimun Android Api -->
<a target="_blank" href="https://developer.android.com/reference/android/os/Build.VERSION_CODES.html#GINGERBREAD"><img src="https://img.shields.io/badge/API-9%2B-blue.svg?style=flat" alt="API" /></a><!--Open Source -->
[![Open Source Love](https://badges.frapsoft.com/os/v2/open-source.svg?v=103)](https://github.com/ellerbrock/open-source-badges/)

<a target="_blank" href="http://www.methodscount.com/?lib=com.github.jrvansuita%3APickImage%3Av2.0.2"><img src="https://img.shields.io/badge/methods-409-e91e63.svg" /></a>

# PickImage


This is an [**Android**](https://developer.android.com) project. It shows a [DialogFragment](https://developer.android.com/reference/android/app/DialogFragment.html) with Camera or Gallery options. The user can choose from which provider wants to pick an image.

<br />

# Screenshot
![test](screenshot/img.png? "Dialog")

# Usage

#### Step 1. Add the JitPack repository to your build file:

    allprojects {
		repositories {
			...
			maven { url "https://jitpack.io" }
		}
	}

#### Step 2. Add the dependency

    dependencies {
           compile 'com.github.jrvansuita:PickImage:v2.0.2'
	}

# Samples
 You can take a look at the sample app [located on this project](/app/).


# Implementation

### Step #1 - Show the dialog.
    PickImageDialog.on(MainActivity.this, new PickSetup(BuildConfig.APPLICATION_ID));
    
    //or 
    
    PickImageDialog.on(getSupportFragmentManager(), new PickSetup(BuildConfig.APPLICATION_ID));

### Step #2 - Override library file provider authority to avoid INSTALL_FAILED_CONFLICTING_PROVIDER ([See](https://developer.android.com/guide/topics/manifest/provider-element.html#auth)).

    <manifest ...>
    
        ... 
    
        <provider
            android:name="android.support.v4.content.FileProvider"
            android:authorities="${applicationId}.com.vansuita.pickimage.provider"
            tools:replace="android:authorities">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/provider_paths" />
        </provider>
    </manifest>   

### Step #3 - Applying the listeners.

#### Method #3.1 - Your AppCompatActivity have to implement IPickResult.

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
                
                //Image path.
                r.getPath();
            } else {
                //Handle possible errors
                //TODO: do what you have to do with r.getError();
            }
        }
        
#### Method #3.2 - Sets the listeners on the dialog constructor.
    PickImageDialog.on(getSupportFragmentManager(), new IPickResult() {
                    @Override
                    public void onPickResult(PickResult r) {
                       //TODO: do what you have to...
                    }
                });
                
#### Method #3.3 - Sets the listeners like this.

    PickImageDialog.on(getSupportFragmentManager())
                   .setOnPickResult(new IPickResult() {
                      @Override
                      public void onPickResult(PickResult r) {
                         //TODO: do what you have to...
                      }
                });


### Step #4 - Customize you Dialog using PickSetup.
    PickSetup setup = new PickSetup();
    setup.setBackgroundColor(yourColor);
    setup.setTitle(yourTitle);
    setup.setDimAmount(yourFloat);
    setup.setTitleColor(yourColor);
    setup.setFlip(true);
    setup.setCancelText("Test");
    setup.setImageSize(500);
    setup.setPickTypes(EPickTypes.GALERY, EPickTypes.CAMERA);
    setup.setProgressText("Loading...");
    setup.setProgressTextColor(Color.BLUE);


# Additionals

### Own click implementations.
 If you want to write your own button click event, your class have to implements IPickClick like in the example below.
 You may want to take a look at the sample app.
 
     @Override
     public void onGalleryClick() {
         //TODO: Your onw implementation
     }
 
     @Override
     public void onCameraClick() {
         //TODO: Your onw implementation
     }
     
## How to dismiss the dialog.
     PickImageDialog dialog = PickImageDialog.on(...);
     dialog.dismiss();
     
