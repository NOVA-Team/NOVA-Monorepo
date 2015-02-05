package nova.core.event;

/**
 * Created by Stan on 5/02/2015.
 */
public class TestEventListener implements EventListener<TestEvent> {
    private final String name;
    private final boolean cancels;

    public TestEventListener(String name) {
        this.name = name;
        this.cancels = false;
    }

    public TestEventListener(String name, boolean cancels) {
        this.name = name;
        this.cancels = cancels;
    }

    @Override
    public void onEvent(TestEvent event) {
        event.append(name);

        if (cancels)
            event.cancel();
    }
}
