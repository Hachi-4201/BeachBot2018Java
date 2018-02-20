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
import org.usfirst.frc330.commands.commandgroups.Calibrate;
import org.usfirst.frc330.constants.ArmConst;
import org.usfirst.frc330.constants.LiftConst;
import org.usfirst.frc330.util.CSVLoggable;
import org.usfirst.frc330.util.CSVLogger;
import org.usfirst.frc330.util.Logger;
import org.usfirst.frc330.util.Logger.Severity;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.command.Subsystem;

import com.ctre.phoenix.ParamEnum;
import com.ctre.phoenix.motorcontrol.ControlMode;
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

public class Arm extends Subsystem {
	
	boolean calibrated = false; // Has the encoder been properly zeroed?

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    private WPI_TalonSRX armL;
    private DigitalInput limitSwitch;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

    public Arm() {
    	
    	super();
    	SmartDashboard.putData("Arm", this);
    	
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        armL = new WPI_TalonSRX(1);
        
        
        limitSwitch = new DigitalInput(4);
        addChild(limitSwitch);
        

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        

        // Setup CAN Talons
        armL.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, ArmConst.CAN_Timeout);
        armL.setInverted(false);
        armL.setSensorPhase(false);
        
        setPIDConstantsArm(ArmConst.proportional, ArmConst.integral, ArmConst.derivative,true);
        setArmAbsoluteTolerance(ArmConst.tolerance);
		
		armL.configForwardSoftLimitEnable(false, ArmConst.CAN_Timeout); // Disable limits until after calibration
		armL.configReverseSoftLimitEnable(false, ArmConst.CAN_Timeout);
		armL.setNeutralMode(NeutralMode.Brake);
		
		//create constants for these two in ArmConst
		armL.configOpenloopRamp(0, ArmConst.CAN_Timeout);
		armL.configPeakOutputForward(ArmConst.MaxOutputPercent, ArmConst.CAN_Timeout);
        armL.configPeakOutputReverse(-ArmConst.MaxOutputPercent, ArmConst.CAN_Timeout);
        
        //set feedback frame so that getClosedLoopError comes faster then 160ms
        armL.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, ArmConst.CAN_Status_Frame_13_Period, ArmConst.CAN_Timeout);
		
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
		
		temp = new CSVLoggable(true) {
			public double get() { 
				if(getCalibrated()) {
					return 1.0;
				}
				else{
					return 0.0;
				}
			};
		};
		CSVLogger.getInstance().add("Arm Calibrated", temp);
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
        // Calibrate the first time the limit switch is pressed
    	if(!calibrated) {
    		if(limitSwitch.get()) {
    			armL.setSelectedSensorPosition(degreesToTicks(ArmConst.limitSwitchAngle), 0, ArmConst.CAN_Timeout_No_Wait);
    			armL.disable();
    			calibrated = true;
    			armL.configForwardSoftLimitThreshold(degreesToTicks(ArmConst.upperLimit), ArmConst.CAN_Timeout_No_Wait);
    			armL.configReverseSoftLimitThreshold(degreesToTicks(ArmConst.lowerLimit), ArmConst.CAN_Timeout_No_Wait);
    			armL.configForwardSoftLimitEnable(true, ArmConst.CAN_Timeout_No_Wait);
    			armL.configReverseSoftLimitEnable(true, ArmConst.CAN_Timeout_No_Wait);
    		}
    	}
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
		return (ticksToDegrees(armL.getSelectedSensorPosition(0)));
	} 
	
	//VERIFY Implement getArmLowerLimit, getArmUpperLimit -JB
    
    public double getLowerLimit()
	{
		return (ticksToDegrees((int)armL.configGetParameter(ParamEnum.eForwardSoftLimitThreshold, 0, ArmConst.CAN_Timeout_No_Wait)));
	}
    
    public double getUpperLimit()
	{
		return (ticksToDegrees((int)armL.configGetParameter(ParamEnum.eReverseSoftLimitThreshold, 0, ArmConst.CAN_Timeout_No_Wait)));
	}
	
	//VERIFY implement get armOutput -JB
    public double getArmOutput() {
		return armL.getMotorOutputVoltage()/armL.getBusVoltage();
	}
	
    public boolean getCalibrated() {
    	return calibrated;
    }	
    
    //VERIFY Implement getArmOnTarget - MF
    public boolean getArmOnTarget() {
    	double error = armL.getClosedLoopError(0);
    	return (Math.abs(error) < tolerance);
    }
    
	public double getSetpoint() {
		if(armL.getControlMode() != ControlMode.Disabled) {
			return ticksToDegrees(armL.getClosedLoopTarget(0));
		}
		else {
			return 0;
		}
	}
	
	//--------------------------------------------------------------------
	// Set Methods
	//--------------------------------------------------------------------
	
    public void setArm(double output) {
        if(calibrated) {
        	armL.set(ControlMode.PercentOutput, output);
        }
        else {
        	Scheduler.getInstance().add(new Calibrate());
        }
    }
    
    //VERIFY Implement setArmAngle -JB
    public void setArmAngle(double position) {
    	if(calibrated) {
    		armL.set(ControlMode.Position, degreesToTicks(position));
    	}
    	else {
    		Scheduler.getInstance().add(new Calibrate());
    	}
    }
    
    //VERIFY Implement setArmPIDConstants -JB
    public void setPIDConstantsArm (double P, double I, double D, boolean timeout)
   	{
       	if(timeout) {
       		//assume using main PID loop (index 0)
       		armL.config_kP(0, P, ArmConst.CAN_Timeout);
       		armL.config_kI(0, I, ArmConst.CAN_Timeout_No_Wait);
       		armL.config_kD(0, D, ArmConst.CAN_Timeout_No_Wait);
       	}
       	else {
   	    	//assume using main PID loop (index 0)
   			armL.config_kP(0, P, ArmConst.CAN_Timeout_No_Wait);
   			armL.config_kI(0, I, ArmConst.CAN_Timeout_No_Wait);
   			armL.config_kD(0, D, ArmConst.CAN_Timeout_No_Wait);
       	}
       	
        Logger.getInstance().println("Lift PID set to: " + P + ", " + I + ", " + D, Severity.INFO);
   	}
    
    //VERIFY IMplement setMaxArmOutput -JB
    public void setMaxArmOutput(double percentOut){
    	armL.configNominalOutputForward(percentOut,ArmConst.CAN_Timeout_No_Wait);
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
			armL.setIntegralAccumulator(0.0, 0, 0);
		
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
    
    private int degreesToTicks(double degrees) {
    	return (int)(degrees * ArmConst.ticksPerEncoderRev * ArmConst.gearRatio / 360.0);
    }
    
    private double ticksToDegrees(int ticks) {
    	return ((double)ticks * 360.0 / (double)ArmConst.ticksPerEncoderRev / ArmConst.gearRatio);
    }
	    
    public void calibrateMove() {
    	if(!calibrated) {
    		armL.set(ControlMode.PercentOutput, ArmConst.calibrationSpeed);
    	}
    }
}
