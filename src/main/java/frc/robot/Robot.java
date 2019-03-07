package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Spark;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoSink;




public class Robot extends TimedRobot {
  //Defines the OtherStuffs
  Spark LEDs = new Spark(0);
  UsbCamera UsBCam1;
  UsbCamera UsBCam2;
  VideoSink server;


  // Defines the Drive Train
  WPI_TalonSRX frontRight = new WPI_TalonSRX(0);
  WPI_TalonSRX frontLeft = new WPI_TalonSRX(1);
  WPI_TalonSRX backRight = new WPI_TalonSRX(2);
  WPI_TalonSRX backLeft = new WPI_TalonSRX(3);
  DifferentialDrive mainDrive = new DifferentialDrive(frontLeft, frontRight);


  // Defines the Darts
  WPI_TalonSRX dartOne = new WPI_TalonSRX(4);
  WPI_TalonSRX dartTwo = new WPI_TalonSRX(5);
  DigitalInput dartOneRetractLimit = new DigitalInput(0);
  DigitalInput dartOneExtendLimit = new DigitalInput(1);
  DigitalInput dartTwoRetractLimit = new DigitalInput(2);
  DigitalInput dartTwoExtendLimit = new DigitalInput(3);
  AnalogInput dartOneAnalogInput = new AnalogInput(0);
  AnalogPotentiometer dartOnePot = new AnalogPotentiometer(dartOneAnalogInput, 360, 30);
  AnalogInput dartTwoAnalogInput = new AnalogInput(1);
  AnalogPotentiometer dartTwoPot = new AnalogPotentiometer(dartTwoAnalogInput, 360, 30);

  //Defines the Wrist Actuator
  WPI_TalonSRX wrist = new WPI_TalonSRX(6);


  //Defines intake/output
  WPI_TalonSRX spinner = new WPI_TalonSRX(7);
  Solenoid puncher = new Solenoid(0);


  //Defines the operator console
  Joystick operatorJoystick = new Joystick(0);
  boolean [] _operatorJoystickButtons = {false, false, false, false, false, false, false, false, false, false, false, false, false};
  Joystick driverJoystick = new Joystick(1);
  boolean [] _driverJoystickButtons = {false, false, false, false, false, false, false, false, false, false, false, false, false};
  Joystick operatorBoard = new Joystick(2);
  boolean [] _operatorBoardButtons = {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false};

  //Defines the 'Climber'
  Solenoid climberFront = new Solenoid(1);




  @Override
  public void robotInit() {
    //Set LEDs to a standard non-animated color scheme
    LEDs.set(0.49);


    //Sets back motors to be slaved to the front motors
    backLeft.follow(frontLeft);
    backRight.follow(frontRight);
    frontLeft.setInverted(false);
    backLeft.setInverted(false);



    //Defines the Camera Servers
    UsBCam1 = CameraServer.getInstance().startAutomaticCapture(0);
    UsBCam2 = CameraServer.getInstance().startAutomaticCapture(1);
    UsBCam1.setBrightness(10);
    server = CameraServer.getInstance().getServer();
  }




  @Override
  public void robotPeriodic() {
    //Some robot telemetry
    SmartDashboard.putBoolean("Dart One Retract", dartOneRetractLimit.get());
    SmartDashboard.putBoolean("Dart One Extend", dartOneExtendLimit.get());
    SmartDashboard.putBoolean("Dart Two Retract", dartTwoRetractLimit.get());
    SmartDashboard.putBoolean("Dart Two Extend", dartTwoExtendLimit.get());
    SmartDashboard.putNumber("Dart One Potentiometer Reading", dartOnePot.get());
    SmartDashboard.putNumber("Dart Two Potentiometer Reading", dartTwoPot.get());
  }




  @Override
  public void autonomousInit() {
  }




  @Override
  public void autonomousPeriodic() {
    //Sets Auto to run the Teleop code.
    teleopPeriodic();
    }




  @Override
  public void disabledPeriodic() {
    }
   
  


  @Override
  public void teleopPeriodic() {
    //Defines the Joystick Axis
    double forward = driverJoystick.getY();
    double turn = driverJoystick.getZ();
    double driveSpeedMultiplier = 1;


    //Sets up the Camera Chooser
    if(_driverJoystickButtons[7] == true){
      server.setSource(UsBCam1);
    }else if(_driverJoystickButtons[8] == true){
      server.setSource(UsBCam1);
    }


    //Sets up the Multiplier for driving speed
    if (driverJoystick.getRawAxis(3) <= 1 && driverJoystick.getRawAxis(3) >=0.8) {
      driveSpeedMultiplier = 0.1;
    }else if(driverJoystick.getRawAxis(3) <= 0.8 && driverJoystick.getRawAxis(3) >= 0.6) {
      driveSpeedMultiplier = 0.2;
    }else if(driverJoystick.getRawAxis(3) <= 0.6 && driverJoystick.getRawAxis(3) >= 0.4) {
      driveSpeedMultiplier = 0.3;
    }else if(driverJoystick.getRawAxis(3) <= 0.4 && driverJoystick.getRawAxis(3) >= 0.2) {
      driveSpeedMultiplier = 0.4;
    }else if(driverJoystick.getRawAxis(3) <= 0.2 && driverJoystick.getRawAxis(3) >= 0) {
      driveSpeedMultiplier = 0.5;
    }else if(driverJoystick.getRawAxis(3) <= 0 && driverJoystick.getRawAxis(3) >= -0.2) {
      driveSpeedMultiplier = 0.6;
    }else if(driverJoystick.getRawAxis(3) <= -0.2 && driverJoystick.getRawAxis(3) >= -0.4) {
      driveSpeedMultiplier = 0.7;
    }else if(driverJoystick.getRawAxis(3) <= -0.4 && driverJoystick.getRawAxis(3) >= -0.6) {
      driveSpeedMultiplier = 0.8;
    }else if(driverJoystick.getRawAxis(3) <= -0.6 && driverJoystick.getRawAxis(3) >= -0.8) {
      driveSpeedMultiplier = 0.9;
    }else if(driverJoystick.getRawAxis(3) <= -0.8 && driverJoystick.getRawAxis(3) >= -1) {
      driveSpeedMultiplier = 1.0;
    }else {
      driveSpeedMultiplier = 1.0;
    }


    
    //Puts the above code together to actually move the robot
    mainDrive.arcadeDrive(forward * -driveSpeedMultiplier * -1, turn * driveSpeedMultiplier);


    //Defines the Joystick Buttons
    boolean [] joystickBtns= new boolean [_operatorJoystickButtons.length];
		for(int i=1;i<_operatorJoystickButtons.length;++i)
      joystickBtns[i] = operatorJoystick.getRawButton(i); 

    boolean [] driverJoystickBtns= new boolean [_driverJoystickButtons.length];
		for(int i=1;i<_driverJoystickButtons.length;++i)
      driverJoystickBtns[i] = driverJoystick.getRawButton(i); 
      
    boolean [] operatorBoardBtns= new boolean [_operatorBoardButtons.length];
    for(int i=1;i<_operatorBoardButtons.length;++i)
      operatorBoardBtns[i] = operatorBoard.getRawButton(i);
      

      //Sets how the darts should react, given various inputs
      if(joystickBtns[1] == true && dartOneExtendLimit.get() == true && dartTwoRetractLimit.get() == false){
        dartOne.set(.50);
        SmartDashboard.putString("What is happening?", "Dart One up 50%");
      }else if(joystickBtns[1] == true && dartOneExtendLimit.get() == true && dartTwoRetractLimit.get() == true){
        dartTwo.set(-.50);
        dartOne.set(.10);
        SmartDashboard.putString("What is happening?", "Dart One up 10%, Dart Two down 50%");
      }else if(joystickBtns[2] == true && dartOneRetractLimit.get() == true && dartTwoRetractLimit.get() == false){
        dartOne.set(-.50);
        SmartDashboard.putString("What is happening?", "Dart One down 50%");
      }else if(joystickBtns[2] == true && dartOneRetractLimit.get() == true && dartTwoRetractLimit.get() == true){
        dartTwo.set(-.50);
        dartOne.set(-.10);
        SmartDashboard.putString("What is happening?", "Dart One down 10%, Dart Two down 50%");
      }else if(joystickBtns[3] == true && dartTwoRetractLimit.get() == true){
        dartTwo.set(-.50);
        SmartDashboard.putString("What is happening?", "Dart Two down 50%");
      }else if(joystickBtns[4] == true && dartTwoExtendLimit.get() == true){
        dartTwo.set(.50);
        SmartDashboard.putString("What is happening?", "Dart Two up 50%");
      }else if(joystickBtns[4] == true && dartTwoExtendLimit.get() == true && dartOneExtendLimit.get() == false){
        dartTwo.set(1);
        SmartDashboard.putString("What is happening?", "Dart Two up 100%");
      }else if(operatorBoardBtns[8] == true && dartTwoRetractLimit.get() == false && dartOnePot.get() > 170 == true && dartOneRetractLimit.get() == true){
        dartOne.set(-.5);
        SmartDashboard.putString("What is happening?", "Dart One down 50%");
      }else if(operatorBoardBtns[8] == true && dartTwoRetractLimit.get() == false && dartOnePot.get() < 150 == true && dartOneExtendLimit.get() == true){
        dartOne.set(.8);
        SmartDashboard.putString("What is happening?", "Dart One up 80%");
      }else if(operatorBoardBtns[8] == true && dartTwoRetractLimit.get() == true && dartOnePot.get() < 170 && dartOnePot.get() > 150 && dartTwoPot.get() > 220 && _operatorJoystickButtons[10] == true){
        dartTwo.set(-.5);
        SmartDashboard.putString("What is happening?", "Dart Two down 50%");
      }else if(operatorBoardBtns[8] == true && dartTwoExtendLimit.get() == true && dartOnePot.get() < 170 && dartOnePot.get() > 150 && dartTwoPot.get() < 216 && _operatorJoystickButtons[10] == true){
        dartTwo.set(.5);
        SmartDashboard.putString("What is happening?", "Dart Two up 50%");
      }else if(operatorBoardBtns[9] == true && dartTwoRetractLimit.get() == false && dartOnePot.get() > 242 == true){
        dartOne.set(-.5);
        SmartDashboard.putString("What is happening?", "Dart One down 50%");
      }else if(operatorBoardBtns[9] == true && dartTwoRetractLimit.get() == false && dartOnePot.get() < 238 == true){
        dartOne.set(.8);
        SmartDashboard.putString("What is happening?", "Dart One up 80%");
      }else if(operatorBoardBtns[9] == true && dartTwoRetractLimit.get() == true && dartOnePot.get() < 244 && dartOnePot.get() > 236 && dartTwoPot.get() > 137 && _operatorJoystickButtons[10] == true){
        dartTwo.set(-.5);
        SmartDashboard.putString("What is happening?", "Dart Two down 50%");
      }else if(operatorBoardBtns[9] == true && dartTwoExtendLimit.get() == true && dartOnePot.get() < 244 && dartOnePot.get() > 236 && dartTwoPot.get() < 133 && _operatorJoystickButtons[10] == true){
        dartTwo.set(.8);
        SmartDashboard.putString("What is happening?", "Dart Two up 80%");
      }else if(operatorBoardBtns[10] == true && dartTwoRetractLimit.get() == false && dartOnePot.get() > 332 == true){
        dartOne.set(-.5);
        SmartDashboard.putString("What is happening?", "Dart One down 50%");
      }else if(operatorBoardBtns[10] == true && dartTwoRetractLimit.get() == false && dartOnePot.get() < 328 == true){
        dartOne.set(.8);
        SmartDashboard.putString("What is happening?", "Dart One up 80%");
      }else if(operatorBoardBtns[10] == true && dartTwoRetractLimit.get() == true && dartOnePot.get() < 334 == true && dartOnePot.get() > 324 == true && dartTwoPot.get() > 331 && _operatorJoystickButtons[10] == true){
        dartTwo.set(-.5);
        SmartDashboard.putString("What is happening?", "Dart Two down 50%");
      }else if(operatorBoardBtns[10] == true && dartTwoExtendLimit.get() == true && dartOnePot.get() < 334 == true && dartOnePot.get() > 324 && dartTwoPot.get() < 327 && _operatorJoystickButtons[10] == true){
        dartTwo.set(.8);
        SmartDashboard.putString("What is happening?", "Dart Two up 80%");
      }else{
        dartOne.set(0);
        dartTwo.set(0);
        SmartDashboard.putString("What is happening?", "Both Darts 0%");
      }


      
      //If/Else for the Wrist
      if(joystickBtns[5] == true){
        wrist.set(0.50);
      }else if(joystickBtns[6] == true){
        wrist.set(-.50);
      }else{
        wrist.set(0);
      }


      //If/Else for the LEDs
      if(joystickBtns[7] == true){
        LEDs.set(-.85);
      }else if(joystickBtns[10] == true){
        LEDs.set(-.83);
      }else if(joystickBtns[9] == true){
        LEDs.set(-.80);
      }


      //If/Else for the Ball intake/output
      if(driverJoystickBtns[3] == true){
        spinner.set(1);
      }else if(driverJoystickBtns[4] == true){
        spinner.set(-.50);
      }else{
        spinner.set(0);
      }


      //If/Else for the Hatch Panel intake/output
      if(joystickBtns[11] == true){
        puncher.set(true);
      }else if(joystickBtns[12] == true){
        puncher.set(false);
      }else{
        puncher.set(false);
      }

      //If/Else for the front climber pistons
      if(driverJoystickBtns[7] == true){
        climberFront.set(true);
      }else if(driverJoystickBtns[8] == true){
        climberFront.set(false);
      }

      
  }




  @Override
  public void testPeriodic() {
  }
}