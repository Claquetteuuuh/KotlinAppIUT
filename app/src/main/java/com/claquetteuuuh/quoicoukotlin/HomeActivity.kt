package com.claquetteuuuh.quoicoukotlin

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts

class HomeActivity : AppCompatActivity() {
    private lateinit var buttonCool: Button
    private lateinit var textCool: TextView
    private val homeActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == Activity.RESULT_OK){
                result.data?.also {data ->
                    val testData = data.getStringExtra("test")
                    textCool.apply {
                        setText(testData)
                    }
                }
            }
        }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        textCool = findViewById(R.id.textInfoUser)
        buttonCool = findViewById(R.id.buttoncool)
        buttonCool.setOnClickListener({
            Log.d("HomeActivity", "Button clicked")
            // sendToForm("QuoicouPrenom")
            sendToFormWaitResponse("QuoicouPrenom")

        })
    }

    fun sendToForm(name: String){
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("com.claquetteuuuh.quoicoukotlin.firstname", name);
        }
        startActivity(intent)
    }

    fun sendToFormWaitResponse(name: String){

        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("com.claquetteuuuh.quoicoukotlin.firstname", name);
        }
        homeActivityResultLauncher.launch(intent)
    }
}