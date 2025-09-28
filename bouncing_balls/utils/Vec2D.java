package bouncing_balls.utils;

public class Vec2D {
    public double x;
    public double y;

    // Inte säker om allt kommer behövs. Ta bort innan inlämning

    public Vec2D(double x, double y) {

        this.x = x;
        this.y = y;
    }

    public Vec2D add(Vec2D other) {
        return new Vec2D(this.x + other.x, this.y + other.y);
    }

    public Vec2D subtract(Vec2D other) {
        return new Vec2D(this.x - other.x, this.y - other.y);
    }

    public Vec2D multiply(double scalar) {
        return new Vec2D(this.x * scalar, this.y * scalar);
    }

    public double dot(Vec2D other) {
        return this.x * other.x + this.y * other.y;
    }

    public double magnitude() {
        return Math.sqrt(x * x + y * y);
    }

    public Vec2D normalize() {
        double mag = magnitude();
        if (mag == 0) {
            return new Vec2D(0, 0);
        }
        return new Vec2D(this.x / mag, this.y / mag);
    }

    PolarCoord rectToPolar(double x, double y){
		double r = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
		double theta = Math.atan2(y, x);
		return new PolarCoord(r, theta);
	}

}
