package com.example.dogtranslator;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DogProfileSettingActivity extends AppCompatActivity {

    private Button btnImageUpload, btnRegister;
    private EditText puppyName;
    private RadioGroup radioGroupDogBreed;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dog_profile_setting);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        btnImageUpload = findViewById(R.id.btn_imageUpload);
        btnRegister = findViewById(R.id.btn_register);
        puppyName = findViewById(R.id.puppy_name);
        radioGroupDogBreed = findViewById(R.id.radioGroup_dog_breed);


        // Set an OnClickListener for the "Image Upload" button
        btnImageUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an Intent to pick an image from the gallery
                Toast.makeText(DogProfileSettingActivity.this, "사진을 업로드 해주세요.", Toast.LENGTH_SHORT).show();
            }
        });


        // radioGroupDogBreed Button
        radioGroupDogBreed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // Sets an OnClickListener for the "Register" button
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = puppyName.getText().toString().trim();
                Intent intent = new Intent(DogProfileSettingActivity.this, AnalysisActivity.class);
                intent.putExtra("PUPPY_NAME", name);
                startActivity(intent);
                }
        });




    }
}