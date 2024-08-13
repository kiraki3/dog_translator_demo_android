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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class DogProfileResultActivity extends AppCompatActivity {

    private TextView puppyNameTextView;  // 강아지 이름 선언

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dog_profile_result);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();  // Intent 수신
        String puppyName = intent.getStringExtra("PUPPY_NAME");     // 받은 값을 String 으로 변환해서 String 변수에 담는다.
        String selectedBreed = intent.getStringExtra("selected_breed");     // 받은 값을 String 으로 변환해서 String 변수에 담는다.
        Uri imageUri = intent.getParcelableExtra("imageUri");       // 받은 값을 Parcelable 객체로 변환해서 Uri 변수에 담는다.

        // Set puppy name
        puppyNameTextView = findViewById(R.id.puppy_name_text_view); // xml 강아지 이름과 연결

        if (puppyName != null) {  // 강아지 이름을 제대로 받았다면..
            puppyNameTextView.setText(puppyName);
        }
        // Set puppy breed
        TextView textViewSelectedBreed = findViewById(R.id.selected_breed); // xml 강아지 종류와 연결
        if (textViewSelectedBreed != null) {  // 강아지 종류를 제대로 받았다면..
            textViewSelectedBreed.setText(puppyName); //
        }

        // Set Image
        ImageView imageView = findViewById(R.id.user_imageView_puppy);  // xml 강아지 이미지와 연결
        // Find image uri
        Log.e("DogProfileResultActivity", "Selected Image URI: " + imageUri);   // imageUri 받았는지 확인

        if (imageUri != null) { // imageUri를 제대로 받았다면
            try {   // 문제 확인
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