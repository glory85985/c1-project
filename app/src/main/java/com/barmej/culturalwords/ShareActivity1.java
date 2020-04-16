package com.barmej.culturalwords;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class ShareActivity1 extends AppCompatActivity {
    private int mQuestionPictureID;
    private ImageView mQuestionPicture;
    public EditText mEditTextShareTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        mEditTextShareTitle = findViewById(R.id.edit_text_share_title);
        mQuestionPicture = findViewById(R.id.image_view_question);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mQuestionPictureID = bundle.getInt("question picture extra");
            mQuestionPicture.setImageResource(mQuestionPictureID);
        }
        SharedPreferences sharedPreferences = getSharedPreferences("app_pref", MODE_PRIVATE);
        String questionTitle = sharedPreferences.getString("share_title","");
        mEditTextShareTitle.setText(questionTitle);
    }

    public void onShareQuestionClicked(View view){
      String questionTitle = mEditTextShareTitle.getText().toString();
        Resources resources = getResources();
        Uri imageUri = new Uri.Builder()
                .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                .authority(resources.getResourcePackageName(mQuestionPictureID))
                .appendPath(resources.getResourceTypeName(mQuestionPictureID))
                .appendPath(resources.getResourceEntryName(mQuestionPictureID))
                .build();
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.putExtra(Intent.EXTRA_TEXT,questionTitle);
        shareIntent.setType("image/*");
        startActivity(shareIntent);
        SharedPreferences sharedPreferences = getSharedPreferences("app_pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("share_title", questionTitle);
        editor.apply();
    }
}
