package rowley.androidarchitectureexample.core.dagger;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rowley.androidarchitectureexample.ExampleApplication;

/**
 * Main application module for injecting dependencies into the app.
 */
@Module
public class ApplicationModule {
    private ExampleApplication application;

    public ApplicationModule(ExampleApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public Context provideApplicationContext() {
        return application;
    }
}
