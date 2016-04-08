package com.colt.android024_image_effects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private Bitmap photoOriginal;
    private Bitmap photoInvert;
    private Bitmap photoEffect;
    private LayerDrawable layerEffect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.imageView);

        //Bitmap photoFinal = invertImage(photoBitmap);
        Drawable photoSource = ContextCompat.getDrawable(this, R.mipmap.forest);
        photoOriginal = ((BitmapDrawable) photoSource).getBitmap(); //Convert (png, jpg...) image to bitmap and store it in memory.
        imageView.setImageBitmap(photoOriginal);
    }

    //Display original image.
    public void photoOriginal(View view) {
        imageView.setImageBitmap(photoOriginal);
    }

    //Inverting bitmap image.
    public void photoInvert(View view) {
        //Check if inverted photo exists, and just show it in that case.
        if (photoInvert != null) {
            imageView.setImageBitmap(photoInvert);
            return;
        }

        //Invert photo. Runs only first time.
        photoInvert = Bitmap.createBitmap(photoOriginal.getWidth(), photoOriginal.getHeight(), photoOriginal.getConfig()); //Create new blank image with input's resolution.

        int A;
        int R;
        int G;
        int B;
        int pixelColor;
        int width = photoOriginal.getWidth();
        int height = photoOriginal.getHeight();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                pixelColor = photoOriginal.getPixel(x, y);

                //Get values and invert 'em.
                A = Color.alpha(pixelColor);
                R = 255 - Color.red(pixelColor);
                G = 255 - Color.green(pixelColor);
                B = 255 - Color.blue(pixelColor);

                //Create outputImage, pixel by pixel.
                photoInvert.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }

        imageView.setImageBitmap(photoInvert);
    }

    //Add effect to a photo.
    public void photoFilter(View view) {
        //Check if filtered photo exists, and just show it in that case.
        if (photoEffect != null) {
            imageView.setImageBitmap(photoEffect);
            return;
        }

        photoEffect = Bitmap.createBitmap(photoOriginal.getWidth(), photoOriginal.getHeight(), photoOriginal.getConfig());

        Drawable[] layers = new Drawable[2];
        layers[0] = ContextCompat.getDrawable(this, R.mipmap.forest);
        layers[1] = ContextCompat.getDrawable(this, R.mipmap.effect);
        layerEffect = new LayerDrawable(layers);
        layerEffect.setBounds(0, 0, photoOriginal.getWidth(), photoOriginal.getHeight());
        layerEffect.draw(new Canvas(photoEffect));

        imageView.setImageBitmap(photoEffect);
    }

    //Save all photos.
    public void savePhotos(View view) {
        if (photoOriginal != null && photoInvert != null && photoEffect != null) {
            MediaStore.Images.Media.insertImage(getContentResolver(), photoOriginal, "Original Photo", "Original Photo");
            MediaStore.Images.Media.insertImage(getContentResolver(), photoInvert, "Inverted Photo", "Inverted Photo");
            MediaStore.Images.Media.insertImage(getContentResolver(), photoEffect, "Effect Photo", "Effect Photo");
            Toast.makeText(this, "Saved.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Not saved. Render all at least once.", Toast.LENGTH_LONG).show();
        }
    }

}