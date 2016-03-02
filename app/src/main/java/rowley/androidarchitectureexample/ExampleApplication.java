package rowley.androidarchitectureexample;

import android.app.Application;

import rowley.androidarchitectureexample.core.dagger.ApplicationModule;
import rowley.androidarchitectureexample.core.dagger.DaggerInjector;

/**
 * Application extension so we can set up Dagger
 */
public class ExampleApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        DaggerInjector injector = DaggerInjector.getInstance();
        ApplicationModule applicationModule = new ApplicationModule(this);
        injector.setUpComponent(applicationModule);
    }
}
