package com.claquetteuuuh.quoicoukotlin

import android.net.Uri
import java.io.Serializable


data class Contact (
    val nom: String,
    val prenom: String,
    val sexe: String,
    val dateNaissance: String,
    val telephone: String,
    val important: Boolean,
    val image: String?,
    val email: String,
): Serializable{

}