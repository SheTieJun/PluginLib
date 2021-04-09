package me.shetj.plugindemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import me.shetj.plugindemo.click.SingleClick;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn).setOnClickListener(v -> onclick(v));
    }

    @SingleClick(value = 5000L)
    void onclick(View view){
        Toast.makeText(this, "这是： xxx", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this,MainActivity.class));
    }

    @SingleClick(value = 5000L)
    void test(){

    }

    @SingleClick(value = 5000L)
    void test2(){

    }
}