import java.util.Optional;

interface Hittable {
    HitRecord hit(
            Ray r,
            Interval rayT
    );
}