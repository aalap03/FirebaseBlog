package com.example.aalap.blogs;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginRegister extends AppCompatActivity {

    EditText userName, password;
    View loginButton;
    private FirebaseAuth authInstance;
    private static final String TAG = "LoginActivity:";
    ProgressBar progressBar;
    TextView loginText;
    int loginButtonExpandedWidth;
    public static final String EMAIL = "Email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton = findViewById(R.id.login_button);
        loginButtonExpandedWidth = loginButton.getMeasuredWidth();
        Log.d(TAG, "onCreate: " + getButtonWidth());
        userName = findViewById(R.id.user_name);
        password = findViewById(R.id.password);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.getIndeterminateDrawable().setColorFilter(
                ContextCompat.getColor(LoginRegister.this, R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
        loginText = findViewById(R.id.text);

        authInstance = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(userName.getText().toString()))
                    Toast.makeText(LoginRegister.this, "Enter Username", Toast.LENGTH_SHORT).show();
                else if (TextUtils.isEmpty(password.getText().toString()))
                    Toast.makeText(LoginRegister.this, "Enter Password", Toast.LENGTH_SHORT).show();
                else {
                    animateButtonWidth(true);
                    loginUser(userName.getText().toString(), password.getText().toString());
                }
            }
        });
    }

    private void registerUser(String userName, String password) {
        authInstance.createUserWithEmailAndPassword(userName, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                //circular effect with for new screen
                resultLogs(authResult);
                Toast.makeText(LoginRegister.this, "Registered", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                //set button with text again

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        animateButtonWidth(false);
                    }
                }, 500);
                Log.d(TAG, "onFailure: " + e.getMessage());
            }
        });
    }

    private void resultLogs(AuthResult authResult) {
        Intent intent = new Intent(this, MainScreen.class);
        intent.putExtra(EMAIL, authResult.getUser().getEmail());
        startActivity(intent);
    }

    private void loginUser(String userName, String password) {
        authInstance.signInWithEmailAndPassword(userName, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                    resultLogs(authResult);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                Log.d(TAG, "onFailure: "+e.getMessage());
            }
        });
    }

    private void animateButtonWidth(final boolean isShrink) {

        ValueAnimator anim;
        if (isShrink)
            anim = ValueAnimator.ofInt(loginButton.getMeasuredWidth(), getFabWidth());
        else
            anim = ValueAnimator.ofInt(getFabWidth(), getButtonWidth());

        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = loginButton.getLayoutParams();
                layoutParams.width = val;
                loginButton.setLayoutParams(layoutParams);

                progressBar.setVisibility(isShrink ? View.VISIBLE : View.INVISIBLE);
                loginText.setVisibility(isShrink ? View.INVISIBLE : View.VISIBLE);
            }
        });
        anim.setDuration(500);
        anim.start();
    }

    private int getFabWidth() {
        return (int) getResources().getDimension(R.dimen.fab_width);
    }

    private int getButtonWidth() {
        return (int) getResources().getDimension(R.dimen.button_width);
    }
}
