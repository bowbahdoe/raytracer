import java.util.Optional;

value class Lambertian implements Material {
    @Color Vec3 albedo;

    Lambertian(@Color Vec3 albedo) {
        this.albedo = albedo;
    }

    @Override
    public ScatteredRay scatter(Ray r_in, HitRecord rec) {
        var scatterDirection = rec.normal().plus(Vec3.randomUnitVector());

        if (scatterDirection.nearZero()) {
            scatterDirection = rec.normal();
        }
        return new ScatteredRay(
                albedo,
                new Ray(rec.p(), scatterDirection)
        );
    }
}
