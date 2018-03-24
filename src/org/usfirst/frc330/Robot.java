// RobotBuilder Version: 2.0BB
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.

package org.usfirst.frc330;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.WPILibVersion;

import org.usfirst.frc330.autoCommands.*;
import org.usfirst.frc330.autoCommands.Chooser_RightLeftStart.StartingPosition;
import org.usfirst.frc330.commands.*;
import org.usfirst.frc330.constants.ArmConst;
import org.usfirst.frc330.constants.ChassisConst;
import org.usfirst.frc330.constants.HandConst;
import org.usfirst.frc330.subsystems.*;
//import org.usfirst.frc330.subsystems.Frills.Alarm;
import org.usfirst.frc330.util.BeachbotLibVersion;
import org.usfirst.frc330.util.Buzzer;
import org.usfirst.frc330.util.CSVLogger;
import org.usfirst.frc330.util.Logger;
import org.usfirst.frc330.util.Logger.Severity;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in 
 * the project.
 */
public class Robot extends TimedRobot {

    Command autonomousCommand;
    SendableChooser<Command> autoProgram = new SendableChooser<>();

    public static OI oi;
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    public static Chassis chassis;
    public static Lift lift;
    public static Arm arm;
    public static Grabber grabber;
    public static Climber climber;
    public static Frills frills;
    public static Hand hand;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

    public static Buzzer buzzer;
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    @Override
    public void robotInit() {

        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        chassis = new Chassis();
        SmartDashboard.putData(chassis);
        lift = new Lift();
        SmartDashboard.putData(lift);
        arm = new Arm();
        SmartDashboard.putData(arm);
        grabber = new Grabber();
        SmartDashboard.putData(grabber);
        climber = new Climber();
        SmartDashboard.putData(climber);
        frills = new Frills();
        SmartDashboard.putData(frills);
        hand = new Hand();

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        
        // OI must be constructed after subsystems. If the OI creates Commands
        //(which it very likely will), subsystems are not guaranteed to be
        // constructed yet. Thus, their requires() statements may grab null
        // pointers. Bad news. Don't move it.
        oi = new OI();
        
        // Setup Cameras --------------------------------------------------------
        frills.initDriverCamera();
        // </Cameras> -------------------------------------------------------------

        // Add commands to Autonomous Sendable Chooser

        autoProgram.addDefault("Do Nothing", new DoNothing());
        autoProgram.addObject("CenterAuto", new Chooser_CenterStart_Switch());
        autoProgram.addObject("RightSide", new Chooser_RightLeftStart(StartingPosition.RIGHT));
        autoProgram.addObject("LeftSide", new Chooser_RightLeftStart(StartingPosition.LEFT));
        
        //SimpleAuto
        autoProgram.addObject("Don't Pick This!", new AllenTest());
        
        
        //Setup the buzzer
        buzzer = new Buzzer(frills.buzzer);
                
        // ---------------------------------------------------------------------
        // Logging
        // ---------------------------------------------------------------------
        CSVLogger.getInstance().writeHeader();
        
        Logger.getInstance().println("BeachbotLib Version:                " + BeachbotLibVersion.Version, Severity.INFO);
        Logger.getInstance().println("WPILib Version:                     " + WPILibVersion.Version, Severity.INFO);
        Logger.getInstance().println("NavX Firmware Version:              " + chassis.getNavXFirmware(), Severity.INFO);
        Logger.getInstance().println("Talon ArmL Firmware Version:        " + arm.getArmLFirmwareVersion(), Severity.INFO);
        Logger.getInstance().println("Talon IntakeLeft Firmware Version:  " + grabber.getIntakeLeftFirmwareVersion(), Severity.INFO);
        Logger.getInstance().println("Talon IntakeRight Firmware Version: " + grabber.getIntakeRightFirmwareVersion(), Severity.INFO);
        Logger.getInstance().println("Talon Wrist Firmware Version:       " + hand.getWristFirmwareVersion(), Severity.INFO);
        Logger.getInstance().println("Talon Lift1 Firmware Version:       " + lift.getLift1FirwareVersion(), Severity.INFO);
        Logger.getInstance().println("Talon Lift2 Firmware Version:       " + lift.getLift2FirwareVersion(), Severity.INFO);
        Logger.getInstance().println("Talon Lift3 Firmware Version:       " + lift.getLift3FirwareVersion(), Severity.INFO);

        if (getIsPracticeRobot())
        	Logger.getInstance().println("Practice Robot Detected",Severity.DEBUG);
        else
        	Logger.getInstance().println("Competition Robot Detected",Severity.DEBUG);
        // </Logging> ----------------------------------------------------------
        
        buzzer.enable(0.4);
        
        SmartDashboard.putData("Auto mode", autoProgram);

    }

    /**
     * This function is called when the disabled button is hit.
     * You can use it to reset subsystems before shutting down.
     */
    @Override
    public void disabledInit(){
    	Logger.getInstance().println("Disabled Init", Severity.INFO);
		Scheduler.getInstance().removeAll();
		Robot.lift.stopLift();
		Robot.climber.lockPlatforms();
		Robot.arm.stopArm();
		Robot.hand.stopWrist();
		Robot.grabber.pickupOff();
		Robot.chassis.stopDrive();
		Robot.grabber.stopGrabber();
    }

    @Override
    public void disabledPeriodic() {
        Scheduler.getInstance().run();
        chassis.calcXY();
    	CSVLogger.getInstance().writeData();
    	Logger.getInstance().updateDate();
    	CSVLogger.getInstance().updateDate();
    	buzzer.update();
    	}
    
    @Override
    public void autonomousInit() {
    	buzzer.enable(1.25);
    	Logger.getInstance().println("Autonomous Init",true);
    	Logger.getInstance().updateDate();
    	CSVLogger.getInstance().updateDate();
    	
    	Robot.chassis.resetPosition();
    	
        autonomousCommand = autoProgram.getSelected();
        // schedule the autonomous command (example)
        if (autonomousCommand != null) autonomousCommand.start();

    	Logger.getInstance().println("Running Auto: " + autonomousCommand.getName(),true);    
    	
	    if(Math.abs(Robot.chassis.getAngle()) > 0.2){
	    	Robot.chassis.resetPosition();
	    	Logger.getInstance().println("Gyro failed to reset, retrying", Severity.ERROR);
	    }
    }
   

    /**
     * This function is called periodically during autonomous
     */
    @Override
    public void autonomousPeriodic() {
    	chassis.calcXY();

        Scheduler.getInstance().run();
        chassis.pidDriveAuto();
    	CSVLogger.getInstance().writeData();
		buzzer.update();
    }

    @Override
    public void teleopInit() {
        //VERIFY copy over code from 2017 (and update it of course!)
        Logger.getInstance().println("Teleop Init", Severity.INFO);
    	Logger.getInstance().updateDate();
    	CSVLogger.getInstance().updateDate();
    	buzzer.enable(1.25);
		Robot.lift.stopLift();
		Robot.climber.lockPlatforms();
		Robot.arm.stopArm();
		Robot.hand.stopWrist();
		Robot.grabber.pickupOff();
		Robot.chassis.stopDrive();
		Robot.grabber.stopGrabber();
        // This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (autonomousCommand != null) autonomousCommand.cancel();
    }

    /**
     * This function is called periodically during operator control
     */
    @Override
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
        //VERIFY copy over code from 2017 (and update it of course!) -JB
            chassis.calcXY();
            Scheduler.getInstance().run();
            chassis.pidDrive();
        	CSVLogger.getInstance().writeData();
        	buzzer.update();
    }
    
    
    // -----------------------------------------------------------
    // Get Methods
    // -----------------------------------------------------------
    public boolean getIsPracticeRobot() {
    	return frills.getIsPracticeRobot();
    }
    
    public static double getHeight() {
    	double armContr = Math.sin(Math.toRadians(Robot.arm.getArmAngle())) * ArmConst.length;
    	double handContr = Math.sin(Math.toRadians(Robot.hand.getHandAngle())) * HandConst.length;
    	double liftContr = 40 + Robot.lift.getPosition();
    	return liftContr + armContr + handContr;
    }
    //other   
}
