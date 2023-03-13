package me.askandola.complaintmanagementsystem

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import me.askandola.complaintmanagementsystem.daos.ComplaintDao

class AddComplaintActivity : AppCompatActivity() {

    private val complaintDao: ComplaintDao = ComplaintDao()
    private lateinit var addCompButton: Button
    private lateinit var title: String
    private lateinit var desc: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_complaint)

        title = "File a complaint"

        addCompButton = findViewById(R.id.addCompButton)

        addCompButton.setOnClickListener {
            title = findViewById<EditText>(R.id.titleField).text.toString()
            desc = findViewById<EditText>(R.id.descField).text.toString()
            complaintDao.addComplaint(this, title, desc)
            finish()
        }

    }
}