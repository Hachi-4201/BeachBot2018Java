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


import org.usfirst.frc330.Robot;
import org.usfirst.frc330.commands.*;
import org.usfirst.frc330.constants.LiftConst;
import org.usfirst.frc330.util.CSVLoggable;
import org.usfirst.frc330.util.CSVLogger;
import org.usfirst.frc330.util.Logger;
import org.usfirst.frc330.util.Logger.Severity;

import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS


/**
 *
 */
public class Lift extends Subsystem {

	private NeutralMode mode;
	
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    private WPI_TalonSRX lift;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

    public Lift() {
    	
    	super();
    	
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        lift = new WPI_TalonSRX(0);
        
        

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        
        //No cascaded PID, 10ms timeout
        lift.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
        lift.setInverted(false); //set if the motor direction does not match the sensor direction
        setPIDConstants(LiftConst.proportional, LiftConst.integral, LiftConst.derivative, true);
        setLiftAbsoluteTolerance(LiftConst.tolerance);
        // TODO Joey: I don't see you setting the lower and upper limits. You enable the limits, but don't set them
		//      setLowerSoftLimit(ArmConst.limitLowerAngle);
		//    	setUpperSoftLimit(ArmConst.limitUpperAngle);
		//    	armL.enableForwardSoftLimit(true);
		//    	armL.enableReverseSoftLimit(true);        
		//    	armL.enableBrakeMode(true);
		//    	armL.setVoltageRampRate(ArmConst.VoltageRampRate);
		//    	armL.configMaxOutputVoltage(ArmConst.MaxOutputVoltage);
        
        lift.configForwardSoftLimitEnable(true,0);
        lift.configReverseSoftLimitEnable(true, 0);
        lift.setNeutralMode(NeutralMode.Brake);
        lift.configOpenloopRamp(LiftConst.VoltageRampRate, LiftConst.timeOutMS);
        lift.configPeakOutputForward(LiftConst.MaxOutputPercent, LiftConst.timeOutMS);
        lift.configPeakOutputReverse(LiftConst.MaxOutputPercent, LiftConst.timeOutMS);
        
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
		//VERIFY Log lift setpoint
		
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
        // Put code here to be run every loop

    }

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=PIDGETTERS


    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=PIDGETTERS

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    
    public void setPIDConstants (double P, double I, double D, boolean timeout)
	{
    	if(timeout) {
    		//assume using main PID loop (index 0)
    		lift.config_kP(0, P, 10);
    		lift.config_kI(0, I, 10);
    		lift.config_kD(0, D, 10);
    	}
    	else {
	    	//assume using main PID loop (index 0)
			lift.config_kP(0, P, 0);
			lift.config_kI(0, I, 0);
			lift.config_kD(0, D, 0);
    	}
    	
        Logger.getInstance().println("Lift PID set to: " + P + ", " + I + ", " + D, Severity.INFO);
	}
    
    
    //------------------------------------------------------------------------------
    // GET Methods
    //------------------------------------------------------------------------------
    
    double tempInfo;
    public double getPosition() {
    	tempInfo = lift.getSelectedSensorPosition(0);
    	return tempInfo*LiftConst.positionScaler;
    	//arg 0 is primary PID
    }
    
    //VERIFY Implement getOutput() -MF
    public double getOutput() {
    	return lift.getMotorOutputVoltage();
    }
    
    public double getSetpoint() {
    	return lift.getClosedLoopTarget(0);
    }
    
    //------------------------------------------------------------------------------
    // SET Methods
    //------------------------------------------------------------------------------

    //TODO Joey: set has a single parameter and double parameter option. Use the double parameter option
    // and set it to controlMode: position (see 2016 arm if you want an example)
    public void setPosition(double setpoint) {
    	lift.set(setpoint);
    }
    
    //------------------------------------------------------------------------------
    // Support Methods
    //------------------------------------------------------------------------------
    private double convertTicksToDegrees(int ticks) {
    	return (ticks * LiftConst.maxAngleDegrees / LiftConst.maxEncoderCounts);
    }
    
    //Methods to check if the lift is on target
    public boolean onLiftTarget() {
    	double error = convertTicksToDegrees(lift.getClosedLoopError(0));
        return (Math.abs(error) < tolerance);
    }
    
    double tolerance;
    public void setLiftAbsoluteTolerance(double absvalue) {
    	tolerance = absvalue;
	}

	public void stopLift() {
		// VERIFY create stopLift method -mf
		lift.disable();
		Logger.getInstance().println("Lift disabled", Logger.Severity.INFO);
		
		
		
	}

	public void setLiftPosition(double position) {
		lift.set(ControlMode.Position, position);
		
	}

	public boolean getLiftOnTarget() {
		double error = lift.getClosedLoopError(0);
    	return (Math.abs(error) < tolerance);
	}
    
}

