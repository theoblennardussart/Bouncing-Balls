package bouncing_balls;
import bouncing_balls.utils.Vec2D;

public class Test {

    // använder sig av projektion för att räkna ut hastighetskomponenterna längs kollisionslinjen

    static Vec2D posVec1 = new Vec2D(3, 4);
    static Vec2D posVec2 = new Vec2D(1, 0);
    static Vec2D velVec1 = new Vec2D(1, 1);
    static Vec2D velVec2 = new Vec2D(0, 2);
    static double m1 = 2;
    static double m2 = 3;

    public static void main(String[] args) {
        
        Vec2D centerLine = posVec1.subtract(posVec2);
        Vec2D velVec1Proj = project(velVec1, centerLine);
        Vec2D velVec2Proj = project(velVec2, centerLine);
        double massScaling1 = calculateMassScalingDuringCollsion(m1, m2);
        double massScaling2 = calculateMassScalingDuringCollsion(m2, m1);

        Vec2D newVelVec1 = calculateVelocityAfterCollision(velVec1 ,velVec1Proj, massScaling1);
        Vec2D newVelVec2 = calculateVelocityAfterCollision(velVec2, velVec2Proj, massScaling2);

    }

    // calculating the projetion of vector v onto vector onto, to get the velocity component along the line of impact
    static Vec2D project(Vec2D v, Vec2D onto) {
        double dot = v.dot(onto);
        double denom = onto.dot(onto);  //||onto||^2
        return onto.scale(dot / denom);
    }

    static double calculateMassScalingDuringCollsion(double m1, double m2) {

        return -(2*m2)/(m1+m2);

    }

    static Vec2D calculateVelocityAfterCollision(Vec2D v, Vec2D projectedVector, double mass) { 

        return v.subtract(projectedVector.scale(mass));
    }

    
	// Return the velocity of one ball after collision. in this case v1. Could be made clearer with better naming or method structure. 
	double calculateConservationOfMomentum(double m1, double m2, double u1,  double u2){
		double R = u2 - u1;
		double I = m1*u1 + m2*u2;
		return (I + m2*R)/(m1 + m2);
	}

}