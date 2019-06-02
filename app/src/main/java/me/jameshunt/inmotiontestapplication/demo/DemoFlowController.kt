package me.jameshunt.inmotiontestapplication.demo

import com.inmotionsoftware.promisekt.Promise
import me.jameshunt.flow.generated.GeneratedDemoController
import me.jameshunt.flow.generated.GeneratedDemoController.DemoFlowState.*

class DemoFlowController: GeneratedDemoController() {

    override fun onBegin(state: Begin): Promise<FromBegin> {
        return state.toAddAssociation()
    }

    override fun onPrompt(state: Prompt): Promise<FromPrompt> {
        return state.toAuthenticate()
    }

    override fun onAddAssociation(state: AddAssociation): Promise<FromAddAssociation> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onAuthenticate(state: Authenticate): Promise<FromAuthenticate> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onEditAssociation(state: EditAssociation): Promise<FromEditAssociation> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onEditAddAssociation(state: EditAddAssociation): Promise<FromEditAddAssociation> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSubmitAddAssociation(state: SubmitAddAssociation): Promise<FromSubmitAddAssociation> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSubmitEditAssociation(state: SubmitEditAssociation): Promise<FromSubmitEditAssociation> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSubmitDeleteAssociation(state: SubmitDeleteAssociation): Promise<FromSubmitDeleteAssociation> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}