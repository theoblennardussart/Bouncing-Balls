package bouncing_balls;



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
		balls[0] = new Ball(width / 3, height * 0.9, 1.2, 1.6, 0.2);
		//balls[1] = new Ball(width / 3, height * 0.9, 1.5, 1.6, 0.2);
		//balls[1] = new Ball(width / 3, height * 0.9, 1.2, 1.6, 0.2);
		//balls[1].x = 2;
		balls[1] = new Ball(2 * width / 3, height * 0.7, -0.6, 0.6, 0.3); // Removed for testing with two identical balls

		System.out.println();
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
		b.vy -= G * deltaT; //gravity affects the y-axis
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
		}
		if (b.y < b.radius) {
			b.vy = Math.abs(b.vy);
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
		if(d <= b1.radius || d <= b2.radius){

			double oldb1vx = b1.vx;   	// vi använde updaterade värden för att räkna ut värdet för hastighet efter kollision.
			double oldb2vx = b2.vx;		// Better naming wouldn't hurt
			double oldb1vy = b1.vy;
			double oldb2vy = b2.vy;

			b1.vx = calculateConservationOfMomentum(b1.mass, b2.mass, oldb1vx, oldb2vx);
			b2.vx = calculateConservationOfMomentum(b2.mass, b1.mass, oldb2vx, oldb1vx);
			System.out.println("This is the speed after collision:" + b1.vx);
			System.out.println("This is the speed after collision:" + b2.vx);
			b1.vy = calculateConservationOfMomentum(b1.mass, b2.mass, oldb1vy, oldb2vy);
			b2.vy = calculateConservationOfMomentum(b2.mass, b1.mass, oldb2vy, oldb1vy);

			balls[0] = b1;
			balls[1] = b2;
		}
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
