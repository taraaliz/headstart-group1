import ShefRobot.*;

public class ColorFinder {
	
    // These lines define the names (variables) we will use elsewhere in the program
    // Having all these names at the top, outside of the main method is 
    // not beautiful, Object-Oriented programming style, but we wanted
    // to keep this exercise simple ;)
    private static Robot robot;
    private static Motor leftMotor;
    private static Motor rightMotor;
    private static Speaker speaker;
    private static ColorSensor sensor;
    private static ColorSensor.Color col;
    
    
    // This is the "main" method - this is where the program starts
    public static void main(String[] args) {
    	
    	// First, we need to get the Robot object
    	robot = new Robot("dia-lego-e5");
    	// Then we get our left and right Motor objects and our Speaker object
    	leftMotor = robot.getLargeMotor(Motor.Port.A);
    	rightMotor = robot.getLargeMotor(Motor.Port.B);
    	speaker = robot.getSpeaker();
    	
    	// You can try different speeds for the motors,
    	// but if you run them too fast the code that counts
    	// the steps won't keep up and you will get less accurate movements.
    	leftMotor.setSpeed(80);
    	rightMotor.setSpeed(80);
    	
    	
    	// **** Make your changes to these lines below **** //
    	
    	// Get a colour sensor (note: we are using the US spelling of color in the code...)
    	sensor = robot.getColorSensor(Sensor.Port.S1);
    	
    	//stops on red, goes forward on black
    	//scan(ColorSensor.Color.BLACK);
    	while(sensor.getColor() != ColorSensor.Color.RED){
			while(sensor.getColor() == ColorSensor.Color.BLACK){
				goForward(50);
			}
			scan(ColorSensor.Color.BLACK);
		}
		
    	// Close the robot and clean up all the connections to ports.
    	robot.close();
    }
    //Scan
        private static void scan(ColorSensor.Color color) {
        	/*System.out.println("Scanning...");
        	boolean right = true;
        	int counter = 15;
        	col = sensor.getColor();
			while(col != color){
				if(right){
					right = false;
					for(int i = 0; i<counter/15; i++){
						turnRight(15);
						col = sensor.getColor();
						if (col == ColorSensor.Color.BLACK){
							break;
						}
					}
				} else {
					right = true;
					for(int i = 0; i<counter/15; i++){
						turnLeft(15);
						col = sensor.getColor();
						if (col == ColorSensor.Color.BLACK){
							break;
						}
					}
				}
				counter = counter * 2;
				col = sensor.getColor();
				if (col == ColorSensor.Color.RED){
					break;
				}
			}*/
			System.out.println("Scanning...");
        	int counter = 1;
        	col = sensor.getColor();
			while(col != color && counter < 24){
				if(counter % 2 == 0){
					turnRight(15*counter);
				} else {
					turnLeft(15*counter);
				}
				counter++;
				col = sensor.getColor();
				if (col == ColorSensor.Color.RED){
					break;
				}
			}
    }
    
    // This method moves the robot forward by count "steps"
    // Technically, the steps are degrees of rotation of the motors
    // but how this translates to actual distances moved will depend
    // on various factors...
    protected static void goForward(int count) {
    	leftMotor.resetTachoCount();
    	rightMotor.resetTachoCount();
    	leftMotor.forward();
    	rightMotor.forward();
    	waitfor(count,false,false);
    }
	
    // This turns the robot to the right by movinf the left
    // motor forward and the right motor backwards.
    // The count value is the number of degrees of rotation 
    // *of the motors*. How this relates to the rotation of the 
    // robot will depend on the size of the wheels, how far
    // apart they are, and various other factors.
    protected static void turnRight(int count) {
    	leftMotor.resetTachoCount();
    	rightMotor.resetTachoCount();
    	leftMotor.forward();
    	rightMotor.backward();
    	waitfor(count,false,true);
    }
    
    // This rotates the robot the other way...
    protected static void turnLeft(int count) {
    	leftMotor.resetTachoCount();
    	rightMotor.resetTachoCount();
    	leftMotor.backward();
    	rightMotor.forward();
    	waitfor(count,true,false);
    }
    
    // This is a simple scanning method. It will
    // rotate the robot a short distance either 
    // to the right or "not right" (a.k.a. left!)
    // and check the currently sensed colour.
    // It looks for the specified colour and stops 
    // if it finds it. It will repeat this a 
    // maximum of 'count' times.
    // This method updates the col variable, so 
    // that will be set to the last colour scanned
    // when the method completes.
    private static void scan(String direction, int count, ColorSensor.Color color) {
    	System.out.println("Scanning " + direction + " for " + count);
    	for(int i = 0; i < count; i++) {
    		if(direction.equals("right")) {
    			turnRight(10);
    		} else if(direction.equals("left")) {
    			turnLeft(10);
    		}
    		else if(direction.equals("forward")) {
    			goForward(10);
    		}
    		col = sensor.getColor();			
    		if(col == color) {
    			break;
    		}
    	}
    }
    
    // This is a somewhat complicated method for monitoring the motors and 
    // stopping them once they have rotated far enough.
    private static void waitfor(int count, boolean leftback, boolean rightback) {
    	boolean lf = false; 
    	boolean rf = true;
    	do {
    		int ltc = leftMotor.getTachoCount();
    		int rtc = rightMotor.getTachoCount();
    		if(leftback && (ltc < (0-count))) {
    			leftMotor.stop();
    			lf = true;
    		} else if(ltc > count) {
    			leftMotor.stop();
    			lf = true;
    		}
    		if(rightback && (rtc < (0-count))) {
    			rightMotor.stop();
    			rf = true;
    		} else if(rtc > count) {
    			rightMotor.stop();
    			rf = true;
    		}
    	} while(!(lf && rf));
    }
	
}


