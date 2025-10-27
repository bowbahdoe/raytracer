import java.util.Optional;

record Sphere(
        @Point Vec3 center,
        double radius,
        Material material
) implements Hittable {
    Sphere(
            @Point Vec3 center,
            double radius,
            Material material
    ) {
        this.center = center;
        this.radius = Math.max(0, radius);
        this.material = material;
    }

    @Override
    public Optional<HitRecord> hit(
            Ray r,
            Interval rayT
    ) {
        var oc = center.minus(r.origin());
        var a = r.direction().lengthSquared();
        var h = r.direction().dotProduct(oc);
        var c = oc.lengthSquared() - radius * radius;
        var discriminant = h * h - a * c;
        if (discriminant < 0) {
            return Optional.empty();
        }

        var sqrtd = Math.sqrt(discriminant);

        var root = (h - sqrtd) / a;
        if (!rayT.surrounds(root)) {
            root = (h + sqrtd) / a;
            if (!rayT.surrounds(root)) {
                return Optional.empty();
            }
        }

        var t = root;
        var p = r.at(t);
        var normal = (p.minus(center)).divide(radius);
        var outwardNormal = p.minus(center).divide(radius);
        return Optional.of(new HitRecord(
                p,
                normal,
                material,
                t,
                false
        ).withFaceNormal(r, outwardNormal));
    }
}