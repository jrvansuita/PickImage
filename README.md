
<img src="https://github.com/jrvansuita/PickImage/app/src/main/res/mipmap-xxxhdpi/ic_launcher.png?raw=true" align="left" hspace="10" vspace="10">
<a target="_blank" href="https://developer.android.com/reference/android/os/Build.VERSION_CODES.html#ICE_CREAM_SANDWICH"><img src="https://img.shields.io/badge/API-14%2B-blue.svg?style=flat" alt="API" /></a>
[![](https://jitpack.io/v/jrvansuita/PickImage.svg)](https://jitpack.io/#jrvansuita/PickImage)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-PickImage-green.svg?)](https://android-arsenal.com/details/1/4614)

# PickImage

This class was created to use in Android projects.

# Porpouse
Shows a DialogFragment with Camera or Gallery options. The user can choose from where provider wants to pick an image.

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
     
# License
See the [LICENSE](/LICENSE.txt). file for license rights and limitations (MIT).