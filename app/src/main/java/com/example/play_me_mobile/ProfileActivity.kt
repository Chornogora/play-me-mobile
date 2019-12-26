package com.example.play_me_mobile

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_lesson.*
import kotlinx.android.synthetic.main.activity_lesson.lessonButton
import kotlinx.android.synthetic.main.activity_lesson.logoutButton
import kotlinx.android.synthetic.main.activity_lesson.profileButton
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_profile.*
import org.json.JSONObject

class ProfileActivity : AppCompatActivity() {

    private val userAddress = "/user/get"
    private val updateUserAddress = "/user/update"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val queue = Volley.newRequestQueue(this)
        val address = "${getString(R.string.server)}${userAddress}"

        //create request
        val request = JsonObjectRequest(
            Request.Method.GET, address, null,
            //if success
            Response.Listener { response ->
                login_edit.setText(response.getString("login"))
                email_edit.setText(response.getString("email"))
                full_name_edit.setText(response.getString("fullName"))
                instrument_edit.setText(response.getJSONObject("instrument").getString("name"))
                if(response.getString("avatarUrl") != "null") {
                    Picasso.get().load(response.getString("avatarUrl")).into(avatar)
                }else{
                    Picasso.get().load("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcTTiVeeYX6baDFn58-dgCJjWGogc54chap9bcCkdTTReqH2RvmX").into(avatar)
                }
            },
            //if not success
            Response.ErrorListener { error ->
                val builder = AlertDialog.Builder(this)
                builder.setMessage(error.toString())
                    .setPositiveButton("Ok", null)
                // Create the AlertDialog object and return it
                builder.create().show()
            }
        )
        queue.add(request)
        setSaveButton()
        setFooterButtons()
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

    private fun setSaveButton(){
        save_button.setOnClickListener {
            val queue = Volley.newRequestQueue(this)
            val address = "${getString(R.string.server)}${updateUserAddress}"

            //form body of request
            val params = HashMap<String,String>()
            params["login"] = login_edit.text.toString()
            params["email"] = email_edit.text.toString()
            params["avatarUrl"] = avatar_edit.text.toString()
            params["fullName"] = full_name_edit.text.toString()
            val jsonObject = JSONObject(params)

            //create request
            val request = object:JsonObjectRequest(Method.POST, address, jsonObject,
                //if success
                Response.Listener {
                    finish()
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
}
