value record Interval(
        double min, double max
) {
    Interval() {
        this(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
    }

    Interval(Interval a, Interval b) {
        // Create the interval tightly enclosing the two input intervals.
        this(
                Math.min(a.min, b.min),
                Math.max(a.max, b.max)
        );
    }
    double size() {
        return max - min;
    }

    boolean contains(double x) {
        return min <= x && x <= max;
    }

    boolean surrounds(double x) {
        return min < x && x < max;
    }

    double clamp(double x) {
        if (x < min) return min;
        if (x > max) return max;
        return x;
    }

    Interval expand(double delta) {
        var padding = delta / 2;
        return new Interval(min - padding, max + padding);
    }


    static final Interval EMPTY = new Interval(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
    static final Interval UNIVERSE = new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
}
