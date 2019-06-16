package me.jameshunt.inmotiontestapplication.login

import com.inmotionsoftware.promisekt.Promise
import com.inmotionsoftware.promisekt.map
import me.jameshunt.business.ProfileBusinessFlowController
import me.jameshunt.business.ProfileManager
import me.jameshunt.flow.generated.GeneratedLoginController
import me.jameshunt.flow.generated.GeneratedLoginController.LoginFlowState.*
import me.jameshunt.flow.proxy
import me.jameshunt.inmotiontestapplication.flow.dialog.DialogMessageFlowController

class LoginFlowController : GeneratedLoginController() {
    private val loginFragmentProxy = proxy(LoginFragment::class.java)

    override fun onLoginForm(state: LoginForm): Promise<FromLoginForm> {
        return this.flow(fragmentProxy = loginFragmentProxy, input = Unit).forResult(
            onBack = { state.toBack() },
            onComplete = { state.toCheckCredentials(it) }
        )
    }

    override fun onCheckCredentials(state: CheckCredentials): Promise<FromCheckCredentials> {
        return ProfileManager
            .login(email = state.formData.email, password = state.formData.password)
            .map<Boolean, FromCheckCredentials> {
                when (it) {
                    true -> GetProfile
                    false -> ShowError
                }
            }
    }

    override fun onShowError(state: ShowError): Promise<FromShowError> {
        return this.flow(
            controller = DialogMessageFlowController::class.java,
            input = "Invalid login credentials"
        ).forResult(
            onComplete = { state.toLoginForm() }
        )
    }

    override fun onGetProfile(state: GetProfile): Promise<FromGetProfile> {
        return this.flowBusiness(
            controller = ProfileBusinessFlowController::class.java,
            input = Unit
        ).forResult(
            onBack = {
                ProfileManager.profile = null
                state.toBack()
            },
            onComplete = { state.toDone() }
        )
    }
}