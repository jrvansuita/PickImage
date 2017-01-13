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
           compile 'com.github.jrvansuita:PickImage:v2.0.2'
	}

# Samples
 You can take a look at the sample app [located on this project](/app/).


# Implementation

### Step #1 - Show the dialog.
    PickImageDialog.on(MainActivity.this, new PickSetup());
    
    //or 
    
    PickImageDialog.on(getSupportFragmentManager(), new PickSetup());

### Step #2 - Applying the listeners.

#### Method #2.1 - Your AppCompatActivity have to implement IPickResult.

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
        
#### Method #2.2 - Sets the listeners on the dialog constructor.
    PickImageDialog.on(getSupportFragmentManager(), new IPickResult() {
                    @Override
                    public void onPickResult(PickResult r) {
                       //TODO: do what you have to...
                    }
                });
                
#### Method #2.3 - Sets the listeners like this.

    PickImageDialog.on(getSupportFragmentManager())
                   .setOnPickResult(new IPickResult() {
                      @Override
                      public void onPickResult(PickResult r) {
                         //TODO: do what you have to...
                      }
                });


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
    setup.setProgressText("Loading...");
    setup.setProgressTextColor(Color.BLUE);


# Additionals

### Own click implementations.
 If you want to write your own pick images functionalities, your class have to implements IPickClick like in the example below.
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