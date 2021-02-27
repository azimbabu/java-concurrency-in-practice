package chapter3;

/**
 * ThisEscape
 * <p/>
 * Implicitly allowing the this reference to escape
 *
 */
public class ThisEscape {

    public ThisEscape(EventSource source) {
        source.registerListener(new EventListener() {
            @Override
            public void onEvent(Event event) {
                doSomething(event);
            }
        });
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
