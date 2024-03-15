package com.claquetteuuuh.quoicoukotlin

import android.annotation.SuppressLint
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

class ContactActivity : AppCompatActivity() {
    private lateinit var name: TextView;
    private lateinit var image: ImageView;
    private lateinit var sexe: TextView;
    private lateinit var phone: TextView;
    private lateinit var email: TextView;
    private lateinit var fav: ImageView;

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)

        name = findViewById(R.id.profile_name)
        image = findViewById(R.id.profile_contact)
        sexe = findViewById(R.id.profile_sexe)
        phone = findViewById(R.id.profile_phone)
        email = findViewById(R.id.profile_email)
        fav = findViewById(R.id.fav_image)

        val firstName: String? = intent.getStringExtra("name")
        val lastName: String? = intent.getStringExtra("prenom")
        name.apply {
            setText(firstName + " " + lastName)
        }
        val emailString: String? = intent.getStringExtra("email")
        email.apply {
            setText(emailString)
        }
        val sexeString: String? = intent.getStringExtra("name")
        sexe.apply {
            setText(sexeString)
        }
        val phoneString: String? = intent.getStringExtra("name")
        phone.apply {
            setText(phoneString)
        }
        val imageUri: String? = intent.getStringExtra("image");
        val uri: Uri? = Uri.parse(imageUri);
        image.setImageURI(uri);
        val important: Boolean? = intent.getBooleanExtra("important", false);
        if(important == true){
            fav.setImageResource(R.drawable.fav_star)
        }
    }
}