import java.util.Optional;

value record Sphere(
        Ray center,
        double radius,
        Material material,
        @Override AABB boundingBox
) implements Hittable {
    Sphere(
            Ray center,
            double radius,
            Material material,
            AABB boundingBox
    ) {
        this.center = center;
        this.radius = Math.max(0, radius);
        this.material = material;
        this.boundingBox = boundingBox;
    }

    Sphere(
            @Point Vec3 staticCenter,
            double radius,
            Material material
    ) {
        var rvec = new Vec3(radius, radius, radius);
        var bbox = new AABB(staticCenter.minus(rvec), staticCenter.plus(rvec));
        this(
                new Ray(staticCenter, new Vec3(0, 0, 0)),
                radius,
                material,
                bbox
        );
    }

    Sphere(
            @Point Vec3 center1,
            @Point Vec3 center2,
            double radius,
            Material material
    ) {
        var center = new Ray(center1, center2.minus(center1));
        var rvec = new Vec3(radius, radius, radius);
        var box1 = new AABB(center.at(0).minus(rvec), center.at(0).plus(rvec));
        var box2 = new AABB(center.at(1).minus(rvec), center.at(1).plus(rvec));
        var bbox = new AABB(box1, box2);
        this(
                center,
                radius,
                material,
                bbox
        );
    }

    @Override
    public Optional<HitRecord> hit(
            Ray r,
            Interval rayT,
            double time
    ) {
        var currentCenter = center.at(r.time());
        var oc = currentCenter.minus(r.origin());
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
        var outwardNormal = p.minus(currentCenter).divide(radius);
        return Optional.of(new HitRecord(
                p,
                outwardNormal,
                material,
                t,
                false
        ).withFaceNormal(r, outwardNormal));
    }
}