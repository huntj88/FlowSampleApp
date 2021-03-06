package me.jameshunt.inmotiontestapplication.settings

import com.inmotionsoftware.promisekt.Promise
import me.jameshunt.flow.SimpleGroupController
import me.jameshunt.flow.castFromInput
import me.jameshunt.flow.generated.GeneratedSettingController
import me.jameshunt.flow.generated.GeneratedSettingController.SettingFlowState.*
import me.jameshunt.flow.proxy
import me.jameshunt.inmotiontestapplication.login.LoginFlowController
import me.jameshunt.business.ProfileManager

class SettingsFlowController : GeneratedSettingController() {

    private val settingsFragment = proxy(SettingsFragment::class.java)
    private val userSettingsFragment = proxy(UserSettingsFragment::class.java)
    private val notLoggedInFragment = proxy(NotLoggedInFragment::class.java)

    override fun onSettings(state: Settings): Promise<FromSettings> {
        ProfileManager.profile?: return Promise.value(NotLoggedIn)

        return this.flow(fragmentProxy = settingsFragment, input = Unit)
            .forResult<SettingsFragment.Output, FromSettings>(
                onBack = { Promise.value(Back) },
                onComplete = {
                    when (it) {
                        SettingsFragment.Output.UserSettings -> state.toUserSettings()
                        SettingsFragment.Output.Logout -> state.toLogout()
                    }
                }
            )
    }

    override fun onUserSettings(state: UserSettings): Promise<FromUserSettings> {
        val input = ProfileManager.profile!!
            .let { UserSettingsFragment.Data(firstName = it.firstName, lastName = it.lastName) }

        return this.flow(fragmentProxy = userSettingsFragment, input = input)
            .forResult(
                onBack = { state.toSettings() },
                onComplete = {
                    ProfileManager.profile = ProfileManager.profile?.copy(
                        firstName = it.firstName,
                        lastName = it.lastName
                    )
                    state.toSettings()
                }
            )
    }

    override fun onLogout(state: Logout): Promise<FromLogout> {
        ProfileManager.profile = null
        return Promise.value(Settings)
    }

    override fun onNotLoggedIn(state: NotLoggedIn): Promise<FromNotLoggedIn> {
        return this.flow(fragmentProxy = notLoggedInFragment, input = Unit)
            .forResult(
                onBack = { state.toBack() },
                onComplete = { state.toLogin() }
            )
    }

    override fun onLogin(state: Login): Promise<FromLogin> {
        val input = SimpleGroupController.input(
            flow = LoginFlowController::class.java,
            input = Unit
        )

        return this.flowGroup(
            controller = SimpleGroupController::class.java.castFromInput(input),
            input = input
        ).forResult(
            onBack = { state.toSettings() },
            onComplete = { state.toSettings() }
        )
    }
}