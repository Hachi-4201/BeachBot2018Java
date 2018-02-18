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
import org.usfirst.frc330.commands.commandgroups.Calibrate;
import org.usfirst.frc330.constants.LiftConst;
import org.usfirst.frc330.util.CSVLoggable;
import org.usfirst.frc330.util.CSVLogger;
import org.usfirst.frc330.util.Logger;
import org.usfirst.frc330.util.Logger.Severity;

import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.command.Subsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;

// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS


/**
 *
 */

public class Lift extends Subsystem {

	boolean calibrated = false; //Has the encoder been properly zeroed?
	
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    private WPI_TalonSRX lift1;
    private WPI_TalonSRX lift2;
    private WPI_TalonSRX lift3;
    private DigitalInput limitSwitch;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

    public Lift() {
    	
    	super();
    	
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        lift1 = new WPI_TalonSRX(0);
        
        
        lift2 = new WPI_TalonSRX(4);
        
        
        lift3 = new WPI_TalonSRX(5);
        
        
        limitSwitch = new DigitalInput(7);
        addChild(limitSwitch);
        

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        
        // Setup CAN Talons
        lift1.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, LiftConst.timeOutMS);
        lift1.setInverted(false); // Set true if the motor direction does not match the sensor direction
        setPIDConstants(LiftConst.proportional, LiftConst.integral, LiftConst.derivative, true);
        setLiftAbsoluteTolerance(LiftConst.tolerance);
        
        // Limits are now set and enabled after calibration
        
        // lift1 is considered the main controller, 2 and 3 follow 
        lift1.configForwardSoftLimitEnable(false, 0); //False until after calibration
        lift1.configReverseSoftLimitEnable(false, 0);
        lift1.setNeutralMode(NeutralMode.Brake);
        lift1.configOpenloopRamp(LiftConst.VoltageRampRate, LiftConst.timeOutMS);
        lift1.configPeakOutputForward(LiftConst.MaxOutputPercent, LiftConst.timeOutMS);
        lift1.configPeakOutputReverse(-LiftConst.MaxOutputPercent, LiftConst.timeOutMS);

        lift2.set(ControlMode.Follower, lift1.getDeviceID());
        lift2.configForwardSoftLimitEnable(false, 0);
        lift2.configReverseSoftLimitEnable(false, 0);
        lift2.setInverted(true);
        lift2.setNeutralMode(NeutralMode.Brake);
        
        lift3.set(ControlMode.Follower, lift1.getDeviceID());
        lift3.configForwardSoftLimitEnable(false, 0);
        lift3.configReverseSoftLimitEnable(false, 0);
        lift3.setNeutralMode(NeutralMode.Brake);

        //------------------------------------------------------------------------------
        // Logging
        //------------------------------------------------------------------------------
        CSVLoggable temp = new CSVLoggable(true) {
			public double get() { return getPosition(); }
		};
		CSVLogger.getInstance().add("LiftPosition", temp);
		
		temp = new CSVLoggable(true) {
			public double get() { return getOutput(); }
		};
		CSVLogger.getInstance().add("LiftOutput", temp);
		
		temp = new CSVLoggable(true) {
			public double get() { return getSetpoint(); }
		};
		CSVLogger.getInstance().add("LiftSetpoint", temp);

		temp = new CSVLoggable(true) {
			public double get() {
				if( getCalibrated()) {
					return 1.0;
				}
				else
					return 0.0;}
		};
		
		CSVLogger.getInstance().add("LiftCalibrated", temp);	
    }

    @Override
    public void initDefaultCommand() {
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

        setDefaultCommand(new ManualLift());

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }

    @Override
    public void periodic() {
    	// Calibrate the first time the limit switch is pressed
    	if(!calibrated) {
    		if(limitSwitch.get()) {
    			lift1.setSelectedSensorPosition(0, 0, 0);
    			lift1.disable();
    			calibrated = true;
    			lift1.configForwardSoftLimitThreshold(inchesToTicks(LiftConst.upperLimit), 0);
    	        lift1.configReverseSoftLimitThreshold(inchesToTicks(LiftConst.lowerLimit), 0);
    			lift1.configForwardSoftLimitEnable(true,0);
    	        lift1.configReverseSoftLimitEnable(true, 0);
    		}
    	}
    }

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=PIDGETTERS


    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=PIDGETTERS

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    
    public void setPIDConstants (double P, double I, double D, boolean timeout)
	{
    	if(timeout) {
    		//assume using main PID loop (index 0)
    		lift1.config_kP(0, P, 10);
    		lift1.config_kI(0, I, 10);
    		lift1.config_kD(0, D, 10);
    	}
    	else {
	    	//assume using main PID loop (index 0)
			lift1.config_kP(0, P, 0);
			lift1.config_kI(0, I, 0);
			lift1.config_kD(0, D, 0);
    	}
	
        Logger.getInstance().println("Lift PID set to: " + P + ", " + I + ", " + D, Severity.INFO);
	}
    
    //------------------------------------------------------------------------------
    // GET Methods
    //------------------------------------------------------------------------------
    
    double tempInfo;
    
    public double getPosition() {
    	return ticksToInches(lift1.getSelectedSensorPosition(0)); //arg 0 is primary PID
    }
    
    //VERIFY Implement getOutput() -MF
    public double getOutput() {
    	return lift1.getMotorOutputVoltage();
    }
    
    public double getSetpoint() {
    	if(lift1.getControlMode() != ControlMode.Disabled) {
    		return ticksToInches(lift1.getClosedLoopTarget(0));
    	}
    	else {
    		return 0;
    	}
    }
    
    public boolean getCalibrated() {
    	return calibrated;
    }
    
    //------------------------------------------------------------------------------
    // SET Methods
    //------------------------------------------------------------------------------

    //VERIFY Joey: set has a single parameter and double parameter option. Use the double parameter option -JB
    // and set it to controlMode: position (see 2016 arm if you want an example)
    public void setLiftPosition(double setpoint) {
    	if(calibrated) {
    		lift1.set(ControlMode.Position, inchesToTicks(setpoint));
    	}
    	else {
    		Scheduler.getInstance().add(new Calibrate());
    	}
    }
    
    //------------------------------------------------------------------------------
    // Support Methods
    //------------------------------------------------------------------------------
    private double ticksToInches(int ticks) {
    	return ((double)ticks / (double)LiftConst.ticksPerRev * LiftConst.inchesPerRev);
    }
    
    //Methods to check if the lift is on target
    public boolean onLiftTarget() {
    	double error = ticksToInches(lift1.getClosedLoopError(0));
        return (Math.abs(error) < tolerance);
    }
    
    double tolerance;
    
    public void setLiftAbsoluteTolerance(double absvalue) {
    	tolerance = absvalue;
	}

	public void stopLift() {
		// VERIFY create stopLift method -mf
		lift1.disable();
		Logger.getInstance().println("Lift disabled", Logger.Severity.INFO);		
	}

	public boolean getLiftOnTarget() {
		double error = lift1.getClosedLoopError(0);
    	return (Math.abs(error) < tolerance);
	}
	
	
	public void calibrateMove() {
    	if(!calibrated) {
    		lift1.set(ControlMode.PercentOutput, LiftConst.calibrationSpeed);
    	}
    }
	
	public int inchesToTicks(double inches) {
		return (int)(inches*LiftConst.ticksPerRev/LiftConst.inchesPerRev);
	}
	
}