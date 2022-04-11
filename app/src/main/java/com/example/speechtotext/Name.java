package com.example.speechtotext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class Name extends AppCompatActivity {

    EditText editText;
    ImageView imageView;
    public static final Integer RecordAudioRequestCode=1;
    private SpeechRecognizer speechRecognizer;
    AlertDialog.Builder alertSpeechDialog;
    AlertDialog alertDialog;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);

        editText = findViewById(R.id.editText1);
        imageView = findViewById(R.id.speech_image);

        if(ContextCompat.checkSelfPermission(Name.this, Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED){
            checkPermission();

        }

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        final Intent speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {
                ViewGroup viewGroup=findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(Name.this).inflate(R.layout.alertcustom,viewGroup,false);

                alertSpeechDialog = new AlertDialog.Builder(Name.this);

                String s = "Listening ....";
                alertSpeechDialog.setMessage(s);
                alertSpeechDialog.setView(dialogView);
                alertDialog = alertSpeechDialog.create();
                alertDialog.show();

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results) {
                imageView.setImageResource(R.drawable.ic_baseline_mic_24);
                ArrayList<String> arrayList = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                editText.setText(arrayList.get(0));

                if(arrayList.get(0).equals("back")){
                    Intent i = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(i);
                }

                if(arrayList.get(0).contains("Narendra Modi")){

                    alertDialog.dismiss();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(Name.this,"Correct Answer !!!", Toast.LENGTH_LONG).show();

                    Intent i = new Intent(getApplicationContext(),Name.class);
                    startActivity(i);
                    //setContentView(R.layout.avtivity_next);

                }
                alertDialog.dismiss();
            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    speechRecognizer.stopListening();
                }
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    imageView.setImageResource(R.drawable.ic_baseline_mic_24);
                    speechRecognizer.startListening(speechIntent);
                }

                return false;
            }
        });
    }

    private void checkPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},RecordAudioRequestCode);
        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        speechRecognizer.destroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == RecordAudioRequestCode && grantResults.length>0){
            if(grantResults[0]  == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Granted",Toast.LENGTH_LONG).show();
            }
        }
    }
}