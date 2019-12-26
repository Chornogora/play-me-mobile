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
import com.example.play_me_mobile.util.CurrentLessonViewAdapter
import kotlinx.android.synthetic.main.activity_current_lesson.*
import org.json.JSONObject

class CurrentLessonActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private val contentAddress = "/content/get"

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_current_lesson)

        val queue = Volley.newRequestQueue(this)
        val address = "${getString(R.string.server)}${contentAddress}?lessonId=${intent.getStringExtra("id")}"

        //create request
        val request = object: JsonArrayRequest(
            Method.GET, address, null,
            //if success
            Response.Listener { response ->
                val arr = Array<JSONObject?>(response.length()) { null}
                for(i in 0 until response.length()){
                    val content = response.getJSONObject(i)
                    arr[i] = content
                }
                arr.sortWith(compareBy{it?.getInt("number")})
                showContent(arr)
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

    private fun showContent(arr: Array<JSONObject?>){
        viewManager = LinearLayoutManager(this)
        viewAdapter = CurrentLessonViewAdapter(arr, this)
        recyclerView = content_list.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
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
