import java.util.List;
import java.util.Optional;

final class BVHNode implements Hittable {
    private Hittable left;
    private Hittable right;
    private AABB boundingBox;

    BVHNode(HittableList list) {
        this(list.objects, 0, list.objects.size());
    }

    BVHNode(List<Hittable> objects, int start, int end) {

    }

    @Override
    public Optional<HitRecord> hit(Ray r, Interval ray_t, double time) {
        if (!boundingBox.hit(r, ray_t)) {
            return Optional.empty();
        }

        var hit_left = left.hit(r, ray_t, time);
        var hit_right = right.hit(
                r,
                new Interval(ray_t.min(),
                        hit_left.isPresent() ? time : ray_t.max()
                ),
                time
        );


        if (hit_left.isPresent()) {
            return hit_left;
        }
        else {
            return hit_right;
        }
    }

    @Override
    public AABB boundingBox() {
        return boundingBox;
    }
}
