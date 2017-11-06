/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.support.v4.app;

import android.app.Notification;

/**
 * Interface used for delivering notifications via a side channel that bypasses
 * the NotificationManagerService.
 *
 * @hide
 */
 interface INotificationSideChannel {
    /**
     * Send an ambient notification to the service.
     */
    void notify(String packageName, int id, String tag,  Notification notification);

    /**
     * Cancel an already-notified notification.
     */
    void cancel(String packageName, int id, String tag);

    /**
     * Cancel all notifications for the given package.
     */
    void cancelAll(String packageName);
}
