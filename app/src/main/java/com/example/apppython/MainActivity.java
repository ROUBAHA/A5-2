package com.example.apppython;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    TextView edt;
    Button btn;
    ImageView iv,iv2;
    EditText edttext;
    BitmapDrawable drawble;
    Bitmap bitmap;
    String imageString = "";
    String key = "";
    private int PICK_IMAGE_REQUEST = 1000;
    Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initPython();
        edt = (TextView)findViewById(R.id.helll);
        btn = (Button)findViewById(R.id.button);
        iv = (ImageView)findViewById(R.id.imageView);
        iv2 = (ImageView)findViewById(R.id.imageView2);
        edttext = (EditText)findViewById(R.id.editText);
        final Python py = Python.getInstance();

        //edt.setText(getText());
    iv.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent i = chooseImage();
    }
    });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                key = edttext.getText().toString();
                ByteArrayOutputStream b = new ByteArrayOutputStream();
                //drawble = (BitmapDrawable)iv.getDrawable();
               // bitmap = drawble.getBitmap();
                bitmap.compress(Bitmap.CompressFormat.JPEG , 100 , b);
                byte[] imageBytes = b.toByteArray();
                imageString = Base64.encodeToString(imageBytes,Base64.DEFAULT);

                PyObject PythonFile = py.getModule("mainA52");

                PyObject obj = PythonFile.callAttr("main",key,imageString);

               String str=  obj.toString();


                byte[] data = Base64.decode(str,Base64.DEFAULT);

                Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);

                iv2.setImageBitmap(bitmap);

            }
        });
    }


    //functions
    public Intent chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

        return  intent;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            uri = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));
                iv.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void initPython()
    {
        if(!Python.isStarted())
        {
            Python.start(new AndroidPlatform(this));
        }
    }
}
