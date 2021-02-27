package chapter10;

import annotations.GuardedBy;
import chapter4.Point;

import java.util.HashSet;
import java.util.Set;

/**
 * CooperatingDeadlock
 *
 * <p>Lock-ordering deadlock between cooperating objects
 */
public class CooperatingDeadlock {
  // Warning: deadlock-prone!
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

    public synchronized void setLocation(Point location) {
      this.location = location;
      if (location.equals(destination)) {
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

    public Image getImage() {
      Set<Taxi> copy;
      synchronized (this) {
        copy = new HashSet<>(taxis);
      }

      Image image = new Image();
      for (Taxi taxi : copy) {
        image.drawMarker(taxi.getLocation());
      }
      return image;
    }
  }

  class Image {
    public void drawMarker(Point p) {}
  }
}
