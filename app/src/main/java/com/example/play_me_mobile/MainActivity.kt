package com.example.play_me_mobile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.net.CookieHandler
import java.net.CookieManager


class MainActivity : AppCompatActivity() {

    private val authorization = "/authorization/authorize"

    override fun onCreate(savedInstanceState: Bundle?) {
        val cookieManager = CookieManager()
        CookieHandler.setDefault(cookieManager)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sign_up_link.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }

        sign_in_button.setOnClickListener{
            val queue = Volley.newRequestQueue(this)
            val address = "${getString(R.string.server)}${authorization}"

            //form body of request
            val params = HashMap<String,String>()
            params["login"] = login.text.toString()
            params["password"] = password.text.toString()
            val jsonObject = JSONObject(params)

            //create request
            val request = object:JsonObjectRequest(Method.POST, address, jsonObject,
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

                /*override fun parseNetworkResponse(response: NetworkResponse?): Response<JSONObject> {
                    val objectResponse = super.parseNetworkResponse(response)
                    if (response != null && response.headers.containsKey("Set-Cookie")){
                        objectResponse.result.put("SESSIONID", response.headers["Set-Cookie"])
                    }
                    return objectResponse
                }*/
            }
            queue.add(request)
        }
    }
}
