package bouncing_balls.utils;

public class PolarCoord {
    public double r, theta;

    public PolarCoord(double r, double theta){
        this.r = r;
        this.theta = theta;
    }

    Vec2D polarToRect(){
        double x = r * Math.cos(theta);
        double y = r * Math.sin(theta);
        return new Vec2D(x, y);
    }

    public static PolarCoord recToPolar(double x, double y){
        double r = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
        double theta = Math.atan2(y, x);
        return new PolarCoord(r, theta);
    }
}
