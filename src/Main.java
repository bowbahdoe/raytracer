double randomDouble(double min, double max) {
    // Returns a random real in [min,max).
    return min + (max-min)*Math.random();
}

void main() {
    // World
    var world = new HittableList();


    var groundMaterial = new Lambertian(new Vec3(0.5, 0.5, 0.5));
    world.add(new Sphere(new Vec3(0,-1000,0), 1000, groundMaterial));

    for (int a = -11; a < 11; a++) {
        for (int b = -11; b < 11; b++) {
            var choose_mat = Math.random();
            var center = new Vec3(a + 0.9*Math.random(), 0.2, b + 0.9*Math.random());

            if ((center.minus(new Vec3(4, 0.2, 0))).length() > 0.9) {
                Material sphereMaterial;

                if (choose_mat < 0.8) {
                    // diffuse
                    var albedo = Vec3.random().multiply(Vec3.random());
                    sphereMaterial = new Lambertian(albedo);
                    world.add(new Sphere(center, 0.2, sphereMaterial));
                } else if (choose_mat < 0.95) {
                    // metal
                    var albedo = Vec3.random(0.5, 1);
                    var fuzz = randomDouble(0, 0.5);
                    sphereMaterial = new Metal(albedo, fuzz);
                    world.add(new Sphere(center, 0.2, sphereMaterial));
                } else {
                    // glass
                    sphereMaterial = new Dialectric(1.5);
                    world.add(new Sphere(center, 0.2, sphereMaterial));
                }
            }
        }
    }

    var material1 = new Dialectric(1.5);
    world.add(new Sphere(new Vec3(0, 1, 0), 1.0, material1));

    var material2 = new Lambertian(new Vec3(0.4, 0.2, 0.1));
    world.add(new Sphere(new Vec3(-4, 1, 0), 1.0, material2));

    var material3 = new Metal(new Vec3(0.7, 0.6, 0.5), 0.0);
    world.add(new Sphere(new Vec3(4, 1, 0), 1.0, material3));


    var camera = new Camera();
    camera.aspectRatio      = 16.0 / 9.0;
    camera.imageWidth       = 400;
    camera.samplesPerPixel = 500;
    camera.maxDepth         = 50;

    camera.vfov     = 20;
    camera.lookFrom = new Vec3(13,2,3);
    camera.lookAt   = new Vec3(0,0,0);
    camera.vup      = new Vec3(0,1,0);

    camera.defocusAngle = 0.6;
    camera.focusDist    = 10.0;
    camera.render(world);
}

