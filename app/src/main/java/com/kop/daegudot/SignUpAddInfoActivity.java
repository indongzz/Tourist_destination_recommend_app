package com.kop.daegudot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class SignUpAddInfoActivity extends AppCompatActivity implements View.OnClickListener{

    EditText editName, editEmail;
    ImageButton backBtn;
    Button btnOk, btnCheckDup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_add_info);

        editName = findViewById(R.id.edit_nickName);
        editEmail = findViewById(R.id.edit_email);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        editEmail.setText(bundle.getString("email"));
        editName.setText(bundle.getString("name"));

        backBtn = findViewById(R.id.backBtn);
        btnCheckDup = findViewById(R.id.btn_checkDup);
        btnOk = findViewById(R.id.btn_ok);

        backBtn.setOnClickListener(this);
        btnCheckDup.setOnClickListener(this);
        btnOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.backBtn:
                finish();
                break;
            case R.id.btn_checkDup:
                // 닉네임 중복 확인
                break;
            case R.id.btn_ok:
                // 회원가입 모두 완료
                break;
        }
    }
}