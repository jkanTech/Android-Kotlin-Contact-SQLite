/*
 * Athor="Jonas Kaninda"
 * Android Studio
 * 2019
 *
 */

package com.jkantech.contact2

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.jkantech.contact2.ContactAdapter.ContactViewHolder
import java.util.*
import java.util.ArrayList as ArrayList1

class ContactAdapter(
    private val context: Context,
    private var listContacts: ArrayList1<Contacts>
) : RecyclerView.Adapter<ContactViewHolder>(), Filterable {
    private val mArrayList: ArrayList1<Contacts> = listContacts
    private val mDatabase: SqliteDatabase = SqliteDatabase(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.contact_list_layout, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contacts = listContacts[position]
        holder.name.text = contacts.name
        holder.ph_no.text = contacts.phno
        holder.editContact.setOnClickListener { editTaskDialog(contacts) }
        holder.deleteContact.setOnClickListener { //delete row from database
            mDatabase.deleteContact(contacts.id)

            //redemarrer l'activite
            (context as Activity).finish()
            context.startActivity(context.intent)
        }
    }

    //.....
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                listContacts = if (charString.isEmpty()) {
                    mArrayList
                } else {
                    val filteredList =
                        ArrayList1<Contacts>()
                    for (contacts in mArrayList) {
                        if (contacts.name.toLowerCase(Locale.ROOT).contains(charString)) {
                            filteredList.add(contacts)
                        }
                    }
                    filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = listContacts
                return filterResults
            }

            override fun publishResults(
                charSequence: CharSequence,
                filterResults: FilterResults
            ) {
                listContacts =
                    filterResults.values as ArrayList<Contacts>
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int {
        return listContacts.size
    }

    private fun editTaskDialog(contacts: Contacts?) {
        val inflater = LayoutInflater.from(context)
        val subView = inflater.inflate(R.layout.add_contact_layout, null)
        val nameField = subView.findViewById<EditText>(R.id.enter_name)
        val contactField = subView.findViewById<EditText>(R.id.enter_phno)
        if (contacts != null) {
            nameField.setText(contacts.name)
            contactField.setText(contacts.phno)
        }
        val builder =
            AlertDialog.Builder(context)
        builder.setTitle("MODIFIER contact")
        builder.setView(subView)
        builder.create()
        builder.setPositiveButton("MODIFIER ") { dialog, which ->
            val name = nameField.text.toString()
            val ph_no = contactField.text.toString()
            if (TextUtils.isEmpty(name)) {
                Toast.makeText(context, "Erreur , non modifier", Toast.LENGTH_SHORT).show()


            } else {
                mDatabase.updateContacts(
                    Contacts(
                        contacts!!.id,
                        name,
                        ph_no
                    )
                )
                //refresh the activity
                (context as Activity).finish()
                context.startActivity(context.intent)
            }
        }
        builder.setNegativeButton("Annuler") { dialog, which ->
            Toast.makeText(context, "Action annuler", Toast.LENGTH_SHORT).show()
        }
        builder.show()
    }

    //....
    inner class ContactViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var name: TextView
        var ph_no: TextView
        var deleteContact: ImageView
        var editContact: ImageView

        init {
            name = itemView.findViewById(R.id.contact_name)
            ph_no = itemView.findViewById(R.id.ph_no)
            deleteContact = itemView.findViewById(R.id.delete_contact)
            editContact = itemView.findViewById(R.id.edit_contact)
        }
    }
    private fun Context.message(message:String){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }

}