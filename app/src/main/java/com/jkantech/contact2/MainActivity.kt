/*
 * Athor="Jonas Kaninda"
 * Android Studio
 * 2019
 *
 */

package com.jkantech.contact2

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.java.simpleName

    private var mDatabase: SqliteDatabase? = null
    private var allContacts =ArrayList<Contacts>()
    private var mAdapter: ContactAdapter? = null
    private  var kaninda:SqliteDatabase?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fLayout = findViewById<FrameLayout>(R.id.activity_to_do)
        val contactView = findViewById<RecyclerView>(R.id.product_list)
        val linearLayoutManager = LinearLayoutManager(this)
        contactView.layoutManager = linearLayoutManager
        contactView.setHasFixedSize(true)
        mDatabase = SqliteDatabase(this)
        allContacts = mDatabase!!.listContacts()
        if (allContacts.size > 0) {
            contactView.visibility = View.VISIBLE
            mAdapter = ContactAdapter(this, allContacts)
            contactView.adapter = mAdapter
        } else {
            contactView.visibility = View.GONE
            message("La base de données est vide, cliquez sur ajouter pour ajouter")
        }
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener { AjoutConact() }
    }

    private fun AjoutConact() {
        val inflater = LayoutInflater.from(this)
        val subView = inflater.inflate(R.layout.add_contact_layout, null)
        val nameField = subView.findViewById<EditText>(R.id.enter_name)
        val noField = subView.findViewById<EditText>(R.id.enter_phno)
        val builder = AlertDialog.Builder(this)
        builder.setTitle("AJOUTER UN NOUVEAU CONTACT")
        builder.setView(subView)
        builder.create()
        builder.setPositiveButton("AJOUTER") { dialog, which ->
            val name = nameField.text.toString()
            val ph_no = noField.text.toString()
            if (TextUtils.isEmpty(name)) {
                message("Aucune donnée enrigistrée")

            } else {
                val newContact = Contacts(name, ph_no)
                mDatabase!!.addContacts(newContact)
                finish()
                startActivity(intent)
            }
        }
        builder.setNegativeButton("ANNULER") { dialog, which ->
            message("Action annuler")}

        builder.show()


    }

    override fun onDestroy() {
        super.onDestroy()
        if (mDatabase != null) {
            mDatabase!!.close()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val search = menu.findItem(R.id.search)
        val searchView = MenuItemCompat.getActionView(search) as SearchView
        searchView.queryHint="Recherche..."

        recherche(searchView)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
       when (item.itemId) {
            R.id.action_about -> {
                val alertDialog=AlertDialog.Builder(this)
                alertDialog.setTitle("A propos")
                alertDialog.setMessage("developper par jkanTech")
                alertDialog.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun recherche(searchView: SearchView) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (mAdapter != null) mAdapter!!.filter.filter(newText)
                return true
            }
        })
    }
    private fun Context.message(message:String){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }

}


