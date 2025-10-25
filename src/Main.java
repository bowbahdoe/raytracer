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
            var r = ((double) i) / (image_width - 1);
            var g = ((double) j) / (image_height - 1);
            var b = 0.0;

            int ir = (int) (255.999 * r);
            int ig = (int) (255.999 * g);
            int ib = (int) (255.999 * b);

            IO.print(ir);
            IO.print(" ");
            IO.print(ig);
            IO.print(" ");
            IO.print(ib);
            IO.print("\n");
        }
    }

    System.err.print("\rDone.                 \n");
}
