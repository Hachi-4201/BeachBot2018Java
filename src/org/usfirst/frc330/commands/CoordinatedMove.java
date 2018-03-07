// RobotBuilder Version: 2.0BB
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc330.commands;
import edu.wpi.first.wpilibj.command.BBCommand;
import org.usfirst.frc330.Robot;
import org.usfirst.frc330.constants.ArmConst;
import org.usfirst.frc330.constants.HandConst;
import org.usfirst.frc330.util.Logger;

/**
 *
 */
public class CoordinatedMove extends BBCommand {

	double time;
	double armAngle;
	double handAngleRelGround;
	boolean armSet, handSet, CGforward;
	boolean bottomUp, topDown;
	
    public CoordinatedMove(double armAngle, double handAngleRelGround) {
    	this.setRunWhenDisabled(false);
    	requires(Robot.arm);
    	requires(Robot.hand);
    	this.armAngle = armAngle;
    	this.handAngleRelGround = handAngleRelGround;
    }
    
    public CoordinatedMove(double armAngle, double handAngleRelGround, double timeout) {
    	this(armAngle, handAngleRelGround);
    	setTimeout(timeout);
    }

    protected void initialize() {
    	armSet = false;
    	handSet = false;
    	bottomUp = false;
    	topDown = false;
    	
    	Logger.getInstance().println("Coordinated Move, Arm Start: " + Robot.arm.getArmAngle(), Logger.Severity.INFO);
    	Logger.getInstance().println("Coordinated Move, Arm Destination: " + armAngle, Logger.Severity.INFO);
    	
    	//Entire motion is above/below danger
    	if((Robot.arm.getArmAngle() >= ArmConst.safeAngle && armAngle >= ArmConst.safeAngle) || (Robot.arm.getArmAngle() <= -ArmConst.safeAngle && armAngle <= -ArmConst.safeAngle)) {
    		Robot.hand.setAngle(handAngleRelGround);
    		Robot.arm.setArmAngle(armAngle);
    		Logger.getInstance().println("Entire motion safe, setting final positions", Logger.Severity.INFO);
    		armSet = true;
    		handSet = true;
    	}
    	//Coming from bottom
    	else if(armAngle > ArmConst.safeAngle) {
    		if(Robot.hand.getHandAngleFromArm() < HandConst.encFrameSafe) { //If the hand is in an unsafe place
    			Robot.hand.setAngleFromArm(HandConst.encFrameSafe); //Put the hand in a safe place
    		}
    		Logger.getInstance().println("Moving from bottom towards top. Storing hand in safe location (if needed)", Logger.Severity.INFO);
    		bottomUp = true;
    	}
    	//Coming from top
    	else {
    		Robot.arm.setArmAngle(ArmConst.safeAngle); //Move the arm towards the safe limit
    		Logger.getInstance().println("Moving from top towards bottom. Moving arm forward to preserve CG", Logger.Severity.INFO);
    		topDown = true;
    		CGforward = false;
    	}
    }


    protected void execute() {
    	if(bottomUp) {
    		if (Robot.hand.getHandOnTarget() && !armSet) {
    			Logger.getInstance().println("Hand stowed. Safe to move arm up", Logger.Severity.INFO);
    			Robot.arm.setArmAngle(armAngle);
    			armSet = true;
    		}
    		else if(Robot.arm.getArmAngle() > ArmConst.safeAngle && !handSet && !Robot.arm.getArmOnTarget()) {
    			Robot.hand.setAngle(handAngleRelGround);
    		}
    		else if(Robot.arm.getArmOnTarget() && !handSet && !Robot.arm.getArmOnTarget()){
    			Robot.hand.setAngle(handAngleRelGround);
    			Logger.getInstance().println("Arm at final position, setting final hand angle", Logger.Severity.INFO);
    			handSet = true;
    		}
    		
    	}
    	else if (topDown) {
    		if (Robot.arm.getArmAngle() < (ArmConst.safeAngle + 10) && !CGforward) {
    			if(Robot.hand.getHandAngleFromArm() < HandConst.encFrameSafe) { //If the hand is in an unsafe place
    				Robot.hand.setAngleFromArm(HandConst.encFrameSafe); //Put the hand in a safe place
    			}
    			Logger.getInstance().println("Now that the CG is forward, stowing the hand", Logger.Severity.INFO);
    			CGforward = true;
    		}
    		else if(Robot.hand.getHandOnTarget() && !armSet) {
    			Robot.arm.setArmAngle(armAngle);
    			Logger.getInstance().println("Now that the hand is stowed, lowering the arm", Logger.Severity.INFO);
    			armSet = true;
    		}
    		else if(Robot.arm.getArmOnTarget() && !handSet && armSet) {
    			Logger.getInstance().println("Arm is on target, setting final hand position", Logger.Severity.INFO);
    			Logger.getInstance().println("Arm Angle: " + Robot.arm.getArmAngle(), Logger.Severity.DEBUG);
    			Robot.hand.setAngle(handAngleRelGround);
    			
    			handSet = true;
    		}
    	}
    }

    protected boolean isFinished() {
        return (armSet && handSet && Robot.hand.getHandOnTarget() && Robot.arm.getArmOnTarget()) || this.isTimedOut();
    }

    protected void end() {
    	Logger.getInstance().println("Arm Setpoint: " + Robot.arm.getSetpoint(), Logger.Severity.INFO);
    	Logger.getInstance().println("Hand Setpoint (rel ground): " + Robot.hand.getSetpoint(), Logger.Severity.INFO);
    	Logger.getInstance().println("Final Arm Angle: " + Robot.arm.getArmAngle(), Logger.Severity.INFO);
    	Logger.getInstance().println("Final Hand Angle: " + Robot.hand.getHandAngle(), Logger.Severity.INFO);
    }


    protected void interrupted() {
    	this.end();
    }
}
