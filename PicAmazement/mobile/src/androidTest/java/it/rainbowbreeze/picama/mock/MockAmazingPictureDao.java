package it.rainbowbreeze.picama.mock;

/**
 * Created by alfredomorresi on 26/12/14.
 */

import android.content.Context;

import it.rainbowbreeze.picama.common.ILogFacility;
import it.rainbowbreeze.picama.data.AmazingPictureDao;
import it.rainbowbreeze.picama.domain.AmazingPicture;

public class MockAmazingPictureDao extends AmazingPictureDao {
    AmazingPicture mPicture;

    public MockAmazingPictureDao(Context appContext, ILogFacility logFacility) {
        super(appContext, logFacility);
    }

    public void setPicture(AmazingPicture newValue) {
        mPicture = newValue;
    }

    @Override
    public AmazingPicture getById(long pictureId) {
        return mPicture;
    }
}
