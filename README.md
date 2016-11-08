# PickImage
[![](https://jitpack.io/v/jrvansuita/PickImage.svg)](https://jitpack.io/#jrvansuita/PickImage)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-PickImage-green.svg?)](https://android-arsenal.com/details/1/4614)

This class was created to use in Android projects.

# Porpouse
Shows a DialogFragment with Camera or Gallery options. The user can choose from where provider wants to pick an image.

# Screeshot
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
           compile 'com.github.jrvansuita:PickImage:v1.0.1'
	}

# Samples
 You can take a look at the sample app [located on this project](/app/).


# Implamentation

### Step #1 - Show the dialog.
    PickImageDialog.on(MainActivity.this, new PickSetup());

### Step #2 - Your AppCompatActivity have to implement IPickResult.IPickResultBitmap or IPickResult.IPickResultUri .
    @Override
    public void onPickImageResult(Bitmap bitmap) {
          //TODO: use bitmap.
      }
      
    //Or
      
    @Override
    public void onPickImageResult(Uri bitmap) {
       //TODO: use bitmap uri.
    }

### Step #3 - Customize you Dialog using PickSetup.
    PickSetup setup = new PickSetup();
    setup.setBackgroundColor(yourColor);
    setup.setTitle(yourTitle);
    setup.setDimAmount(yourFloat);
    setup.setTitleColor(yourColor);
    
