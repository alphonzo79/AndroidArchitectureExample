package rowley.androidarchitectureexample.core.dagger;

import javax.inject.Singleton;

import dagger.Component;
import rowley.androidarchitectureexample.landing.viewcontrollers.activities.LandingActivity;
import rowley.androidarchitectureexample.landing.viewcontrollers.fragments.LandingFragment;

/**
 * Component for Dagger to use for injecting into the app from the ApplicationModule
 */
@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {
    void inject(LandingActivity landingActivity);
    void inject(LandingFragment landingFragment);
}
