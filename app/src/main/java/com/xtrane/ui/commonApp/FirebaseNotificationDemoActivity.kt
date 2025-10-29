package com.xtrane.ui.commonApp

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.xtrane.R
import com.xtrane.databinding.ActivityFirebaseNotificationDemoBinding
import com.xtrane.retrofit.controller.IBaseController
import com.xtrane.ui.BaseActivity
import com.xtrane.utils.FirebaseNotificationHelper
import com.xtrane.utils.FirebaseNotificationManager

/**
 * Demo activity showing how to use Firebase notifications
 * This can be used as a reference for implementing notifications in other activities
 */
abstract class FirebaseNotificationDemoActivity : BaseActivity() {

    private lateinit var binding: ActivityFirebaseNotificationDemoBinding
    private lateinit var notificationHelper: FirebaseNotificationHelper
    private lateinit var notificationManager: FirebaseNotificationManager

    companion object {
        private const val TAG = "FirebaseNotificationDemo"
        private const val NOTIFICATION_PERMISSION_REQUEST_CODE = 1001
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        binding = ActivityFirebaseNotificationDemoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeFirebaseNotifications()
        setupClickListeners()
    }

    @SuppressLint("LongLogTag")
    private fun initializeFirebaseNotifications() {
        notificationHelper = FirebaseNotificationHelper(this)
        notificationManager = FirebaseNotificationManager(this)

        // Check and request notification permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!notificationManager.isNotificationPermissionGranted()) {
                requestNotificationPermission()
            }
        }

        // Get Firebase token
        notificationHelper.getFirebaseToken { token ->
            if (token != null) {
                Log.d(TAG, "FCM Token: $token")
                binding.tvToken.text = "Token: ${token.take(20)}..."
                Toast.makeText(this, "FCM Token obtained", Toast.LENGTH_SHORT).show()
            } else {
                Log.e(TAG, "Failed to get FCM token")
                Toast.makeText(this, "Failed to get FCM token", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnGetToken.setOnClickListener {
            notificationHelper.getFirebaseToken { token ->
                if (token != null) {
                    binding.tvToken.text = "Token: ${token.take(20)}..."
                    Toast.makeText(this, "Token refreshed", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failed to get token", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnSubscribeGeneral.setOnClickListener {
            notificationHelper.subscribeToTopic("general_notifications")
            Toast.makeText(this, "Subscribed to general notifications", Toast.LENGTH_SHORT).show()
        }

        binding.btnSubscribeCoach.setOnClickListener {
            notificationHelper.subscribeToTopic("coach_notifications")
            Toast.makeText(this, "Subscribed to coach notifications", Toast.LENGTH_SHORT).show()
        }

        binding.btnSubscribeParent.setOnClickListener {
            notificationHelper.subscribeToTopic("parent_notifications")
            Toast.makeText(this, "Subscribed to parent notifications", Toast.LENGTH_SHORT).show()
        }

        binding.btnUnsubscribeGeneral.setOnClickListener {
            notificationHelper.unsubscribeFromTopic("general_notifications")
            Toast.makeText(this, "Unsubscribed from general notifications", Toast.LENGTH_SHORT).show()
        }

        binding.btnCheckPermission.setOnClickListener {
            val isGranted = notificationManager.isNotificationPermissionGranted()
            Toast.makeText(
                this, 
                "Notification permission: ${if (isGranted) "Granted" else "Not granted"}", 
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.btnRequestPermission.setOnClickListener {
            requestNotificationPermission()
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                NOTIFICATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        
        when (requestCode) {
            NOTIFICATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

