@interface Color {
}

@interface Point {
}

value record Vec3(
        double x,
        double y,
        double z
) {
    Vec3() {
        this(0, 0, 0);
    }

    Vec3 withX(double x) {
        return new Vec3(x, y, z);
    }

    Vec3 withY(double y) {
        return new Vec3(x, y, z);
    }

    Vec3 withZ(double z) {
        return new Vec3(x, y, z);
    }

    Vec3 plus(Vec3 v) {
        return new Vec3(
                x + v.x, y + v.y, z + v.z
        );
    }

    Vec3 multiply(double t) {
        return new Vec3(
                x * t, y * t, z * t
        );
    }

    Vec3 divide(double t) {
        return multiply(1 / t);
    }

    double length() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    double dotProduct(Vec3 v) {
        return x * v.x + y * v.y + z * v.z;
    }

    Vec3 crossProduct(Vec3 v) {
        return new Vec3(
                y * v.z - z * v.y,
                z * v.x - x * v.z,
                x * v.y - y * v.x
        );
    }

    Vec3 unitVector() {
        return this.divide(length());
    }
}

void writeColor(
        PrintStream out,
        @Color Vec3 pixelColor
) {
    var r = pixelColor.x;
    var g = pixelColor.y;
    var b = pixelColor.z;

    int rByte = (int) (255.999 * r);
    int gByte = (int) (255.999 * g);
    int bByte = (int) (255.999 * b);

    out.print(rByte);
    out.print(" ");
    out.print(gByte);
    out.print(" ");
    out.print(bByte);
    out.print("\n");
}

void main() {
    int image_width = 256;
    int image_height = 256;

    IO.print("P3\n");
    IO.print(image_width);
    IO.print(" ");
    IO.print(image_height);
    IO.print("\n255\n");

    for (int j = 0; j < image_height; j++) {
        System.err.print("\rScanlines remaining: ");
        System.err.print((image_height - j));
        System.err.print(" ");
        for (int i = 0; i < image_width; i++) {
            @Color Vec3 pixelColor = new Vec3(
                    ((double) i) / (image_width - 1),
                    ((double) j) / (image_height - 1),
                    0.0
            );

            writeColor(System.out, pixelColor);
        }
    }

    System.err.print("\rDone.                 \n");
}
