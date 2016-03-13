package rowley.androidarchitectureexample.nycdemographic.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

/**
 * Base loader to handle some common management tasks
 */
public abstract class BaseLoader<D> extends AsyncTaskLoader<D> {
    protected D result;

    public BaseLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        if(result != null)
            deliverResult(result);

        if(takeContentChanged() || result == null)
            forceLoad();
    }

    @Override
    protected void onReset() {
        stopLoading();

        if (result != null)
        {
            result=null;
        }
    }

    @Override
    public void deliverResult(D data) {
        result = data;
        if(isReset()){
            if(result != null){
                result = null;
                return;
            }
        }
        if(isStarted())
            super.deliverResult(result);
    }
}
