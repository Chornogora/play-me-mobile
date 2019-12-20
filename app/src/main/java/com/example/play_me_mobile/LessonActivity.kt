package com.example.play_me_mobile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley

class LessonActivity : AppCompatActivity() {

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
                /*TODO save myself*/
                val intent = Intent(this, LessonActivity::class.java)
                startActivity(intent)
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
    }
}
