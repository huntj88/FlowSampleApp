package me.jameshunt.inmotiontestapplication.login

import com.inmotionsoftware.promisekt.*
import me.jameshunt.business.ProfileBusinessFlowController
import me.jameshunt.business.ProfileManager
import me.jameshunt.flow.generated.GeneratedLoginController
import me.jameshunt.flow.generated.GeneratedLoginController.LoginFlowState.*
import me.jameshunt.flow.proxy
import me.jameshunt.inmotiontestapplication.flow.dialog.DialogMessageFlowController

class LoginFlowController : GeneratedLoginController() {
    private val loginFragmentProxy = proxy(LoginFragment::class.java)

    override suspend fun onLoginForm(state: LoginForm): FromLoginForm {
        return this.flow(fragmentProxy = loginFragmentProxy, input = Unit).forResult(
            onBack = { state.toBack() },
            onComplete = { state.toCheckCredentials(it) }
        )
    }

    override suspend fun onCheckCredentials(state: CheckCredentials): FromCheckCredentials {
        return ProfileManager
            .login(email = state.formData.email, password = state.formData.password)
            .let {
                when (it) {
                    true -> state.toGetProfile()
                    false -> state.toShowError()
                }
            }
    }

    override suspend fun onShowError(state: ShowError): FromShowError {
        return this.flow(
            controller = DialogMessageFlowController::class.java,
            input = "Invalid login credentials"
        ).forResult(
            onComplete = { state.toLoginForm() }
        )
    }

    override suspend fun onGetProfile(state: GetProfile): FromGetProfile {
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