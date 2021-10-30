package com.github.romandezhin.pecodetesttask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.romandezhin.pecodetesttask.databinding.FragmentScreenBinding

class ScreenFragment : Fragment() {
    private val number by argument<Int>(KEY_ID)
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

        if (number == 1) binding.deleteFragmentButton.visibility = View.GONE

        val userActions = activity as UserActions

        with(binding) {
            screenNumber.text = number.toString()

            addFragmentButton.setOnClickListener {
                userActions.addNewFragment()
            }

            deleteFragmentButton.setOnClickListener {
                userActions.removeLastFragment()
            }

            addNotificationButton.setOnClickListener {
                userActions.addNotification(number)
            }
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