package chapter3;

/**
 * SafeListener
 * <p/>
 * Using a factory method to prevent the this reference from escaping during construction
 *
 */
public class SafeListener {
    private final EventListener listener;

    private SafeListener() {
        listener = new EventListener() {
            @Override
            public void onEvent(Event event) {
                doSomething(event);
            }
        };
    }

    public static SafeListener newInstance(EventSource source) {
        SafeListener safe = new SafeListener();
        source.registerListener(safe.listener);
        return safe;
    }
    void doSomething(Event event) {
    }

    interface EventSource {
        void registerListener(EventListener listener);
    }

    interface EventListener {
        void onEvent(Event event);
    }

    interface Event {
    }
}
