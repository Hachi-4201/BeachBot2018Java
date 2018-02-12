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
import org.usfirst.frc330.constants.ArmConst;
import org.usfirst.frc330.constants.WristConst;
import org.usfirst.frc330.util.CSVLoggable;
import org.usfirst.frc330.util.CSVLogger;
import org.usfirst.frc330.util.Logger;
import org.usfirst.frc330.util.Logger.Severity;

import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;

import com.ctre.phoenix.ParamEnum;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS


/**
 *
 */
public class Arm extends Subsystem {

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    private WPI_TalonSRX armL;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

    public Arm() {
    	
    	super();
    	SmartDashboard.putData("Arm", this);
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        armL = new WPI_TalonSRX(1);
        
        

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        
        //VERIFY Joey: port the following commands for both elbow and wrist -JB
		//        armL.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
		//    	armL.reverseSensor(false);
		//    	armL.reverseOutput(false);
		//    	setPIDConstants(ArmConst.proportional, ArmConst.integral, ArmConst.derivative);
		//    	setArmAbsoluteTolerance(ArmConst.tolerance);
		//    	setLowerSoftLimit(ArmConst.limitLowerAngle);
		//    	setUpperSoftLimit(ArmConst.limitUpperAngle);
		//    	armL.enableForwardSoftLimit(true);
		//    	armL.enableReverseSoftLimit(true);
		//    	armL.enableBrakeMode(true);
		//    	armL.setVoltageRampRate(ArmConst.VoltageRampRate);
		//    	armL.configMaxOutputVoltage(ArmConst.MaxOutputVoltage);
        
        //		Joey's notes							all time outs estimated to 10 ms
        												//Device,pidIdx,timeoutMS
        armL.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
        armL.setInverted(false);
        armL.setSensorPhase(false);
        setPIDConstantsArm(ArmConst.proportional, ArmConst.integral, ArmConst.derivative,true);
        setArmAbsoluteTolerance(ArmConst.tolerance);

        setLowerSoftLimit(ArmConst.minArm);
		setUpperSoftLimit(ArmConst.maxArm);
		armL.configForwardSoftLimitEnable(true, 10);
		armL.configReverseSoftLimitEnable(false, 10);
		armL.setNeutralMode(NeutralMode.Brake);
		//create constants for these two in ArmConst
		armL.configOpenloopRamp(ArmConst.VoltageRampRate, 10);
		armL.configNominalOutputForward(ArmConst.MaxOutputVoltage, 10);
		
        //--------------------------------------------------------------------
    	// Logging
    	//--------------------------------------------------------------------
        //VERIFY log all of the get commands. See 2017 as example -JB
		CSVLoggable temp = new CSVLoggable(true) {
			public double get() { return getArmAngle(); }
		};
		CSVLogger.getInstance().add("ArmAngle", temp);

		temp = new CSVLoggable(true) {
			public double get() { return getArmOutput(); }
		};
		CSVLogger.getInstance().add("ArmOutput", temp);
		
		temp = new CSVLoggable(true) {
			public double get() { return getCurrentQuadrant(); }
		};
		CSVLogger.getInstance().add("ArmQuadrant", temp);
		
		temp = new CSVLoggable(true) {
			public double get() { return getSetpoint(); }
		};
		CSVLogger.getInstance().add("ArmSetpoint", temp);

		temp = new CSVLoggable(true) {
			public double get() { return getLowerLimit(); }
		};
		CSVLogger.getInstance().add("ArmLowerLimit", temp);
		
		temp = new CSVLoggable(true) {
			public double get() { return getUpperLimit(); }
		};
		CSVLogger.getInstance().add("ArmUpperLimit", temp);	
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
	
	//--------------------------------------------------------------------
	// Get Methods
	//--------------------------------------------------------------------
	
	//VERIFY implement getArmAngle -JB
    public double getArmAngle()
	{
		return (-convertRotationsToDegrees(armL.getSelectedSensorPosition(0)));
	} 
    
	
	//VERIFY Implement getArmLowerLimit, getArmUpperLimit -JB
    
    public double getLowerLimit()
	{
		return (convertTicksToDegrees((int)armL.configGetParameter(ParamEnum.eForwardSoftLimitThreshold, 0, 0)));

	}
    public double getUpperLimit()
	{
		return (convertTicksToDegrees((int)armL.configGetParameter(ParamEnum.eReverseSoftLimitThreshold, 0, 0)));

	}
    
    
    
	
	//VERIFY implement get armOutput -JB
    public double getArmOutput() {
		return armL.getMotorOutputVoltage()/armL.getBusVoltage();
	}
	
    
	
	
	
	
	
    
    //VERIFY Implement getArmOnTarget - MF
    public boolean getArmOnTarget() {
    	double error = armL.getClosedLoopError(0);
    	return (Math.abs(error) < tolerance);
    }
    
	 public int getCurrentQuadrant() {
	    	return (int)(getArmAngle()/ArmConst.maxAngleDegrees);
	 }
	 public double getSetpoint() {
		 if(armL.getControlMode() != ControlMode.Disabled) {
			 return convertRotationsToDegrees(-armL.getClosedLoopTarget(0));
		 }
		 else {
			 return 0;
		 }
	    
	}
	//--------------------------------------------------------------------
	// Set Methods
	//--------------------------------------------------------------------
	
	//VERIFY Implement setArmOutput -JB
    public void setArm(double output) {
    //	changeControlMode(ControlMode.PercentOutput);
    	armL.set(ControlMode.PercentOutput, output);
    }
    //VERIFY Implement setArmAngle -JB
    public void setArmAngle(double position) {
    	armL.set(ControlMode.Position, position);
    	armL.set(convertDegreesToRotations(position));
    //	if (SCtable != null)
    //		SCtable.putNumber("setpoint", position);
    }
    
    
    //VERIFY Implement setArmPIDConstants -JB
    public void setPIDConstantsArm (double P, double I, double D, boolean timeout)
   	{
       	if(timeout) {
       		//assume using main PID loop (index 0)
       		armL.config_kP(0, P, 10);
       		armL.config_kI(0, I, 10);
       		armL.config_kD(0, D, 10);
       	}
       	else {
   	    	//assume using main PID loop (index 0)
   			armL.config_kP(0, P, 0);
   			armL.config_kI(0, I, 0);
   			armL.config_kD(0, D, 0);
       	}
       	
           Logger.getInstance().println("Lift PID set to: " + P + ", " + I + ", " + D, Severity.INFO);
   	}
    
    //VERIFY IMplement setMaxArmOutput -JB
    public void setMaxArmOutput(double percentOut){
    	armL.configNominalOutputForward(percentOut,10);
    	Logger.getInstance().println("Max Arm output set to: " + percentOut, Severity.INFO);
    }
    
  
    
       	
    
    double tolerance = 0;
    public void setArmAbsoluteTolerance(double absvalue) {
    	tolerance = absvalue;
	}
    
    
    
    
	
	//--------------------------------------------------------------------
	// Other Methods
	//--------------------------------------------------------------------
    
    // VERIFY:stopArm -JB
	public void stopArm() {
		if (armL.isAlive())
		{
			armL.setIntegralAccumulator(0.0, 0, 0);
		}
		armL.set(0);
		armL.disable(); 
		Logger.getInstance().println("Arm disabled", Logger.Severity.INFO); 
	}
	//VERIFY:isEnable - JB
	public boolean isEnable() {
		return armL.isAlive();
	}
		
	//VERIFY Implement manualArm -JB
	 int inertiaCounter;
	    public void manualArm() {	
	    	double gamepadCommand = -Robot.oi.armGamePad.getY();
	    	double angle;
	    	
	    	if (Math.abs(gamepadCommand) > ArmConst.gamepadDeadZone) {
	    		setArm(gamepadCommand/Math.abs(gamepadCommand)*Math.pow(gamepadCommand, 2));
	    		inertiaCounter = ArmConst.inertiaCounter;
	    	}
	    	else if (inertiaCounter > 0) {
	    		inertiaCounter--;
				setArm(0);
	    	}
	    	else if ( armL.getControlMode() != ControlMode.Position) {
				angle = getArmAngle();
				
				
				if (angle < getLowerLimit())
					angle = getLowerLimit();
				else if (angle > getUpperLimit())
					angle = getUpperLimit();

				setArmAngle(angle);
	    	} 
	    	
	    }

	    
	    private void setLowerSoftLimit(double lowerAngle) {
	    	// VERIFY -JB
	    	armL.configForwardSoftLimitThreshold(convertDegreesToTicks(lowerAngle), (int)ArmConst.defaultTimeout);
	    }
	    
	    private void setUpperSoftLimit(double upperAngle) {
	    	// VERIFY -JB
	    	armL.configReverseSoftLimitThreshold(convertDegreesToTicks(upperAngle), (int)ArmConst.defaultTimeout);
	    }
	    	
	    
	    private int convertDegreesToTicks(double degrees) {
	    	return (int)(degrees * ArmConst.maxEncoderCounts / ArmConst.maxAngleDegrees + 0.5);
	    }
	    
	    private double convertTicksToDegrees(int ticks) {
	    	return (ticks * ArmConst.maxAngleDegrees / ArmConst.maxEncoderCounts);
	    }
	    
	    private double convertDegreesToRotations(double degrees) {
	    	return (-degrees / ArmConst.maxAngleDegrees);
	    }
	    
	    private double convertRotationsToDegrees(double rotations) {
	    	return (rotations * ArmConst.maxAngleDegrees);
	    }
	    


	    
}

