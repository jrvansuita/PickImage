# PickImage
[![](https://jitpack.io/v/jrvansuita/IconHandler.svg)](https://jitpack.io/#jrvansuita/IconHandler)

This class was created to use in Android projects.

# Porpouse
Shows a DialogFragment with Camera or Gallery options. The user can choose from where provider wants to pick an image.


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

* Step #1 - Show the dialog.
    PickImageDialog.on(MainActivity.this, new PickSetup());

* Step #1 - Your AppCompatActivity have to implement IPickResult.
    @Override
      public void onPickImageResult(Bitmap bitmap) {
          //TODO: use bitmap.
      }
