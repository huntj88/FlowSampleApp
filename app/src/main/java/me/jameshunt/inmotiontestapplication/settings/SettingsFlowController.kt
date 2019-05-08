package me.jameshunt.inmotiontestapplication.settings

import me.jameshunt.flow.SimpleGroupController
import me.jameshunt.flow.ViewId
import me.jameshunt.flow.castFromInput
import me.jameshunt.flow.generated.GeneratedSettingController
import me.jameshunt.flow.generated.GeneratedSettingController.SettingFlowState.*
import me.jameshunt.flow.promise.Promise
import me.jameshunt.flow.proxy
import me.jameshunt.inmotiontestapplication.login.LoginFlowController
import me.jameshunt.inmotiontestapplication.service.AccountManager

class SettingsFlowController(viewId: ViewId) : GeneratedSettingController(viewId) {

    private val settingsFragment = proxy(SettingsFragment::class.java)
    private val userSettingsFragment = proxy(UserSettingsFragment::class.java)
    private val notLoggedInFragment = proxy(NotLoggedInFragment::class.java)

    override fun onSettings(state: Settings): Promise<FromSettings> {
        AccountManager.currentUser?: return Promise(NotLoggedIn)

        return this.flow(fragmentProxy = settingsFragment, input = Unit)
            .forResult<SettingsFragment.Output, FromSettings>(
                onBack = { Promise(Back) },
                onComplete = {
                    when (it) {
                        SettingsFragment.Output.UserSettings -> Promise(UserSettings)
                        SettingsFragment.Output.Logout -> Promise(Logout)
                    }
                }
            )
    }

    override fun onUserSettings(state: UserSettings): Promise<FromUserSettings> {
        val input = AccountManager.currentUser!!
            .let { UserSettingsFragment.Data(firstName = it.firstName, lastName = it.lastName) }

        return this.flow(fragmentProxy = userSettingsFragment, input = input)
            .forResult<UserSettingsFragment.Data, FromUserSettings>(
                onBack = { Promise(Settings) },
                onComplete = {
                    AccountManager.currentUser = AccountManager.currentUser?.copy(
                        firstName = it.firstName,
                        lastName = it.lastName
                    )
                    Promise(Settings)
                }
            )
    }

    override fun onLogout(state: Logout): Promise<FromLogout> {
        AccountManager.currentUser = null
        return Promise(Settings)
    }

    override fun onNotLoggedIn(state: NotLoggedIn): Promise<FromNotLoggedIn> {
        return this.flow(fragmentProxy = notLoggedInFragment, input = Unit)
            .forResult<Unit, FromNotLoggedIn>(
                onBack = { Promise(Back) },
                onComplete = { Promise(Login) }
            )
    }

    override fun onLogin(state: Login): Promise<FromLogin> {
        val input = SimpleGroupController.input(flow = LoginFlowController::class.java, input = Unit)

        return this.flowGroup(controller = SimpleGroupController::class.java.castFromInput(input), input = input).forResult<Unit, FromLogin>(
            onBack = { Promise(Settings) },
            onComplete = { Promise(Settings) }
        )
    }
}