default:
    @java scripts/Value.java
    @rm -rf build/classes
    @javac -Xlint:preview --enable-preview --release 26 src/Main.java --source-path src -d build/classes

    @echo 'Java With Value Classes'
    @time java --enable-preview --class-path build/classes Main > value_classes.ppm
run:
    @zig c++ reference/src/InOneWeekend/main.cc
    echo 'Reference C++ implementation (no optimizations)'
    @time ./a.out > reference_cplusplus.ppm

    @zig c++ -O3 reference/src/InOneWeekend/main.cc
    @echo 'Reference C++ implementation (-O3)'
    @time ./a.out > reference_cplusplus.ppm

    @java scripts/Value.java
    @rm -rf build/classes
    @javac -Xlint:preview --enable-preview --release 26 src/Main.java --source-path src -d build/classes

    @echo 'Java With Value Classes'
    @time java --enable-preview --class-path build/classes Main > value_classes.ppm

    @java scripts/NoValue.java
    @rm -rf build/classes
    @javac -Xlint:preview --enable-preview --release 26 src/Main.java --source-path src -d build/classes

    @echo 'Java Without Value Classes'
    @time java --enable-preview --class-path build/classes Main > identity_classes.ppm

    @java scripts/Value.java