package it.rainbowbreeze.picama.logic.storage;

import android.text.TextUtils;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import it.rainbowbreeze.picama.common.ILogFacility;

/**
 * See: http://stackoverflow.com/questions/25893030/download-binary-fie-from-okhttp
 *
 * To use Picasso, instead of OkHttp, see:
 *  https://github.com/square/picasso/issues/227
 *  http://stackoverflow.com/questions/18603190/picasso-never-caches-to-disk-on-emulator
 *  https://gist.github.com/fada21/10655652
 *
 * Created by alfredomorresi on 01/12/14.
 */
public class FileDownloaderHelper {
    private static final String LOG_TAG = FileDownloaderHelper.class.getSimpleName();

    private final ILogFacility mLogFacility;
    private final OkHttpClient mOkHttpClient;

    public FileDownloaderHelper(ILogFacility logFacility) {
        mLogFacility = logFacility;
        mOkHttpClient = new OkHttpClient();
    }

    public boolean saveUrlToFile(File destinationFile, String url) {
        if (TextUtils.isEmpty(url)) {
            mLogFacility.i(LOG_TAG, "Url to download is null, aborting");
        }
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response;
        try {
            response = mOkHttpClient.newCall(request).execute();
        } catch (IOException e) {
            mLogFacility.e(LOG_TAG, "Cannot download requested file", e);
            return false;
        }
        InputStream is = response.body().byteStream();

        BufferedInputStream input = new BufferedInputStream(is);
        OutputStream output = null;
        try {
            output = new FileOutputStream(destinationFile);
        } catch (FileNotFoundException e) {
            mLogFacility.e(LOG_TAG, "Cannot create output file " + destinationFile.getAbsolutePath(), e);
            return false;
        }

        try {
            byte[] data = new byte[1024];
            int count;

            while ((count = input.read(data)) != -1) {
                output.write(data, 0, count);
            }
        } catch (IOException e) {
            mLogFacility.e(LOG_TAG, "Cannot save requested file " + destinationFile.getAbsolutePath(), e);
        } finally {
            try {
                output.flush();
                output.close();
                input.close();
            } catch (IOException e) {
                mLogFacility.e(LOG_TAG, "Strange errors closing stuff", e);
            }
        }

        return true;
    }
}
