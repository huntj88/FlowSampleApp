package me.jameshunt.inmotiontestapplication.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_login.*
import me.jameshunt.flow.FlowFragment
import me.jameshunt.inmotiontestapplication.R

class LoginFragment : FlowFragment<Unit, LoginFragment.LoginFormData>() {

    data class LoginFormData(
        val email: String,
        val password: String
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        this.loginButton.setOnClickListener {
            val output = LoginFormData(
                email = this.email.text.toString(),
                password = this.password.text.toString()
            )

            this.resolve(output)
        }
    }
}