import java.util.ArrayList;
import java.util.Optional;

value class HittableList implements Hittable {
    private final ArrayList<Hittable> objects
            = new ArrayList<>();

    HittableList() {}

    HittableList(Hittable object) {
        super();
        add(object);
    }

    void clear() {
        objects.clear();
    }

    void add(Hittable object) {
        objects.add(object);
    }

    @Override
    public Optional<HitRecord> hit(Ray r, Interval rayT) {
        Optional<HitRecord> rec = Optional.empty();
        double closestSoFar = rayT.max();

        for (var object : objects) {
            var hit = object.hit(r, new Interval(rayT.min(), closestSoFar))
                    .orElse(null);
            if (hit != null) {
                closestSoFar = hit.t();
                rec = Optional.of(hit);
            }
        }

        return rec;
    }

    @Override
    public String toString() {
        return objects.toString();
    }
}