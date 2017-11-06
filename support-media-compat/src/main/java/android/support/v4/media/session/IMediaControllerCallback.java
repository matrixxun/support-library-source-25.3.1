/* Copyright (C) 2014 The Android Open Source Project
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

package android.support.v4.media.session;

import android.os.Bundle;
import android.os.IInterface;
import android.os.RemoteException;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.ParcelableVolumeInfo;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v4.media.session.MediaSessionCompat;

/**
 * Callback interface for a MediaSessionCompat to send updates to a
 * MediaControllerCompat. This is only used on pre-Lollipop systems.
 * @hide
 */
 import java.util.List;

interface IMediaControllerCallback extends IInterface {
    void onEvent(String event,  Bundle extras) throws RemoteException, Exception;
    void onSessionDestroyed() throws RemoteException, Exception;

    // These callbacks are for the TransportController
    void onPlaybackStateChanged( PlaybackStateCompat state) throws RemoteException, Exception;
    void onMetadataChanged( MediaMetadataCompat metadata) throws RemoteException, Exception;
    void onQueueChanged( List<MediaSessionCompat.QueueItem> queue) throws RemoteException, Exception;
    void onQueueTitleChanged(CharSequence title) throws RemoteException, Exception;
    void onExtrasChanged( Bundle extras) throws RemoteException, Exception;
    void onVolumeInfoChanged( ParcelableVolumeInfo info) throws RemoteException, Exception;
    void onRepeatModeChanged(int repeatMode) throws RemoteException, Exception;
    void onShuffleModeChanged(boolean enabled) throws RemoteException, Exception;
}
