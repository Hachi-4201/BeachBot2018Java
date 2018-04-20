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
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.BBCommand;
import org.usfirst.frc330.Robot;
import org.usfirst.frc330.constants.ArmConst;
import org.usfirst.frc330.constants.HandConst;
import org.usfirst.frc330.constants.LiftConst;
import org.usfirst.frc330.util.Logger;

/**
 *
 */
public class ThrowCubeArm extends BBCommand {

	double releaseAngle, stopAngle;
	double rollerThrottle = 0.75;
	
    public ThrowCubeArm(double releaseAngle, double stopAngle) {
    	this.releaseAngle = releaseAngle;
    	this.stopAngle = stopAngle;
        requires(Robot.arm);
        requires(Robot.hand);
        requires(Robot.grabber);
        requires(Robot.lift);
    }
    
    public ThrowCubeArm() {
    	this(10, 65);
    }
    
    public ThrowCubeArm(double rollerThrottle) {
    	this(10, 65);
    	this.rollerThrottle = rollerThrottle;
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
    	Robot.arm.setArmAngle(stopAngle);
    	Logger.getInstance().println("Setting arm angle to: " + ArmConst.vertical, Logger.Severity.INFO);
    	Robot.hand.setAngle(HandConst.Defense-20);
    	Robot.lift.setLiftPosition(LiftConst.scaleDropoffHigh);
    }

    Timer timer = new Timer();
    boolean rollerOn = false;
    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
    	if (Robot.arm.getArmAngle() > releaseAngle - 5 && !rollerOn) {
    		Robot.grabber.rollerReverse(rollerThrottle);
    		timer.reset();
    		timer.start();
    		rollerOn = true;
		}
		if (Robot.arm.getArmAngle() > releaseAngle) {
			Robot.grabber.openClaw();
		}
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
    	return (isTimedOut() || (Robot.arm.getArmOnTarget()) && timer.get() > 0.8);
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
    	Logger.getInstance().println("Final arm Setpoint: " + Robot.arm.getSetpoint(), Logger.Severity.INFO);
    	Logger.getInstance().println("Final arm angle: " + Robot.arm.getArmAngle(), Logger.Severity.INFO);
    	Robot.arm.setArmAngle(stopAngle);
    	Robot.grabber.rollerOff();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
    	end();
    }
}
