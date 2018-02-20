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
import org.usfirst.frc330.commands.commandgroups.Calibrate;
import org.usfirst.frc330.constants.HandConst;
import org.usfirst.frc330.util.Logger;
import org.usfirst.frc330.util.Logger.Severity;

import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.command.Subsystem;

import com.ctre.phoenix.ParamEnum;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.Faults;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS

/**
 *
 */

public class Hand extends Subsystem {

    boolean calibrated = false; //Has the encoder been properly zeroed?

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    private WPI_TalonSRX wrist;
    private DigitalInput limitSwitch;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

    public Hand() {
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        wrist = new WPI_TalonSRX(2);
        
        
        limitSwitch = new DigitalInput(8);
        addChild(limitSwitch);
        

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        
        wrist.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, HandConst.CAN_Timeout);
		wrist.setInverted(false);
		wrist.setSensorPhase(false);
		setPIDConstants(HandConst.proportional,HandConst.integral,HandConst.derivative,true);

		wrist.configForwardSoftLimitEnable(false, HandConst.CAN_Timeout); //Set these false until calibrated
		wrist.configReverseSoftLimitEnable(false, HandConst.CAN_Timeout);
		wrist.setNeutralMode(NeutralMode.Brake);
		wrist.configOpenloopRamp(HandConst.VoltageRampRate, HandConst.CAN_Timeout);
		wrist.configNominalOutputForward(HandConst.MaxOutputVoltage, HandConst.CAN_Timeout);	
		
		//set feedback frame so that getClosedLoopError comes faster then 160ms
        wrist.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, HandConst.CAN_Status_Frame_13_Period, HandConst.CAN_Timeout);
    }
    
    public void calibrateMove() {
    	if(!calibrated) {
    		wrist.set(ControlMode.PercentOutput, HandConst.calibrationSpeed);
    	}
    }
    
    public void setWrist(double output) {
    //	changeControlMode(ControlMode.PercentOutput);
    	if(calibrated) {
    		wrist.set(ControlMode.PercentOutput, output);
    	}
    	else {
    		Scheduler.getInstance().add(new Calibrate());
    	}
    }
    
    public void stopWrist() {
		wrist.disable();
		Logger.getInstance().println("Wrist disabled", Logger.Severity.INFO);
	}
    
    
    public void setAngle(double angleRelativeToGround) {
    	double absAngle; 
    	absAngle = angleRelativeToGround - Robot.arm.getArmAngle();
    	setAngleFromArm(absAngle);
    }
    
    public void setAngleFromArm(double angle) {
    	if(calibrated) {
    		wrist.set(ControlMode.Position, degreesToTicks(angle));
    	}
    	else {
    		Scheduler.getInstance().add(new Calibrate());
    	}  
    }
    
    public void setPIDConstants (double P, double I, double D, boolean timeout)
   	{
       	if(timeout) {
       		//assume using main PID loop (index 0)
       		wrist.config_kP(0, P, HandConst.CAN_Timeout);
       		wrist.config_kI(0, I, HandConst.CAN_Timeout);
       		wrist.config_kD(0, D, HandConst.CAN_Timeout);
       	}
       	else {
   	    	//assume using main PID loop (index 0)
   			wrist.config_kP(0, P, HandConst.CAN_Timeout_No_Wait);
   			wrist.config_kI(0, I, HandConst.CAN_Timeout_No_Wait);
   			wrist.config_kD(0, D, HandConst.CAN_Timeout_No_Wait);
       	}
       	
        Logger.getInstance().println("Wrist PID set to: " + P + ", " + I + ", " + D, Severity.INFO);
   	}
    
    double tolerance = 0;
    
  //VERIFY Implement getHandOnTarget - MF
  	public boolean getHandOnTarget() {
  		double error = wrist.getClosedLoopError(0);
      	return (Math.abs(error) < tolerance);
  	}
  	
  //VERIFY IMplement setMaxWristOutput -JB
    public void setMaxWristOutput(double percentOut) {
    	wrist.configNominalOutputForward(percentOut,HandConst.CAN_Timeout_No_Wait);
    	Logger.getInstance().println("Max wrist output set to: " + percentOut, Severity.INFO);
    }
    
  //VERIFY implement getWristOUtput - JB
    public double getWristOutput() {
  		return wrist.getMotorOutputVoltage()/wrist.getBusVoltage();
  	}
    
  //VERIFY Implement getWristLowerLimit, getWristUpperLimit -JB
    public double getWristLowerLimit() {
		return (ticksToDegrees((int)wrist.configGetParameter(ParamEnum.eForwardSoftLimitThreshold, 0, HandConst.CAN_Timeout_No_Wait)));
	}
    
    public double getWristUpperLimit() {
		return (ticksToDegrees((int)wrist.configGetParameter(ParamEnum.eReverseSoftLimitThreshold, 0, HandConst.CAN_Timeout_No_Wait)));
	}
	
  //VERIFY IMplement getLowerWristLimitTripped, getUpperWristLimitTripped -ejo
    public boolean getLowerLimitTripped(){
    	Faults allFaults = new Faults();	//define a faults variable with a new Faults() object (creates a list of faults w/out booleans)
    	wrist.getFaults(allFaults);			//passes empty list of faults to be updated. 
		return allFaults.ForwardSoftLimit; 	//return boolean of ForwardSoftLimit fault
	}
    
    public boolean getUpperLimitTripped(){
    	Faults allFaults = new Faults();
    	wrist.getFaults(allFaults);			 
		return allFaults.ReverseSoftLimit;
	}
    
    public double getHandAngleFromArm() {
		return (ticksToDegrees(wrist.getSelectedSensorPosition(0)));
	} 
    
    public double getHandAngle() {
    	return getHandAngleFromArm() + Robot.arm.getArmAngle();
    }
    
    public boolean getCalibrated() {
    	return calibrated;
    }
    
    @Override
    public void initDefaultCommand() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

        setDefaultCommand(new HandLevel());

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }

    @Override
    public void periodic() {
    	// Calibrate the first time the limit switch is pressed
    	if(!calibrated) {
    		if(limitSwitch.get()) {
    			wrist.setSelectedSensorPosition(degreesToTicks(HandConst.encLimitSwitch), 0, HandConst.CAN_Timeout);
    			wrist.disable();
    			calibrated = true;
    			wrist.configForwardSoftLimitThreshold(degreesToTicks(HandConst.encUpperLimit), HandConst.CAN_Timeout_No_Wait);
    			wrist.configReverseSoftLimitThreshold(degreesToTicks(HandConst.encLowerLimit), HandConst.CAN_Timeout_No_Wait);
    			wrist.configForwardSoftLimitEnable(true, HandConst.CAN_Timeout_No_Wait);
    			wrist.configReverseSoftLimitEnable(true, HandConst.CAN_Timeout_No_Wait);
    		}
    	}
    }

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=PIDGETTERS


    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=PIDGETTERS


    private int degreesToTicks(double degrees) {
    	return (int)(degrees/360.0 * HandConst.ticksPerEncoderRev * HandConst.gearRatio);
    }
    
    private double ticksToDegrees(int ticks) {
    	return (ticks * 360.0 / HandConst.ticksPerEncoderRev / HandConst.gearRatio);
    }
}
