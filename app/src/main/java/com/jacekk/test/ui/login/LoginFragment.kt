package com.jacekk.test.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.jacekk.test.R
import com.jacekk.test.databinding.FragmentLoginBinding
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

const val USERNAME_KEY = "username"
const val PASSWORD_KEY = "password"

class LoginFragment : Fragment() {

    private val viewModel: LoginFragmentViewModel by viewModel()
    private lateinit var binding: FragmentLoginBinding

    private val afterTextChangedListener = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            /// not important
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // not important
        }

        override fun afterTextChanged(s: Editable?) {
            viewModel.credentialsChanged(
                binding.inputUsername.editText?.text.toString(),
                binding.inputPassword.editText?.text.toString()
            )
        }
    }

    var time: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (time == null) {
                time = System.currentTimeMillis()
                return@addCallback
            }
            time?.let {
                val currentTime = System.currentTimeMillis()
                val longTimeInSeconds = TimeUnit.MILLISECONDS.toSeconds(currentTime - it)
                if (longTimeInSeconds < 2) {
                    Toast.makeText(requireContext(), "Bye bye!", Toast.LENGTH_SHORT).show()
                    requireActivity().finishAndRemoveTask()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Fast click again to quit :)",
                        Toast.LENGTH_SHORT
                    ).show()
                    time = currentTime
                }
            }
        }

        input_username.editText?.addTextChangedListener(afterTextChangedListener)
        input_password.editText?.addTextChangedListener(afterTextChangedListener)
        button_login.setOnClickListener {
            val bundle = Bundle().apply {
                putString(USERNAME_KEY, input_username.editText?.text.toString())
                putString(PASSWORD_KEY, input_username.editText?.text.toString())
            }
            viewModel.reset()
            findNavController().navigate(R.id.action_loginFragment_to_loginResultFragment, bundle)
        }

        viewModel.loginViewState.observe(viewLifecycleOwner, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            button_login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                input_username_error.text = getString(loginState.usernameError)
                input_username_error.visibility = View.VISIBLE
            } else {
                input_username_error.visibility = View.GONE
            }
            if (loginState.passwordError != null) {
                input_password_error.text = getString(loginState.passwordError)
                input_password_error.visibility = View.VISIBLE
            } else {
                input_password_error.visibility = View.GONE
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        input_username?.let {
            it.editText?.removeTextChangedListener(afterTextChangedListener)
        }
        input_password?.let {
            it.editText?.removeTextChangedListener(afterTextChangedListener)
        }
    }

}