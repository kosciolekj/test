package com.jacekk.test.ui.loginresult

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.jacekk.test.databinding.FragmentLoginResultBinding
import com.jacekk.test.ui.login.PASSWORD_KEY
import com.jacekk.test.ui.login.USERNAME_KEY
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginResultFragment : Fragment() {

    private val viewModel: LoginResultViewModel by viewModel()
    private lateinit var binding: FragmentLoginResultBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        arguments?.let {
            viewModel.init(it.getString(USERNAME_KEY)!!, it.getString(PASSWORD_KEY)!!)
        }

        viewModel.error.observe(viewLifecycleOwner, Observer { dataEvent ->
            dataEvent?.getDataIfNotHandled()?.let { error ->
                Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG).show()
            }

        })
    }

}