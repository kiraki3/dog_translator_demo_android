package com.example.dogtranslator;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AnalysisResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_analysis_result);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Get the ImageView for user image
        ImageView imageView = findViewById(R.id.user_imageView_puppy);

        // Get the Intent
        Intent intent = getIntent();

        // Get image URI from Intent
        Uri imageUri = intent.getParcelableExtra("imageUri");

        // If image URI is available, load image
        if (imageUri != null) { // imageUri를 제대로 받았다면
            try {  // 문제 확인
                // Load the selected image into the ImageView
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);  // setImageBitmap은 bitmap형식으로 값을 받아와야하기 때문에 MediaStore.Images.Media.getBitmap() 메소드로 값을 가져오고
                imageView.setImageBitmap(bitmap); // 전달받은 이미지를 화면에 설정해준다.
            } catch (Exception e) { // Exception Error 확인
                e.printStackTrace();
                // Load default image if there's an error
                imageView.setImageResource(R.drawable.puppy_logo);  // Error 가 있다면 기본 이미지로 설정
            }
        } else {
            // Set default image if no image URI is provided
            imageView.setImageResource(R.drawable.puppy_logo);  // imageUri를 제대로 받지 않았다면, 기본 이미지로 설정
        }
    }
}