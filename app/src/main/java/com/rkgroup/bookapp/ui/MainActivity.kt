package com.rkgroup.bookapp.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.rkgroup.bookapp.R
import com.rkgroup.bookapp.adapter.DatabaseFilesViewAdapter
import com.rkgroup.bookapp.databinding.ActivityMainBinding
import com.rkgroup.bookapp.model.DatabasePdfFile
import com.rkgroup.bookapp.utils.Constants

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private lateinit var databasePdfFile: DatabasePdfFile
    private val adapter by lazy {
        DatabaseFilesViewAdapter {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val intent = Intent(this, PdfViewActivity::class.java)
                intent.putExtra(Constants.KEY_SOURCE_FILE, it)
                startActivity(intent)
            } else {
                databasePdfFile = it
                storagePermissions.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()

    }

    private fun initUI() {
        binding.rvInternalPDF.adapter = adapter
        //  adapter.updateAdapter(Constants.databasePdfFiles)
        Firebase.database.reference.child("books").addValueEventListener(valueEventListener)
        binding.btnViewStorage.setOnClickListener {
            startActivity(Intent(this, LocalFilesActivity::class.java))
        }
    }


    private val valueEventListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.exists() && snapshot.childrenCount != 0L) {
                val books = ArrayList<DatabasePdfFile>()
                snapshot.children.forEach {
                    val databasePdfFile: DatabasePdfFile =
                        it.getValue(DatabasePdfFile::class.java)!!
                    books.add(databasePdfFile)
                }
                adapter.updateAdapter(books)
                binding.progress.visibility = View.GONE
            }
        }

        override fun onCancelled(error: DatabaseError) {

        }
    }

    override fun onDestroy() {
        Firebase.database.reference.child("books").removeEventListener(valueEventListener)
        super.onDestroy()
    }

    private val storagePermissions =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                val intent = Intent(this, PdfViewActivity::class.java)
                intent.putExtra(Constants.KEY_SOURCE_FILE, databasePdfFile)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Permission not Granted!", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_sign_out) {
            Firebase.auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}