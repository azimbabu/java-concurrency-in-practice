package chapter4;

import annotations.GuardedBy;
import annotations.ThreadSafe;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * MonitorVehicleTracker
 * <p/>
 * Monitor-based vehicle tracker implementation
 *
 */
@ThreadSafe
public class MonitorVehicleTracker {
    @GuardedBy("this") private final Map<String, MutablePoint> locations;

    public MonitorVehicleTracker(Map<String, MutablePoint> locations) {
        this.locations = deepCopy(locations);
    }

    public synchronized Map<String, MutablePoint> getLocations() {
        return deepCopy(locations);
    }

    public synchronized MutablePoint getLocation(String id) {
        MutablePoint point = locations.get(id);
        return point == null ? null : new MutablePoint(point);
    }

    public synchronized void setLocation(String id, int x, int y) {
        MutablePoint point = locations.get(id);
        if (point == null) {
            throw new IllegalArgumentException("No such ID: " + id);
        }
        point.x = x;
        point.y = y;
    }

    private Map<String, MutablePoint> deepCopy(Map<String, MutablePoint> locations) {
        Map<String, MutablePoint> result = new HashMap<>();
        for (String id : locations.keySet()) {
            result.put(id, new MutablePoint(locations.get(id)));
        }
        return Collections.unmodifiableMap(result);
    }
}
