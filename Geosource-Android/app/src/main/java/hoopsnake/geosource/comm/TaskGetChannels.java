package hoopsnake.geosource.comm;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import ServerClientShared.Commands;
import hoopsnake.geosource.Channel;
import hoopsnake.geosource.ChannelSelectionActivity;
import hoopsnake.geosource.R;

import static junit.framework.Assert.assertNotNull;

/**
* Created by wsv759 on 02/04/15.
 * IMPORTANT: Must be executed with execute(Boolean getPictureChannelsOnly).
*/
public class TaskGetChannels extends AsyncTask<Boolean, Void, SocketResult>
{
    private static final String LOG_TAG = "geosource comm";
    private ChannelSelectionActivity activity;

    public TaskGetChannels(ChannelSelectionActivity activity) {
        this.activity = activity;
    }

    @Override
    protected SocketResult doInBackground(Boolean... params) {
        boolean picturesOnly = params[0];
        assertNotNull(picturesOnly);

        SocketWrapper socketWrapper;
        ObjectOutputStream outStream; //wrapped stream to client
        ObjectInputStream inStream; //stream from client
        try //create socket
        {
            socketWrapper = new SocketWrapper();
            outStream = socketWrapper.getOut();
            inStream = socketWrapper.getIn();
        }
        catch(IOException e)
        {
            e.printStackTrace();

            return SocketResult.FAILED_CONNECTION;
        }


        String[] channels;
        try
        {
            Log.i(LOG_TAG, "Attempting to get channels.");
            outStream.writeObject(Commands.IOCommand.GET_CHANNELS);
            outStream.flush();

            Log.i(LOG_TAG, "Retrieving reply...");
            channels = (String[]) inStream.readObject();

            if (channels == null)
                return SocketResult.FAILED_FORMATTING;
        }
        catch (IOException e)
        {
            e.printStackTrace();

            return SocketResult.UNKNOWN_ERROR;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();

            return SocketResult.CLASS_NOT_FOUND;
        } finally {
            socketWrapper.closeAll();
        }

        //TODO some of this code should maybe go on the ui thread.
        activity.setChannels(Channel.fromStringArray(channels));

        return SocketResult.SUCCESS;
    }

    @Override
    protected void onPostExecute(SocketResult result)
    {
        result.makeToastAndLog(
                activity.getString(R.string.downloaded_channel_choices),
                activity.getString(R.string.failed_to_download_channel_choices),
                activity,
                LOG_TAG);

        if (result.equals(SocketResult.SUCCESS))
            activity.onUpdated();
    }
}
