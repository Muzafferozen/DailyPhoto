package com.muzafferozen.dailyphoto;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.muzafferozen.dailyphoto.databinding.ActivityDailyactivityBinding;
import com.muzafferozen.dailyphoto.databinding.ActivityMainBinding;

import java.io.ByteArrayOutputStream;

public class Dailyactivity extends AppCompatActivity {

    private ActivityDailyactivityBinding binding;
    ActivityResultLauncher<Intent> activityResultLauncher; //to gallery
    ActivityResultLauncher<String> permissionLauncher;  // to permission
    Bitmap selectedImage;
    SQLiteDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDailyactivityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        registerLauncher();


    }

    public void save(View view) {

        String name = binding.nameText.getText().toString();
        String artistName = binding.artistText.getText().toString();
        String year = binding.yearText.getText().toString();


        Bitmap smallImage = makeSmallerImage(selectedImage,300);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        smallImage.compress(Bitmap.CompressFormat.PNG,50,outputStream);
        byte[]  byteArray = outputStream.toByteArray();

        try{

            database = this.openOrCreateDatabase("Daily",MODE_PRIVATE,null);
            database.execSQL("CREATE TABLE IF NOT EXISTS daily(id INTEGER PRIMARY KEY, dailyname VARCHAR,paintername VARCHAR,year VARCHAR,image BLOB)");

            String sqlString ="INSERT INTO daily(dailyname,paintername,year,image)VALUES(?,?,?,?)";
            SQLiteStatement  sqLiteStatement = database.compileStatement(sqlString);
            sqLiteStatement.bindString(1,name);
            sqLiteStatement.bindString(2,artistName);
            sqLiteStatement.bindString(3,year);
            sqLiteStatement.bindBlob(4,byteArray);
            sqLiteStatement.execute();
        }catch (Exception e) {

            e.printStackTrace();
        }



        Intent intent = new Intent(Dailyactivity.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);


    }

    public  Bitmap makeSmallerImage(Bitmap image,int maximumSize){

        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;

        if (bitmapRatio>1){

            width = maximumSize;
            height =(int) ( width / bitmapRatio);



        }else{

            height = maximumSize;
            width= (int) (height*bitmapRatio);


        }

       return image.createScaledBitmap(image,100,100,true);


    }




    public void selectImage(View view) {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Snackbar.make(view, "Permisson needed for gallery", Snackbar.LENGTH_INDEFINITE).setAction("Give permission", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);

                    }
                }).show();
            } else {

                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                //request permission
            }

        }else {

            //gallery
            Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            activityResultLauncher.launch(intentToGallery);
        }
    }




    private  void registerLauncher(){

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {

            @Override
            public void onActivityResult(ActivityResult result) {

                if (result.getResultCode() ==RESULT_OK){

                    Intent intentFromResult = result.getData();
                    if (intentFromResult != null ) {

                       Uri imageData = intentFromResult.getData();
                      // binding.imageView.setImageURI(imageData);

                        try {
                            if (Build.VERSION.SDK_INT >= 28) {

                                ImageDecoder.Source source = ImageDecoder.createSource(Dailyactivity.this.getContentResolver(),imageData);
                                selectedImage  = ImageDecoder.decodeBitmap(source);
                                binding.imageView.setImageBitmap(selectedImage);

                            }else{

                                selectedImage =MediaStore.Images.Media.getBitmap(Dailyactivity.this.getContentResolver(),imageData);
                                binding.imageView.setImageBitmap(selectedImage);
                            }


                        }catch (Exception e){
                            e.printStackTrace();

                        }


                    }

                }

            }
        });


        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if(result){

                    //permission granted


                    Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    activityResultLauncher.launch(intentToGallery);
                }else{
                    //permission denied
                    Toast.makeText(Dailyactivity.this,"Permission needed",Toast.LENGTH_LONG).show();

                }

            }
        });


    }
}