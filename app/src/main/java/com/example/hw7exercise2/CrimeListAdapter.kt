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


class CrimeHolder (
    val binding: ListItemCrimeBinding

): RecyclerView.ViewHolder(binding.root){

    fun bind(crime:Crime,onCrimeClicked:(crimeId:UUID)-> Unit){

        binding.crimeTitle.text = crime.title
        binding.crimeDate.text = crime.date.toString()


        binding.root.setOnClickListener{


            onCrimeClicked(crime.id)

        }

        binding.crimeSolved.visibility = if (crime.isSolved) {
            View.VISIBLE

        } else
        {
            View.GONE
        }
    }
}



class CrimeListAdapter(
    private val crimes: List<Crime>,
    private val onCrimeClicked:(crimeId:UUID)-> Unit): RecyclerView.Adapter<CrimeHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemCrimeBinding.inflate(inflater, parent, false)
        return CrimeHolder(binding)


    }

    override fun onBindViewHolder(holder: CrimeHolder, position: Int) {

        val crime = crimes[position]

        holder.bind(crime, onCrimeClicked)
    }

    override fun getItemCount() = crimes.size
}


















