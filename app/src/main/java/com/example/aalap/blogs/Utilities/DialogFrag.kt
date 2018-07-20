package com.example.aalap.blogs.Utilities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.aalap.blogs.MainScreen
import com.example.aalap.blogs.R
import com.example.aalap.blogs.TabFragments.FragmentPostItem
import kotlinx.android.synthetic.main.dialog_frag_view.*
import kotlinx.android.synthetic.main.fragment_post_item.*

class DialogFrag : DialogFragment() {

    lateinit var imageResult: ImageResult

    interface ImageResult {
        fun imageUri(uri: Uri)
        fun imageBitmap(bitmap: Bitmap)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_frag_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        image_camera.setOnClickListener {
            startActivityForResult(intent, FragmentPostItem.CAMERA)
        }
        image_gallery.setOnClickListener {
            intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, FragmentPostItem.GALLERY)
        }
    }

    override fun onAttach(context: Context?) {
        imageResult = targetFragment as ImageResult
        super.onAttach(context)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == FragmentPostItem.CAMERA) {
                imageResult.imageBitmap(data?.extras?.get("data") as Bitmap)
                dismiss()
            } else if (requestCode == FragmentPostItem.GALLERY) {
                imageResult.imageUri(data?.data!!)
                dismiss()
            }

        }
    }
}