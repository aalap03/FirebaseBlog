package com.example.aalap.blogs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class PostScreen extends AppCompatActivity {

    private static final int PICK_GALLERY = 5;
    EditText title, desc;
    ImageView postImage;
    Button post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_screen);

        title = findViewById(R.id.post_title);
        desc = findViewById(R.id.post_description);
        postImage = findViewById(R.id.post_image);
        post = findViewById(R.id.post);

        postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_GALLERY);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            if(requestCode == PICK_GALLERY) {
                postImage.setImageURI(data.getData());
            }
        }
    }
}
