import java.util.Optional;

interface Material {
    Optional<ScatteredRay> scatter(Ray r_in, HitRecord rec);

    AABB boundingBox();
}
