package utils;

import java.util.Optional;

public class Triangle implements Intersectable {

    // Points of the Triangle
    public final Vector3 a;
    public final Vector3 b;
    public final Vector3 c;

    private Vector3 normal;


    public Triangle(Vector3 a, Vector3 b, Vector3 c) {
        this.a = a;
        this.b = b;
        this.c = c;

        //TODO: Blatt 6, Aufgabe 1

        normal = (b.minus(a)).cross(c.minus(a));
        normal.normalize();
        //d = -n.dot(a);
    }

    /**
     * Calculates the barycentric coordinates of a point p when projected onto the plane of the triangle.
     * @param p A point in R^3
     * @return The barycentric coordinates of p projected onto the triangle's plane.
     */
    public BarycentricCoordinates barycentricCoords(Vector3 p) {
        //TODO: Blatt 6: Aufgabe 1

        Vector3 n = (b.minus(a)).cross(c.minus(a));
        Vector3 na = (c.minus(b)).cross(p.minus(b));
        Vector3 nb = (a.minus(c)).cross(p.minus(c));
        Vector3 nc = (b.minus(a)).cross(p.minus(a));

        double n2 = n.length()*n.length();
        double u = n.dot(na)/n2;
        double v = n.dot(nb)/n2;
        double w = n.dot(nc)/n2;

        return new BarycentricCoordinates(u, v, w);
    }


    public Optional<Intersection> intersect(Ray ray, double near) {
        //TODO: Blatt 6: Aufgabe 1

        double t = -(ray.origin.dot(normal) + (-normal.dot(a))) / (ray.direction.dot(normal));

        if (t < near) return Optional.of(new Intersection(0,normal));

        Vector3 p = ray.origin.plus(ray.direction.times(t));

        BarycentricCoordinates barycentric = barycentricCoords(p);

        if (barycentric.x >= 0 && barycentric.x <= 1 &&
                barycentric.y >= 0 && barycentric.y <= 1 &&
                barycentric.z >= 0 && barycentric.z <= 1) {
           return Optional.of(new Intersection(t,normal));
        }

        return Optional.empty();
    }

}
