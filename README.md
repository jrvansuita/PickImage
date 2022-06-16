<!-- Library Logo -->
<img src="https://github.com/jrvansuita/PickImage/blob/master/app/src/main/res/mipmap-xxxhdpi/ic_launcher.png?raw=true" align="left" hspace="1" vspace="1">

<!-- Buy me a cup of coffee -->
<a href='https://ko-fi.com/A406JCM' style='margin:13px;' target='_blank' align="right"><img align="right" height='36' src='https://az743702.vo.msecnd.net/cdn/kofi4.png?v=f' alt='Buy Me a Coffee at ko-fi.com' /></a>
<a href='https://play.google.com/store/apps/details?id=com.vansuita.pickimage.sample&pcampaignid=MKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1' target='_blank' align="right"  style='margin:13px;'><img align="right" height='45' src='https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png' alt='Get it on Google Play' /></a>
# PickImage


This is an [**Android**](https://developer.android.com) project. It shows a [DialogFragment](https://developer.android.com/reference/android/app/DialogFragment.html) with Camera or Gallery options. The user can choose from which provider wants to pick an image.

</br> 
</br> 
</br> 

<!-- JitPack integration -->
[![JitPack](https://jitpack.io/v/jrvansuita/PickImage.svg)](https://jitpack.io/#jrvansuita/PickImage)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-PickImage-green.svg?)](https://android-arsenal.com/details/1/4614) [![MaterialUp](https://img.shields.io/badge/MaterialUp-PickImage-6ad0d9.svg?)](https://www.uplabs.com/posts/pickimage) [![Hits](https://hits.seeyoufarm.com/api/count/incr/badge.svg?url=https%3A%2F%2Fgithub.com%2Fjrvansuita%2FPickImage&count_bg=%2379C83D&title_bg=%23555555&icon=&icon_color=%23E7E7E7&title=VIEWS&edge_flat=false)](https://hits.seeyoufarm.com)

# Dialog screenshots

#### Default icons.
<img src="images/dialogs/light_vertical_left_simple.png" height='auto' width='250'/><img src="images/dialogs/light_horizontal_top_simple.png" height='auto' width='250'/><img src="images/dialogs/light_horizontal_left_simple.png" height='auto' width='250'/>

#### Colored icons.
<img src="images/dialogs/light_vertical_left_colored.png" height='auto' width='250'/><img src="images/dialogs/light_horizontal_top_colored.png" height='auto' width='250'/><img src="images/dialogs/light_horizontal_left_colored.png" height='auto' width='250'/>

#### Custom dialog.
<img src="images/dialogs/dark_vertical_left.png" height='auto' width='250'/><img src="images/dialogs/dark_horizontal_top.png" height='auto' width='250'/><img src="images/dialogs/dark_horizontal_left.png" height='auto' width='250'/>

#### System dialog.
<img src="images/dialogs/all_system_dialog.png" height='auto' width='250'/><img src="images/dialogs/camera_system_dialog.png" height='auto' width='250'/><img src="images/dialogs/images_system_dialog.png" height='auto' width='250'/>

[![Appetize.io](https://img.shields.io/badge/Apptize.io-Run%20Now-brightgreen.svg?)](https://appetize.io/embed/91gknbry9vrtz281b7jy244e0m?device=nexus7&maxSize=50&autoplay=true&orientation=portrait&deviceColor=black) [![Demo](https://img.shields.io/badge/Demo-Download-blue.svg)](http://apk-dl.com/dl/com.vansuita.pickimage.sample)
 [![Codacy Badge](https://api.codacy.com/project/badge/Grade/118bb89e3bed43e2b462201654224a60)](https://www.codacy.com/app/jrvansuita/PickImage?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=jrvansuita/PickImage&amp;utm_campaign=Badge_Grade) <a target="_blank" href="https://developer.android.com/reference/android/os/Build.VERSION_CODES.html#GINGERBREAD"><img src="https://img.shields.io/badge/API-9%2B-blue.svg?style=flat" alt="API" /></a>
 
 
# Setup

#### Step #1. Add the JitPack repository to your build.gradle file:
```gradle
allprojects {
    repositories {
	maven { url "https://jitpack.io" }
    }
}
```
#### Step #1.1 Or add the JitPack repository to the settings.gradle file:

```gradle
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
       maven { url 'https://jitpack.io' }
    }
}
```
     
#### Step #2. Add the dependency ([See latest release](https://jitpack.io/#jrvansuita/PickImage)).
```groovy
dependencies {
    implementation 'com.github.jrvansuita:PickImage:+'
}
```
# Implementation

#### Step #1. Overriding the library file provider authority to avoid installation conflicts.
The use of this library can cause [INSTALL_FAILED_CONFLICTING_PROVIDER](https://developer.android.com/guide/topics/manifest/provider-element.html#auth) if you skip this step. Update your AndroidManifest.xml with this exact provider declaration below.
```xml
<manifest ...>
    <application ...>
        <provider
            android:name="com.vansuita.pickimage.provider.PickImageFileProvider"
            android:authorities="${applicationId}.com.vansuita.pickimage.provider"
            android:exported="false"
            android:grantUriPermissions="true"
            tools:replace="android:authorities">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/picker_provider_paths" />
        </provider>
    </application>	
</manifest> 
```

#### Step #2 - Showing the dialog.
```java
PickImageDialog.build(new PickSetup()).show(this);
``` 
#### Step #3 - Applying the listeners.
##### Method #3.1 - Make your AppCompatActivity implements IPickResult.
```java
@Override
public void onPickResult(PickResult r) {
    if (r.getError() == null) {
        //If you want the Uri.
        //Mandatory to refresh image from Uri.
        //getImageView().setImageURI(null);

        //Setting the real returned image.
        //getImageView().setImageURI(r.getUri());

        //If you want the Bitmap.
        getImageView().setImageBitmap(r.getBitmap());

        //Image path
        //r.getPath();
    } else {
        //Handle possible errors
        //TODO: do what you have to do with r.getError();
        Toast.makeText(this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
    }
}
```

##### Method #3.2 - Set the listener using the public method (Good for Fragments).

```java
PickImageDialog.build(new PickSetup())
               .setOnPickResult(new IPickResult() {
                  @Override
                  public void onPickResult(PickResult r) {
                     //TODO: do what you have to...
                  }
               })
	       .setOnPickCancel(new IPickCancel() {
		  @Override
		  public void onCancelClick() {
			//TODO: do what you have to if user clicked cancel
		   }
		}).show(getSupportFragmentManager());
```

#### Step #4 - Customize you Dialog using PickSetup.

```java
PickSetup setup = new PickSetup()
            .setTitle(yourText)
            .setTitleColor(yourColor)
            .setBackgroundColor(yourColor)
            .setProgressText(yourText)
            .setProgressTextColor(yourColor)
            .setCancelText(yourText)
            .setCancelTextColor(yourColor)
            .setButtonTextColor(yourColor)
            .setDimAmount(yourFloat)
            .setFlip(true)
            .setMaxSize
            .setPickTypes(EPickType.GALLERY, EPickType.CAMERA)
            .setCameraButtonText(yourText)
            .setGalleryButtonText(yourText)
            .setIconGravity(Gravity.LEFT)
            .setButtonOrientation(LinearLayoutCompat.VERTICAL)
            .setSystemDialog(false)
            .setGalleryIcon(yourIcon)
            .setCameraIcon(yourIcon)
            .setGalleryChooserTitle(yourText)
            .setCameraChooserTitle(yourText);
/*... and more to come. */
```

# Additionals

#### Own click implementations.
If you want to write your own button click event, just use [IPickClick](library/src/main/java/com/vansuita/pickimage/listeners/IPickClick.java) listener like in the example below. You may want to take a look at the sample app.
 
 ```java
 PickImageDialog.build(setup)
         .setOnClick(new IPickClick() {
             @Override
             public void onGalleryClick() {
                 Toast.makeText(SampleActivity.this, "Gallery Click!", Toast.LENGTH_LONG).show();
             }

             @Override
             public void onCameraClick() {
                 Toast.makeText(SampleActivity.this, "Camera Click!", Toast.LENGTH_LONG).show();
             }
          }).show(this);
``` 

#### For dismissing the dialog.

```java
PickImageDialog dialog = PickImageDialog.build(...);
dialog.dismiss();
```

#### Force a specific width and height.
```java
new PickSetup().setWidth(600).setHeight(800);
```

#### Not just an ImagePicker anymore! You can pick video too.
You can tell the setup to pick video instead of photo! (default option if you don't mention is to pick Image)
```java
new PickSetup().setVideo(true);
```
  
# Sample app code.
 You can take a look at the sample app [located on this project](/app/).
 
# 

<a href="https://www.instagram.com/jnrvans/" target="_blank">
  <img src="https://camo.githubusercontent.com/c9dacf0f25a1489fdbc6c0d2b41cda58b77fa210a13a886d6f99e027adfbd358/68747470733a2f2f6564656e742e6769746875622e696f2f537570657254696e7949636f6e732f696d616765732f7376672f696e7374616772616d2e737667" alt="Instagram" witdh="44" height="44" hspace="10">
</a>
<a href="https://github.com/jrvansuita" target="_blank">
  <img src="https://camo.githubusercontent.com/b079fe922f00c4b86f1b724fbc2e8141c468794ce8adbc9b7456e5e1ad09c622/68747470733a2f2f6564656e742e6769746875622e696f2f537570657254696e7949636f6e732f696d616765732f7376672f6769746875622e737667" alt="Github" witdh="44" height="44" hspace="10">
</a>
<a href="https://play.google.com/store/apps/dev?id=8002078663318221363" target="_blank">
  <img src="https://camo.githubusercontent.com/8ce12185c778e13eed2073e7a6aba042ce5092d4d41744e7052e0fc16363c386/68747470733a2f2f6564656e742e6769746875622e696f2f537570657254696e7949636f6e732f696d616765732f7376672f676f6f676c655f706c61792e737667" alt="Google Play Store" witdh="44" height="44" hspace="10">
</a>
<a href="mailto:vansuita.jr@gmail.com" target="_blank" >
  <img src="https://camo.githubusercontent.com/4a3dd8d10a27c272fd04b2ce8ed1a130606f95ea6a76b5e19ce8b642faa18c27/68747470733a2f2f6564656e742e6769746875622e696f2f537570657254696e7949636f6e732f696d616765732f7376672f676d61696c2e737667" alt="E-mail" witdh="44" height="44" hspace="10">
</a>
