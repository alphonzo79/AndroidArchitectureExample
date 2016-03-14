package rowley.androidarchitectureexample;

import android.test.ActivityInstrumentationTestCase2;

import rowley.androidarchitectureexample.core.dagger.ApplicationModule;
import rowley.androidarchitectureexample.core.dagger.DaggerInjector;
import rowley.androidarchitectureexample.core.dagger.DaggerTestApplicationComponent;
import rowley.androidarchitectureexample.core.dagger.TestApplicationModule;
import rowley.androidarchitectureexample.testable.viewcontroller.activity.SimpleTestActivity;

/**
 * Base test class for instrumented Activity tests. This sets up dagger in a central place
 */
public abstract class SimpleActivityTestBase extends ActivityInstrumentationTestCase2<SimpleTestActivity> {
    public SimpleActivityTestBase() {
        super(SimpleTestActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        DaggerInjector injector = DaggerInjector.getInstance();
        TestApplicationModule testModule = new TestApplicationModule(new ApplicationModule((ExampleApplication) getActivity().getApplication()));
        injector.setApplicationComponent(DaggerTestApplicationComponent.builder().testApplicationModule(testModule).build());
    }
}