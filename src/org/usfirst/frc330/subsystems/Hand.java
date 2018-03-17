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
import org.usfirst.frc330.constants.ArmConst;
import org.usfirst.frc330.constants.HandConst;
import org.usfirst.frc330.util.CSVLoggable;
import org.usfirst.frc330.util.CSVLogger;
import org.usfirst.frc330.util.Logger;
import org.usfirst.frc330.util.Logger.Severity;

import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.command.Subsystem;

import com.ctre.phoenix.ParamEnum;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.Faults;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.RemoteLimitSwitchSource;
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
        
        //Setup CAN Talons
        wrist.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, HandConst.CAN_Timeout);
		wrist.setInverted(true);
		wrist.setSensorPhase(false);
		
		setPIDConstants(HandConst.proportional,HandConst.integral,HandConst.derivative,true);
		setHandAbsoluteTolerance(HandConst.defaultTolerance);
		
		wrist.configForwardSoftLimitEnable(false, HandConst.CAN_Timeout); //Set these false until calibrated
		wrist.configReverseSoftLimitEnable(false, HandConst.CAN_Timeout);
		wrist.setNeutralMode(NeutralMode.Brake);
		
		wrist.configOpenloopRamp(HandConst.VoltageRampRate, HandConst.CAN_Timeout);
		wrist.configNominalOutputForward(0, HandConst.CAN_Timeout);	
		wrist.configNominalOutputReverse(0, HandConst.CAN_Timeout);	
		
		wrist.configPeakOutputForward(HandConst.MaxOutputPercent, HandConst.CAN_Timeout);
        wrist.configPeakOutputReverse(-HandConst.MaxOutputPercent, HandConst.CAN_Timeout);
		
		wrist.configForwardLimitSwitchSource(RemoteLimitSwitchSource.Deactivated, LimitSwitchNormal.Disabled, 0, HandConst.CAN_Timeout);
		wrist.configReverseLimitSwitchSource(RemoteLimitSwitchSource.Deactivated, LimitSwitchNormal.Disabled, 0, HandConst.CAN_Timeout);
		
		//set feedback frame so that getClosedLoopError comes faster then 160ms
        wrist.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, HandConst.CAN_Status_Frame_13_Period, HandConst.CAN_Timeout);
        
        
        CSVLoggable temp = new CSVLoggable(true) {
			public double get() { return getHandAngle(); }
		};
		CSVLogger.getInstance().add("HandAngle", temp);
		
		temp = new CSVLoggable(true) {
			public double get() { return getHandAngleFromArm(); }
		};
		CSVLogger.getInstance().add("HandAngleFromArm", temp);
		
		temp = new CSVLoggable(true) {
			public double get() { return getSetpoint(); }
		};
		CSVLogger.getInstance().add("HandSetpoint", temp);

		temp = new CSVLoggable(true) {
			public double get() { return getWristOutput(); }
		};
		CSVLogger.getInstance().add("HandOutput", temp);
		
		temp = new CSVLoggable(true) {
			public double get() {
				if( getCalibrated()) {
					return 1.0;
				}
				else
					return 0.0;}
		};
		
		CSVLogger.getInstance().add("HandCalibrated", temp);	
    
    }
    
    public void calibrateMove() {
    	if(!calibrated) {
    		wrist.set(ControlMode.PercentOutput, HandConst.calibrationSpeed);
    	}
    }
    
    double tolerance = 0;
    
    @Override
    public void initDefaultCommand() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND


    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

    	this.setDefaultCommand(new ManualWrist());
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }

    @Override
    public void periodic() {
    	// Calibrate the first time the limit switch is pressed
    	if(!calibrated) {
    		if(!limitSwitch.get()) {
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


    public void manualWrist() {
    	double gamepadCommand = -Robot.oi.armGamePad.getRawAxis(0);
    	double position;
    	
    	if (Math.abs(gamepadCommand) > HandConst.gamepadDeadZone) {
    		this.setWrist(gamepadCommand/Math.abs(gamepadCommand)*Math.pow(gamepadCommand, 2)*0.4); //scaled to 0.4 max
    	}
    	else if (wrist.getControlMode() != ControlMode.Position) {
			position = this.getHandAngle();
			this.setAngle(position);
    	}  
    }
    
    
    //--------------------------------------------------------------------
  	// Set Methods
  	//--------------------------------------------------------------------
    
    public void setUncalibrated() {
		this.calibrated = false;
	}
    
    public void setHandAbsoluteTolerance(double absvalue) {
    	tolerance = absvalue;
	}
    
    public void setMaxWristOutput(double percentOut) {
    	wrist.configPeakOutputForward(percentOut,HandConst.CAN_Timeout_No_Wait);
    	wrist.configPeakOutputReverse(percentOut,HandConst.CAN_Timeout_No_Wait);
    	Logger.getInstance().println("Max wrist output set to: " + percentOut, Severity.INFO);
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
    	if (angleRelativeToGround > 90) {
    		absAngle = angleRelativeToGround - HandConst.rearSlopAdjust - Robot.arm.getArmAngle();
    	}
    	else {
    		absAngle = angleRelativeToGround + HandConst.slopAdjust - Robot.arm.getArmAngle();
    	}
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
    
    //--------------------------------------------------------------------
  	// Get Methods
  	//--------------------------------------------------------------------
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
    
    public double getSetpoint() {
    	if(wrist.getControlMode() == ControlMode.Position || wrist.getControlMode() == ControlMode.Velocity) {
			return ticksToDegrees(wrist.getClosedLoopTarget(0));
		}
		else {
			return 0;
		}
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
    
    //VERIFY Implement getHandOnTarget - MF
  	public boolean getHandOnTarget() {
  		double error = this.getSetpointRelArm() - this.getHandAngleFromArm();
      	return (Math.abs(error) < tolerance);
  	}
  	
  	public double getSetpointRelArm() {
  		return ticksToDegrees(wrist.getClosedLoopTarget(0));
  	}
    
    //--------------------------------------------------------------------
  	// Other Methods
  	//--------------------------------------------------------------------
    private int degreesToTicks(double degrees) {
    	return (int)(degrees/360.0 * HandConst.ticksPerEncoderRev * HandConst.gearRatio);
    }
    
    private double ticksToDegrees(int ticks) {
    	return (ticks * 360.0 / HandConst.ticksPerEncoderRev / HandConst.gearRatio);
    }

	public double getWristFirmwareVersion() {
		int firmwareVersion = wrist.getFirmwareVersion();
		return ((firmwareVersion & 0xFF00) >> 8) + (firmwareVersion & 0xFF) / 100.0;
	}
    
    
}
