import java.util.Optional;

value class Dialectric implements Material {
    private double refractionIndex;

    Dialectric(double refractionIndex) {
        this.refractionIndex = refractionIndex;
    }

    @Override
    public Optional<ScatteredRay> scatter(Ray r_in, HitRecord rec) {
        var attenuation = new Vec3(1.0, 1.0, 1.0);
        double ri = rec.frontFace() ? (1.0/refractionIndex) : refractionIndex;

        var unit_direction = r_in.direction().unitVector();
        var refracted = Vec3.refract(unit_direction, rec.normal(), ri);

        double cos_theta = Math.min(unit_direction.multiply(-1).dotProduct(rec.normal()), 1.0);
        double sin_theta = Math.sqrt(1.0 - cos_theta*cos_theta);

        var cannot_refract = ri * sin_theta > 1.0;
        Vec3 direction;
        if (cannot_refract || reflectance(cos_theta, ri) > Math.random())
            direction = Vec3.reflect(unit_direction, rec.normal());
        else
            direction = Vec3.refract(unit_direction, rec.normal(), ri);

        var scattered = new Ray(rec.p(), direction);
        return Optional.of(new ScatteredRay(
                attenuation,
                scattered
        ));
    }

    private static double reflectance(double cosine, double refraction_index) {
        // Use Schlick's approximation for reflectance.
        var r0 = (1 - refraction_index) / (1 + refraction_index);
        r0 = r0*r0;
        return r0 + (1-r0)*Math.pow((1 - cosine),5);
    }
}
