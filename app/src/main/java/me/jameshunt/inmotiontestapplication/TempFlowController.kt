package me.jameshunt.inmotiontestapplication

import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import com.inmotionsoftware.promisekt.Promise
import com.inmotionsoftware.promisekt.ensure
import com.inmotionsoftware.promisekt.thenMap
import me.jameshunt.flow.generated.GeneratedTempController
import me.jameshunt.flow.generated.GeneratedTempController.TempFlowState.*
import me.jameshunt.flow.promise.DispatchExecutor
import me.jameshunt.flow.proxy
import me.jameshunt.inmotiontestapplication.splash.SplashFragment

class TempFlowController : GeneratedTempController() {

    private val testFragmentProxy = proxy(TestFragment::class.java)
    private val splashFragmentProxy = proxy(SplashFragment::class.java)

    override fun onTemp(state: Temp): Promise<FromTemp> {
        return this.flow(fragmentProxy = testFragmentProxy, input = "wooooow").forResult(
            onBack = { state.toBack() },
            onComplete = { state.toSplash() }
        )
    }

    override fun onSplash(state: Splash): Promise<FromSplash> {
        return this.flow(fragmentProxy = splashFragmentProxy, input = Unit).forResult(
            onComplete = { state.toLoad() }
        )
    }

    override fun onLoad(state: Load): Promise<FromLoad> {
        return Promise.value(Unit)
            .ensure(on = DispatchExecutor.background) { Thread.sleep(3000) }
            .thenMap { state.toActivity() }
    }

    override fun onActivity(state: Activity): Promise<FromActivity> {

        val getIntent = Intent(Intent.ACTION_GET_CONTENT)
        getIntent.type = "image/*"

        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickIntent.type = "image/*"

        val chooserIntent = Intent.createChooser(getIntent, "Select Image")
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))

        val bitmap = this.flow<Bitmap>(chooserIntent) { context, data ->
            MediaStore.Images.Media.getBitmap(context.contentResolver, data!!.data)
        }

        return bitmap.forResult(
            onComplete = {
                println("woohoooooo bitmap: ${it.allocationByteCount}")
                state.toTemp()
            },
            onBack = {
                println("back from activity")
                state.toTemp()
            })
    }
}