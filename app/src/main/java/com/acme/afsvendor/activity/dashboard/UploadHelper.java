package com.acme.afsvendor.activity.dashboard;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class UploadHelper {
    private Activity activity;
    private ByteArrayOutputStream imageStream;

    public UploadHelper(Activity activity) {
        this.activity = activity;
    }

    public void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(intent, 1);
    }

    public void handleActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            try {
                InputStream inputStream = activity.getContentResolver().openInputStream(selectedImageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                if (bitmap != null) {
                    imageStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, imageStream);
                } else {
                    // Handle the situation where bitmap is null
                    // You might want to log this or show a message to the user
                }

            } catch (Exception e) {
                Log.d("tag908", e.toString());
            }
        }
    }

    public ByteArrayOutputStream getImageStream() {
        return imageStream;

    }


}
