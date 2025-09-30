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
	private final static double G = 9.82; //gravitational constant (in Sweden)
	Ball [] balls;

	Model(double width, double height) {

		areaWidth = width;
		areaHeight = height;
		
		// Initialize the model with a few balls
		balls = new Ball[2];
		balls[0] = new Ball(width / 3, height * 0.9, 1.2, 1.6, 0.2);
		balls[1] = new Ball(2 * width / 3, height * 0.7, -0.6, 0.6, 0.3); 

	}

	void step(double deltaT) {

		/**
		 * Moves the simulation forward in time by deltaT seconds.
		 * 
		 * @param deltaT - The time step, in seconds.
		 */

		for (Ball b : balls) {
			checkWallCollision(b);
			checkBallCollision(b);
			moveBall(b, deltaT);
		}
	}

	void moveBall(Ball b, double deltaT){

		/** 
		 * Updates the position of ball b
		 * 
		 * @param b - The specified ball
		 */
		
		b.vx += 0 * deltaT; //Unecessary, but for symmetry
		b.vy -= G * deltaT; //gravity's effect on the y-axis

		//update position using euler's method

		b.x += deltaT * b.vx;
		b.y += deltaT * b.vy;
	}

	void checkWallCollision(Ball b){

		/**
		 * Detect ball b collision with the border
		 * 
		 * @param b The ball to check for collision against the walls
		 * 
		 */ 

		if (b.x < b.radius || b.x > areaWidth - b.radius) {
			b.vx *= -1;
		}
		if (b.y > areaHeight - b.radius) {
			b.vy = -Math.abs(b.vy);
			b.y = areaHeight - b.radius; //unstuck the balls in case speed gets really high
		}
		if (b.y < b.radius) {
			b.vy = Math.abs(b.vy);
			b.y = b.radius;		// Doesnt allow balls to sink through the ground
		}
	}
	
	void checkBallCollision(Ball b){

		/**
		 * Detect collision between the balls and updates their speeds accordingly
		 * 
		 * @param b The ball to check for collision against the other ball
		 * 
		 */


		if (b.equals(balls[1])){return;} //makes sure we calculate ball collision once per step
		
		//for cleaner code
		Ball b1 = balls[0];
		Ball b2 = balls[1];

		double distance = Math.sqrt(Math.pow(b1.x - b2.x,2) + Math.pow(b1.y - b2.y,2));

		//check if they "overlap" aka if they are colliding
		if(distance <= b1.radius + b2.radius){
			
			double contactAngle = Math.atan2((b1.vy-b2.vy), (b1.vx-b2.vx));
			PolarCoord b1V = PolarCoord.recToPolar(b1.vx, b1.vy);
			PolarCoord b2V = PolarCoord.recToPolar(b2.vx, b2.vy);
			Vec2D newb1V = calculateBouncingSpeed(b1V, b1.mass, b2V, b2.mass, contactAngle);
			Vec2D newb2V = calculateBouncingSpeed(b2V, b2.mass, b1V, b1.mass, contactAngle);

			b1.vx = newb1V.x;
			b1.vy = newb1V.y;
			b2.vx = newb2V.x;
			b2.vy = newb2V.y;

			//overlap to move the balls outside of each other, to make sure they don't collid next step
			double overlap = b1.radius + b2.radius - distance;
			double totalMass = b1.mass + b2.mass;
			double overlapCorrection1 = overlap * (b2.mass / totalMass);
			double overlapCorrection2 = overlap * (b1.mass / totalMass);

			b1.x -= overlapCorrection1*Math.cos(contactAngle);
			b1.y -= overlapCorrection1*Math.sin(contactAngle);
			b2.x += overlapCorrection2*Math.cos(contactAngle);
			b2.y += overlapCorrection2*Math.sin(contactAngle);
			
			//save onto the balls
			balls[0] = b1;
			balls[1] = b2;
		}
	}

	Vec2D calculateBouncingSpeed(PolarCoord v1, double m1, PolarCoord v2, double m2, double contactAngle ){
		
		/**
		 *  Calculates the new velocity vector of object v1 after an elisatic collision
		 *  using v2, given their velocities, masses and the contact angle.
		 * 
		 * @param v1 Velocity of object 1 in polar coordinates
		 * @param m1 Mass of object 1
		 * @param v2 Velocity of object 2 in polar coordinates
		 * @param m2 Mass of object 2
		 * @param contactAngle Angle of contact between the two objects, in radians
		 * @return New velocity of object 1 in Cartesian coordinates
		 */
		
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
		 * Position, speed, radius, and mass of the ball
		 */
		double x, y, vx, vy, radius, mass;
	}
}
