package it.rainbowbreeze.picama.logic;

/**
 * Created by alfredomorresi on 01/12/14.
 */
public abstract class BaseResult {
    public static final int SUCCESS = 0;
    public static final int NOT_SUCCESS = 1;

    protected final int mReturnCode;

    protected BaseResult(int result) {
        mReturnCode = result;
    }

    public boolean isSuccess() {
        return mReturnCode == SUCCESS;
    }
    public boolean isNotSuccess() {
        return mReturnCode != SUCCESS;
    }

    public int getReturnCode() {
        return mReturnCode;
    }
}
