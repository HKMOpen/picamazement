package it.rainbowbreeze.picama.logic.action;

import android.content.Context;

import it.rainbowbreeze.picama.common.ILogFacility;

/**
 * Created by alfredomorresi on 15/11/14.
 */
public abstract class BasePictureAction extends ActionsManager.BaseAction {
    protected final Context mAppContext;

    public BasePictureAction(Context appContext, ILogFacility logFacility, ActionsManager actionsManager) {
        super(logFacility, actionsManager);
        mAppContext = appContext;
    }

    @Override
    protected boolean isCoreDataValid() {
        return null != mAppContext && super.isCoreDataValid();
    }
}
