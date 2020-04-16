package com.barmej.culturalwords;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSIONS_WRITE_EXTERNAL_STORAGE = 123;
    private static final String BUNDLE_CURRENT_INDEX = "BUNDLE_CURRENT_INDEX";
    int randomQuestionPictures;
    private  String mCurrentAnswer;
    private String mCurrentAnswerDescription;
    private String [] Answers;
    private String [] Answer_description;
    ImageView questionImageView;
    Random random = new Random();
    public int [] questionPictures = {
            R.drawable.icon_1,
            R.drawable.icon_2,
            R.drawable.icon_3,
            R.drawable.icon_4,
            R.drawable.icon_5,
            R.drawable.icon_6,
            R.drawable.icon_7,
            R.drawable.icon_8,
            R.drawable.icon_9,
            R.drawable.icon_10,
            R.drawable.icon_11,
            R.drawable.icon_12,
            R.drawable.icon_13
    };
    private int mcurrentindex = 0;
    public int mCurrentPicture = questionPictures[0];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences("app_pref",MODE_PRIVATE);
        String appLang = sharedPreferences.getString("app_lang","");
        if(!appLang.equals(""))
            LocaleHelper.setLocale(this, appLang);
        setContentView(R.layout.activity_main);
        questionImageView = findViewById(R.id.image_view_question);
        Answers = getResources().getStringArray(R.array.answers);
        Answer_description = getResources().getStringArray(R.array.answer_description);
    }

    private void showLanguageDialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.change_lang_text)
                .setItems(R.array.languages, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String language = "ar";
                        switch (which) {
                            case 0:
                                language = "ar";
                                break;
                            case 1:
                                language = "en";
                                break;
                        }
                        saveLanguage(language);
                        LocaleHelper.setLocale(MainActivity.this, language);
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }).create();
        alertDialog.show();
    }

    private void saveLanguage(String lang){
        SharedPreferences sharedPreferences = getSharedPreferences("app_pref",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("app_lang",lang) ;
        editor.apply();
    }

    public void onClickedChangeLang(View view){
        showLanguageDialog();
    }

        public void show(int i){
        Drawable questionDrawable = ContextCompat.getDrawable(this, questionPictures[mcurrentindex]);
        questionImageView.setImageDrawable(questionDrawable);

    }

    public void changePicture(View view){
         randomQuestionPictures = random.nextInt(13);
         mcurrentindex = randomQuestionPictures;
         mCurrentAnswer = Answers[randomQuestionPictures] + Answer_description[randomQuestionPictures];
         mCurrentAnswerDescription = Answer_description[randomQuestionPictures];
         mCurrentPicture = questionPictures[randomQuestionPictures];
         show(mcurrentindex);
    }
    public void onClickedOpenAnswer(View view){
        if(mcurrentindex==0){
            mCurrentAnswer = Answers[0] + Answer_description[0] ;}
        Intent intent =new Intent(MainActivity.this,AnswerActivity.class);
        intent.putExtra("question_answer",mCurrentAnswer);
        startActivity(intent);
    }

    public void onShareQuestionClicked(View view){
        Intent intent = new Intent(MainActivity.this,ShareActivity1.class);
        intent.putExtra ("question picture extra",mCurrentPicture);
        startActivity(intent);
    }

    private void shareImage() {

    }

    /**
     *  هذه الدالة تقوم بطلب صلاحية الكتابة على ال external storage حتى يمكن حفظ ملف الصورة
     * <p>
     * وفي حال الحصول على الصلاحية تقوم باستدعاء دالة shareImage لمشاركة الصورة
     **/
    private void checkPermissionAndShare() {
        // insertImage في النسخ من آندرويد 6 إلى آندرويد 9 يجب طلب الصلاحية لكي نتمكن من استخدام الدالة
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // هنا لا يوجد صلاحية للتطبيق ويجب علينا طلب الصلاحية منه عن طريك الكود التالي
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // بسبب عدم منح المستخدم الصلاحية للتطبيق فمن الأفضل شرح فائدتها له عن طريق عرض رسالة تشرح ذلك
                // هنا نقوم بإنشاء AlertDialog لعرض رسالة تشرح للمستخدم فائدة منح الصلاحية
                AlertDialog alertDialog = new AlertDialog.Builder(this)
                        .setTitle(R.string.permission_title)
                        .setMessage(R.string.permission_explanation)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // requestPermissions عند الضغط على زر منح نقوم بطلب الصلاحية عن طريق الدالة
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        PERMISSIONS_WRITE_EXTERNAL_STORAGE);
                            }
                        }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //  عند الضغط على زر منع نقوم بإخفاء الرسالة وكأن شيء لم يكن
                                dialogInterface.dismiss();
                            }
                        }).create();

                // نقوم بإظهار الرسالة بعد إنشاء alertDialog
                alertDialog.show();

            } else {
                // لا داعي لشرح فائدة الصلاحية للمستخدم ويمكننا طلب الصلاحية منه
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSIONS_WRITE_EXTERNAL_STORAGE);
            }

        } else {
            // الصلاحية ممنوحه مسبقا لذلك يمكننا مشاركة الصورة
            shareImage();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // يتم استدعاء هذه الدالة بعد اختيار المستخدم احد الخيارين من رسالة طلب الصلاحية
        if (requestCode == PERMISSIONS_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // تم منح الصلاحية من قبل المستخدم لذلك يمكننا مشاركة الصورة الآن
                shareImage();
            } else {
                // لم يتم منح الصلاحية من المستخدم لذلك لن نقوم بمشاركة الصورة، طبعا يمكننا تنبيه المستخدم بأنه لن يتم مشاركة الصورة لسبب عدم منح الصلاحية للتطبيق
                Toast.makeText(MainActivity.this, R.string.permission_explanation, Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(BUNDLE_CURRENT_INDEX, mcurrentindex);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            mcurrentindex = savedInstanceState.getInt(BUNDLE_CURRENT_INDEX);
            if (mcurrentindex != -1) {
                mCurrentPicture = questionPictures[mcurrentindex];
                mCurrentAnswer=Answers[mcurrentindex] + Answer_description[mcurrentindex];
                show(mcurrentindex);
            }
        }
    }

}
