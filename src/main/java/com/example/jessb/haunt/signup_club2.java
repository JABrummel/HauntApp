package com.example.jessb.haunt;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class signup_club2 extends AppCompatActivity {

    ImageView profImage;
    Button uploadButton;
    Uri imageUri;
    private static final int PICK_IMAGE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_club2);

        profImage = findViewById(R.id.iv_profphoto);
        uploadButton = findViewById(R.id.button_upload);

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });


    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int result, Intent data) {
        super.onActivityResult(requestCode, result, data);
        if(result == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            profImage.setImageURI(imageUri);

        }
    }

    protected void goBack(View v) {}

    protected void createAccount(View v) {
        Intent intent = new Intent(signup_club2.this, ListedEvents.class);
        startActivity(intent);
    }
}

