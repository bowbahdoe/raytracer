public final class Camera {
    double aspectRatio = 1.0;
    int imageWidth = 100;
    int samplesPerPixel = 10;
    int maxDepth = 10;

    double vfov = 90;  // Vertical view angle (field of view)
    @Point Vec3 lookFrom = new Vec3(0,0,0);   // Point camera is looking from
    @Point Vec3 lookAt   = new Vec3(0,0,-1);  // Point camera is looking at
    Vec3   vup      = new Vec3(0,1,0);     // Camera-relative "up" direction

    double defocusAngle = 0;  // Variation angle of rays through each pixel
    double focusDist = 10;    // Distance from camera lookfrom point to plane of perfect focus

    void render(Hittable world) {
        initialize();

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

                @Color Vec3 pixelColor = new Vec3(0, 0, 0);

                for (int sample = 0; sample < samplesPerPixel; sample++) {
                    var r = getRay(i, j);
                    pixelColor = pixelColor.plus(rayColor(r, maxDepth, world));
                }

                Colors.write(System.out, pixelColor.multiply(pixelSamplesScale));
            }
        }

        System.err.print("\rDone.                 \n");
    }


    private int imageHeight;
    double pixelSamplesScale;
    private @Point Vec3 center;
    private @Point Vec3 pixel00Loc;
    private Vec3 pixelDeltaU;
    private Vec3 pixelDeltaV;
    private Vec3   u, v, w;              // Camera frame basis vectors
    private Vec3 defocusDiskU;
    private Vec3 defocusDiskV;

    private void initialize() {
        int imageHeight = (int) (imageWidth / aspectRatio);
        this.imageHeight = Math.max(imageHeight, 1);

        pixelSamplesScale = 1.0 / samplesPerPixel;

        center = lookFrom;

        // Camera
        var theta = Math.toRadians(vfov);
        var h = Math.tan(theta/2);
        var viewportHeight = 2 * h * focusDist;
        var viewportWidth = viewportHeight * (((double) imageWidth) / imageHeight);

        // Calculate the u,v,w unit basis vectors for the camera coordinate frame.
        w = lookFrom.minus(lookAt).unitVector();
        u = vup.crossProduct(w).unitVector();
        v = w.crossProduct(u);

        // Calculate the vectors across the horizontal and down the vertical viewport edges.
        var viewportU = u.multiply(viewportWidth);
        var viewportV = v.multiply(-viewportHeight);

        // Calculate the horizontal and vertical delta vectors from pixel to pixel.
        this.pixelDeltaU = viewportU.divide(imageWidth);
        this.pixelDeltaV = viewportV.divide(imageHeight);

        // Calculate the location of the upper left pixel.
        var viewportUpperLeft = center
                .minus(w.multiply(focusDist))
                .minus(viewportU.divide(2))
                .minus(viewportV.divide(2));

        this.pixel00Loc = viewportUpperLeft.plus(
                pixelDeltaU.plus(pixelDeltaV).multiply(0.5)
        );

        // Calculate the camera defocus disk basis vectors.
        var defocusRadius = focusDist * Math.tan(Math.toRadians(defocusAngle / 2));
        defocusDiskU = u.multiply(defocusRadius);
        defocusDiskV = v.multiply(defocusRadius);
    }

    private Ray getRay(int i, int j) {
        // Construct a camera ray originating from the defocus disk and directed at a randomly
        // sampled point around the pixel location i, j.

        var offset = sampleSquare();
        var pixelSample = pixel00Loc
                .plus(pixelDeltaU.multiply(i + offset.x()))
                .plus(pixelDeltaV.multiply(j + offset.y()));
        var rayOrigin = (defocusAngle <= 0) ? center : defocusDiskSample();;
        var rayDirection = pixelSample.minus(rayOrigin);

        return new Ray(rayOrigin, rayDirection);
    }

    private @Point Vec3 defocusDiskSample() {
        // Returns a random point in the camera defocus disk.
        var p = Vec3.randomInUnitDisk();
        return center
                .plus(defocusDiskU.multiply(p.x()))
                .plus(defocusDiskV.multiply(p.y()));
    }

    private Vec3 sampleSquare() {
        // Returns the vector to a random point in the [-.5,-.5]-[+.5,+.5] unit square.
        return new Vec3(Math.random() - 0.5, Math.random() - 0.5, 0);
    }

    private @Color Vec3 rayColor(Ray r, int depth, Hittable world) {
        // If we've exceeded the ray bounce limit, no more light is gathered.
        if (depth <= 0) {
            return new Vec3(0,0,0);
        }


        var rec = world.hit(r, new Interval(0.001, Double.POSITIVE_INFINITY))
                .orElse(null);
        if (rec != null) {
            var scatteredRay = rec.material().scatter(r, rec).orElse(null);
            if (scatteredRay != null) {
                return rayColor(scatteredRay.scattered(), depth-1, world).multiply(scatteredRay.attenuation());
            }

            return new Vec3(0, 0, 0);
        }

        var unitDirection = r.direction().unitVector();
        var a = 0.5 * (unitDirection.y() + 1);
        return new Vec3(1, 1, 1).multiply(1.0 - a)
                .plus(new Vec3(0.5, 0.7, 1).multiply(a));
    }
}
