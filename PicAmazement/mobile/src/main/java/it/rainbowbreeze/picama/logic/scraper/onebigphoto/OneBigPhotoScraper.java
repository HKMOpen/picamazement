package it.rainbowbreeze.picama.logic.scraper.onebigphoto;

import android.text.TextUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.rainbowbreeze.picama.common.ILogFacility;
import it.rainbowbreeze.picama.domain.AmazingPicture;
import it.rainbowbreeze.picama.logic.scraper.BasePictureScraper;

/**
 * http://onebigphoto.com/
 * http://onebigphoto.com/magical-rainbow-mountains-of-china/
 *
 * Now, OkHttp is the way to go - even Android itself, starting with version 4.4, uses OkHttp instead of old HttpUrlConnection. OkHttp is a wrapper around HttpUrlConnection, and it does all the networking.
 *
 * Created by alfredomorresi on 01/11/14.
 */
public class OneBigPhotoScraper extends BasePictureScraper<OneBigPhotoScraperConfig> {
    private static final String LOG_TAG = OneBigPhotoScraper.class.getSimpleName();

    private final ILogFacility mLogFacility;

    public OneBigPhotoScraper(
            ILogFacility logFacility,
            OneBigPhotoScraperConfig config) {
        super(config);

        mLogFacility = logFacility;
        mLogFacility.v(LOG_TAG, "Initializing...");
    }

    @Override
    public List<AmazingPicture> getNewPictures() {
        ArrayList<AmazingPicture> pictures = new ArrayList<AmazingPicture>();

        try {
            Document doc = Jsoup.connect("http://onebigphoto.com/").get();
            Elements postElements = doc.getElementsByClass("post");
            for (Element postElement : postElements) {
                Element element = postElement.getElementsByTag("img").first();
                String url = element.attr("src");
                String title = element.attr("alt");

                AmazingPicture pic = new AmazingPicture();
                pic
                        .setUrl(sanitizeUrl(url))
                        .setTitle(title)
                        .setDesc("")
                        .setSource(getSourceName())
                        .setAuthor("")
                        .setDate(new Date());
                pictures.add(pic);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return pictures;
    }

    @Override
    public String getSourceName() {
        return "OneBigPhoto";
    }

    protected String sanitizeUrl(String sourceUrl) {
        if (TextUtils.isEmpty(sourceUrl)) return sourceUrl;
        // http://onebigphoto.com/wp-content/themes/onebigphotoNEW/timthumb.php?src=http://onebigphoto.com/uploads/2015/03/siberian-husky-walking-on-a-frozen-lake.jpg&w=280&h=270
        final String begin = "timthumb.php?src=";
        int posIni = sourceUrl.indexOf(begin);
        if (-1 == posIni) return sourceUrl;
        posIni = posIni + begin.length();
        int posEnd = sourceUrl.indexOf("&");
        if (-1 == posEnd)
            return sourceUrl.substring(posIni);
        else
            return sourceUrl.substring(posIni, posEnd);
    }
}
