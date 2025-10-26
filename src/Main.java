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

value record Ray(
        @Point Vec3 origin,
        Vec3 direction
) {
    @Point Vec3 at(double t) {
        return origin.plus(direction.multiply(t));
    }
}

@Color Vec3 rayColor(Ray r) {
    var unitDirection = r.direction().unitVector();
    var a = 0.5 * (unitDirection.y + 1);
    return new Vec3(1, 1, 1).multiply(1.0 - a)
            .plus(new Vec3(0.5, 0.7, 1).multiply(a));
}

void main() {
    // Image

    var aspectRatio = 16.0 / 9.0;
    int imageWidth = 400;

    // Calculate the image height, and ensure that it's at least 1.
    int imageHeight = (int) (imageWidth / aspectRatio);
    imageHeight = Math.max(imageHeight, 1);

    // Camera
    var focalLength = 1.0;
    var viewportHeight = 2.0;
    var viewportWidth = viewportHeight * (((double) imageWidth) / imageHeight);
    @Point Vec3 cameraCenter = new Vec3(0, 0, 0);

    // Calculate the vectors across the horizontal and down the vertical viewport edges.
    var viewportU = new Vec3(viewportWidth, 0, 0);
    var viewportV = new Vec3(0, -viewportHeight, 0);

    // Calculate the horizontal and vertical delta vectors from pixel to pixel.
    var pixelDeltaU = viewportU.divide(imageWidth);
    var pixelDeltaV = viewportV.divide(imageHeight);

    // Calculate the location of the upper left pixel.
    var viewportUpperLeft = cameraCenter
            .minus(new Vec3(0, 0, focalLength))
            .minus(viewportU.divide(2))
            .minus(viewportV.divide(2));

    var pixel00Loc = viewportUpperLeft.plus(
            pixelDeltaU.plus(pixelDeltaV).multiply(0.5)
    );

    IO.print("P3\n");
    IO.print(imageWidth);
    IO.print(" ");
    IO.print(imageHeight);
    IO.print("\n255\n");

    for (int j = 0; j < imageHeight; j++) {
        System.err.print("\rScanlines remaining: ");
        System.err.print((imageHeight - j));
        System.err.print(" ");
        for (int i = 0; i < imageWidth; i++) {
            var pixelCenter = pixel00Loc
                    .plus(pixelDeltaU.multiply(i))
                    .plus(pixelDeltaV.multiply(j));
            var rayDirection = pixelCenter.minus(cameraCenter);
            var r = new Ray(cameraCenter, rayDirection);

            @Color Vec3 pixelColor = rayColor(r);

            writeColor(System.out, pixelColor);
        }
    }

    System.err.print("\rDone.                 \n");
}
