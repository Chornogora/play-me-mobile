package com.example.play_me_mobile.util

import android.content.Context
import android.graphics.Color
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import org.json.JSONObject

class CurrentLessonViewAdapter(private val myDataset: Array<JSONObject?>, private val Context: Context) :
    RecyclerView.Adapter<CurrentLessonViewAdapter.MyViewHolder>() {

    var counter = 0

    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MyViewHolder {

        val data = myDataset[counter++]
        val view = when{
            data?.getString("type") == "text" -> {
                val result = TextView(Context)
                result.text = data.getString("data")
                result.setTextColor(Color.WHITE)
                result.setBackgroundColor(Color.GREEN)
                result.textSize = 24f

                val params = LinearLayout.LayoutParams(100, 100)
                params.topMargin = 90
                params.leftMargin = 200
                params.width = 800
                result.layoutParams = params
                result
            }
            data?.getString("type") == "image" -> {
                val imView = ImageView(Context)
                val url = data.getString("data")
                imView.scaleType = ImageView.ScaleType.MATRIX
                Picasso.get().load(url).into(imView)

                val params = LinearLayout.LayoutParams(100, 100)
                params.topMargin = 90
                params.leftMargin = 200
                params.width = 800
                params.height = 600
                imView.layoutParams = params
                imView
            }
            data?.getString("type") == "video" -> {
                val url = data.getString("data")
                val videoView = object: WebView(Context){
                    override fun onTouchEvent(event: MotionEvent?): Boolean {
                        loadUrl(url)
                        return super.onTouchEvent(event)
                    }
                }
                videoView.loadUrl("https://sourceout.s3-us-west-1.amazonaws.com/uploads/service_image/image/48386/small_YouTube-logo-play-icon.png")
                videoView.settings.javaScriptEnabled = true


                val params = LinearLayout.LayoutParams(100, 100)
                params.topMargin = 90
                params.leftMargin = 200
                params.width = 800
                params.height = 600
                videoView.layoutParams = params

                videoView
            }
            else -> {
                val result = TextView(Context)
                result.text = data?.getString("content")
                result
            }
        }

        return MyViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size
}