package it.rainbowbreeze.picama.logic.storage;

import java.io.File;
import java.io.FileInputStream;

import it.rainbowbreeze.picama.common.ILogFacility;

/**
 * Created by alfredomorresi on 01/12/14.
 */
public class DropboxCloudProvider extends BaseCloudProvider {
    private static final String LOG_TAG = DropboxCloudProvider.class.getSimpleName();

    /*
    // In the class declaration section:
    private DropboxAPI<AndroidAuthSession> mDBApi;
    private final ILogFacility mLogFacility;

    public DropboxCloudProvider(ILogFacility logFacility) {
        mLogFacility = logFacility;
    }

    private void initDropboxApi() {
        // And later in some initialization function:
        AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeys, ACCESS_TYPE);
        mDBApi = new DropboxAPI<AndroidAuthSession>(session);
    }

    public void uploadFile() {
        File file = new File("working-draft.txt");
        FileInputStream inputStream = new FileInputStream(file);
        Entry response = mDBApi.putFile("/magnum-opus.txt", inputStream,
                file.length(), null, null);
        mLogFacility.v(LOG_TAG, "The uploaded file's rev is: " + response.rev);
    }
    */
}
