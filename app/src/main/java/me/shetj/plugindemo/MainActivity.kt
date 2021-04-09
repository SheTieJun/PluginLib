package me.shetj.plugindemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import me.shetj.plugindemo.click.SingleClick

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.btn).setOnClickListener {
            onClick(it)
        }
    }

    @SingleClick(value = 5000)
    fun onClick(v: View) {
        Toast.makeText(this, "这是：${System.currentTimeMillis()}ssssss", Toast.LENGTH_SHORT).show()
    }

}