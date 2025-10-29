# Firebase Notification Setup Guide

This guide explains how Firebase notifications are implemented in the Jannet app and how to use them.

## Overview

Firebase Cloud Messaging (FCM) has been integrated into the app to handle push notifications for events, updates, and reminders related to sports activities.

## Files Added/Modified

### New Files:
1. `FirebaseNotificationHelper.kt` - Helper class for managing FCM tokens and topics
2. `FirebaseNotificationManager.kt` - Manager class for initializing and managing notifications
3. `FirebaseNotificationDemoActivity.kt` - Demo activity showing how to use notifications
4. `activity_firebase_notification_demo.xml` - Layout for the demo activity

### Modified Files:
1. `AndroidManifest.xml` - Added notification permissions and Firebase messaging service
2. `MyFirebaseMessagingService.java` - Improved existing service with better notification handling
3. `MainApplication.kt` - Added Firebase notification initialization

## Features Implemented

### 1. Notification Permissions
- Added `POST_NOTIFICATIONS` permission for Android 13+
- Added `WAKE_LOCK` and `VIBRATE` permissions for notification functionality

### 2. Firebase Messaging Service
- Registered `MyFirebaseMessagingService` in AndroidManifest.xml
- Improved notification handling for both data and notification payloads
- Added proper notification channels for Android O+
- Enhanced notification display with proper icons and click actions

### 3. Notification Helper Classes
- `FirebaseNotificationHelper`: Basic FCM operations (token, topics)
- `FirebaseNotificationManager`: Advanced notification management and initialization

### 4. Topic Subscription System
- General notifications: `general_notifications`
- Role-based topics: `coach_notifications`, `parent_notifications`, `player_notifications`
- Sport-specific topics: `sport_[sport_name]`
- User-specific topics: `user_[user_id]`

## How to Use

### 1. Basic Usage in Activities

```kotlin
// Initialize notification helper
val notificationHelper = FirebaseNotificationHelper(this)

// Get FCM token
notificationHelper.getFirebaseToken { token ->
    if (token != null) {
        // Send token to your server
        sendTokenToServer(token)
    }
}

// Subscribe to topics
notificationHelper.subscribeToTopic("coach_notifications")
```

### 2. Advanced Usage with NotificationManager

```kotlin
// Initialize notification manager
val notificationManager = FirebaseNotificationManager(this)

// Subscribe user to specific topics based on their profile
notificationManager.subscribeToUserTopics(
    userId = "user123",
    userType = "coach",
    sports = listOf("football", "basketball")
)

// Check notification permission
if (notificationManager.isNotificationPermissionGranted()) {
    // Notifications are enabled
}
```

### 3. Request Notification Permission (Android 13+)

```kotlin
// In your Activity
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    if (!notificationManager.isNotificationPermissionGranted()) {
        notificationManager.requestNotificationPermission(this)
    }
}
```

## Server Integration

### 1. Sending Notifications from Server

To send notifications from your server, you can use the Firebase Admin SDK or REST API:

```json
{
  "to": "FCM_TOKEN_HERE",
  "notification": {
    "title": "Event Update",
    "body": "Your upcoming match has been rescheduled"
  },
  "data": {
    "event_id": "123",
    "type": "event_update"
  }
}
```

### 2. Topic-based Notifications

```json
{
  "to": "/topics/coach_notifications",
  "notification": {
    "title": "New Event Created",
    "body": "A new football event has been created"
  }
}
```

## Testing

### 1. Using the Demo Activity
- Navigate to `FirebaseNotificationDemoActivity` to test notification features
- Get FCM token, subscribe to topics, and check permissions

### 2. Testing Notifications
- Use Firebase Console to send test notifications
- Send to specific tokens or topics
- Test both notification and data payloads

## Configuration

### 1. Firebase Console Setup
1. Go to Firebase Console
2. Select your project
3. Navigate to Cloud Messaging
4. Create notification campaigns or use the API

### 2. Server-side Token Storage
- Store FCM tokens in your database
- Update tokens when they refresh (handled in `onNewToken`)
- Associate tokens with user accounts

## Troubleshooting

### Common Issues:

1. **Notifications not received**
   - Check if notification permission is granted
   - Verify FCM token is valid
   - Check Firebase Console for delivery status

2. **Token not generated**
   - Ensure Google Services plugin is applied
   - Check `google-services.json` is in the correct location
   - Verify Firebase dependencies are added

3. **Notifications not displayed**
   - Check notification channel is created
   - Verify notification service is registered in manifest
   - Check if app is in foreground (notifications may be handled differently)

## Next Steps

1. **Implement server-side token storage** - Update `sendTokenToServer()` method
2. **Add notification preferences** - Allow users to customize notification types
3. **Implement rich notifications** - Add images, actions, and custom layouts
4. **Add notification analytics** - Track notification open rates and engagement

## Security Considerations

- Never expose FCM server key in client code
- Validate notification payloads on the server
- Implement proper authentication for notification endpoints
- Use HTTPS for all server communications

