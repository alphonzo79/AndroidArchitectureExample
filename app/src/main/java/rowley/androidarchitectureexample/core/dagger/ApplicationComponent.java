package rowley.androidarchitectureexample.core.dagger;

import javax.inject.Singleton;

import dagger.Component;
import rowley.androidarchitectureexample.landing.viewcontrollers.activities.LandingActivity;

/**
 * Component for Dagger to use for injecting into the app from the ApplicationModule
 */
@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {
    void inject(LandingActivity landingActivity);
}
