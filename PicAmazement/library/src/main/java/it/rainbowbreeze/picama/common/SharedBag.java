package it.rainbowbreeze.picama.common;

/**
 * Created by alfredomorresi on 19/10/14.
 */
public abstract class SharedBag {
    public final static String APP_NAME_LOG = "PicAmazement";


    public final static String WEAR_PATH_AMAZINGPICTURE = "/AmazingPicture";
    public final static String WEAR_PATH_REMOVEPICTURE = "/RemovePicture";
    public final static String WEAR_PATH_SAVEPICTURE = "/SavePicture";
    public final static String WEAR_PATH_SIMPLEMESSAGE = "/SimpleMessage";

    public final static String WEAR_DATAMAPITEM_PICTUREID = "PictureId";
    public static final String WEAR_DATAMAPITEM_TIMESTAMP = "Timestamp"; // Avoids caching

    // Also registered for the service
    public final static String INTENT_ACTION_REMOVEPICTURE = "it.rainbowbreeze.picama.Action.Picture.Remove";
    public static final String INTENT_ACTION_SAVEPICTURE = "it.rainbowbreeze.picama.Action.Picture.Save";
    public final static String INTENT_EXTRA_PICTUREID = "Extra.PictureId";

    public static final long ID_NOT_SET = -1;
}
