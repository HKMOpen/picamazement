package it.rainbowbreeze.picama.logic.storage;

import java.io.File;

/**
 * Created by alfredomorresi on 01/12/14.
 */
public abstract class BaseCloudProvider {

    /**
     * Returns if is possible to save files in the given cloud provider
     * @return
     */
    public abstract boolean isSavePossible();

    /**
     * Transfer the given file to the cloud storage
     * @param dataFile
     */
    public abstract boolean save(File dataFile);

    public abstract String getName();
}
