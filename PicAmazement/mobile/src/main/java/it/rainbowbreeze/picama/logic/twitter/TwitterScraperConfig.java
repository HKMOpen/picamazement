package it.rainbowbreeze.picama.logic.twitter;

import java.util.ArrayList;
import java.util.List;

import it.rainbowbreeze.picama.logic.IPictureScraperConfig;

/**
 * Created by alfredomorresi on 01/11/14.
 */
public class TwitterScraperConfig extends IPictureScraperConfig {
    public List<String> getUserNames() {
        List<String> usernames = new ArrayList<String>();
        usernames.add("EarthBeauties");
        //usernames.add("_Paisajes_");
        usernames.add("HighFromAbove");
        usernames.add("HistoryInPics");
        usernames.add("planetepics");
        usernames.add("FotoSplendide");
        usernames.add("LuoghiDalMondo");
        usernames.add("FotoFavolose");
        usernames.add("FotoFavolose");

        return usernames;
    }
}
