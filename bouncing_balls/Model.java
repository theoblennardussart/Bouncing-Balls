package bouncing_balls;

import bouncing_balls.utils.PolarCoord;
import bouncing_balls.utils.Vec2D;

/**
 * The physics model.
 * 
 * This class is where you should implement your bouncing balls model.
 * 
 * The code has intentionally been kept as simple as possible, but if you wish, you can improve the design.
 * 
 * @author Simon Robillard
 *
 */
class Model {

	double areaWidth, areaHeight;

	private final static double G = 9.82; //gravity
	
	Ball [] balls;

	Model(double width, double height) {
		areaWidth = width;
		areaHeight = height;
		
		// Initialize the model with a few balls
		balls = new Ball[2];
		// balls[0] = new Ball(width / 3, height * 0.9, 1.2, 10.6, 0.2);
		// // balls[1] = new Ball(width / 3, height * 0.9, 1.2, 1.6, 0.2);
		// //balls[1].x = 2;
		// balls[1] = new Ball(2 * width / 3, height * 0.7, -0.6, 0.6, 0.3); // Removed for testing with two identical balls

		balls[0] = new Ball(1, 1.9, 1, 0, 0.25);

		balls[1] = new Ball(2, 2.1, 0, 0, 0.5);

	}

	void step(double deltaT) {
		// TODO this method implements one step of simulation with a step deltaT

		for (Ball b : balls) {
			checkWallCollide(b);
			checkBallCollide(b);	
			applyNewPosition(b, deltaT);
		}
	}

	void applyNewPosition(Ball b, double deltaT){
		b.vx += 0 * deltaT; //speed doesn't change in the x-axis
		b.vy -= G * deltaT; //gravity affects the y-axis TODO: Fixa så att gravity 
		b.x += deltaT * b.vx;
		b.y += deltaT * b.vy;		
	}

	// Return the velocity of one ball after collision. in this case v1. Could be made clearer with better naming or method structure. 
	double calculateConservationOfMomentum(double m1, double m2, double u1,  double u2){
		double R = u2 - u1;
		double I = m1*u1 + m2*u2;
		return (I + m2*R)/(m1 + m2);
	}

	void checkWallCollide(Ball b){       					// pot boolean?
		// detect collision with the border
		if (b.x < b.radius || b.x > areaWidth - b.radius) {
			b.vx *= -1; // change direction of ball
		}
		if (b.y > areaHeight - b.radius) {
			b.vy = -Math.abs(b.vy);
			b.y = areaHeight - b.radius; //unstuck the balls
		}
		if (b.y < b.radius) {
			b.vy = Math.abs(b.vy);
			b.y = b.radius;		// Doesnt allow balls to sink through the ground
		}
	}

	
	void checkBallCollide(Ball b){

		if (b.equals(balls[1])){return;} //calculates collision once per step
		
		//for cleaner code
		Ball b1 = balls[0];
		Ball b2 = balls[1];

		//distance between the balls
		double d = Math.sqrt(Math.pow(b1.x - b2.x,2) + Math.pow(b1.y - b2.y,2));

		//check if they "overlap" aka if they are colliding
		if(d <= b1.radius + b2.radius){

			double oldb1vx = b1.vx;   	// vi använde updaterade värden för att räkna ut värdet för hastighet efter kollision.
			double oldb2vx = b2.vx;		// Better naming wouldn't hurt
			double oldb1vy = b1.vy;
			double oldb2vy = b2.vy;

			//b1.vx = calculateConservationOfMomentum(b1.mass, b2.mass, oldb1vx, oldb2vx);
			//b2.vx = calculateConservationOfMomentum(b2.mass, b1.mass, oldb2vx, oldb1vx);
			//b1.vy = calculateConservationOfMomentum(b1.mass, b2.mass, oldb1vy, oldb2vy);
			//b2.vy = calculateConservationOfMomentum(b2.mass, b1.mass, oldb2vy, oldb1vy);

			//balls[0] = b1;
			//balls[1] = b2;

			
			double contactAngle = Math.atan2((b1.vy-b2.vy), (b1.vx-b2.vx));
			PolarCoord b1V = PolarCoord.recToPolar(b1.vx, b1.vy);
			PolarCoord b2V = PolarCoord.recToPolar(b2.vx, b2.vy);
			Vec2D newb1V = calculateBouncingSpeed(b1V, b1.mass, b2V, b2.mass, contactAngle);
			Vec2D newb2V = calculateBouncingSpeed(b2V, b2.mass, b1V, b1.mass, contactAngle);

			b1.vx = newb1V.x;
			b1.vy = newb1V.y;
			b2.vx = newb2V.x;
			b2.vy = newb2V.y;

			double overlap = b1.radius + b2.radius - d;
			double totalMass = b1.mass + b2.mass;
			double overlapCorrection1 = overlap * (b2.mass / totalMass);
			double overlapCorrection2 = overlap * (b1.mass / totalMass);

			if(overlap > 0) {

				b1.x -= overlapCorrection1*Math.cos(contactAngle);
				b1.y -= overlapCorrection1*Math.sin(contactAngle);
				b2.x += overlapCorrection2*Math.cos(contactAngle);
				b2.y += overlapCorrection2*Math.sin(contactAngle);
				
			}

			balls[0] = b1;
			balls[1] = b2;
		}
	}

	Vec2D calculateBouncingSpeed(PolarCoord v1, double m1, PolarCoord v2, double m2, double contactAngle ){
		double v1x, v1y;

		double partially = ((v1.r * Math.cos(v1.theta-contactAngle)*(m1-m2)) + (2*m2*v2.r*Math.cos(v2.theta-contactAngle))) / (m1+m2);

		v1x = partially * Math.cos(contactAngle) + v1.r * Math.sin(v1.theta-contactAngle)*Math.cos(contactAngle + (Math.PI/2));
		v1y = partially * Math.sin(contactAngle) + v1.r * Math.sin(v1.theta-contactAngle)*Math.sin(contactAngle + (Math.PI/2));

		return new Vec2D(v1x, v1y);
	}

	/**
	 * Simple inner class describing balls.
	 */
	class Ball {
		
		Ball(double x, double y, double vx, double vy, double r) {
			this.x = x;
			this.y = y;
			this.vx = vx;
			this.vy = vy;
			this.radius = r;
			this.mass = Math.PI * Math.pow(r,2);
		}

		/**
		 * Position, speed, and radius of the ball. You may wish to add other attributes.
		 */
		double x, y, vx, vy, radius, mass;
	}
}
