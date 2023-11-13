package com.example.hw7exercise2

import android.R
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.util.Preconditions.checkNotNull
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.example.hw7exercise2.databinding.FragmentCrimeDetailBinding
import kotlinx.coroutines.launch
import java.util.*


//private const val TAG = "CrimeDetailFragment"

class CrimeDetailsFragment :Fragment(){

    // private lateinit var crime:Crime
    private val args: CrimeDetailsFragmentArgs by navArgs()

    private val crimeDetailViewModel: CrimeDetailViewModel by viewModels {
        CrimeDetailViewModel.CrimeDetailViewModelFactory(args.crimeId)
    }

    //private lateinit var binding :FragmentCrimeDetailBinding
    private var _binding : FragmentCrimeDetailBinding? =  null

    private val binding
        get() = checkNotNull(_binding){
            "Cannot access binding because it is null. Is the view visisble"

        }

    /*  override fun onCreate(savedInstanceState: Bundle?) {
          super.onCreate(savedInstanceState)

          crime = Crime(

              UUID.randomUUID(),
              title = "",
              date = Date(),
              isSolved = false

          )
          Log.d(TAG, "The crime ID is: ${args.crimeId}")
      }*/

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //binding = FragmentCrimeDetailBinding.inflate(layoutInflater, container, false)
        _binding = FragmentCrimeDetailBinding.inflate(layoutInflater, container, false)
        return binding.root


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


                val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                if(binding.crimeTitle == null) {

                    Toast.makeText(
                        binding.root.context,
                        "Cannot Leave Blank Title",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    requireActivity().onBackPressedDispatcher.addCallback(this, backPressedCallback)
                }

            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)


    }



    //Wiring up views in a fragment



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            // listener for edit text
            crimeTitle.doOnTextChanged{text,_,_,_ ->
                // crime = crime.copy(title = text.toString())`false`
                crimeDetailViewModel.updateCrime { oldCrime -> oldCrime.copy(title = text.toString())
                }

            }

            // listener for button
            crimeDate.apply {
                //  text = crime.date.toString()
                isEnabled = false

            }

            // listener for textbox changes
            crimeSolved.setOnCheckedChangeListener{_, isCheckeed ->
                //  crime = crime.copy(isSolved = isCheckeed)
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


