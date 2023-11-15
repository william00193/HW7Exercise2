package com.example.hw7exercise2

import android.R
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.window.OnBackInvokedDispatcher
import androidx.activity.OnBackPressedCallback
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
            "Cannot access binding because it is null. Is the view visisble"
        }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _binding = FragmentCrimeDetailBinding.inflate(layoutInflater, container, false)
        return binding.root


//Exercise #2 Attempt #1 for back button recognition
//        val backPressedCallback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
//
//            override fun handleOnBackPressed() {
//                CrimeDetailsFragmentArgs.Builder(Objects.requireNonNull(activity))
//
//                if(binding.crimeTitle == " ") {
//                    Toast.makeText(
//                        binding.root.context,
//                        "${crime.title} clicked",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                } else {
//                    requireActivity().onBackPressedDispatcher.addCallback(this, backPressedCallback)
//                }
//            }
//
//        }
//        requireActivity().onBackPressedDispatcher.addCallback(this, backPressedCallback)


//Exercise #2 Attempt #2 for back button recognition

//I feel Like after my third attempt I have most of the commands coded
//Im just looking for help as to where this piece of code needs to be in the page
//The last error im running into is that my binding.crimeTitle isn't being recognized as string but an editText
//I think this is because im using binding to find it and im not sure what other way I can reference the title...
        val onBackPressedCallback = object : OnBackPressedCallback(true)

      fun handleOnBackPressed(): OnBackPressedCallback {


            if (binding.crimeTitle == " ") {


                Toast.makeText(
                    binding.root.context,
                    "Cannot Leave Blank Title",
                    Toast.LENGTH_SHORT
                ).show()

                requireActivity().onBackPressedDispatcher.addCallback(
                    viewLifecycleOwner,
                    onBackPressedCallback
                )

                findNavController().popBackStack()

            } else {
                requireActivity().onBackPressedDispatcher.addCallback(
                    viewLifecycleOwner,
                    onBackPressedCallback
                )
            }

//Im not sure but I think this error is coming down to where this statement is placed in the page
//I have another similar version at the bottom but I feel this one is closer to being right
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            handleOnBackPressed()
        )

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



//Exercise #2 Attempt #3 for back button recognition
//    override fun onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState)
//
//        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
//
//            requireActivity().onBackPressedDispatcher.addCallback(
//                this,
//                object : OnBackPressedCallback(true) {
//                    override fun handleOnBackPressed() {
//
//                        Toast.makeText(
//                                      binding.root.context,
//                                      "${crime.title} clicked",
//                                      Toast.LENGTH_SHORT
//                                  ).show()
//                    }
//                        if (isEnabled) {
//                            isEnabled = false
//                            requireActivity().onBackPressed()
//                        }
//                    }
//                }
//            )
//    }

//


}


