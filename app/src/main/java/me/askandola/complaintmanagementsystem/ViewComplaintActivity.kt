package me.askandola.complaintmanagementsystem

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import me.askandola.complaintmanagementsystem.daos.ComplaintDao
import me.askandola.complaintmanagementsystem.models.Complaint

class ViewComplaintActivity : AppCompatActivity() {

    private lateinit var titleField: TextView
    private lateinit var desc: TextView
    private lateinit var status: TextView
    private lateinit var verdict: TextView
    private lateinit var date: TextView
    private lateinit var id: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_complaint)

        title = "Complaint"

        titleField = findViewById(R.id.titleTextView)
        desc = findViewById(R.id.descTextView)
        status = findViewById(R.id.statusTextView)
        verdict = findViewById(R.id.verdictTextView)
        date = findViewById(R.id.dateTextView)

        id = intent.getStringExtra("id")!!

        show()
    }

    private fun show() {
        GlobalScope.launch(Dispatchers.IO) {
            val comp = ComplaintDao().getComplaintById(id).await().toObject(Complaint::class.java)!!
            withContext(Dispatchers.Main) {
                titleField.text = comp.title
                desc.text = comp.desc
                status.text = comp.status
                verdict.text = comp.verdict
                date.text = comp.date
            }
        }
    }
}