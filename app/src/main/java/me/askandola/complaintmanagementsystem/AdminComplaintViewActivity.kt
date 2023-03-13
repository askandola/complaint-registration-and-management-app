package me.askandola.complaintmanagementsystem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.widget.SwitchCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import me.askandola.complaintmanagementsystem.daos.ComplaintDao
import me.askandola.complaintmanagementsystem.models.Complaint

class AdminComplaintViewActivity : AppCompatActivity() {

    private lateinit var titleField: TextView
    private lateinit var desc: TextView
    private lateinit var status: SwitchCompat
    private lateinit var verdict: EditText
    private lateinit var saveButton: Button
    private lateinit var date: TextView
    private lateinit var id: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_complaint_view)

        title = "Complaint"

        titleField = findViewById(R.id.adminTitleTextView)
        desc = findViewById(R.id.adminDescTextView)
        status = findViewById(R.id.statusSwitch)
        verdict = findViewById(R.id.verdictEditText)
        date = findViewById(R.id.adminDateTextView)
        saveButton = findViewById(R.id.saveButton)

        id = intent.getStringExtra("id")!!

        show()

        saveButton.setOnClickListener {
            val st = if(status.isChecked) {
                "Resolved"
            }
            else {
                "Pending"
            }
            val ver = verdict.text.toString()
            ComplaintDao().updateComplaint(id, st, ver)
            Toast.makeText(this, "Complaint updated successfully.", Toast.LENGTH_SHORT).show()
            finish()
        }

    }

    private fun show() {
        GlobalScope.launch(Dispatchers.IO) {
            val comp = ComplaintDao().getComplaintById(id).await().toObject(Complaint::class.java)!!
            withContext(Dispatchers.Main) {
                titleField.text = comp.title
                desc.text = comp.desc
                if(comp.status=="Resolved") {
                    status.isChecked = true
                }
                verdict.setText(comp.verdict)
                date.text = comp.date
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        MenuInflater(this).inflate(R.menu.menu_delete_complaint, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.deleteComplaintOption) {
            ComplaintDao().deleteComplaintById(id)
            Toast.makeText(this, "Complaint deleted successfully", Toast.LENGTH_SHORT).show()
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}