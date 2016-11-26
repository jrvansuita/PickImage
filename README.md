# PickImage
[![](https://jitpack.io/v/jrvansuita/PickImage.svg)](https://jitpack.io/#jrvansuita/PickImage)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-PickImage-green.svg?)](https://android-arsenal.com/details/1/4614)

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
           compile 'com.github.jrvansuita:PickImage:v1.0.7'
	}

# Samples
 You can take a look at the sample app [located on this project](/app/).


# Implementation

### Step #1 - Show the dialog.
    PickImageDialog.on(MainActivity.this, new PickSetup());

### Step #2 - Your AppCompatActivity have to implement IPickResult.IPickResultBitmap or IPickResult.IPickResultUri .
    @Override
    public void onPickImageResult(Bitmap bitmap) {
          ImageView imageView = ((ImageView) findViewById(R.id.image_view));
          
          imageView.setImageBitmap(bitmap);
      }
      
    //Or
      
    @Override
    public void onPickImageResult(Uri bitmap) {
       ImageView imageView = ((ImageView) findViewById(R.id.image_view));
       
       //Mandatory to refresh image from Uri.
       imageView.setImageURI(null);
       
       //Setting the real returned image.
       imageView.setImageURI(bitmapUri);
    }

### Step #3 - Customize you Dialog using PickSetup.
    PickSetup setup = new PickSetup();
    setup.setBackgroundColor(yourColor);
    setup.setTitle(yourTitle);
    setup.setDimAmount(yourFloat);
    setup.setTitleColor(yourColor);
    setup.setFlip(true);
    setup.setCancelText("Test");
    setup.setImageSize(500);
    setup.setPickTypes(EPickTypes.GALERY, EPickTypes.CAMERA);

### Step #4 - Handle possible errors.
    @Override
    public void onPickError(Exception e) {
        //TODO: handle the error.
    }


# Additionals
 If you want to write your own pick images functionalities, your class have to implements IPickResult.IPickClick like in the example below.
 You may want to take a look at the sample app.
 
     @Override
     public void onGaleryClick() {
         //TODO: Your onw implementation
     }
 
     @Override
     public void onCameraClick() {
         //TODO: Your onw implementation
     }
     
# License
See the [LICENSE](/LICENSE.txt). file for license rights and limitations (MIT).