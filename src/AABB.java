value record AABB(Interval x, Interval y, Interval z) {

    AABB() {
        this(new Interval(), new Interval(), new Interval());
    }

    AABB(@Point Vec3 a, @Point Vec3 b) {
        // Treat the two points a and b as extrema for the bounding box, so we don't require a
        // particular minimum/maximum coordinate order.
        this(
                (a.x() <= b.x()) ? new Interval(a.x(), b.x()) : new Interval(b.x(), a.x()),
                (a.y() <= b.y()) ? new Interval(a.y(), b.y()) : new Interval(b.y(), a.y()),
                (a.z() <= b.z()) ? new Interval(a.z(), b.z()) : new Interval(b.z(), a.z())
        );
    }

    AABB(AABB box0, AABB box1) {
        this(
                new Interval(box0.x, box1.x),
                new Interval(box0.y, box1.y),
                new Interval(box0.z, box1.z)
        );
    }

    Interval axisInterval(int n) {
        if (n == 1) return y;
        if (n == 2) return z;
        return x;
    }

    boolean hit(Ray r, Interval ray_t) {
        @Point Vec3 ray_orig = r.origin();
        Vec3        ray_dir  = r.direction();

        for (int axis = 0; axis < 3; axis++) {
            var ax = axisInterval(axis);
            double adinv = 1.0 / ray_dir.get(axis);

            var t0 = (ax.min() - ray_orig.get(axis)) * adinv;
            var t1 = (ax.max() - ray_orig.get(axis)) * adinv;

            var min = ray_t.min();
            var max = ray_t.max();
            if (t0 < t1) {
                if (t0 > ray_t.min()) {
                    min = t0;
                }
                if (t1 < ray_t.max()) {
                    max = t1;
                }
            } else {
                if (t1 > ray_t.min()) {
                    min = t1;
                }
                if (t0 < ray_t.max()) {
                    max = t0;
                }
            }

            if (max <= min)
                return false;
        }
        return true;
    }
}
/*
class aabb {
  public:
    interval x, y, z;

    aabb() {} // The default AABB is empty, since intervals are empty by default.

    const interval& axis_interval(int n) const {

    }

    bool hit(const ray& r, interval ray_t) const {
        const point3& ray_orig = r.origin();
        const vec3&   ray_dir  = r.direction();

        for (int axis = 0; axis < 3; axis++) {
            const interval& ax = axis_interval(axis);
            const double adinv = 1.0 / ray_dir[axis];

            auto t0 = (ax.min - ray_orig[axis]) * adinv;
            auto t1 = (ax.max - ray_orig[axis]) * adinv;

            if (t0 < t1) {
                if (t0 > ray_t.min) ray_t.min = t0;
                if (t1 < ray_t.max) ray_t.max = t1;
            } else {
                if (t1 > ray_t.min) ray_t.min = t1;
                if (t0 < ray_t.max) ray_t.max = t0;
            }

            if (ray_t.max <= ray_t.min)
                return false;
        }
        return true;
    }
};
 */