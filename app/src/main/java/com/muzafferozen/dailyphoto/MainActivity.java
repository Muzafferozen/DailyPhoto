package com.muzafferozen.dailyphoto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.muzafferozen.dailyphoto.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Daily> dailyArrayList;

    private ActivityMainBinding binding;

    DailyAdapter dailyAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view =binding.getRoot();
        setContentView(view);
        getData();
        dailyArrayList = new ArrayList<>();

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dailyAdapter = new DailyAdapter(dailyArrayList);
        binding.recyclerView.setAdapter(dailyAdapter);



    }

   private void getData(){

        try {

            SQLiteDatabase sqLiteDatabase = this.openOrCreateDatabase("Daily",MODE_PRIVATE,null);
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM daily ",null);
            int nameIx = cursor.getColumnIndex("dailyname");
            int idIx = cursor.getColumnIndex("id");

            while (cursor.moveToNext()) {

                String name = cursor.getString(nameIx);
                int id = cursor.getInt(idIx);

                Daily daily = new Daily(name,id);

                dailyArrayList.add(daily);

            }
            dailyAdapter.notifyDataSetChanged();
            cursor.close();



        }catch (Exception e){
            e.printStackTrace();


        }


   }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        MenuInflater menuInflater =  getMenuInflater();
        menuInflater.inflate(R.menu.daily_menu,menu);




        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId()==R.id.add_daily){

            Intent intent = new Intent(this,Dailyactivity.class);
            startActivity(intent);


        }

        return super.onOptionsItemSelected(item);
    }



}