package com.example.play_me_mobile.util

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.play_me_mobile.CurrentLessonActivity

class MyAdapter(private val myDataset: Map<String, String>, private val Context: Context) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class MyViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MyViewHolder {
        // create a new view
        val textView = TextView(Context)
        // set the view's size, margins, paddings and layout parameters
        textView.setTextColor(Color.WHITE)
        textView.setBackgroundColor(Color.GREEN)
        textView.textSize = 24f
        val params = LinearLayout.LayoutParams(100, 100)
        params.topMargin = 90
        params.leftMargin = 200
        params.width = 800
        textView.layoutParams = params

            /*        android:layout_alignParentStart="true"
        android:layout_alignParentEnd="true"
        android:layout_marginStart="91dp"*/
        return MyViewHolder(textView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val iterator = myDataset.asIterable().iterator()
        var value: Map.Entry<String, String>? = null
        var counter = 0
        while(iterator.hasNext()){
            if(counter == position){
                value = iterator.next()
                break
            }
            iterator.next()
            ++counter
        }
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textView.text = value?.value
        holder.textView.tag = value?.key

        holder.textView.setOnClickListener{
            val intent = Intent(Context, CurrentLessonActivity::class.java)
            intent.putExtra("id", holder.textView.tag as String)
            Context.startActivity(intent)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size
}