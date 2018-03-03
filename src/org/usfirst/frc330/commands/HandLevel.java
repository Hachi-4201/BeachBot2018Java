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
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc330.Robot;
import org.usfirst.frc330.constants.*;
import org.usfirst.frc330.util.Logger;

/**
 *
 */
public class HandLevel extends BBCommand {

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS
 
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
    public HandLevel() {

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
        requires(Robot.hand);

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
    	// totalExtension is our length from the lift, if the hand were to be level
    	double totalExtension = Math.cos(Math.toRadians(Robot.arm.getArmAngle()))*ArmConst.length + HandConst.length;
    	//Logger.getInstance().println("totalExtension: " + totalExtension, Logger.Severity.DEBUG);
    	// maxAllowable is the longest totalExtension that does not violate the rules
    	double maxAllowable = ChassisConst.liftToFrame + ChassisConst.maxExtension - HandConst.framePerimeterSafety;
    	//Logger.getInstance().println("maxAllowable: " + maxAllowable, Logger.Severity.DEBUG);

    	//Calculate hand angle
    	double lengthContrArm = Math.sin(Math.toRadians(90 + Robot.arm.getArmAngle())) * ArmConst.length;
    	//Logger.getInstance().println("lengthContrArm: " + lengthContrArm, Logger.Severity.DEBUG);
    	double lengthContrHand = maxAllowable - lengthContrArm;
    	double handAngle = Math.toDegrees(Math.acos(lengthContrHand/HandConst.length));
    	//Logger.getInstance().println("handAngle: " + handAngle, Logger.Severity.DEBUG);
    	
    	SmartDashboard.putNumber("lengthContrArm", lengthContrArm);
    	SmartDashboard.putNumber("lengthContrHand", lengthContrHand);
    	SmartDashboard.putNumber("targetWristAngle", handAngle);
    	
    	// First Case - No rule violation
    	if(totalExtension < maxAllowable) {
    		Logger.getInstance().println("Hand leveled to 0 degrees", Logger.Severity.DEBUG);
    		Robot.hand.setAngle(0.0);
    	}
    	
    	// Second Case - Would violate
    	else {
    		//Logger.getInstance().println("Hand below level" , Logger.Severity.DEBUG);
    		Robot.hand.setAngle(handAngle);
    	}
    	
    	
    	
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
    }
}
