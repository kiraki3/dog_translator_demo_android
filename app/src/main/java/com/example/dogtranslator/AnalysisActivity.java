package com.example.dogtranslator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AnalysisActivity extends AppCompatActivity {

    private TextView puppyNameTextView;
    private Button btnAnalysisResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_analysis);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        puppyNameTextView = findViewById(R.id.puppy_name_text_view);

        Intent intent = getIntent();
        String puppyName = intent.getStringExtra("PUPPY_NAME");

        if (puppyName != null) {
            puppyNameTextView.setText(puppyName);
        }


        //
        btnAnalysisResult = findViewById(R.id.btn_analysisResult);
        btnAnalysisResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AnalysisActivity.this, AnalysisResultActivity.class);
                startActivity(intent);
            }
        });
    }
}