package com.example.play_me_mobile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_registration.*
import org.json.JSONObject

class RegistrationActivity : AppCompatActivity() {

    private val instruments = "/instrument/get/all"
    private val registration = "/registration/register"
    private val instrumentSetting = "/user/instrument/set"

    private var instrumentsMap = HashMap<String, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        sign_in_link.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        config()
    }

    private fun config(){
        val queue = Volley.newRequestQueue(this)
        val address = "${getString(R.string.server)}${instruments}"

        //create request
        val request = object: JsonArrayRequest(
            Method.GET, address, null,
            //if success
            Response.Listener { response ->
                val instruments = Array(response.length()){""}
                for(i in 0 until response.length()){
                    val instrument = response.getJSONObject(i)
                    instruments[i] = instrument.getString("name")
                    instrumentsMap[instrument.getString("name")] = instrument.getString("id")
                }
                val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, instruments)
                instruments_spinner.adapter = adapter
                setButtonListener()
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
            /*//define headers
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                return headers
            }*/
        }
        queue.add(request)
    }

    fun setButtonListener(){
        sign_up_button.setOnClickListener {
            val queue = Volley.newRequestQueue(this)
            val address = "${getString(R.string.server)}${registration}"

            //form body of request
            val params = HashMap<String,String>()
            params["login"] = Login.text.toString()
            params["password"] = Password.text.toString()
            params["email"] = email.text.toString()
            val jsonObject = JSONObject(params)

            //create request
            val request = object: JsonObjectRequest(
                Method.POST, address, jsonObject,
                //if success
                Response.Listener {
                    setInstrument()
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

    fun setInstrument(){
        val queue = Volley.newRequestQueue(this)
        val address = "${getString(R.string.server)}${instrumentSetting}"

        //form body of request
        val params = HashMap<String,String>()
        val id = instrumentsMap[instruments_spinner.selectedItem.toString()]
        if(id != null){
            params["instrumentId"] = id
        }
        val jsonObject = JSONObject(params)

        //create request
        val request = object: JsonObjectRequest(
            Method.POST, address, jsonObject,
            //if success
            Response.Listener {
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
