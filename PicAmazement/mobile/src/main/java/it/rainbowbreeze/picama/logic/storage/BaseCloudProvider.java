package it.rainbowbreeze.picama.logic.storage;

import java.io.File;

/**
 * Created by alfredomorresi on 01/12/14.
 */
public class BaseCloudProvider {

    public boolean isEnable() {
        return false;
    }

    /**
     * Transfer the given files to the cloud storage
     * @param dataFile
     */
    public boolean save(File dataFile) {
        return false;
    }
}
