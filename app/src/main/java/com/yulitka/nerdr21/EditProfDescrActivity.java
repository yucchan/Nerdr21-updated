package com.yulitka.nerdr21;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EditProfDescrActivity extends AppCompatActivity {

    User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_prof_descr);
        SharedPreferences ref = getSharedPreferences("connected",MODE_PRIVATE);
        String username = ref.getString("username",null);

        user.setUsername(username);
        updateUserInfoViews();

        final Button edSaveBtn = findViewById(R.id.edSaveBtn);
        edSaveBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String description=((EditText) findViewById(R.id.edProfileEditText)).getText().toString();
                editUser(description);
                finish();
            }
        });

        final Button edExiButton = findViewById(R.id.edExitBtn);
        edExiButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        updateUserInfoViews();
    }

    private void editUser(String description) {
//        Toast.makeText(getApplicationContext(),description,Toast.LENGTH_SHORT).show();
        user.setDescription(description);
        user.storeUser();
    }

    private void updateUserInfoViews() {
        user.refreshUser(user.getUsername());
        final TextView profileEditText = findViewById(R.id.edProfileEditText);
        profileEditText.setText(user.getDescription());

    }
}
