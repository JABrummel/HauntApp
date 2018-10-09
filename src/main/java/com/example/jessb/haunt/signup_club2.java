package com.example.jessb.haunt;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.net.URI;

import models.Club;
import sql.DatabaseHelper;


public class signup_club2 extends AppCompatActivity {

    ImageView profImage;
    Button uploadButton;
    Uri imageUri;
    private DatabaseHelper db;
    private Bitmap bp;
    private byte[] photo;
    private static final int PICK_IMAGE = 100;
    Club club;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_club2);
        club = (Club) getIntent().getSerializableExtra("club_object");
        db = DatabaseHelper.getInstance(getApplicationContext());
        profImage = findViewById(R.id.iv_profphoto);
        uploadButton = findViewById(R.id.button_upload);

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });


    }

    public void selectImage(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch(requestCode){
            case 2:
                if(resultCode == RESULT_OK) {
                    Uri chosenImage = data.getData();
                    if (chosenImage != null) {
                        bp = decodeUri(chosenImage, 200);
                        profImage.setImageBitmap(bp);
                    }
                }
        }
//        super.onActivityResult(requestCode, resultCode, data);
    }
    protected Bitmap decodeUri(Uri selectedImage, int REQUIRED_SIZE){
        try {
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true){
                if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
                    break;
                }
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private byte[] profileImage(Bitmap b){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 0, bos);
        return bos.toByteArray();
    }

    private void getPhotoValue(){
        photo = profileImage(bp);
    }

    private void addClub() {
        getPhotoValue();
        club.setPhoto(photo);
        //System.out.println(photo);
        db.addClub(club);
    }

    protected void goBack(View v) {}

    protected void createAccount(View v) {
        addClub();
        Intent intent = new Intent(signup_club2.this, UserType.class);
        startActivity(intent);
    }
}

