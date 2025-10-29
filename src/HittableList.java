import java.util.ArrayList;
import java.util.Optional;

final class HittableList implements Hittable {
    final ArrayList<Hittable> objects
            = new ArrayList<>();
    private AABB boundingBox;

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
        boundingBox = new AABB(boundingBox, object.boundingBox());
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
    public AABB boundingBox() {
        return boundingBox;
    }

    @Override
    public String toString() {
        return objects.toString();
    }
}