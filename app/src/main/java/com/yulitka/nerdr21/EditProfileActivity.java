package com.yulitka.nerdr21;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

public class EditProfileActivity extends AppCompatActivity {

    User user = new User();
    FirebaseStorage storage;
    StorageReference storageReference;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    final static int RESAULT_SUCCESS = 0;
//    private Button btnChoose, btnUpload;
//    private ImageView imageView;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        SharedPreferences ref = getSharedPreferences("connected",MODE_PRIVATE);
        String username = ref.getString("username",null);

        user.setUsername(username);
        updateUserInfoViews();

        final Button chgImgBtn = findViewById(R.id.epChgImgBtn);
        chgImgBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),R.string.available_soon,Toast.LENGTH_SHORT).show();
                chooseImage();
            }
        });

        final Button changeDescBtn = findViewById(R.id.epChgDescBtn);
        changeDescBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(EditProfileActivity.this,EditProfDescrActivity.class);
                startActivity(i);
            }
        });

        final Button saveExitBtn= findViewById(R.id.epSaveExitBtn);
        saveExitBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(EditProfileActivity.this,LoginActivity.class);
                startActivity(i);

            }
        });
        final Button epChatBtn= findViewById(R.id.epChatBtn);
        epChatBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(),R.string.available_soon,Toast.LENGTH_SHORT).show();
                Intent i = new Intent(EditProfileActivity.this,ChatActivity.class);
                startActivity(i);
            }
        });
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateUserInfoViews();
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        updateUserInfoViews();
    }

    private void updateUserInfoViews() {

            user.refreshUser(user.getUsername());

            final ImageView profileImg = findViewById(R.id.epProfileIMG);
            if (user.getProfileImg() != null)
                profileImg.setImageBitmap(user.getProfileImg());

            final TextView usernameTV = findViewById(R.id.epUsernameTV);
            usernameTV.setText(user.getUsername());

            final TextView profileDescriptionTV = findViewById(R.id.epProfileDescrTV);
            profileDescriptionTV.setText(user.getDescription());

    }

    private void uploadImage(){
        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/"+ user.getUsername());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(EditProfileActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(EditProfileActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                user.setProfileImg(bitmap);
                ((ImageView)findViewById(R.id.epProfileIMG)).setImageBitmap(bitmap);
                uploadImage();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

}
