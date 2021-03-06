/*
 * Copyright (C) 2013 The Android Open Source Project
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

package android.support.v4.media;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.RemoteControlClient;
import android.os.Build;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.KeyEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Helper for implementing a media transport control (with play, pause, skip, and
 * other media actions).  Takes care of both key events and advanced features
 * like {@link android.media.RemoteControlClient}.  This class is intended to
 * serve as an intermediary between transport controls (whether they be on-screen
 * controls, hardware buttons, remote controls) and the actual player.  The player
 * is represented by a single {@link TransportPerformer} that must be supplied to
 * this class.  On-screen controls that want to control and show the state of the
 * player should do this through calls to the {@link TransportController} interface.
 *
 * <p>Here is a simple but fairly complete sample of a video player that is built
 * around this class.  Note that the MediaController class used here is not the one
 * included in the standard Android framework, but a custom implementation.  Real
 * applications often implement their own transport controls, or you can copy the
 * implementation here out of Support4Demos.</p>
 *
 * {@sample frameworks/support/samples/Support4Demos/src/com/example/android/supportv4/media/TransportControllerActivity.java
 *      complete}
 *
 * @deprecated Use {@link MediaControllerCompat}.
 */
@Deprecated
public class TransportMediator extends TransportController {
    final Context mContext;
    final TransportPerformer mCallbacks;
    final AudioManager mAudioManager;
    final View mView;
    final Object mDispatcherState;
    final TransportMediatorJellybeanMR2 mController;
    final ArrayList<TransportStateListener> mListeners
            = new ArrayList<TransportStateListener>();
    final TransportMediatorCallback mTransportKeyCallback
            = new TransportMediatorCallback() {
        @Override
        public void handleKey(KeyEvent key) {
            key.dispatch(mKeyEventCallback);
        }
        @Override
        public void handleAudioFocusChange(int focusChange) {
            mCallbacks.onAudioFocusChange(focusChange);
        }

        @Override
        public long getPlaybackPosition() {
            return mCallbacks.onGetCurrentPosition();
        }

        @Override
        public void playbackPositionUpdate(long newPositionMs) {
            mCallbacks.onSeekTo(newPositionMs);
        }
    };

    /**
     * Synonym for {@link KeyEvent#KEYCODE_MEDIA_PLAY KeyEvent.KEYCODE_MEDIA_PLAY}
     *
     * @deprecated Use {@link KeyEvent#KEYCODE_MEDIA_PLAY}.
     */
    @Deprecated
    public static final int KEYCODE_MEDIA_PLAY = 126;
    /**
     * Synonym for {@link KeyEvent#KEYCODE_MEDIA_PAUSE KeyEvent.KEYCODE_MEDIA_PAUSE}
     *
     * @deprecated Use {@link KeyEvent#KEYCODE_MEDIA_PAUSE}.
     */
    @Deprecated
    public static final int KEYCODE_MEDIA_PAUSE = 127;
    /**
     * Synonym for {@link KeyEvent#KEYCODE_MEDIA_RECORD KeyEvent.KEYCODE_MEDIA_RECORD}
     *
     * @deprecated Use {@link KeyEvent#KEYCODE_MEDIA_RECORD}.
     */
    @Deprecated
    public static final int KEYCODE_MEDIA_RECORD = 130;

    /**
     * Synonym for {@link android.media.RemoteControlClient#FLAG_KEY_MEDIA_PREVIOUS
     * RemoteControlClient.FLAG_KEY_MEDIA_PREVIOUS}
     *
     * @deprecated Use {@link RemoteControlClient#FLAG_KEY_MEDIA_PREVIOUS}.
     */
    @Deprecated
    public final static int FLAG_KEY_MEDIA_PREVIOUS = 1 << 0;
    /**
     * Synonym for {@link android.media.RemoteControlClient#FLAG_KEY_MEDIA_REWIND
     * RemoteControlClient.FLAG_KEY_MEDIA_REWIND}
     *
     * @deprecated Use {@link RemoteControlClient#FLAG_KEY_MEDIA_REWIND}.
     */
    @Deprecated
    public final static int FLAG_KEY_MEDIA_REWIND = 1 << 1;
    /**
     * Synonym for {@link android.media.RemoteControlClient#FLAG_KEY_MEDIA_PLAY
     * RemoteControlClient.FLAG_KEY_MEDIA_PLAY}
     *
     * @deprecated Use {@link RemoteControlClient#FLAG_KEY_MEDIA_PLAY}.
     */
    @Deprecated
    public final static int FLAG_KEY_MEDIA_PLAY = 1 << 2;
    /**
     * Synonym for {@link android.media.RemoteControlClient#FLAG_KEY_MEDIA_PLAY_PAUSE
     * RemoteControlClient.FLAG_KEY_MEDIA_PLAY_PAUSE}
     *
     * @deprecated Use {@link RemoteControlClient#FLAG_KEY_MEDIA_PLAY_PAUSE}.
     */
    @Deprecated
    public final static int FLAG_KEY_MEDIA_PLAY_PAUSE = 1 << 3;
    /**
     * Synonym for {@link android.media.RemoteControlClient#FLAG_KEY_MEDIA_PAUSE
     * RemoteControlClient.FLAG_KEY_MEDIA_PAUSE}
     *
     * @deprecated Use {@link RemoteControlClient#FLAG_KEY_MEDIA_PAUSE}.
     */
    @Deprecated
    public final static int FLAG_KEY_MEDIA_PAUSE = 1 << 4;
    /**
     * Synonym for {@link android.media.RemoteControlClient#FLAG_KEY_MEDIA_STOP
     * RemoteControlClient.FLAG_KEY_MEDIA_STOP}
     *
     * @deprecated Use {@link RemoteControlClient#FLAG_KEY_MEDIA_STOP}.
     */
    @Deprecated
    public final static int FLAG_KEY_MEDIA_STOP = 1 << 5;
    /**
     * Synonym for {@link android.media.RemoteControlClient#FLAG_KEY_MEDIA_FAST_FORWARD
     * RemoteControlClient.FLAG_KEY_MEDIA_FAST_FORWARD}
     *
     * @deprecated Use {@link RemoteControlClient#FLAG_KEY_MEDIA_FAST_FORWARD}.
     */
    @Deprecated
    public final static int FLAG_KEY_MEDIA_FAST_FORWARD = 1 << 6;
    /**
     * Synonym for {@link android.media.RemoteControlClient#FLAG_KEY_MEDIA_NEXT
     * RemoteControlClient.FLAG_KEY_MEDIA_NEXT}
     *
     * @deprecated Use {@link RemoteControlClient#FLAG_KEY_MEDIA_NEXT}.
     */
    @Deprecated
    public final static int FLAG_KEY_MEDIA_NEXT = 1 << 7;

    static boolean isMediaKey(int keyCode) {
        switch (keyCode) {
            case KEYCODE_MEDIA_PLAY:
            case KEYCODE_MEDIA_PAUSE:
            case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
            case KeyEvent.KEYCODE_MUTE:
            case KeyEvent.KEYCODE_HEADSETHOOK:
            case KeyEvent.KEYCODE_MEDIA_STOP:
            case KeyEvent.KEYCODE_MEDIA_NEXT:
            case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
            case KeyEvent.KEYCODE_MEDIA_REWIND:
            case KEYCODE_MEDIA_RECORD:
            case KeyEvent.KEYCODE_MEDIA_FAST_FORWARD: {
                return true;
            }
        }
        return false;
    }

    final KeyEvent.Callback mKeyEventCallback = new KeyEvent.Callback() {
        @Override
        public boolean onKeyDown(int keyCode, KeyEvent event) {
            return isMediaKey(keyCode) ? mCallbacks.onMediaButtonDown(keyCode, event) : false;
        }

        @Override
        public boolean onKeyLongPress(int keyCode, KeyEvent event) {
            return false;
        }

        @Override
        public boolean onKeyUp(int keyCode, KeyEvent event) {
            return isMediaKey(keyCode) ? mCallbacks.onMediaButtonUp(keyCode, event) : false;
        }

        @Override
        public boolean onKeyMultiple(int keyCode, int count, KeyEvent event) {
            return false;
        }
    };

    /**
     * @deprecated Use {@link MediaControllerCompat}.
     */
    @Deprecated
    public TransportMediator(Activity activity, TransportPerformer callbacks) {
        this(activity, null, callbacks);
    }

    /**
     * @deprecated Use {@link MediaControllerCompat}.
     */
    @Deprecated
    public TransportMediator(View view, TransportPerformer callbacks) {
        this(null, view, callbacks);
    }

    private TransportMediator(Activity activity, View view, TransportPerformer callbacks) {
        mContext = activity != null ? activity : view.getContext();
        mCallbacks = callbacks;
        mAudioManager = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE);
        mView = activity != null ? activity.getWindow().getDecorView() : view;
        mDispatcherState = mView.getKeyDispatcherState();
        if (Build.VERSION.SDK_INT >= 18) { // JellyBean MR2
            mController = new TransportMediatorJellybeanMR2(mContext, mAudioManager,
                    mView, mTransportKeyCallback);
        } else {
            mController = null;
        }
    }

    /**
     * Return the {@link android.media.RemoteControlClient} associated with this transport.
     * This returns a generic Object since the RemoteControlClient is not availble before
     * {@link android.os.Build.VERSION_CODES#ICE_CREAM_SANDWICH}.  Further, this class
     * will not use RemoteControlClient in its implementation until
     * {@link android.os.Build.VERSION_CODES#JELLY_BEAN_MR2}.  You should always check for
     * null here and not do anything with the RemoteControlClient if none is given; this
     * way you don't need to worry about the current platform API version.
     *
     * <p>Note that this class takes possession of the
     * {@link android.media.RemoteControlClient.OnGetPlaybackPositionListener} and
     * {@link android.media.RemoteControlClient.OnPlaybackPositionUpdateListener} callbacks;
     * you will interact with these through
     * {@link TransportPerformer#onGetCurrentPosition() TransportPerformer.onGetCurrentPosition} and
     * {@link TransportPerformer#onSeekTo TransportPerformer.onSeekTo}, respectively.</p>
     *
     * @deprecated Use {@link MediaControllerCompat}.
     */
    @Deprecated
    public Object getRemoteControlClient() {
        return mController != null ? mController.getRemoteControlClient() : null;
    }

    /**
     * Must call from {@link Activity#dispatchKeyEvent Activity.dispatchKeyEvent} to give
     * the transport an opportunity to intercept media keys.  Any such keys will show up
     * in {@link TransportPerformer}.
     * @param event
     *
     * @deprecated Use {@link MediaControllerCompat#dispatchMediaButtonEvent}.
     */
    @Deprecated
    public boolean dispatchKeyEvent(KeyEvent event) {
        return event.dispatch(mKeyEventCallback, (KeyEvent.DispatcherState) mDispatcherState, this);
    }

    /**
     * Start listening to changes in playback state.
     *
     * @deprecated Use
     *         {@link MediaControllerCompat#registerCallback(MediaControllerCompat.Callback)}.
     */
    @Deprecated
    @Override
    public void registerStateListener(TransportStateListener listener) {
        mListeners.add(listener);
    }

    /**
     * Stop listening to changes in playback state.
     *
     * @deprecated Use
     *         {@link MediaControllerCompat#unregisterCallback(MediaControllerCompat.Callback)}.
     */
    @Deprecated
    @Override
    public void unregisterStateListener(TransportStateListener listener) {
        mListeners.remove(listener);
    }

    private TransportStateListener[] getListeners() {
        if (mListeners.size() <= 0) {
            return null;
        }
        TransportStateListener listeners[] = new TransportStateListener[mListeners.size()];
        mListeners.toArray(listeners);
        return listeners;
    }

    private void reportPlayingChanged() {
        TransportStateListener[] listeners = getListeners();
        if (listeners != null) {
            for (TransportStateListener listener : listeners) {
                listener.onPlayingChanged(this);
            }
        }
    }

    private void reportTransportControlsChanged() {
        TransportStateListener[] listeners = getListeners();
        if (listeners != null) {
            for (TransportStateListener listener : listeners) {
                listener.onTransportControlsChanged(this);
            }
        }
    }

    private void pushControllerState() {
        if (mController != null) {
            mController.refreshState(mCallbacks.onIsPlaying(),
                    mCallbacks.onGetCurrentPosition(),
                    mCallbacks.onGetTransportControlFlags());
        }
    }

    /**
     * @deprecated Not needed when you use {@link MediaControllerCompat}.
     */
    @Deprecated
    public void refreshState() {
        pushControllerState();
        reportPlayingChanged();
        reportTransportControlsChanged();
    }

    /**
     * Move the controller into the playing state.  This updates the remote control
     * client to indicate it is playing, and takes audio focus for the app.
     *
     * @deprecated Use {@link MediaControllerCompat.TransportControls#play}.
     */
    @Deprecated
    @Override
    public void startPlaying() {
        if (mController != null) {
            mController.startPlaying();
        }
        mCallbacks.onStart();
        pushControllerState();
        reportPlayingChanged();
    }

    /**
     * Move the controller into the paused state.  This updates the remote control
     * client to indicate it is paused, but keeps audio focus.
     *
     * @deprecated Use {@link MediaControllerCompat.TransportControls#pause}.
     */
    @Deprecated
    @Override
    public void pausePlaying() {
        if (mController != null) {
            mController.pausePlaying();
        }
        mCallbacks.onPause();
        pushControllerState();
        reportPlayingChanged();
    }

    /**
     * Move the controller into the stopped state.  This updates the remote control
     * client to indicate it is stopped, and removes audio focus from the app.
     *
     * @deprecated Use {@link MediaControllerCompat.TransportControls#stop}.
     */
    @Deprecated
    @Override
    public void stopPlaying() {
        if (mController != null) {
            mController.stopPlaying();
        }
        mCallbacks.onStop();
        pushControllerState();
        reportPlayingChanged();
    }

    /**
     * Retrieve the total duration of the media stream, in milliseconds.
     *
     * @deprecated Use {@link MediaMetadataCompat#METADATA_KEY_DURATION}.
     */
    @Deprecated
    @Override
    public long getDuration() {
        return mCallbacks.onGetDuration();
    }

    /**
     * Retrieve the current playback location in the media stream, in milliseconds.
     *
     * @deprecated Use {@link PlaybackStateCompat#getPosition} and
     *         {@link PlaybackStateCompat#getLastPositionUpdateTime}.
     */
    @Deprecated
    @Override
    public long getCurrentPosition() {
        return mCallbacks.onGetCurrentPosition();
    }

    /**
     * Move to a new location in the media stream.
     * @param pos Position to move to, in milliseconds.
     *
     * @deprecated Use {@link MediaControllerCompat.TransportControls#seekTo}.
     */
    @Deprecated
    @Override
    public void seekTo(long pos) {
        mCallbacks.onSeekTo(pos);
    }

    /**
     * Return whether the player is currently playing its stream.
     *
     * @deprecated Use {@link PlaybackStateCompat#getState}.
     */
    @Deprecated
    @Override
    public boolean isPlaying() {
        return mCallbacks.onIsPlaying();
    }

    /**
     * Retrieve amount, in percentage (0-100), that the media stream has been buffered
     * on to the local device.  Return 100 if the stream is always local.
     *
     * @deprecated Use {@link PlaybackStateCompat#getBufferedPosition} and
     *         {@link MediaMetadataCompat#METADATA_KEY_DURATION}.
     */
    @Deprecated
    @Override
    public int getBufferPercentage() {
        return mCallbacks.onGetBufferPercentage();
    }

    /**
     * Retrieve the flags for the media transport control buttons that this transport supports.
     * Result is a combination of the following flags:
     *      {@link #FLAG_KEY_MEDIA_PREVIOUS},
     *      {@link #FLAG_KEY_MEDIA_REWIND},
     *      {@link #FLAG_KEY_MEDIA_PLAY},
     *      {@link #FLAG_KEY_MEDIA_PLAY_PAUSE},
     *      {@link #FLAG_KEY_MEDIA_PAUSE},
     *      {@link #FLAG_KEY_MEDIA_STOP},
     *      {@link #FLAG_KEY_MEDIA_FAST_FORWARD},
     *      {@link #FLAG_KEY_MEDIA_NEXT}
     *
     * @deprecated Use {@link PlaybackStateCompat#getActions}.
     */
    @Deprecated
    @Override
    public int getTransportControlFlags() {
        return mCallbacks.onGetTransportControlFlags();
    }

    /**
     * Optionally call when no longer using the TransportController.  Its resources
     * will also be automatically cleaned up when your activity/view is detached from
     * its window, so you don't normally need to call this explicitly.
     *
     * @deprecated Not needed when you use {@link MediaControllerCompat}.
     */
    @Deprecated
    public void destroy() {
        mController.destroy();
    }
}
