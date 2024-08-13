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

    private static final int PICK_IMAGE_REQUEST = 1; // 이미지 선택 요청을 위한 코드
    private ImageView imageViewPuppy; // 선택된 이미지를 표시할 ImageView
    private Uri imageUri; // 선택된 이미지의 Uri를 저장하는 변수
    private Button btnImageUpload, btnRegister; // 버튼 변수들
    private EditText puppyName; // 강아지 이름 입력 필드
    private RadioGroup radioGroupDogBreed; // 강아지 종류 선택 라디오 그룹

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
        btnImageUpload = findViewById(R.id.btn_imageUpload);  // 이미지 업로드 버튼 초기화
        btnRegister = findViewById(R.id.btn_register);        // 등록하기 버튼 초기화
        puppyName = findViewById(R.id.puppy_name);            // 강아지 이름 입력 필드 초기화
        imageViewPuppy = findViewById(R.id.imageView_puppy);  // 이미지 뷰 초기화
        radioGroupDogBreed = findViewById(R.id.radioGroup_dog_breed); // 강아지 종류 라디오 그룹 초기화

        // Set default image
        imageViewPuppy.setImageResource(R.drawable.puppy_logo); // 기본 이미지를 로고로 설정

        // Set an OnClickListener for the "Image Upload" button
        btnImageUpload.setOnClickListener(new View.OnClickListener() {   // 이미지 업로드 버튼 클릭 리스너 설정
            @Override
            public void onClick(View view) {
                // Create an Intent to pick an image from the gallery
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);  // 갤러리에서 이미지 선택을 위한 인텐트 생성
                startActivityForResult(intent, PICK_IMAGE_REQUEST);     // 이미지 선택 액티비티를 시작하고, 결과는 PICK_IMAGE_REQUEST 코드로 전달됨
            }
        });

        // Set an OnClickListener for the "Register" button
        btnRegister.setOnClickListener(new View.OnClickListener() {             // 등록하기 버튼 클릭 리스너 설정
            @Override
            public void onClick(View view) {
                // Get user's puppy name
                Intent intent = new Intent(DogProfileSettingActivity.this, DogProfileResultActivity.class);   // 현재 액티비티에서 결과 액티비티로 이동하기 위한 인텐트 생성

                Log.e("DogProfileSettingActivity", "imageURI : " + imageUri); // 현재 이미지 Uri 로그로 출력

                // Get image URI
                if (imageUri != null) {
                    // Add image to the Intent
                    intent.putExtra("imageUri", imageUri); // 선택된 이미지의 Uri를 인텐트에 추가
                }

                // Check if the puppy name is empty
                String name = puppyName.getText().toString().trim();
                if (name.isEmpty()) {
                    Toast.makeText(DogProfileSettingActivity.this, "강아지 이름을 입력해 주세요.", Toast.LENGTH_SHORT).show(); // 강아지 이름이 입력되지 않았을 경우 경고
                    return; // 이후 실행을 중단
                }

                // Add puppy name to the Intent
                intent.putExtra("PUPPY_NAME", name); // 강아지 이름을 인텐트에 추가

                // Get The ID of The selected RadioButton
                int selectedId = radioGroupDogBreed.getCheckedRadioButtonId(); // 선택된 라디오 버튼의 ID 가져오기

                // Check if no RadioButton is selected
                if (selectedId == -1) {
                    Toast.makeText(DogProfileSettingActivity.this, "강아지 종류를 선택해 주세요.", Toast.LENGTH_SHORT).show(); // 강아지 종류가 선택되지 않았을 경우 경고
                } else {
                    // Get the selected ID
                    RadioButton selectedRadioButton = findViewById(selectedId); // 선택된 라디오 버튼 객체 가져오기

                    // Get the text of the selected RadioButton
                    String selectedBreed = selectedRadioButton.getText().toString(); // 선택된 라디오 버튼의 텍스트 가져오기

                    // Add selected breed to the Intent
                    intent.putExtra("selected_breed", selectedBreed); // 선택된 강아지 종류를 인텐트에 추가
                    startActivity(intent); // 결과 액티비티 시작
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData(); // 선택된 이미지의 Uri를 가져와서 imageUri 변수에 저장
            Log.d("DogProfileSettingActivity", "Selected Image URI: " + imageUri); // 선택된 이미지의 Uri를 로그로 출력
            try {
                // Load the selected image into the ImageView
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri); // Uri를 통해 이미지를 Bitmap으로 변환
                imageViewPuppy.setImageBitmap(bitmap); // 변환된 이미지를 ImageView에 설정
            } catch (Exception e) {
                e.printStackTrace(); // 예외 발생 시 스택 트레이스 출력
                // Load default image if there's an error
                imageViewPuppy.setImageResource(R.drawable.puppy_logo); // 이미지 로딩 오류 시 기본 이미지로 설정
            }
        }
    }
}
