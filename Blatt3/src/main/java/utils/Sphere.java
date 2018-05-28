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


        double a = 1;
        double b = 2 * ray.direction.dot(ray.origin.minus(center));
        double c = (ray.origin.minus(center)).length() * (ray.origin.minus(center)).length() - radius2;
        double dis = b*b - 4*a*c;

        if(dis < 0){
            return Optional.empty();
        }

        double t1 = (-b + sqrt(dis))/(2*a);
        double t2 = (-b - sqrt(dis))/(2*a);
        double t = min(t1,t2);

        if (t < 0){
            return Optional.empty();
        }

        Vector3 p = ray.origin.plus(ray.direction.times(t));

        Vector3 n = (p.minus(center));
        n.normalize();

        return Optional.of(new Intersection(t,n));

    }
}
