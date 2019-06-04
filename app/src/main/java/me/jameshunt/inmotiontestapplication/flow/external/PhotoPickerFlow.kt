package me.jameshunt.inmotiontestapplication.flow.external

import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import com.inmotionsoftware.promisekt.Promise
import me.jameshunt.flow.FlowResult
import me.jameshunt.flow.FragmentFlowController

interface PhotoPickerFlow {
    fun FragmentFlowController<*, *>.choosePhotoFromPicker(): Promise<FlowResult<Bitmap>>
}

class PhotoPickerFlowImpl: PhotoPickerFlow {
    override fun FragmentFlowController<*, *>.choosePhotoFromPicker(): Promise<FlowResult<Bitmap>> {
        val getIntent = Intent(Intent.ACTION_GET_CONTENT)
        getIntent.type = "image/*"

        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickIntent.type = "image/*"

        val chooserIntent = Intent.createChooser(getIntent, "Select Image")
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))

        return this.flow(chooserIntent) { context, data ->
            MediaStore.Images.Media.getBitmap(context.contentResolver, data.data)
        }
    }
}