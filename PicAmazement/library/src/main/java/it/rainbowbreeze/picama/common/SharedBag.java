package it.rainbowbreeze.picama.common;

/**
 * Created by alfredomorresi on 19/10/14.
 */
public abstract class SharedBag {
    public final static String APP_NAME_LOG = "PicAmazement";


    public final static String WEAR_DATAMAP_AMAZINGPICTURE = "/AmazingPicture";
    public final static String WEAR_MESSAGE_SIMPLE = "/SimpleMessage";

    // Also registered for the service
    public final static String INTENT_ACTION_REMOVEPICTURE = "it.rainbowbreeze.picama.DELETE_PICTURE";
    public static final String INTENT_ACTION_SAVEPICTURE = "it.rainbowbreeze.picama.SAVE_PICTURE";
    public final static String INTENT_EXTRA_PICTUREID = "RemovePicture";
}
