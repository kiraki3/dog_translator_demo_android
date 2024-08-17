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

import com.example.dogtranslator.audio.AudioPlayer;
import com.example.dogtranslator.audio.AudioPlayerImpl;
import com.example.dogtranslator.databinding.ActivityAnalysisResultBinding;

import java.io.File;
import java.util.Random;

public class AnalysisResultActivity extends AppCompatActivity {

    private ActivityAnalysisResultBinding binding;
    private Uri imageUri;
    private String puppyName;

    private AudioPlayer player;
    private File audioFile;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setDataBinding();
        setClickEvent();
        setIntentData();
        setRandomMessage();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    private void setDataBinding() {
        binding = ActivityAnalysisResultBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
    }

    private void setClickEvent() {
        // 재생 시작 버튼 클릭 리스너 설정
        binding.ibPlayStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (audioFile == null) {
                    audioFile = new File(getCacheDir(), "audio.mp3");
                }
                if (player == null) {
                    player = new AudioPlayerImpl(getApplicationContext());
                }
                player.playFile(audioFile);
                binding.setIsPlay(true);
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

        binding.btnBackAnalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AnalysisResultActivity.this, AnalysisActivity.class);
                intent.putExtra("puppy_image", imageUri);
                intent.putExtra("puppy_name", puppyName);
                startActivity(intent);
            }
        });

        binding.btnBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AnalysisResultActivity.this, MainActivity.class);
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
//            binding.tvPuppyName.setText(mPuppyName);
            puppyName= mPuppyName;
        }
    }

    private void setRandomMessage() {
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
        binding.tvAnalysisResult.setText(randomMessage);
    }

}