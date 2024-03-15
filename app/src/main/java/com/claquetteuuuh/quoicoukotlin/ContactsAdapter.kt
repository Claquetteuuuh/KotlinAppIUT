package com.claquetteuuuh.quoicoukotlin

import android.annotation.SuppressLint
import android.app.Activity
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class ContactsAdapter (private val context: Activity, private val arrayList: ArrayList<Contact>): ArrayAdapter<Contact>(
    context, R.layout.contact_item, arrayList){

    @SuppressLint("MissingInflatedId")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater: LayoutInflater = LayoutInflater.from(context);
        val view: View = inflater.inflate(R.layout.contact_item, null);

        val imageView: ImageView = view.findViewById(R.id.profile_picture);
        val name: TextView = view.findViewById(R.id.name);
        val phone: TextView = view.findViewById(R.id.phone);

        if(arrayList[position].image == null){
            imageView.setImageResource(R.drawable.default_user);
        }else{
            imageView.setImageURI(Uri.parse(arrayList[position].image));
        }
        name.text = arrayList[position].nom + " " + arrayList[position].prenom;
        phone.text = arrayList[position].telephone;

        return view;
    }

}

