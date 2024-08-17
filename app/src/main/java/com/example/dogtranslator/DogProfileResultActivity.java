package com.example.dogtranslator;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dogtranslator.databinding.ActivityDogProfileResultBinding;

public class DogProfileResultActivity extends AppCompatActivity {

    private ActivityDogProfileResultBinding binding;
    private Uri imageUri;
    private String puppyName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setDataBinding();
        setClickEvent();
        setIntentData();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setDataBinding() {
        binding = ActivityDogProfileResultBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
    }

    private void setClickEvent() {
        binding.btnRewriteProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DogProfileResultActivity.this, DogProfileSettingActivity.class);
                startActivity(intent);
            }
        });

        binding.btnGoAnalytics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DogProfileResultActivity.this, AnalysisActivity.class);
                intent.putExtra("puppy_image", imageUri);
                intent.putExtra("puppy_name", puppyName);
                startActivity(intent);
            }
        });
    }

    private void setIntentData() {
        Intent intent = getIntent();  // Intent 수신
        Uri mImageUri = intent.getParcelableExtra("puppy_image");       // 받은 값을 Parcelable 객체로 변환해서 Uri 변수에 담는다.
        String mPuppyName = intent.getStringExtra("puppy_name");     // 받은 값을 String 으로 변환해서 String 변수에 담는다.
        String puppyBreed = intent.getStringExtra("puppy_breed");     // 받은 값을 String 으로 변환해서 String 변수에 담는다.


        if (mImageUri != null) { // imageUri를 제대로 받았다면
            try {   // 문제 확인
                // Load the selected image into the ImageView
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageUri);  // setImageBitmap은 bitmap형식으로 값을 받아와야하기 때문에 MediaStore.Images.Media.getBitmap() 메소드로 값을 가져오고
                binding.ivPuppyImage.setImageBitmap(bitmap); // 전달받은 이미지를 화면에 설정해준다.
                imageUri = mImageUri;
            } catch (Exception e) { // Exception Error 확인
                e.printStackTrace();
            }
        }

        if (mPuppyName != null && !mPuppyName.isEmpty()) {
            binding.tvPuppyName.setText(mPuppyName);
            puppyName = mPuppyName;
        }

        if (puppyBreed != null && !puppyBreed.isEmpty()) {
            binding.tvPuppyBreed.setText(puppyBreed);
        }


    }
}