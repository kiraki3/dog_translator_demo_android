package com.example.dogtranslator;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AnalysisResultActivity extends AppCompatActivity {

    private ImageView imageView;
    private EditText editaAalysisResult;
    private Button btnBack, btnHome;


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
        imageView = findViewById(R.id.user_imageView_puppy);
        // Get the EditText for displaying analysis result
        editaAalysisResult = findViewById(R.id.edit_analysis_result);
        // Get the Button for displaying analysis result
        btnBack = findViewById(R.id.btn_back_toAnalysis);
        // Get the Button for displaying analysis result
        btnHome = findViewById(R.id.btn_back_toHome);

        // Get the Intent
        Intent intent = getIntent();
        // Get puppy name from Intent
        String puppyName = intent.getStringExtra("puppyname");
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

        // Click the back button to return to the previous page.
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AnalysisResultActivity.this, AnalysisActivity.class);
                intent.putExtra("imageUri", imageUri);
                intent.putExtra("puppyname", puppyName);
                startActivity(intent);
            }
        });

        // Click the Button to navigate to Home.
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AnalysisResultActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}