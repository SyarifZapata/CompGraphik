package raytracing;

import illumination.DirectionalLight;
import image.Image;
import image.RGBA;
import projection.PinholeProjection;

import projection.TurnableRenderer;
import utils.*;

import java.util.Optional;
import java.util.function.BiPredicate;

public class RayTracer implements TurnableRenderer {

    private final int width;
    private final int height;

    private final PinholeProjection projection;

    // Beleuchtung und Environment Map
    private Optional<EnvironmentMap<RGBA>> environmentMap = Optional.empty();
    private Optional<DirectionalLight> lightSource = Optional.empty();

    private double ambientLight = 0.28;

    // Ray Tracing und Rektursionstiefe
    private boolean rayTracingEnabled = false;
    private int rayTraceDepth = 5;

    // Reflexionen
    private boolean roughReflectionsEnabled = false;
    private double roughReflectionRoughness = 0.05;
    private int roughReflectionSamples = 10;

    // Schatten
    private boolean shadowsEnabled = false;
    private boolean softShadowsEnabled = false;
    private int softShadowSamples = 20;
    private double shadowSoftness = 0.05;

    // Tiefenschärfe
    private boolean depthOfFieldEnabled = false;
    private int depthOfFieldSamples = 20;
    private double depthOfFieldFocalLength = 5.0;
    private double depthOfField = 0.05;

    // Adaptives Supersampling
    private boolean adaptiveSupersamplingEnabled = false;
    private double adaptiveSupersamplingThreshold = 0.3;
    private int adaptiveSupersamplingSamples = 3;

    private Scene scene = Scene.empty();


    public RayTracer(int width, int height, PinholeProjection projection) {
        this.width = width;
        this.height = height;

        this.projection = projection;
    }

    public Image<RGBA> render() {
        Image<RGBA> framebuffer = new Image<RGBA>(width, height);

        //TODO: Blatt 6: Aufgabe 2, 10
        // ab hier von Syarif geschrieben
        for(int x =0; x<width;x++){
            for(int y = 0; y<height; y++){
                framebuffer.set(x,y,followRay(x,y));
            }
        }

        return framebuffer;
    }

    private RGBA followRay(double x, double y) {

        Matrix4 invertedProjection = projection.getMatrix().inverted();
        Vector4 zeroPoint = new Vector4(0,0,0,1);
        Vector4 origin4 = invertedProjection.multiply(zeroPoint);
        Vector3 origin = new Vector3(origin4.x,origin4.y,origin4.z);

        Vector4 dirRaw = new Vector4(x,y,1,1);
        Vector4 directHomo = invertedProjection.multiply(dirRaw);
        Vector3 direction = new Vector3(directHomo.x,directHomo.y,directHomo.z);
        //TODO: Blatt 6, Aufgabe 2, 9


        Ray ray = new Ray(origin,direction.minus(origin).times(-1));

        return followRay(rayTraceDepth,ray);

    }

    private RGBA followRay(int depth, Ray ray) {
        return followRay(depth, ray, 0.001);
    }

    private RGBA followRay(int depth, Ray ray, double eps) {
        //TODO: Blatt 6: Aufgabe 2, 3, 4, 5, 6, 7, 8

        Optional<RayCastResult> hit = scene.rayCastScene(ray,eps);

        if(hit.isPresent()) {

            //Farbe Strahl (Pixel) = c · max{−⟨n, l⟩, 0} + a)
            RGBA color = hit.get().object.getMaterial().getColor();
            RGBA farbe = color.plus(color.times(ambientLight)).times(1);
            Vector3 normal = hit.get().intersection.normal;
            Vector3 l;

            if (lightSource.isPresent()) {
                l = lightSource.get().direction;

                farbe= color.times(Math.max(-1 * (normal.dot(l)), 0) + ambientLight);

                double t = hit.get().intersection.t;
                Vector3 hitPoint= ray.pointAt(t);

                if(shadowsEnabled){
                    Ray fromPoint;
                    Optional<RayCastResult> inShadow = Optional.empty();
                    int counter=0;
                    if(softShadowsEnabled){
                        for(int i = 0; i<softShadowSamples ; i++){
                            fromPoint = new Ray(hitPoint,l.neg().plus(RandomHelper.sampleStandardNormal3D().times(shadowSoftness)));
                            inShadow = scene.rayCastScene(fromPoint,eps);

                            if(!inShadow.isPresent()){
                                counter++;      // 1.0 if total beleuchtet, 0.0 if total dunkle.
                            }
                        }
                        if(inShadow.isPresent()){
                            if(inShadow.get().object != hit.get().object){
                                farbe = farbe.times(((double)counter/softShadowSamples)+0.24);
                            }
                        }


                    }else {
                        fromPoint = new Ray(hitPoint,l.neg()); // man muss den wert flippen damit es in die richtung
                        inShadow = scene.rayCastScene(fromPoint,eps);
                        if(inShadow.isPresent()){
                            if(inShadow.get().object != hit.get().object){ // check ob es nicht das gleiche object ist.
                                farbe = farbe.times(0.24); // leider must man hier manuell eingeben.
                            }
                        }
                    }


                }

            }

            if(rayTracingEnabled){
                Vector3 hitPoint = ray.pointAt(hit.get().intersection.t);
                Vector3 hitPointNormal = hit.get().intersection.normal;
                // R+V = 2<VN>N
                // R = V-2<VN>N
                Vector3 reflectVector = ray.direction
                        .minus(hitPointNormal.times(ray.direction.dot(hitPointNormal)*2));
                // ray is allways Ray(origin,direction);
                Ray reflectRay = new Ray(hitPoint,reflectVector);
                RGBA temp = followRay(depth-1, reflectRay,eps);

                //Farbe(Strahl (Pixel) ) = v · c · I l + r · I r
                //I l = max{−⟨n, l⟩, 0} + a //(Lambert-Term)
                //I r = Farbe Strahl (Reflexion)  //(Reflexionsterm)

                RGBA r = hit.get().object.getMaterial().getReflectance();
                farbe = farbe.plus(temp.multElementWise(r));
                farbe.clamp();
            }


            farbe.clamp();
            return farbe;


        }else {
            RGBA farbe = RGBA.grey; // initialize farbe with standard color;
            if(environmentMap.isPresent()){     // we check if there is environtmentMap member available;
               farbe = environmentMap.get().access(ray.direction); // get the color of the background for each ray.
            }

            return farbe;
        }


    }


    // TurnableRenderer Interface methods
    @Override
    public void setProjectionView(Matrix4 currentView) {
        if (projection != null)
            projection.setView(currentView);
    }

    @Override
    public void rotateLights(Matrix4 rotation) {

    }

    // region Getters & Setters...
    public PinholeProjection getProjection() {
        return projection;
    }

    public Optional<EnvironmentMap<RGBA>> getEnvironmentMap() {
        return environmentMap;
    }

    public void setEnvironmentMap(Optional<EnvironmentMap<RGBA>> environmentMap) {
        this.environmentMap = environmentMap;
    }

    public Optional<DirectionalLight> getLightSource() {
        return lightSource;
    }

    public void setLightSource(Optional<DirectionalLight> lightSource) {
        this.lightSource = lightSource;
    }

    public double getAmbientLight() {
        return ambientLight;
    }

    public void setAmbientLight(double ambientLight) {
        this.ambientLight = ambientLight;
    }

    public boolean isRayTracingEnabled() {
        return rayTracingEnabled;
    }

    public void setRayTracingEnabled(boolean rayTracingEnabled) {
        this.rayTracingEnabled = rayTracingEnabled;
    }

    public int getRayTraceDepth() {
        return rayTraceDepth;
    }

    public void setRayTraceDepth(int rayTraceDepth) {
        this.rayTraceDepth = rayTraceDepth;
    }

    public boolean isRoughReflectionsEnabled() {
        return roughReflectionsEnabled;
    }

    public void setRoughReflectionsEnabled(boolean roughReflectionsEnabled) {
        this.roughReflectionsEnabled = roughReflectionsEnabled;
    }

    public double getRoughReflectionRoughness() {
        return roughReflectionRoughness;
    }

    public void setRoughReflectionRoughness(double roughReflectionRoughness) {
        this.roughReflectionRoughness = roughReflectionRoughness;
    }

    public int getRoughReflectionSamples() {
        return roughReflectionSamples;
    }

    public void setRoughReflectionSamples(int roughReflectionSamples) {
        this.roughReflectionSamples = roughReflectionSamples;
    }

    public boolean isShadowsEnabled() {
        return shadowsEnabled;
    }

    public void setShadowsEnabled(boolean shadowsEnabled) {
        this.shadowsEnabled = shadowsEnabled;
    }

    public boolean isSoftShadowsEnabled() {
        return softShadowsEnabled;
    }

    public void setSoftShadowsEnabled(boolean softShadowsEnabled) {
        this.softShadowsEnabled = softShadowsEnabled;
    }

    public int getSoftShadowSamples() {
        return softShadowSamples;
    }

    public void setSoftShadowSamples(int softShadowSamples) {
        this.softShadowSamples = softShadowSamples;
    }

    public double getShadowSoftness() {
        return shadowSoftness;
    }

    public void setShadowSoftness(double shadowSoftness) {
        this.shadowSoftness = shadowSoftness;
    }

    public boolean isDepthOfFieldEnabled() {
        return depthOfFieldEnabled;
    }

    public void setDepthOfFieldEnabled(boolean depthOfFieldEnabled) {
        this.depthOfFieldEnabled = depthOfFieldEnabled;
    }

    public int getDepthOfFieldSamples() {
        return depthOfFieldSamples;
    }

    public void setDepthOfFieldSamples(int depthOfFieldSamples) {
        this.depthOfFieldSamples = depthOfFieldSamples;
    }

    public double getDepthOfFieldFocalLength() {
        return depthOfFieldFocalLength;
    }

    public void setDepthOfFieldFocalLength(double depthOfFieldFocalLength) {
        this.depthOfFieldFocalLength = depthOfFieldFocalLength;
    }

    public double getDepthOfField() {
        return depthOfField;
    }

    public void setDepthOfField(double depthOfField) {
        this.depthOfField = depthOfField;
    }

    public boolean isAdaptiveSupersamplingEnabled() {
        return adaptiveSupersamplingEnabled;
    }

    public void setAdaptiveSupersamplingEnabled(boolean adaptiveSupersamplingEnabled) {
        this.adaptiveSupersamplingEnabled = adaptiveSupersamplingEnabled;
    }

    public double getAdaptiveSupersamplingThreshold() {
        return adaptiveSupersamplingThreshold;
    }

    public void setAdaptiveSupersamplingThreshold(double adaptiveSupersamplingThreshold) {
        this.adaptiveSupersamplingThreshold = adaptiveSupersamplingThreshold;
    }

    public int getAdaptiveSupersamplingSamples() {
        return adaptiveSupersamplingSamples;
    }

    public void setAdaptiveSupersamplingSamples(int adaptiveSupersamplingSamples) {
        this.adaptiveSupersamplingSamples = adaptiveSupersamplingSamples;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    // endregion
}
