package com.example.play_me_mobile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.play_me_mobile.util.MyAdapter
import kotlinx.android.synthetic.main.activity_current_lesson.*
import kotlinx.android.synthetic.main.activity_lesson.*
import kotlinx.android.synthetic.main.activity_lesson.lessonButton
import kotlinx.android.synthetic.main.activity_lesson.logoutButton
import kotlinx.android.synthetic.main.activity_lesson.profileButton

class LessonActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private val lessonsAddress = "/lesson/get"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lesson)

        val queue = Volley.newRequestQueue(this)
        val address = "${getString(R.string.server)}${lessonsAddress}"

        //create request
        val request = object: JsonArrayRequest(
            Method.GET, address, null,
            //if success
            Response.Listener { response ->
                val map = HashMap<String, String>()
                for(i in 0 until response.length()){
                    val lesson = response.getJSONObject(i)
                    map[lesson.getString("id")] = lesson.getString("name")
                }
                configRecycler(map)
            },
            //if not success
            Response.ErrorListener { error ->
                val builder = AlertDialog.Builder(this)
                builder.setMessage(error.toString())
                    .setPositiveButton("Ok", null)
                // Create the AlertDialog object and return it
                builder.create().show()
            }
        ){
            //define headers
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                return headers
            }
        }
        queue.add(request)
        setFooterButtons()
    }

    private fun configRecycler(map: Map<String, String>){
        viewManager = LinearLayoutManager(this)
        viewAdapter = MyAdapter(map, this)
        recyclerView = list.apply {
            setHasFixedSize(true)
            // use a linear layout manager
            layoutManager = viewManager
            // specify an viewAdapter (see also next example)
            adapter = viewAdapter
        }
    }

    private fun setFooterButtons(){
        logoutButton.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        profileButton.setOnClickListener{
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
        lessonButton.setOnClickListener{
            val intent = Intent(this, LessonActivity::class.java)
            startActivity(intent)
        }
    }
}
