package it.rainbowbreeze.picama.logic.storage;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import it.rainbowbreeze.picama.common.Bag;
import it.rainbowbreeze.picama.common.ILogFacility;

/**
 * Created by alfredomorresi on 01/12/14.
 */
public class DropboxCloudProvider extends BaseCloudProvider {
    private static final String LOG_TAG = DropboxCloudProvider.class.getSimpleName();

    private final ILogFacility mLogFacility;
    private DropboxAPI<AndroidAuthSession> mDBApi;
    final static private Session.AccessType ACCESS_TYPE = Session.AccessType.DROPBOX;

    public DropboxCloudProvider(ILogFacility logFacility) {
        mLogFacility = logFacility;
    }

    private void initDropboxApi() {
        // And later in some initialization function:
        AppKeyPair appKeys = new AppKeyPair(Bag.DROPBOX_APP_KEY, Bag.DROPBOX_APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeys, ACCESS_TYPE);
        mDBApi = new DropboxAPI<AndroidAuthSession>(session);
    }

    public void uploadFile() throws FileNotFoundException, DropboxException {
        File file = new File("working-draft.txt");
        FileInputStream inputStream = new FileInputStream(file);
        DropboxAPI.Entry response = mDBApi.putFile("/magnum-opus.txt", inputStream,
                file.length(), null, null);
        mLogFacility.v(LOG_TAG, "The uploaded file's rev is: " + response.rev);
    }
}
