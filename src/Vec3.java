value record Vec3(
        double x,
        double y,
        double z
) {
    Vec3 plus(Vec3 v) {
        return new Vec3(
                x + v.x, y + v.y, z + v.z
        );
    }

    Vec3 minus(Vec3 v) {
        return new Vec3(
                x - v.x, y - v.y, z - v.z
        );
    }

    Vec3 multiply(double t) {
        return new Vec3(
                x * t, y * t, z * t
        );
    }

    Vec3 multiply(Vec3 v) {
        return new Vec3(
                x * v.x, y * v.y, z * v.z
        );
    }


    Vec3 divide(double t) {
        return multiply(1 / t);
    }

    double length() {
        return Math.sqrt(lengthSquared());
    }

    double lengthSquared() {
        return x * x + y * y + z * z;
    }

    double dotProduct(Vec3 v) {
        return x * v.x + y * v.y + z * v.z;
    }

    static Vec3 cross(Vec3 u, Vec3 v) {
        return new Vec3(
                u.y * v.z - u.z * v.y,
                u.z * v.x - u.x * v.z,
                u.x * v.y - u.y * v.x
        );
    }

    Vec3 crossProduct(Vec3 v) {
        return cross(this, v);
    }

    Vec3 unitVector() {
        return this.divide(length());
    }

    static Vec3 random() {
        return new Vec3(Math.random(), Math.random(), Math.random());
    }

    private static double randomDouble(double min, double max) {
        // Returns a random real in [min,max).
        return min + (max-min)*Math.random();
    }

    static Vec3 random(double min, double max) {
        return new Vec3(
                randomDouble(min, max),
                randomDouble(min, max),
                randomDouble(min, max)
        );
    }

    static Vec3 randomUnitVector() {
        while (true) {
            var p = Vec3.random(-1,1);
            var lensq = p.lengthSquared();
            if (1e-160 < lensq && lensq <= 1)
                return p.divide(Math.sqrt(lensq));
        }
    }

    static Vec3 randomOnHemisphere(Vec3 normal) {
        var on_unit_sphere = randomUnitVector();
        if (on_unit_sphere.dotProduct(normal) > 0.0) {
            // In the same hemisphere as the normal
            return on_unit_sphere;
        }
        else {
            return on_unit_sphere.multiply(-1);
        }
    }

    boolean nearZero() {
        // Return true if the vector is close to zero in all dimensions.
        var s = 1e-8;
        return (Math.abs(x) < s) && (Math.abs(y) < s) && (Math.abs(z) < s);
    }

    static Vec3 reflect(Vec3 v, Vec3 n) {
        return v.minus(n.multiply(v.dotProduct(n)).multiply(2));
    }

    static Vec3 refract(Vec3 uv, Vec3 n, double etai_over_etat) {
        var cos_theta = Math.min(uv.multiply(-1).dotProduct(n), 1.0);
        var r_out_perp =  uv.plus(n.multiply(cos_theta)).multiply(etai_over_etat);
        var r_out_parallel = n.multiply(-Math.sqrt(Math.abs(1.0 - r_out_perp.lengthSquared())));
        return r_out_perp.plus(r_out_parallel);
    }

    static Vec3 randomInUnitDisk() {
        while (true) {
            var p = new Vec3(randomDouble(-1,1), randomDouble(-1,1), 0);
            if (p.lengthSquared() < 1) {
                return p;
            }
        }
    }

    double get(int index) {
        return switch (index) {
            case 0 -> x;
            case 1 -> y;
            case 2 -> z;
            default -> throw new IndexOutOfBoundsException("" + index);
        };
    }
}