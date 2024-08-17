package com.example.dogtranslator;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.RadioButton;
import android.graphics.Bitmap;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dogtranslator.databinding.ActivityDogProfileSettingBinding;

public class DogProfileSettingActivity extends AppCompatActivity {

    private ActivityDogProfileSettingBinding binding;
    private static final int PICK_IMAGE_REQUEST = 1; // 이미지 선택 요청을 위한 코드
    private Uri imageUri; // 선택된 이미지의 Uri를 저장하는 변수


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setDataBinding();
        setClickEvent();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setDataBinding() {
        binding = ActivityDogProfileSettingBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
    }

    private void setClickEvent() {
        // Set an OnClickListener for the "Image Upload" button
        binding.btnSelectImage.setOnClickListener(new View.OnClickListener() {   // 이미지 업로드 버튼 클릭 리스너 설정
            @Override
            public void onClick(View view) {
                // Create an Intent to pick an image from the gallery
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);  // 갤러리에서 이미지 선택을 위한 인텐트 생성
                startActivityForResult(intent, PICK_IMAGE_REQUEST);     // 이미지 선택 액티비티를 시작하고, 결과는 PICK_IMAGE_REQUEST 코드로 전달됨
            }
        });

        // Set an OnClickListener for the "Register" button
        binding.btnRegister.setOnClickListener(new View.OnClickListener() {             // 등록하기 버튼 클릭 리스너 설정
            @Override
            public void onClick(View view) {
                if (isValidate()) {
                    Intent intent = new Intent(DogProfileSettingActivity.this, DogProfileResultActivity.class);   // 현재 액티비티에서 결과 액티비티로 이동하기 위한 인텐트 생성
                    intent.putExtra("puppy_image", imageUri); // 선택된 이미지의 Uri를 인텐트에 추가
                    String name = binding.etPuppyName.getText().toString().trim();
                    // Add puppy name to the Intent
                    intent.putExtra("puppy_name", name); // 강아지 이름을 인텐트에 추가
                    // Get the selected ID
                    // Get The ID of The selected RadioButton
                    int selectedId = binding.rgPuppyBreed.getCheckedRadioButtonId(); // 선택된 라디오 버튼의 ID 가져오기
                    RadioButton selectedRadioButton = binding.getRoot().findViewById(selectedId);
                    // Get the text of the selected RadioButton
                    String selectedBreed = selectedRadioButton.getText().toString(); // 선택된 라디오 버튼의 텍스트 가져오기
                    // Add selected breed to the Intent
                    intent.putExtra("puppy_breed", selectedBreed); // 선택된 강아지 종류를 인텐트에 추가
                    startActivity(intent); // 결과 액티비티 시작
                } else {
                    Toast.makeText(getApplicationContext(), "이름과 종류를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isValidate() {
        return !binding.etPuppyName.getText().toString().trim().isEmpty() && binding.rgPuppyBreed.getCheckedRadioButtonId() != -1;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData(); // 선택된 이미지의 Uri를 가져와서 imageUri 변수에 저장
            try {
                // Load the selected image into the ImageView
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri); // Uri를 통해 이미지를 Bitmap으로 변환
                binding.ivPuppyImage.setImageBitmap(bitmap); // 변환된 이미지를 ImageView에 설정
            } catch (Exception e) {
                e.printStackTrace(); // 예외 발생 시 스택 트레이스 출력
                // Load default image if there's an error
                binding.ivPuppyImage.setImageResource(R.drawable.puppy_logo); // 이미지 로딩 오류 시 기본 이미지로 설정
            }
        }
    }
}
