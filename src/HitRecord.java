value record HitRecord(
        @Point Vec3 p,
        Vec3 normal,
        Material material,
        double t,
        boolean frontFace
) {
    HitRecord withFaceNormal(
            Ray r,
            Vec3 outwardNormal
    ) {
        // Sets the hit record normal vector.
        // NOTE: the parameter `outward_normal` is assumed to have unit length.
        var frontFace = r.direction().dotProduct(outwardNormal) < 0;
        var normal = frontFace ? outwardNormal : outwardNormal.multiply(-1);
        return new HitRecord(
                p,
                normal,
                material,
                t,
                frontFace
        );
    }
}