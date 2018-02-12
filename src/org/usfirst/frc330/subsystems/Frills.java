// RobotBuilder Version: 2.0BB
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc330.subsystems;


import org.usfirst.frc330.commands.*;
import org.usfirst.frc330.util.Logger;
import org.usfirst.frc330.util.Logger.Severity;

import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS


/**
 *
 */
public class Frills extends Subsystem {
	

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    public DigitalOutput buzzer;
    private DigitalInput isPracticeRobot;
    
    private UsbCamera driverCam;

    public Frills() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        buzzer = new DigitalOutput(5);
        addChild(buzzer);
        
        isPracticeRobot = new DigitalInput(6);
        addChild(isPracticeRobot);
        

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
    }

    @Override
    public void initDefaultCommand() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND


    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }

    @Override
    public void periodic() {
        // Put code here to be run every loop

    }

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=PIDGETTERS


    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=PIDGETTERS

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    
    
    
    //Camera Functions ----------------------------------------------------
    //VERIFY implement frills.initDriverCamera();  (See 2017 Code)
    //VERIFY Implement the other camera functions as well
    public void initDriverCamera() {
    	try {
    		driverCam = CameraServer.getInstance().startAutomaticCapture("Driver", "/dev/v4l/by-path/platform-ci_hdrc.0-usb-0:1.2.2:1.0-video-index0");
    		//if (!driverCam.setResolution(160, 120))
    		if (!driverCam.setResolution(160, 120))
    			Logger.getInstance().println("Resolution failed to set", Severity.ERROR);
    		if (!driverCam.setFPS(30))
    			Logger.getInstance().println("FPS failed to set", Severity.ERROR);
    		Logger.getInstance().println("DriverCam Get Video Mode: " + driverCam.getVideoMode().width +
    				", " + driverCam.getVideoMode().height + ", " + driverCam.getVideoMode().fps, Severity.DEBUG);
    		driverCam.setExposureManual(50);
    	}
    	catch (Exception ex) {
    		Logger.getInstance().println("Exception initializing Driver Camera", Severity.ERROR);
    		Logger.getInstance().printStackTrace(ex);
    	}
    }
    
    public void driverCameraBright(){
    	driverCam.setExposureAuto();
    }
    
    public void driverCameraDark(){
    	driverCam.setExposureManual(1);
    }
    //  </Camera Functions> -----------------------------------------------

	public boolean getIsPracticeRobot() {
		
		return isPracticeRobot.get();
	}

}

