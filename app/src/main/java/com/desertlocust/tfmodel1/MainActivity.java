package com.desertlocust.tfmodel1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MainActivity extends AppCompatActivity {
    private EditText ed;
    private TextView tv;
    private Button bt;
    private Interpreter interpreter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ed = findViewById(R.id.input);
        tv = findViewById(R.id.output);
        bt = findViewById(R.id.submit);

        try {
            interpreter = new Interpreter(loadModelFile(),null);
        }catch (IOException e){
            e.printStackTrace();
        }

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               float f = doInference(ed.getText().toString());
                tv.setText(("Value of Y: "+f));

            }
        });

    }
    private MappedByteBuffer loadModelFile() throws IOException{
        AssetFileDescriptor assetFileDescriptor = this.getAssets().openFd("linear.tflite");
        FileInputStream fileInputStream = new FileInputStream(assetFileDescriptor.getFileDescriptor());
        FileChannel fileChannel = fileInputStream.getChannel();
        long startOffset = assetFileDescriptor.getStartOffset();
        long  length = assetFileDescriptor.getLength();
        return  fileChannel.map(FileChannel.MapMode.READ_ONLY,startOffset,length);
    }

    public float doInference(String val){
        float [] input = new float[1];
        input [0] = Float.parseFloat(val);

        float [][] output = new float[1][1];
        interpreter.run(input,output);
        return output[0][0];
    }
}