import java.util.Optional;

public class SphereList implements Hittable {
    private final Sphere[] spheres;

    SphereList(Sphere[] spheres) {
        this.spheres = spheres;
    }

    @Override
    public HitRecord hit(Ray r, Interval rayT) {
        HitRecord rec = null;
        double closestSoFar = rayT.max();

        for (var object : spheres) {
            var hit = object.hit(r, new Interval(rayT.min(), closestSoFar));
            if (hit != null) {
                closestSoFar = hit.t();
                rec = hit;
            }
        }

        return rec;
    }
}
