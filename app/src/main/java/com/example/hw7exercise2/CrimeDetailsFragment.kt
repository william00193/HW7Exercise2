package com.example.hw7exercise2

import android.R
import android.annotation.SuppressLint
import android.app.ProgressDialog.show
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.window.OnBackInvokedDispatcher
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.annotation.Nullable
import androidx.core.text.trimmedLength
import androidx.core.util.Preconditions.checkNotNull
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.hw7exercise2.databinding.FragmentCrimeDetailBinding
import kotlinx.coroutines.launch
import java.lang.Compiler.enable
import java.util.*


class CrimeDetailsFragment :Fragment() {


    private val args: CrimeDetailsFragmentArgs by navArgs()

    private val crimeDetailViewModel: CrimeDetailViewModel by viewModels {
        CrimeDetailViewModel.CrimeDetailViewModelFactory(args.crimeId)
    }

    private var _binding: FragmentCrimeDetailBinding? = null

    private val binding
        @SuppressLint("RestrictedApi")
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible"
        }





    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


//Calling function for part B of exercise
        setOnBackPressed()

        _binding = FragmentCrimeDetailBinding.inflate(layoutInflater, container, false)
        return binding.root


    }



private fun setOnBackPressed() {

    requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true){



        override fun handleOnBackPressed() {

            //Started with just the first variable, (crimeTitle == " ") wasn't working I tried a work around below
            val crimeTitle = binding.crimeTitle.toString()
            val codedCrimeTitleLength = crimeTitle.length


//No matter how many characters I add to the title It continues to register as '130' in length and doesn't budge
//What I left the condition to equal for debugging
//Going back and fourth between ( != 130 ) & ( == 130 ) allows me to see a title being saved blank or not allowed
            if (codedCrimeTitleLength == 130) {

//Toast that will display the length of current crime title
                Toast.makeText(binding.root.context,
                    "$codedCrimeTitleLength",
                    Toast.LENGTH_SHORT)
                    .show()

//Toast that shows why (crimeTitle == " ") wont register
                Toast.makeText(binding.root.context,
                    "$crimeTitle",
                    Toast.LENGTH_SHORT)
                    .show()

//Toast I will keep once debugging is done
                Toast.makeText(binding.root.context,
                    "Title Does Not Have a Value",
                    Toast.LENGTH_SHORT)
                    .show()

        } else  {


//What toast will be once debugging is complete
                Toast.makeText(binding.root.context,
                    "Title Has Been Saved",
                    Toast.LENGTH_SHORT)
                    .show()

//Toast that displays what the current title is registering as
                Toast.makeText(binding.root.context,
                    "$crimeTitle",
                    Toast.LENGTH_SHORT)
                    .show()

                isEnabled = true

                findNavController().popBackStack()
        }

        }

    })

}




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {


            crimeTitle.doOnTextChanged{text,_,_,_ ->

                crimeDetailViewModel.updateCrime { oldCrime -> oldCrime.copy(title = text.toString())
                }

            }

            crimeDate.apply {

                isEnabled = false
            }


            crimeSolved.setOnCheckedChangeListener{_, isCheckeed ->

                crimeDetailViewModel.updateCrime { oldCrime -> oldCrime.copy(isSolved = isCheckeed)
                }
            }
        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                crimeDetailViewModel.crime.collect { crime ->
                    crime?.let { updateUi(it) }
                }
            }
        }
    }

    private fun updateUi(crime:Crime) {

        binding.apply {
            if (crimeTitle.text.toString() != crime.title) {
                crimeTitle.setText(crime.title)
            }
            crimeDate.text = crime.date.toString()
            crimeSolved.isChecked = crime.isSolved
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //binding = null
        _binding = null
    }



}


