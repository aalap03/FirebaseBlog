package com.example.aalap.blogs.TabFragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.aalap.blogs.Manifest
import com.example.aalap.blogs.R
import com.example.aalap.blogs.Utilities.DialogFrag
import kotlinx.android.synthetic.main.fragment_post_item.*

class FragmentPostItem : Fragment(), DialogFrag.ImageChhosed, DialogFrag.ImageResult {
    override fun imageUri(uri: Uri) {
        post_image.setImageURI(uri)
    }

    override fun imageBitmap(bitmap: Bitmap) {
        post_image.setImageBitmap(bitmap)
    }

    companion object {
        val TAG = "FragmentPostItem:"
        val GALLERY = 1
        val CAMERA = 2
        val CODE = 3
    }

    override fun isGallery(isGallery: Boolean) {
        var intent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)

        if (isGallery) {
            intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, GALLERY)
        } else {
            startActivityForResult(intent, CAMERA)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_post_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_post.setOnClickListener { validateAndPost() }

        post_image.setOnClickListener { handlePermissions() }
    }

    private fun validateAndPost() {

        if (TextUtils.isEmpty(post_title.text.toString())
                || TextUtils.isEmpty(post_description.text.toString())
                || TextUtils.isEmpty(post_city.text.toString())
                || TextUtils.isEmpty(post_country.text.toString())
                || TextUtils.isEmpty(post_email.text.toString())
                || TextUtils.isEmpty(post_state_province.text.toString()))
            Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_SHORT).show()
    }

    fun handlePermissions() {

        if (ContextCompat.checkSelfPermission(activity!!, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(activity!!, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(activity!!, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity!!, arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE), CODE)
        } else
            showDialog()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == CODE) {

            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                showDialog()
            else
                handlePermissions()
        }
    }

    private fun showDialog() {
        val dialog  = DialogFrag()
        dialog.setTargetFragment(this, 1)
        dialog.show(fragmentManager, TAG)
    }


}