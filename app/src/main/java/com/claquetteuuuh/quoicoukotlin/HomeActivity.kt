package com.claquetteuuuh.quoicoukotlin

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.claquetteuuuh.quoicoukotlin.databinding.ActivityHomeBinding
import com.claquetteuuuh.quoicoukotlin.databinding.ActivityMainBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var addButton: Button
    private lateinit var textCool: TextView;
    private lateinit var binding: ActivityHomeBinding;
    private lateinit var adapter: ArrayAdapter<Contact>;

    private lateinit var contacts: ArrayList<Contact>;

    private val homeActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == Activity.RESULT_OK){
                result.data?.also {data ->
                    val contact: Contact = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        data.getSerializableExtra("userinfo", Contact::class.java) as Contact
                    } else {
                        data.getSerializableExtra("userinfo") as Contact
                    };
                    contacts.add(contact);
                    val adapter = object : ArrayAdapter<Contact>(this, android.R.layout.simple_list_item_1, contacts) {
                        @SuppressLint("MissingInflatedId")
                        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                            val inflater: LayoutInflater = LayoutInflater.from(context)
                            val view: View = inflater.inflate(R.layout.contact_item, null)

                            val imageView: ImageView = view.findViewById(R.id.profile_picture)
                            val name: TextView = view.findViewById(R.id.name)
                            val phone: TextView = view.findViewById(R.id.phone)

                            val currentContact = getItem(position)

                            if (currentContact?.image == null) {
                                imageView.setImageResource(R.drawable.default_user)
                            } else {
                                imageView.setImageURI(Uri.parse(currentContact.image))
                            }
                            name.text = "${currentContact?.nom} ${currentContact?.prenom}"
                            phone.text = currentContact?.telephone

                            return view
                        }
                    }
                    binding.listView.adapter = adapter
                    adapter.notifyDataSetChanged();

                }
            }
        }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater);
        setContentView(R.layout.activity_home)
//        textCool = findViewById(R.id.textInfoUser)
        contacts = ArrayList();
        contacts.add(Contact("QUoicoubeh", "apagnan", "F", "10011010", "0202002", false, null, email = "apagnanquoicoubeh"))

        val adapter = object : ArrayAdapter<Contact>(this, android.R.layout.simple_list_item_1, contacts) {
            @SuppressLint("MissingInflatedId")
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val inflater: LayoutInflater = LayoutInflater.from(context)
                val view: View = inflater.inflate(R.layout.contact_item, null)

                val imageView: ImageView = view.findViewById(R.id.profile_picture)
                val name: TextView = view.findViewById(R.id.name)
                val phone: TextView = view.findViewById(R.id.phone)

                val currentContact = getItem(position)

                if (currentContact?.image == null) {
                    imageView.setImageResource(R.drawable.default_user)
                } else {
                    imageView.setImageURI(Uri.parse(currentContact.image))
                }
                name.text = "${currentContact?.nom} ${currentContact?.prenom}"
                phone.text = currentContact?.telephone

                return view
            }
        }
        binding.listView.adapter = adapter
        var listView: ListView = findViewById(R.id.listView);
        listView.adapter = adapter;

        listView.setOnItemClickListener { _, view, position, _ ->
            val selectedContact = contacts[position]
            Toast.makeText(
                applicationContext, (view as TextView).text,
                Toast.LENGTH_SHORT
            ).show()
        }
        listView.setOnItemClickListener{parent, view, position, id ->
            val name = contacts[position].nom;
            val prenom = contacts[position].prenom;
            val phone = contacts[position].telephone;
            val dateNaissance = contacts[position].dateNaissance;
            val importante = contacts[position].important;
            val image = contacts[position].image;
            val sexe = contacts[position].sexe;
            val email = contacts[position].email;
            val important = contacts[position].important;

            val i = Intent(this, ContactActivity::class.java);
            i.putExtra("name", name);
            i.putExtra("prenom", prenom);
            i.putExtra("dateNaissance", dateNaissance);
            i.putExtra("importante", importante);
            i.putExtra("image", image);
            i.putExtra("sexe", sexe);
            i.putExtra("phone", phone);
            i.putExtra("email", email);
            i.putExtra("important", important)
            startActivity(i)

        }
        binding.listView.setOnItemLongClickListener{parent, view, position, id ->
            contacts.remove(contacts[position]);

            adapter.notifyDataSetChanged();
            true;
        }


        addButton = findViewById(R.id.add_button)
        addButton.setOnClickListener({
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