package com.jacekk.test.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionManager
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
    private var time: Long? = null

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

        setupDoubleBackPress()

        input_username.editText?.doAfterTextChanged {
            onCredentialsChanged()
        }
        input_password.editText?.doAfterTextChanged {
            onCredentialsChanged()
        }
        button_login.setOnClickListener {
            viewModel.reset()
            findNavController().navigate(
                R.id.action_loginFragment_to_loginResultFragment,
                bundleOf(
                    USERNAME_KEY to input_username.editText?.text.toString(),
                    PASSWORD_KEY to input_password.editText?.text.toString()
                )
            )
        }

        observeViewModel()
    }

    private fun onCredentialsChanged() {
        viewModel.credentialsChanged(
            binding.inputUsername.editText?.text.toString(),
            binding.inputPassword.editText?.text.toString()
        )
    }

    private fun observeViewModel() {
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

        viewModel.animRunning.observe(viewLifecycleOwner, Observer {
            if (it) {
                TransitionManager.beginDelayedTransition(container)
                val constrainSet = ConstraintSet()
                constrainSet.clone(container)
                constrainSet.connect(
                    image_logo.id,
                    ConstraintSet.TOP,
                    guideline_logo.id,
                    ConstraintSet.BOTTOM
                )
                constrainSet.applyTo(container)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.onVisible()
    }

    private fun setupDoubleBackPress() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            val toast = Toast.makeText(
                requireContext(),
                getString(R.string.press_again),
                Toast.LENGTH_SHORT
            )
            if (time == null) {
                time = System.currentTimeMillis()
                toast.show()
                return@addCallback
            }
            time?.let {
                val currentTime = System.currentTimeMillis()
                val longTimeInSeconds = TimeUnit.MILLISECONDS.toSeconds(currentTime - it)
                if (longTimeInSeconds < 2) {
                    requireActivity().finishAndRemoveTask()
                } else {
                    toast.show()
                    time = currentTime
                }
            }
        }
    }

}