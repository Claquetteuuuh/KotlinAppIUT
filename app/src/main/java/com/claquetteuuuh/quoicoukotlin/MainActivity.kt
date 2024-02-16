package com.claquetteuuuh.quoicoukotlin

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import java.util.regex.Matcher
import java.util.regex.Pattern
import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var picture: ImageView;
    private lateinit var button: Button; // définir le button avant de le trouver
    private lateinit var prenom: EditText;
    private lateinit var nom: EditText;
    private lateinit var dateNaissance: EditText;
    private lateinit var numeroTel: EditText;
    private lateinit var email: EditText;

    private var imageUri: Uri? = null;

    private lateinit var photoFile: File
    private val REQUEST_IMAGE_CAPTURE = 1003

    private val cameraResultLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ){ isSuccess ->
        if(isSuccess){
            imageUri?.let { Uri ->

                picture.setImageURI(Uri)
            }
        }
    }


    private fun createImageUri(): Uri? {

        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val file: File = File.createTempFile("JPEG_${timeStamp}_",".jpg",storageDir).apply {
            photoFile = this
        }
        val photoURI: Uri = FileProvider.getUriForFile(this,
            "com.claquetteuuuh.quoicoukotlin.fileprovider", file)
        return photoURI

    }

    private fun askPerm(){
        when{
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                dispatchTakePictureIntent()
            }
            else -> {
                requestPermissionLauncher.launch(
                    Manifest.permission.CAMERA
                )
            }
        }
    }

    val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()){ isGranted: Boolean ->
            if(isGranted){
                dispatchTakePictureIntent();
            }else{
                Log.d("MainActivity", "L'utilisateur a refusé la perm")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        picture = findViewById(R.id.peppapig)
        picture.setOnClickListener({
            askPerm()
        })
        prenom = findViewById(R.id.prenom)
        nom = findViewById(R.id.nom)
        email = findViewById(R.id.email)
        dateNaissance = findViewById(R.id.date)
        numeroTel = findViewById(R.id.phone)

        button = findViewById(R.id.button) // récuperer le bouton par id
        button.setOnClickListener({
            if(prenom.text.toString().equals("") || nom.text.toString().equals("") || email.text.toString().equals("") || numeroTel.text.toString().equals("")){
                val snack: Snackbar = Snackbar.make(it, "Gros golmon tous les champs sont pas valid", Snackbar.LENGTH_LONG)
                snack.show()
            }else{
                val regex = "^(.+)@(.+)$"

                val pattern: Pattern = Pattern.compile(regex)
                val matcher: Matcher = pattern.matcher(email.text.toString())
                if(!matcher.matches()){
                    val snack: Snackbar = Snackbar.make(it, "Gros golmon ton email est invalid", Snackbar.LENGTH_LONG)
                    snack.show()
                }

                val intent = Intent().apply {
                    val info: String = "Le frro s'appelle " + prenom.text +" " + nom.text+
                            ", son email de golmon c'est "+ email.text
                    putExtra("test", info)
                }
                setResult(Activity.RESULT_OK, intent)
                finish()
            }

            //    Log.d("MainActivity", "T'as cliqué le button")
            //Log.d("MainActivity", prenom.text.toString())
        })

        val firstName: String? = intent.getStringExtra("com.claquetteuuuh.quoicoukotlin.firstname")
        prenom.apply {
            setText(firstName)
        }

    }

    val REQUEST_CAMERA_PERMISSION = 1001
    private val REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION = 1002
    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_CAMERA_PERMISSION
            )
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION
            )
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION || requestCode == REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                Log.d("MainActivity", "Permission accordé ")
            } else {
                // Permission denied
                // Handle the denial case
            }
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                imageUri = createImageUri();
                if (imageUri == null){
                    return;
                }
                cameraResultLauncher.launch(imageUri)
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Photo captured successfully, you can now update the gallery
            updateGallery(photoFile)
        }
    }
    private fun updateGallery(photoFile: File) {
        MediaScannerConnection.scanFile(this, arrayOf(photoFile.absolutePath), null) { _, uri ->
            // Gallery is updated, you can use the uri if needed
        }
    }

    override fun onStart() {
        super.onStart()
    }
}