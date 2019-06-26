package me.jameshunt.inmotiontestapplication.flow.external

import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import me.jameshunt.flow.ActivityAdapterFlowController
import me.jameshunt.flow.FlowResult
import me.jameshunt.inmotiontestapplication.flow.external.PhotoPickerFlowController.Output
import me.jameshunt.inmotiontestapplication.flow.external.PhotoPickerFlowController.PhotoType

class PhotoPickerFlowController : ActivityAdapterFlowController<PhotoType, Output>() {

    enum class PhotoType {
        Bitmap,
        Uri
    }

    sealed class Output {
        data class Bitmap(val value: android.graphics.Bitmap) : Output()
        data class Uri(val value: android.net.Uri) : Output()
    }

    override suspend fun handleIOActivityIntents(context: () -> Context, flowInput: PhotoType): FlowResult<Output> {
        val getIntent = Intent(Intent.ACTION_GET_CONTENT)
        getIntent.type = "image/*"

        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickIntent.type = "image/*"

        val chooserIntent = Intent.createChooser(getIntent, "Select Image")
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))

        return this.flow(chooserIntent) { result: Intent ->
            when (flowInput) {
                PhotoType.Bitmap -> {
                    val bitmap = MediaStore.Images.Media.getBitmap(context().contentResolver, result.data)
                    Output.Bitmap(bitmap)
                }
                PhotoType.Uri -> Output.Uri(result.data!!)
            }
        }
    }
}