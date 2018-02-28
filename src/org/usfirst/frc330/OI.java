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

import org.usfirst.frc330.commands.*;
import org.usfirst.frc330.commands.commandgroups.*;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
//import org.usfirst.frc330.commands.commandgroups.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
    //// CREATING BUTTONS
    // One type of button is a joystick button which is any button on a joystick.
    // You create one by telling it which joystick it's on and which button
    // number it is.
    // Joystick stick = new Joystick(port);
    // Button button = new JoystickButton(stick, buttonNumber);

    // There are a few additional built in buttons you can use. Additionally,
    // by subclassing Button you can create custom triggers and bind those to
    // commands the same as any other Button.

    //// TRIGGERING COMMANDS WITH BUTTONS
    // Once you have a button, it's trivial to bind it to a button in one of
    // three ways:

    // Start the command when the button is pressed and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenPressed(new ExampleCommand());

    // Run the command while the button is being held down and interrupt it once
    // the button is released.
    // button.whileHeld(new ExampleCommand());

    // Start the command when the button is released  and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenReleased(new ExampleCommand());


    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    public JoystickButton shiftLow_1;
    public Joystick driverL;
    public JoystickButton shiftHigh_1;
    public Joystick driverR;
    public JoystickButton liftStepUp_6;
    public JoystickButton liftStepDown_8;
    public Joystick armGamePad;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

    public OI() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS

        armGamePad = new Joystick(2);
        
        liftStepDown_8 = new JoystickButton(armGamePad, 8);
        liftStepDown_8.whenPressed(new LiftStepDown());
        liftStepUp_6 = new JoystickButton(armGamePad, 6);
        liftStepUp_6.whenPressed(new LiftStepUp());
        driverR = new Joystick(1);
        
        shiftHigh_1 = new JoystickButton(driverR, 1);
        shiftHigh_1.whenPressed(new ShiftHigh());
        driverL = new Joystick(0);
        
        shiftLow_1 = new JoystickButton(driverL, 1);
        shiftLow_1.whenPressed(new ShiftLow());


        // SmartDashboard Buttons
        SmartDashboard.putData("Autonomous Command", new AutonomousCommand());
        SmartDashboard.putData("ShiftLow", new ShiftLow());
        SmartDashboard.putData("ShiftHigh", new ShiftHigh());
        SmartDashboard.putData("OpenClaw", new OpenClaw());
        SmartDashboard.putData("CloseClaw", new CloseClaw());
        SmartDashboard.putData("SensorCloseClaw", new SensorCloseClaw());
        SmartDashboard.putData("ManualLift", new ManualLift());
        SmartDashboard.putData("UnlockPlatforms", new UnlockPlatforms());
        SmartDashboard.putData("ManualArm", new ManualArm());
        SmartDashboard.putData("ManualWrist", new ManualWrist());
        SmartDashboard.putData("RollerOn", new RollerOn());
        SmartDashboard.putData("RollerOff", new RollerOff());
        SmartDashboard.putData("RollerReverse", new RollerReverse());
        SmartDashboard.putData("RollerReverseOff", new RollerReverseOff());
        SmartDashboard.putData("DeployCube", new DeployCube());
        SmartDashboard.putData("IntakeCube", new IntakeCube());
        SmartDashboard.putData("dropoffPositionSwitch", new dropoffPositionSwitch());
        SmartDashboard.putData("dropoffPositionLow", new dropoffPositionLow());
        SmartDashboard.putData("dropoffPositionMed", new dropoffPositionMed());
        SmartDashboard.putData("dropoffPositionMax", new dropoffPositionMax());
        SmartDashboard.putData("Defense", new Defense());
        SmartDashboard.putData("ClimbReady", new ClimbReady());
        SmartDashboard.putData("FlipWrist", new FlipWrist());
        SmartDashboard.putData("HandLevel", new HandLevel());
        SmartDashboard.putData("IntakePortal", new IntakePortal());
        SmartDashboard.putData("UnlockRatchet", new UnlockRatchet());
        SmartDashboard.putData("LockRatchet", new LockRatchet());
        SmartDashboard.putData("LiftStepUp", new LiftStepUp());
        SmartDashboard.putData("LiftStepDown", new LiftStepDown());

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        SmartDashboard.putData("PushMe, I dare you", new Buzz());
        SmartDashboard.putData("QuickBuzz", new BuzzQuick());
        SmartDashboard.putData("ResetPosition", new ResetPosition());
        SmartDashboard.putData("DashboardArmMove", new DashboardArmMove());
        SmartDashboard.putNumber("DashboardArmMoveSetpoint", 0.0);
        SmartDashboard.putData("DashboardHandMove", new DashboardHandMove());
        SmartDashboard.putNumber("DashboardHandMoveSetpoint", 0.0);
        SmartDashboard.putData("DashboardLiftMove", new DashboardLiftMove());
        SmartDashboard.putNumber("DashboardLiftMoveSetpoint", 0.0);
    }

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=FUNCTIONS
    public Joystick getDriverL() {
        return driverL;
    }

    public Joystick getDriverR() {
        return driverR;
    }

    public Joystick getarmGamePad() {
        return armGamePad;
    }


    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=FUNCTIONS
}

