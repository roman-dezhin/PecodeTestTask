package com.github.romandezhin.pecodetesttask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.github.romandezhin.pecodetesttask.databinding.FragmentScreenBinding

class ScreenFragment : Fragment() {
    private val number by argument<Int>(KEY_ID)
    private val items: MainViewModel by activityViewModels()
    private var _binding: FragmentScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userActions = activity as UserActions

        binding.number.text = number.toString()

        if (number == 1) binding.deleteFragmentButton.visibility = View.GONE

        binding.addFragmentButton.setOnClickListener {
            userActions.addNewFragment()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val KEY_ID = "id"
    }
}