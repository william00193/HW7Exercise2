package com.example.hw7exercise2

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.hw7exercise2.databinding.ListItemCrimeBinding
import java.text.SimpleDateFormat
import java.util.*


class CrimeHolder(
    private val binding: ListItemCrimeBinding
) : RecyclerView.ViewHolder(binding.root) {



    fun bind(crime: Crime) {
        binding.crimeTitle.text = crime.title


//Date formatting section

//Old date format
//binding.crimeDate.text = crime.date.toString()

//New formatted date for each entry
//Documentation: StackOverFlow -- How to use "android.text.format.DateFormat" with Kotlin?
        val crimeDate = SimpleDateFormat("EE. MMM. dd, yyyy", Locale.US)
        val NewDate : String = crimeDate.format(crime.date).toString()
        binding.crimeDate.text = NewDate



        binding.root.setOnClickListener {

            Toast.makeText(
                binding.root.context,
                "${crime.title} clicked!",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.crimeSolved.visibility = if (crime.isSolved){
            View.VISIBLE
        } else {
                    View.GONE
            }
            }
}

class CrimeListAdapter(
    private val crimes: List<Crime>
) : RecyclerView.Adapter<CrimeHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CrimeHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemCrimeBinding.inflate(inflater, parent, false)
        return CrimeHolder(binding)
    }

    override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
        val crime = crimes[position]
        holder.bind(crime)
    }

    override fun getItemCount() = crimes.size
}
















