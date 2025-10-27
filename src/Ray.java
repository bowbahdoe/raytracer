value record Ray(
        @Point Vec3 origin,
        Vec3 direction
) {
    @Point Vec3 at(double t) {
        return origin.plus(direction.multiply(t));
    }
}
