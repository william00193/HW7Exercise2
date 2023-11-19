package com.example.hw7exercise2

import android.R
import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils.isEmpty
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.util.Preconditions.checkNotNull
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
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

            requireActivity().onBackPressedDispatcher.addCallback(object :
                OnBackPressedCallback(true) {

                    override fun handleOnBackPressed() {


//Started with just the first variable, (crimeTitle == " ") wasn't working I tried a work around below
//Everything worked when getText() was inserted between the title name and turining into string

                        val crimeTitle = binding.crimeTitle.getText().toString()
//                        val crimeTitle = binding.crimeTitle.getText().toString().length

//No matter how many characters I add to the title It continues to register as '130' in length and doesn't budge
//Finally got this one, I had to change how I got the title with getText() and I referenced isEmpty() in the condition

                            if (crimeTitle.isEmpty()) {

//                                if (crimeTitle == 0) {


                                Toast.makeText(
                                    binding.root.context,
                                    "Title Does Not Have a Value",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()


                            } else {

//What toast will be once debugging is complete
                                Toast.makeText(
                                    binding.root.context,
                                    "Title: '$crimeTitle' Has Been Saved",
                                    Toast.LENGTH_SHORT
                                )
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


