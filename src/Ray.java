value record Ray(
        @Point Vec3 origin,
        Vec3 direction,
        double time
) {
    Ray(@Point Vec3 origin, Vec3 direction) {
        this(origin, direction, 0);
    }

    @Point Vec3 at(double t) {
        return origin.plus(direction.multiply(t));
    }
}
