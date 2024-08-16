package com.example.dogtranslator;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class AnalysisActivity extends AppCompatActivity {

    private TextView puppyNameTextView;  // 강아지 이름 선언
    private ImageView imageView;

    private static final int REQUEST_PERMISSIONS = 1; // 권한 요청 코드

    private MediaRecorder recorder; // MediaRecorder 객체, 녹음 기능을 수행
    private MediaPlayer player; // MediaPlayer 객체, 재생 기능을 수행
    private File outputFile; // 녹음된 파일을 저장할 File 객체

    private ImageButton recordButton; // 녹음 시작 버튼
    private ImageButton stopRecordButton; // 녹음 중지 버튼
    private ImageButton playButton; // 재생 시작 버튼
    private ImageButton stopPlayButton; // 재생 중지 = 분석하기 버튼

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis); // 레이아웃 설정

        Intent intent = getIntent();  // Intent 수신
        Uri imageUri = intent.getParcelableExtra("imageUri");       // 받은 값을 Parcelable 객체로 변환해서 Uri 변수에 담는다.
        String puppyName = intent.getStringExtra("PUPPY_NAME");     // 받은 값을 String 으로 변환해서 String 변수에 담는다.

        // 버튼 객체 초기화
        recordButton = findViewById(R.id.recordButton);
        stopRecordButton = findViewById(R.id.stopRecordButton);
        playButton = findViewById(R.id.playButton);
        stopPlayButton = findViewById(R.id.stopPlayButton);
        puppyNameTextView = findViewById(R.id.puppy_name_text_view); // xml 강아지 이름과 연결
        imageView = findViewById(R.id.user_imageView_puppy);  // xml 강아지 이미지와 연결


        if (puppyName != null) {  // 강아지 이름을 제대로 받았다면..
            puppyNameTextView.setText(puppyName + "(이)의 말을 들어보아요.");
        }

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


        // 오디오 녹음 권한 확인 및 요청
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_PERMISSIONS);
        }

        // 녹음 시작 버튼 클릭 리스너 설정
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecording(); // 녹음 시작
                // 버튼 상태 조정
                recordButton.setVisibility(View.GONE); // 녹음 시작 버튼 숨기기
                stopRecordButton.setVisibility(View.VISIBLE); // 녹음 중지 버튼 보이기
                playButton.setVisibility(View.GONE); // 재생 시작 버튼 숨기기
                stopPlayButton.setVisibility(View.VISIBLE); // 재생 중지 버튼 보이기
            }
        });

        // 녹음 중지 버튼 클릭 리스너 설정
        stopRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRecording(); // 녹음 중지
                // 버튼 상태 조정
                recordButton.setVisibility(View.VISIBLE); // 녹음 시작 버튼 보이기
                stopRecordButton.setVisibility(View.GONE); // 녹음 중지 버튼 숨기기
                playButton.setVisibility(View.VISIBLE); // 재생 시작 버튼 보이기
                stopPlayButton.setVisibility(View.VISIBLE); // 재생 중지 버튼 보이기
            }
        });

        // 재생 시작 버튼 클릭 리스너 설정
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPlaying(); // 재생 시작
                // 버튼 상태 조정
                playButton.setVisibility(View.VISIBLE); // 재생 시작 버튼 보이기
                stopPlayButton.setVisibility(View.VISIBLE); // 재생 중지 버튼 보이기
                recordButton.setVisibility(View.VISIBLE); // 녹음 시작 버튼 보이기
                stopRecordButton.setVisibility(View.GONE); // 녹음 중지 버튼 숨기기
            }
        });

        // 녹음 중지 버튼 클릭 리스너 설정
        stopPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopPlaying(); // 재생 중지
                // 버튼 상태 조정
                playButton.setVisibility(View.VISIBLE); // 재생 시작 버튼 보이기
                stopPlayButton.setVisibility(View.VISIBLE); // 재생 중지 버튼 보이기
                recordButton.setVisibility(View.VISIBLE); // 녹음 시작 버튼 보이기
                stopRecordButton.setVisibility(View.GONE); // 녹음 중지 버튼 숨기기
            }
        });

        // 분석하기 버튼 클릭 리스너 설정
        stopPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (outputFile != null && outputFile.exists()) {
                    Intent intent = new Intent(AnalysisActivity.this, AnalysisResultActivity.class);
                    intent.putExtra("imageUri", imageUri);
                    intent.putExtra("puppyname", puppyName);
                    intent.putExtra("audioFilePath", outputFile.getAbsolutePath());
                    startActivity(intent);
                } else {
                    Toast.makeText(AnalysisActivity.this, "녹음된 파일이 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // 녹음 시작 메서드
    private void startRecording() {
        // 기존의 MediaRecorder 객체가 있다면 해제
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }

        try {
            // 캐시 디렉터리에 녹음 파일을 저장하기 위한 File 객체 생성
            outputFile = new File(getCacheDir(), "recording.mp4");

            // MediaRecorder 객체 생성 및 설정
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC); // 마이크 소리 입력
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4); // 출력 포맷 설정
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC); // 오디오 인코더 설정

            // FileOutputStream을 사용하여 파일 경로 설정
            FileOutputStream fos = new FileOutputStream(outputFile);
            recorder.setOutputFile(fos.getFD());

            // 녹음 준비 및 시작
            recorder.prepare();
            recorder.start();
        } catch (IOException e) {
            Log.e("AnalysisActivity", "녹음 시작 오류", e); // 오류 로그
        }
    }

    // 녹음 중지 메서드
    private void stopRecording() {
        if (recorder != null) {
            recorder.stop(); // 녹음 중지
            recorder.release(); // MediaRecorder 객체 해제
            recorder = null;
        }
    }

    // 재생 시작 메서드
    private void startPlaying() {
        // 기존의 MediaPlayer 객체가 있다면 해제
        if (player != null) {
            player.release();
            player = null;
        }

        // 녹음된 파일이 존재할 경우
        if (outputFile != null && outputFile.exists()) {
            player = new MediaPlayer();
            try {
                player.setDataSource(outputFile.getAbsolutePath()); // 파일 경로 설정
                player.prepare();
                player.start(); // 재생 시작
            } catch (IOException e) {
                Log.e("AnalysisActivity", "재생 오류", e); // 오류 로그
            }
        } else {
            Log.e("AnalysisActivity", "파일이 존재하지 않음: " + outputFile); // 파일 없음 로그
        }
    }

    // 재생 중지 메서드
    private void stopPlaying() {
        if (player != null) {
            player.stop(); // 재생 중지
            player.release(); // MediaPlayer 객체 해제
            player = null;
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
