package com.example.aalap.blogs.TabFragments

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.aalap.blogs.PostItem
import com.example.aalap.blogs.R
import com.example.aalap.blogs.Utilities.DialogFrag
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_post_item.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.io.ByteArrayOutputStream


class FragmentPostItem : Fragment(), DialogFrag.ImageResult, AnkoLogger {

    var bitmap: Bitmap? = null
    var uri: Uri? = null
    lateinit var byteArray: ByteArray

    override fun imageUri(uri: Uri) {
        info { "Image:uri:$uri" }
        post_image.setImageURI(uri)
        this.uri = uri
        bitmap = null
    }


    override fun imageBitmap(bitmap: Bitmap) {
        info { "Image:Bitmap$bitmap" }
        post_image.setImageBitmap(bitmap)
        this.bitmap = bitmap
        this.uri = null
    }

    companion object {
        const val TAG = "FragmentPostItem:"
        const val CODE = 3
        const val CAMERA = 2
        const val GALLERY = 4
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
                || TextUtils.isEmpty(post_state_province.text.toString())) {

            Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_SHORT).show()
        } else {

            if (bitmap != null || uri != null) {

                if (bitmap != null)
                    CompressImage().execute(null)
                else
                    CompressImage().execute(uri)

            } else
                Toast.makeText(context, "Please Add Image", Toast.LENGTH_SHORT).show()

        }
    }

    private fun handlePermissions() {

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
        val dialog = DialogFrag()
        dialog.setTargetFragment(this, 1)
        dialog.show(fragmentManager, TAG)
    }

    inner class CompressImage() : AsyncTask<Uri, Void, ByteArray>() {

        override fun onPostExecute(result: ByteArray?) {
            super.onPostExecute(result)
            byteArray = result!!
            info { "byteArray:$result" }
            postToFirebase()
        }

        override fun doInBackground(vararg params: Uri?): ByteArray {

            if (bitmap == null) {
                bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, params[0])
            }

            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)


            return byteArrayOutputStream.toByteArray()
        }

        override fun onPreExecute() {
            super.onPreExecute()
            Toast.makeText(context, "Compressing...", Toast.LENGTH_SHORT).show()
        }
    }

    private fun postToFirebase() {

        Toast.makeText(context, "Trying to upload image...", Toast.LENGTH_SHORT).show()

        val postId = FirebaseDatabase.getInstance().reference.push().key
        val storageReference = FirebaseStorage.getInstance().reference.child("posts/users/" + FirebaseAuth.getInstance().currentUser?.uid + "/" + postId + "/post_image")
        val databaseReference = FirebaseDatabase.getInstance().reference

        val uploadTask = storageReference.putBytes(byteArray)

        uploadTask
                .continueWithTask { taskSnapShot ->
                    if (!taskSnapShot.isSuccessful)
                        RuntimeException(taskSnapShot.exception)

                    Toast.makeText(context, "Uploaded image...", Toast.LENGTH_SHORT).show()

                    storageReference.downloadUrl
                }

                .addOnCompleteListener { taskSnapshot ->


                    Toast.makeText(context, "Image Posted...", Toast.LENGTH_SHORT).show()

                    val postItem = PostItem(
                            postId!!,
                            FirebaseAuth.getInstance().currentUser?.uid!!,
                            post_title.text.toString(),
                            post_description.text.toString(),
                            post_price.text.toString(),
                            post_country.text.toString(),
                            post_state_province.text.toString(),
                            post_city.text.toString(),
                            post_email.text.toString(),
                            taskSnapshot.toString())

                    info { "posting...$postItem" }

                    databaseReference.child("posts")
                            .child(postId)
                            .setValue(postItem)

                    resetFields()


                }.addOnFailureListener { exception ->
                    exception.printStackTrace()
                    info { "Exception Message: ${exception.localizedMessage }"}
                }
    }

    private fun resetFields() {

        post_title.setText("")
        post_description.setText("")
        post_city.setText("")
        post_country.setText("")
        post_email.setText("")
        post_price.setText("")
        post_state_province.setText("")
        post_image.setImageResource(0)

    }
}