import java.util.Optional;

class Metal implements Material {
    @Color Vec3 albedo;
    double fuzz;

    Metal(@Color Vec3 albedo, double fuzz) {
        this.albedo = albedo;
        this.fuzz = fuzz < 1 ? fuzz : 1;
    }

    @Override
    public Optional<ScatteredRay> scatter(Ray r_in, HitRecord rec) {
        var reflected = Vec3.reflect(r_in.direction(), rec.normal());
        reflected = reflected.unitVector().plus(Vec3.randomUnitVector().multiply(fuzz));
        var scattered = new Ray(rec.p(), reflected);

        if (scattered.direction().dotProduct(rec.normal()) > 0) {
            return Optional.of(new ScatteredRay(
                    albedo,
                    scattered
            ));
        }
        else {
            return Optional.empty();
        }

    }
}
