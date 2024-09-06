package com.e.jannet_stable_code.utils

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.e.jannet_stable_code.R
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

const val TAG = "PickImage"

class PickImage(private val activity: Activity) {

    private var mImageBitmap: Bitmap? = null
    private var mCurrentPhotoPath: String? = null
    private val GALLERY = 1
    private var REQUEST_CAMERA: Int = 0
    private var imageUri: Uri? = null

    init {
        Log.d(TAG, ": PickImage>>INIT")
        Dexter.withActivity(activity)
            .withPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) { // check if all permissions are granted
                    if (report.areAllPermissionsGranted()) {
                        selectImage()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).withErrorListener { Utilities.showLog("Permission Error!") }
            .onSameThread()
            .check()
    }



    private fun selectImage() {
        val options = arrayOf<CharSequence>(
            activity.resources.getString(R.string.option_camera),
            activity.resources.getString(R.string.option_gallery),
            activity.resources.getString(R.string.cancel)
        )
        val builder = AlertDialog.Builder(activity)
        builder.setItems(options) { dialog, item ->
            when {
                options[item] == activity.resources.getString(R.string.option_camera) -> {
                    takePhotoFromCamera()
                }
                options[item] == activity.resources.getString(R.string.option_gallery) -> {
                    choosePhotoFromGallary()
                }
                options[item] == activity.resources.getString(R.string.cancel) -> {
                    dialog.dismiss()
                }
            }
        }
        builder.show()
    }

    private fun choosePhotoFromGallary() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        activity.startActivityForResult(galleryIntent, GALLERY)
    }

    private fun takePhotoFromCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if (cameraIntent.resolveActivity(activity.packageManager) != null) {
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (ex: IOException) {
                Log.i(TAG, "IOException")
            }
            if (photoFile != null) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile))
                } else {
                    val file = File(Uri.fromFile(photoFile).path)
                    val photoUri = FileProvider.getUriForFile(
                        activity,
                        activity.packageName + ".provider",
                        file
                    )
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                }
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                if (intent.resolveActivity(activity.packageManager) != null) {
                    activity.startActivityForResult(intent, REQUEST_CAMERA)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File? {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = activity.getExternalFilesDir(
            Environment.DIRECTORY_PICTURES
        )
        val image = File.createTempFile(
            imageFileName,
            ".jpg",
            storageDir
        )
        mCurrentPhotoPath = "file:" + image.absolutePath
        return image
    }

    fun activityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        imageView: ImageView?
    ): Uri {
        try {

            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == REQUEST_CAMERA) {
                    try {
                        mImageBitmap = MediaStore.Images.Media.getBitmap(
                            activity.contentResolver,
                            Uri.parse(mCurrentPhotoPath)
                        )
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    if (mImageBitmap != null) {
                        imageUri = getImageUri(mImageBitmap!!)

//                        startCropImageActivity(getImageUri(mImageBitmap!!)) //OLD AKSHAY

                        //NEW
                        if (imageView != null)
                            Glide.with(activity)
                                .load(imageUri)
                                .into(imageView)
                    }
                } else if (requestCode == GALLERY) {
                    if (data != null) {
                        val selectedImage = data.data
                        imageUri = selectedImage

                        //                        startCropImageActivity(selectedImage)// OLD AKSHAY

                        //NEW
                        if (imageView != null)
                            Glide.with(activity)
                                .load(imageUri)
                                .into(imageView)


                    }
                }

                //OLD AKSHAY
                /* else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                    val result: CropImage.ActivityResult = CropImage.getActivityResult(data)
                    imageUri = result.uri
                    if (imageView != null)
                        Glide.with(activity)
                            .load(result.uri)
                            .into(imageView)
                }*/
            } else {
                Utilities.showLog(TAG, "onActivityResult: ==== error ===")
            }
            return imageUri!!

        } catch (e: Exception) {
            e.printStackTrace()
            return Uri.parse("")
        }
    }

    private fun getImageUri(inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val path = MediaStore.Images.Media.insertImage(
            activity.contentResolver,
            inImage,
            "" + System.currentTimeMillis(),
            null
        )
        return Uri.parse(path)
    }


    //OLD AKSHAY
    /*private fun startCropImageActivity(imageUri: Uri?) {
        val intent: Intent = CropImage.activity(imageUri!!)
            .setGuidelines(CropImageView.Guidelines.ON)
            .setMultiTouchEnabled(true)
            .setOutputCompressQuality(50)
            .getIntent(activity)
        activity.startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
    }
*/
    fun getImage(): String? {
        return try {
            ImageFilePath.getPath(activity, imageUri!!)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }
}
