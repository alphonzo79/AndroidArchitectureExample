package rowley.androidarchitectureexample;

import android.test.ActivityInstrumentationTestCase2;

import rowley.androidarchitectureexample.core.dagger.ApplicationModule;
import rowley.androidarchitectureexample.core.dagger.DaggerInjector;
import rowley.androidarchitectureexample.core.dagger.DaggerTestApplicationComponent;
import rowley.androidarchitectureexample.core.dagger.TestApplicationModule;
import rowley.androidarchitectureexample.landing.viewcontrollers.activities.LandingActivity;

/**
 * Base test class for instrumented Activity tests. This sets up dagger in a central place
 */
public abstract class SimpleActivityTestBase extends ActivityInstrumentationTestCase2<LandingActivity> {
    public SimpleActivityTestBase() {
        super(LandingActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        DaggerInjector injector = DaggerInjector.getInstance();
        TestApplicationModule testModule = new TestApplicationModule(new ApplicationModule((ExampleApplication) getActivity().getApplication()));
        injector.setApplicationComponent(DaggerTestApplicationComponent.builder().testApplicationModule(testModule).build());
    }
}