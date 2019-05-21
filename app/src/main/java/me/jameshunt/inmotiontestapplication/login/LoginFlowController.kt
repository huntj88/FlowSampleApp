package me.jameshunt.inmotiontestapplication.login

import android.util.Log
import me.jameshunt.flow.generated.GeneratedLoginController
import me.jameshunt.flow.generated.GeneratedLoginController.LoginFlowState.*
import me.jameshunt.flow.promise.Promise
import me.jameshunt.flow.promise.then
import me.jameshunt.flow.proxy
import me.jameshunt.inmotiontestapplication.profile.Profile
import me.jameshunt.inmotiontestapplication.profile.ProfileFlowController
import me.jameshunt.inmotiontestapplication.profile.ProfileManager

class LoginFlowController : GeneratedLoginController() {
    private val loginFragmentProxy = proxy(LoginFragment::class.java)

    override fun onLoginForm(state: LoginForm): Promise<FromLoginForm> {
        return this.flow(fragmentProxy = loginFragmentProxy, input = Unit)
            .forResult<LoginFragment.LoginFormData, FromLoginForm>(
                onBack = { Promise(Back) },
                onComplete = { Promise(CheckCredentials(it)) }
            )
    }

    override fun onCheckCredentials(state: CheckCredentials): Promise<FromCheckCredentials> {
        return ProfileManager
            .login(email = state.formData.email, password = state.formData.password)
            .then<Boolean, FromCheckCredentials> {
                when (it) {
                    true -> GetProfile
                    false -> ShowError
                }
            }
    }

    override fun onShowError(state: ShowError): Promise<FromShowError> {
        Log.d("wow and error", "bad error go away")
        return Promise(LoginForm)
    }

    override fun onGetProfile(state: GetProfile): Promise<FromGetProfile> {
        return this.flow(controller = ProfileFlowController::class.java, input = Unit)
            .forResult<Profile, FromGetProfile>(
                onComplete = { Promise(Done(Unit)) },
                onCatch = { Promise(ShowError) }
            )
    }
}