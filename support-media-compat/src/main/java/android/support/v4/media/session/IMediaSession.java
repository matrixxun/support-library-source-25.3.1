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

import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.RemoteException;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.RatingCompat;
import android.support.v4.media.session.IMediaControllerCallback;
import android.support.v4.media.session.ParcelableVolumeInfo;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.os.Bundle;
import android.view.KeyEvent;

import java.util.List;

/**
 * Interface to a MediaSessionCompat. This is only used on pre-Lollipop systems.
 * @hide
 */
interface IMediaSession {
    // Next ID: 44
    void sendCommand(String command,  Bundle args,  MediaSessionCompat.ResultReceiverWrapper cb);
    boolean sendMediaButton( KeyEvent mediaButton);
    void registerCallbackListener( IMediaControllerCallback cb) ;
    void unregisterCallbackListener( IMediaControllerCallback cb);
    boolean isTransportControlEnabled() ;
    String getPackageName() ;
    String getTag() ;
    PendingIntent getLaunchPendingIntent() ;
    long getFlags();
    ParcelableVolumeInfo getVolumeAttributes() ;
    void adjustVolume(int direction, int flags, String packageName) ;
    void setVolumeTo(int value, int flags, String packageName) ;
    MediaMetadataCompat getMetadata() ;
    PlaybackStateCompat getPlaybackState() ;
    List<MediaSessionCompat.QueueItem> getQueue() ;
    CharSequence getQueueTitle();
    Bundle getExtras() ;
    int getRatingType() ;
    int getRepeatMode() ;
    boolean isShuffleModeEnabled() ;
    void addQueueItem( MediaDescriptionCompat description) ;
    void addQueueItemAt( MediaDescriptionCompat description, int index) ;
    void removeQueueItem( MediaDescriptionCompat description) ;
    void removeQueueItemAt(int index) ;

    // These commands are for the TransportControls
    void prepare() throws RemoteException, Exception;
    void prepareFromMediaId(String uri,  Bundle extras) throws RemoteException, Exception;
    void prepareFromSearch(String string,  Bundle extras) throws RemoteException, Exception;
    void prepareFromUri(Uri uri, Bundle extras) throws RemoteException, Exception;
    void play() throws RemoteException, Exception;
    void playFromMediaId(String uri,   Bundle extras) throws RemoteException, Exception;
    void playFromSearch(String string,   Bundle extras) throws RemoteException, Exception;
    void playFromUri(  Uri uri,   Bundle extras) throws RemoteException, Exception;
    void skipToQueueItem(long id) ;
    void pause() throws RemoteException, Exception;
    void stop() throws RemoteException, Exception;
    void next() throws RemoteException, Exception;
    void previous() throws RemoteException, Exception;
    void fastForward() throws RemoteException, Exception;
    void rewind() throws RemoteException, Exception;
    void seekTo(long pos) throws RemoteException, Exception;
    void rate(  RatingCompat rating) throws RemoteException, Exception;
    void setRepeatMode(int repeatMode) throws RemoteException, Exception;
    void setShuffleModeEnabled(boolean shuffleMode) throws RemoteException, Exception;
    void sendCustomAction(String action,   Bundle args) throws RemoteException, Exception;
}
