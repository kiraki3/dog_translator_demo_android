package com.example.dogtranslator;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class AnalysisResultActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView txtAnalysisResult;
    private Button btnBack, btnHome;
    private ImageButton playButton;
    private ImageButton stopButton;
    private MediaPlayer player; // MediaPlayer 객체, 재생 기능을 수행
    private File audioFile; // 파일 객체를 이용해 오디오 파일을 처리합니다.


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
        playButton = findViewById(R.id.playButton);
        stopButton = findViewById(R.id.stopButton);
        // Get the EditText for displaying analysis result
        txtAnalysisResult = findViewById(R.id.txt_analysis_result);
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
        String audioFilePath = intent.getStringExtra("audioFilePath");


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

        // 오디오 파일 설정
        if (audioFilePath != null) {
            audioFile = new File(audioFilePath);
        }

        // Display the Button to recorder play
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPlaying();
                playButton.setVisibility(View.GONE); // 재생 시작 버튼 숨기기
                stopButton.setVisibility(View.VISIBLE); // 재생 중지 버튼 보이기
            }
        });

        // Stop 버튼 클릭 리스너 설정
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopPlaying();
                playButton.setVisibility(View.VISIBLE); // 재생 시작 버튼 숨기기
                stopButton.setVisibility(View.GONE); // 재생 중지 버튼 보이기
            }
        });



        // Generate and display a random analysis result
        String[] messages = {
                "와, 당신은 정말 대단해요!",
                "오늘도 잘 지내고 있네요.",
                "항상 행복하세요!",
                "당신의 사랑이 필요해요.",
                "당신과 함께 하는 시간이 좋아요.",
                "물 좀 주세요, 목이 너무 말라요!",
                "산책 가고 싶어요, 신나게 뛰어놀고 싶어요!",
                "지금 놀아주세요, 저도 재미있게 놀고 싶어요!",
                "햇볕 쬐고 싶어요, 밖으로 나가고 싶어요!",
                "편안한 자리를 찾아주세요, 좀 쉬고 싶어요!",
                "산책은 언제 갈까요? 기다리고 있어요!",
                "저도 간식으로 하루를 시작하고 싶어요!",
                "물어볼게 있어요, 잘 들어주세요!",
                "목욕하고 싶어요."
        };
        Random random = new Random();
        String randomMessage = messages[random.nextInt(messages.length)];
        txtAnalysisResult.setText(randomMessage);  // 분석 결과를 표시


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

    private void startPlaying() {
        if (audioFile != null && audioFile.exists()) {
            // 기존 MediaPlayer 객체가 있다면 해제
            if (player != null) {
                player.release();
                player = null;
            }

            player = new MediaPlayer();
            try {
                player.setDataSource(audioFile.getAbsolutePath());
                player.prepare();
                player.start();
                // 재생이 완료되면 호출
                player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        playButton.setVisibility(View.VISIBLE); // 재생 시작 버튼 보이기
                        stopButton.setVisibility(View.GONE); // 재생 중지 버튼 숨기기
                    }
                });
            } catch (IOException e) {
                Log.e("AnalysisResultActivity", "재생 오류", e);
                Toast.makeText(this, "오디오 재생 오류", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "오디오 파일이 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopPlaying() {
        if (player != null) {
            player.stop();
            player.release(); // MediaPlayer 객체 해제
            player = null;
        }
    }
}