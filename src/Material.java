import java.util.Optional;

interface Material {
    ScatteredRay scatter(Ray r_in, HitRecord rec);
}
