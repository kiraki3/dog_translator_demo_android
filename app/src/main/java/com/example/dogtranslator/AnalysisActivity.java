package com.example.dogtranslator;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.io.InputStream;

public class AnalysisActivity extends AppCompatActivity {

    private TextView puppyNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_analysis);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        String puppyName = intent.getStringExtra("PUPPY_NAME");
        String selectedBreed = intent.getStringExtra("selected_breed");
        String imageUriString = intent.getStringExtra("imageUri");

        // Set puppy name
        puppyNameTextView = findViewById(R.id.puppy_name_text_view);

        if (puppyName != null) {
            puppyNameTextView.setText(puppyName);
        }
        // Set puppy breed
        TextView textViewSelectedBreed = findViewById(R.id.selected_breed);
        textViewSelectedBreed.setText(selectedBreed);

        // Set Image
        ImageView imageView = findViewById(R.id.user_imageView_puppy);
        // Find image uri
        Log.e("AnalysisActivity", "Selected Image URI: " + imageUriString);
        if (imageUriString != null) {
            try {
                Uri imageUri = Uri.parse(imageUriString);
                Log.d("AnalysisActivity", "Selected Image URI: " + imageUri.toString());
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("AnalysisActivity", "Failed to load image: " + e.getMessage());
                imageView.setImageResource(R.drawable.puppy_logo);
            }
        } else {
            // Set default image if no image URI is provided
            imageView.setImageResource(R.drawable.puppy_logo);
        }

    }
}