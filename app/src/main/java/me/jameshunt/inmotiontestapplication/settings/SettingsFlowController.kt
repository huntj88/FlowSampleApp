package me.jameshunt.inmotiontestapplication.settings

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

    override suspend fun onSettings(state: Settings): FromSettings {
        ProfileManager.profile ?: return state.toNotLoggedIn()

        return this.flow(fragmentProxy = settingsFragment, input = Unit).forResult(
            onBack = { state.toBack() },
            onComplete = {
                when (it) {
                    SettingsFragment.Output.UserSettings -> state.toUserSettings()
                    SettingsFragment.Output.Logout -> state.toLogout()
                }
            }
        )
    }

    override suspend fun onUserSettings(state: UserSettings): FromUserSettings {
        val input = ProfileManager.profile!!
            .let { UserSettingsFragment.Data(firstName = it.firstName, lastName = it.lastName) }

        return this.flow(fragmentProxy = userSettingsFragment, input = input).forResult(
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

    override suspend fun onLogout(state: Logout): FromLogout {
        ProfileManager.profile = null
        return state.toSettings()
    }

    override suspend fun onNotLoggedIn(state: NotLoggedIn): FromNotLoggedIn {
        return this.flow(fragmentProxy = notLoggedInFragment, input = Unit).forResult(
            onBack = { state.toBack() },
            onComplete = { state.toLogin() }
        )
    }

    override suspend fun onLogin(state: Login): FromLogin {
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