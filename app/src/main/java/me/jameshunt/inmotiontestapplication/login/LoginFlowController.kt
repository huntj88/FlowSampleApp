package me.jameshunt.inmotiontestapplication.login

import me.jameshunt.flow.ViewId
import me.jameshunt.flow.generated.GeneratedLoginController
import me.jameshunt.flow.generated.GeneratedLoginController.LoginFlowState.*
import me.jameshunt.flow.promise.Promise
import me.jameshunt.flow.promise.then
import me.jameshunt.flow.proxy
import me.jameshunt.inmotiontestapplication.service.AccountManager

class LoginFlowController(viewId: ViewId) : GeneratedLoginController(viewId) {
    private val loginFragmentProxy = proxy(LoginFragment::class.java)

    override fun onLoginForm(state: LoginForm): Promise<FromLoginForm> {
        return this.flow(fragmentProxy = loginFragmentProxy, input = Unit)
            .forResult<LoginFragment.LoginFormData, FromLoginForm>(
                onBack = { Promise(Back) },
                onComplete = { Promise(CheckCredentials(it)) }
            )
    }

    override fun onCheckCredentials(state: CheckCredentials): Promise<FromCheckCredentials> {
        return AccountManager
            .login(email = state.formData.email, password = state.formData.password)
            .then<Boolean, FromCheckCredentials> {
                when (it) {
                    true -> Done(Unit)
                    false -> LoginForm
                }
            }
    }
}