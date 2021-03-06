package chapter10;

import annotations.GuardedBy;
import annotations.ThreadSafe;
import chapter4.Point;

import java.util.HashSet;
import java.util.Set;

/**
 * CooperatingNoDeadlock
 *
 * <p>Using open calls to avoiding deadlock between cooperating objects
 */
public class CooperatingNoDeadlock {

  @ThreadSafe
  class Taxi {
    private final Dispatcher dispatcher;

    @GuardedBy("this")
    private Point location;

    @GuardedBy("this")
    private Point destination;

    public Taxi(Dispatcher dispatcher) {
      this.dispatcher = dispatcher;
    }

    public synchronized Point getLocation() {
      return location;
    }

    public void setLocation(Point location) {
      boolean reachedDestination;
      synchronized (this) {
        this.location = location;
        reachedDestination = location.equals(destination);
      }

      if (reachedDestination) {
        dispatcher.notifyAvailable(this);
      }
    }

    public synchronized Point getDestination() {
      return destination;
    }

    public synchronized void setDestination(Point destination) {
      this.destination = destination;
    }
  }

  @ThreadSafe
  class Dispatcher {
    @GuardedBy("this")
    private final Set<Taxi> taxis;

    @GuardedBy("this")
    private final Set<Taxi> availableTaxis;

    public Dispatcher() {
      taxis = new HashSet<>();
      availableTaxis = new HashSet<>();
    }

    public synchronized void notifyAvailable(Taxi taxi) {
      availableTaxis.add(taxi);
    }

    public synchronized Image getImage() {
      Image image = new Image();
      for (Taxi taxi : taxis) {
        image.drawMarker(taxi.getLocation());
      }
      return image;
    }
  }

  class Image {
    public void drawMarker(Point p) {}
  }
}
