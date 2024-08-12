package com.example.dogtranslator;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.graphics.Bitmap;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DogProfileSettingActivity extends AppCompatActivity {


    private static final int PICK_IMAGE_REQUEST = 1; // Request code for image picking
    private ImageView imageViewPuppy; // ImageView to display the chosen image
    private Uri imageUri;
    private Button btnImageUpload, btnRegister;
    private EditText puppyName;
    private RadioGroup radioGroupDogBreed;

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
        imageViewPuppy = findViewById(R.id.imageView_puppy);
        radioGroupDogBreed = findViewById(R.id.radioGroup_dog_breed);

        // Set default image
        imageViewPuppy.setImageResource(R.drawable.puppy_logo);

        // Set an OnClickListener for the "Image Upload" button
        btnImageUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an Intent to pick an image from the gallery
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);

            }
        });


        // Sets an OnClickListener for the "Register" button
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get user's puppy name
                Intent intent = new Intent(DogProfileSettingActivity.this, DogProfileResultActivity.class);

                // Get image URI
                if (imageUri != null) {
                    // Add image to the Intent
                    intent.putExtra("imageUri", imageUri.toString());

                }

                // Check if the puppy name is empty
                String name = puppyName.getText().toString().trim();
                if (name.isEmpty()) {
                    Toast.makeText(DogProfileSettingActivity.this, "강아지 이름을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    return; // Stop further execution
                }

                // Add puppy name to the Intent
                intent.putExtra("PUPPY_NAME", name);

                // Get The ID of The selected RadioButton
                int selectedId = radioGroupDogBreed.getCheckedRadioButtonId();

                // Check if no RadioButton is selected
                if (selectedId == -1) {
                    Toast.makeText(DogProfileSettingActivity.this, "강아지 종류를 선택해 주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    // Get the selected ID
                    RadioButton selectedRadioButton = findViewById(selectedId);

                    // Get the text of the selected RadioButton
                    String selectedBreed = selectedRadioButton.getText().toString();

                    // Add selected breed to the Intent
                    intent.putExtra("selected_breed", selectedBreed);
                    startActivity(intent);
                    }
                }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            Log.d("DogProfileSettingActivity", "Selected Image URI: " + imageUri.toString());
            try {
                // Load the selected image into the ImageView
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imageViewPuppy.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
                // Load default image if there's an error
                imageViewPuppy.setImageResource(R.drawable.puppy_logo);
            }
        }
    }
}