package rowley.androidarchitectureexample.core.dagger;

/**
 * Singleton class to be created by the Application and that other classes in the app will use
 * to request injection from the ApplicationComponent. This class should be instantiated in
 * the Application onCreate
 */
public class DaggerInjector {
    private static DaggerInjector instance;
    private static Object keyLock = new Object();

    private ApplicationComponent applicationComponent;

    public static DaggerInjector getInstance() {
        if(instance == null) {
            synchronized (keyLock) {
                if(instance == null) {
                    instance = new DaggerInjector();
                }
            }
        }

        return instance;
    }

    private DaggerInjector() {}

    public void setUpComponent(ApplicationModule applicationModule) {
        applicationComponent = DaggerApplicationComponent.builder().applicationModule(applicationModule).build();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
