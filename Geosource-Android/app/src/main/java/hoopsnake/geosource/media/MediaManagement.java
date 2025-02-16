package hoopsnake.geosource.media;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by wsv759 on 16/02/15.
 * This class governs the methods to call Android's built-in media APIs: IMAGE, VIDEO, AUDIO
 * in particular.
 */
public class MediaManagement {

    /**
     * Any logcat messages from this class use this tag.
     */
    private static final String LOG_TAG = "geosource media";

    private static MediaRecorder mRecorder;
    private static MediaPlayer mPlayer;


    /**
     * The name of the directory to which to save all media files created by this app.
     */
    public static final String GEOSOURCE_MEDIA_DIR_NAME = "geosource_media_files";

    public enum MediaType {
        IMAGE,
        VIDEO,
        AUDIO
    }

    /**
     * @precond playingFilePath != null.
     * @postcond Start playing the audio stored at the given file.
     * @param playingFilePath the file to play.
     */
    public static void startPlaying(Uri playingFilePath) {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(playingFilePath.getPath());
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }


    public static void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }

    /**
     * start recording audio and save the resulting file
     * @param recordingFilePath the path to which the recording will save
     */
    public static void getAudioRecording(Uri recordingFilePath)
    {
        String mFileName = recordingFilePath.getPath();
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
    }

    public static void stopAudioRecording()
    {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

    /**
     * @param activity the activity from which to launch the new camera activity.
     * @param requestCodeForIntent the request code with which to launch the new camera activity, so activity can handle the result properly.
     * @param fileUriForNewImage the file in which to store the new image.
     * @precond activity and fileUriForNewImage are not null.
     * @postcond start Android's built-in Camera activity, allowing the user to take a video, and save it
     * to the provided file. The result of the camera is returned to activity.
     */
    public static void startCameraActivityForVideo(Activity activity, int requestCodeForIntent, Uri fileUriForNewImage)
    {
        startCameraActivityForMedia(activity, requestCodeForIntent, fileUriForNewImage, MediaType.VIDEO);
    }

    /**
     * @param activity the activity from which to launch the new camera activity.
     * @param requestCodeForIntent the request code with which to launch the new camera activity, so activity can handle the result properly.
     * @param fileUriForNewImage the file in which to store the new image.
     * @precond activity and fileUriForNewImage are not null.
     * @postcond start Android's built-in Camera activity, allowing the user to take a picture, and save it
     * to the provided file. The result of the camera is returned to activity.
     */
    public static void startCameraActivityForImage(Activity activity, int requestCodeForIntent, Uri fileUriForNewImage)
    {
        startCameraActivityForMedia(activity, requestCodeForIntent, fileUriForNewImage, MediaType.IMAGE);
    }

    /**
     * @param activity the activity from which to launch the new camera activity.
     * @param requestCodeForIntent the request code with which to launch the new camera activity, so activity can handle the result properly.
     * @param fileUriForNewMedia the file in which to store the new image.
     * @param mediaType the type of media to start a camera activity for.
     * @precond activity and fileUriForNewMedia and mediaType are not null. mediaType must be either IMAGE or VIDEO.
     * @postcond start Android's built-in Camera activity, allowing the user to take a picture or video, and save it
     * to the provided file. The result of the camera is returned to activity.
     */
    private static void startCameraActivityForMedia(Activity activity, int requestCodeForIntent, Uri fileUriForNewMedia, MediaType mediaType)
    {
        assertNotNull(activity);
        assertNotNull(fileUriForNewMedia);
        assertNotNull(mediaType);
        assertTrue(mediaType.equals(MediaType.IMAGE) || mediaType.equals(MediaType.VIDEO));

        // create Intent to take a picture and return control to the calling application
        Intent mediaIntent;
        if (mediaType.equals(MediaType.IMAGE))
            mediaIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        else {
            mediaIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            mediaIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1); // set the video image quality to high
        }

        mediaIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUriForNewMedia); // set the file to use.

        Log.i(LOG_TAG, "starting new camera activity. Media file will be stored in " + fileUriForNewMedia);
        // start the image capture Intent
        activity.startActivityForResult(mediaIntent, requestCodeForIntent);
    }

    /**
     * @precond none.
     * @postcond see return.
     * @return returns true if the device allows this app to create new external storage files for writing,
     *  or false otherwise.
     */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /** Create a file Uri for saving an image.
     * @precond none.
     * @postcond a new empty image file is created on the system, and its Uri is returned. If no new file
     * could be created, null is returned. */
    public static Uri getOutputImageFileUri(){
        return getOutputMediaFileUri(MediaType.IMAGE);
    }

    /** Create a file Uri for saving a video.
     * @precond none.
     * @postcond a new empty video file is created on the system, and its Uri is returned. If no new file
     * could be created, null is returned. */
    public static Uri getOutputVideoFileUri(){
        return getOutputMediaFileUri(MediaType.VIDEO);
    }

    /** Create a file Uri for saving an Audio file.
     * @precond none.
     * @postcond a new empty aidio mp3 file is created on the system, and its Uri is returned. If no new file
     * could be created, null is returned. */
    public static Uri getOutputAudioFileUri() { return getOutputMediaFileUri(MediaType.AUDIO);}

    /** Create a file Uri for saving an image or video.
     * @param type the type of file to create. This dictates the file name.
     * @precond type is not null.
     * @postcond a new empty file is created on the system, and its Uri is returned. If no new file
     * could be created, null is returned. */
    private static Uri getOutputMediaFileUri(MediaType type){
        assertNotNull(type);
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * @param type The type of media to be saved.
     * @precond type is not null. Currently we are not saving files to internal storage, so
     *  external storage must be writable (permissions & space required).
     * @postcond a new file is created on the device in geosource's media folder, with a filename
     *  governed by timestamp and file type.
     * @return The File object that is created.
     */
    private static File getOutputMediaFile(MediaType type){

        // Check that the SDCard is mounted. If it is, initialize an external file.
        if (isExternalStorageWritable()) {
            File mediaStorageDir;
            switch(type)
            {
                case IMAGE:
                    mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES), GEOSOURCE_MEDIA_DIR_NAME);
                    break;
                case VIDEO:
                    mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_MOVIES), GEOSOURCE_MEDIA_DIR_NAME);
                    break;
                case AUDIO:
                    mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_MUSIC), GEOSOURCE_MEDIA_DIR_NAME);
                    break;
                default:
                    throw new InvalidParameterException("media file type is invalid.");
            }


            // This location works best if you want the created images to be shared
            // between applications and persist after your app has been uninstalled.

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d(LOG_TAG, "failed to create directory at " + mediaStorageDir.getAbsolutePath() + ".");
                    return null;
                }
            }
            Log.i(LOG_TAG, "created media storage directory at " + mediaStorageDir.getAbsolutePath() + ".");

            // Create a media file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File mediaFile;
            switch (type)
            {
                case IMAGE:
                    mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                            "IMG_" + timeStamp + ".jpg");
                    break;
                case VIDEO:
                    mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                            "VID_" + timeStamp + ".mp4");
                    break;
                case AUDIO:
                    mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                            "AUD_" + timeStamp + ".mp3");
                    break;
//                    throw new RuntimeException("Audio files are unimplemented, sorry.");
                default:
                    throw new InvalidParameterException("media file type is invalid.");
            }

            Log.i(LOG_TAG, "created new media file at " + mediaFile.getAbsolutePath() + ".");
            return mediaFile;
        }
        else
        {
            Log.d(LOG_TAG, "failed to create media file. External storage is not writable.");
            //TODO consider using internal storage instead.
            return null;
        }
    }
}
