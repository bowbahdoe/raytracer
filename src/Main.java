double degreesToRadians(double degrees) {
    return degrees * Math.PI / 180.0;
}


void main() {
    // World
    var world = new HittableList();


    var material_ground = new Lambertian(new Vec3(0.8, 0.8, 0.0));
    var material_center = new Lambertian(new Vec3(0.1, 0.2, 0.5));
    var material_left   = new Dialectric(1.50);
    var material_bubble = new Dialectric(1.00 / 1.50);
    var material_right  = new Metal(new Vec3(0.8, 0.6, 0.2), 1.0);

    world.add(new Sphere(new Vec3( 0.0, -100.5, -1.0), 100.0, material_ground));
    world.add(new Sphere(new Vec3( 0.0,    0.0, -1.2),   0.5, material_center));
    world.add(new Sphere(new Vec3(-1.0,    0.0, -1.0),   0.5, material_left));
    world.add(new Sphere(new Vec3(-1.0,    0.0, -1.0),   0.4, material_bubble));
    world.add(new Sphere(new Vec3( 1.0,    0.0, -1.0),   0.5, material_right));


    var camera = new Camera();
    camera.imageWidth = 1600;
    camera.aspectRatio = 16.0 / 9.0;
    camera.samplesPerPixel = 100;
    camera.maxDepth = 5000;

    camera.render(world);
}

