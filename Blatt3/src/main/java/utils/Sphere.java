package utils;

import java.util.Optional;

import static java.lang.Math.min;
import static java.lang.Math.sqrt;

public class Sphere implements Intersectable {

    public final Vector3 center;
    public final double radius;
    private final double radius2;

    public Sphere(Vector3 center, double radius) {
        this.center = center;
        this.radius = radius;
        this.radius2 = radius * radius;
    }

    public Optional<Intersection> intersect(Ray ray, double near) {
        //TODO: Blatt 6: Aufgabe 1


        Vector3 l = center.minus(ray.origin);
        double tca = l.dot(ray.direction);

        if (tca<0) return Optional.empty();

        double d2 = l.dot(l) - tca*tca;

        if (d2 > radius2) return Optional.empty();

        double thc = sqrt(radius2 - d2);
        double t = tca - thc;

        Vector3 p = ray.origin.plus(ray.direction.times(t));

        Vector3 n = (p.minus(center));
        n.normalize();

        if (t < 0){
            return Optional.of(new Intersection(0,n));
        }


        return Optional.of(new Intersection(t,n));

    }
}
