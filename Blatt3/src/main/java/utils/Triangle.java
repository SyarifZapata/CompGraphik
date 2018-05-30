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

        normal = (b.minus(a)).cross(c.minus(a)); // get normal using the cross product..
        normal.normalize();

    }

    /**
     * Calculates the barycentric coordinates of a point p when projected onto the plane of the triangle.
     * @param p A point in R^3
     * @return The barycentric coordinates of p projected onto the triangle's plane.
     */
    public BarycentricCoordinates barycentricCoords(Vector3 p) {
        //TODO: Blatt 6: Aufgabe 1
        // Paul fragen warum is richtig hier..

        Vector3 n = (b.minus(a)).cross(c.minus(a)); // normal
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
        double d = -normal.dot(c); // we calculate d using a/b/c, because p is not calculated yet.
        // result is the same.
        //System.out.println("d: "+d);
        double t = -(ray.origin.dot(normal) + d) / (ray.direction.dot(normal));

        if (t < near) return Optional.empty(); // if it smaller than near plane, ignore.

        Vector3 p = ray.origin.plus(ray.direction.times(t));
//        double dd = -normal.dot(p);
//        System.out.println("dd " + dd);

        BarycentricCoordinates barycentric = barycentricCoords(p);

        if(normal.dot(ray.direction) > 0){ // if normal goes in the same direction as ray direction. but why?
            normal = normal.times(-1);
        }

        if (barycentric.isInside()) {

           return Optional.of(new Intersection(t,normal));
        }

        return Optional.empty();
    }

}
