package bouncing_balls.utils;

public class PolarCoord {
    double r, theta;

    PolarCoord(double r, double theta){
        this.r = r;
        this.theta = theta;
    }

    Vec2D polarToRect(){
        double x = r * Math.cos(theta);
        double y = r * Math.sin(theta);
        return new Vec2D(x, y);
    }
}
