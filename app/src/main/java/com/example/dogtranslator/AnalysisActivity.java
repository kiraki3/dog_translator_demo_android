package com.example.dogtranslator;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dogtranslator.audio.AudioPlayer;
import com.example.dogtranslator.audio.AudioPlayerImpl;
import com.example.dogtranslator.audio.AudioRecorder;
import com.example.dogtranslator.audio.AudioRecorderImpl;
import com.example.dogtranslator.databinding.ActivityAnalysisBinding;

import java.io.File;

public class AnalysisActivity extends AppCompatActivity {

    private ActivityAnalysisBinding binding;
    private Uri imageUri;
    private String puppyName;

    private AudioRecorder recorder;
    private AudioPlayer player;
    private File audioFile;

    private static final int REQUEST_PERMISSIONS = 1; // 권한 요청 코드

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setDataBinding();
        setClickEvent();
        setIntentData();
        requestAudioPermission();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setDataBinding() {
        binding = ActivityAnalysisBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
    }

    private void setClickEvent() {
        // 녹음 시작 버튼 클릭 리스너 설정
        binding.ibRecordStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recorder == null) {
                    recorder = new AudioRecorderImpl(getApplicationContext());
                }
                audioFile = new File(getCacheDir(), "audio.mp3");
                recorder.start(audioFile);
                binding.setIsRecord(true);
            }
        });
        // 녹음 중지 버튼 클릭 리스너 설정
        binding.ibRecordStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (audioFile != null) {
                    recorder.stop();
                    binding.setIsRecord(false);
                }
            }
        });
        // 재생 시작 버튼 클릭 리스너 설정
        binding.ibPlayStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (audioFile != null) {
                    if (player == null) {
                        player = new AudioPlayerImpl(getApplicationContext());
                    }
                    player.playFile(audioFile);
                    binding.setIsPlay(true);
                }
            }
        });
        // 재생 중지 버튼 클릭 리스너 설정
        binding.ibPlayStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (player != null) {
                    player.stop();
                    binding.setIsPlay(false);
                }
            }
        });

        // 분석 결과 이동 버튼 클릭 리스너 설정
        binding.ibGoAnalysisResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AnalysisActivity.this, AnalysisResultActivity.class);
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


        if (mImageUri != null) { // imageUri를 제대로 받았다면
            try {   // 문제 확인
                // Load the selected image into the ImageView
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);  // setImageBitmap은 bitmap형식으로 값을 받아와야하기 때문에 MediaStore.Images.Media.getBitmap() 메소드로 값을 가져오고
                binding.ivPuppyImage.setImageBitmap(bitmap); // 전달받은 이미지를 화면에 설정해준다.
                imageUri = mImageUri;
            } catch (Exception e) { // Exception Error 확인
                e.printStackTrace();
            }
        }

        if (mPuppyName != null && !mPuppyName.isEmpty()) {
            binding.tvPuppyName.setText(mPuppyName);
            puppyName= mPuppyName;
        }
    }

    private void requestAudioPermission() {
        // 오디오 녹음 권한 확인 및 요청
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 권한 요청 결과 처리
        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한이 부여되면 아무 작업도 하지 않음
            } else {
                // 권한이 거부되면 앱 종료
                finish();
            }
        }
    }
}
