package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.shape.*;
import javafx.scene.Group;
import javafx.scene.input.*;
import javafx.scene.image.*;
import javafx.scene.paint.*;
import javafx.animation.AnimationTimer;

public class Main extends Application {
    public static double currentX = 0;
    public static double currentY = 0;
    public static double currentAngle = 0;

    public static boolean forward = false;
    public static boolean backward = false;
    public static boolean clockwise = false;
    public static boolean counterclockwise = false;

    /**
     * Main (launch app)
     *
     * @param args  Standard
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Starts App
     *
     * @param stage         Stage for App
     * @throws Exception    Crash Prevention
     */
    @Override
    public void start(Stage stage) throws Exception {
        double width = getPixels(45.72);
        double height = width;

        //Background
        Image backgroundImage = new Image("sample/Grid.png", 700, 700, true, false);
        ImageView imgView = new ImageView(backgroundImage);

        //Robot
        Image robotImage = new Image("sample/Robot.png");
        Rectangle robot = new Rectangle(0, 0, width, height);
        robot.setFill(new ImagePattern(robotImage));

        //Initialize Group
        Group group = new Group(imgView);
        group.getChildren().add(robot);

        //Set Stage
        Scene scene = new Scene(group, 700, 700); //SET SP
        stage.setTitle("Overcharged Field Tracking");
        stage.setScene(scene);
        stage.show();

        //Initial Robot Positions
        //Blue Alliance
        //setRobot(robot, 0, 400, 90);
        //Red Alliance
        setRobot(robot, 610, 400, 270);

        //Move Robot (With Keystone)
        scene.setOnKeyPressed(e ->{
            if(e.getCode() == KeyCode.UP) forward = true;
            if(e.getCode() == KeyCode.DOWN) backward = true;
            if(e.getCode() == KeyCode.RIGHT) clockwise = true;
            if(e.getCode() == KeyCode.LEFT) counterclockwise = true;
        });

        scene.setOnKeyReleased(e ->{
            if(e.getCode() == KeyCode.UP) forward = false;
            if(e.getCode() == KeyCode.DOWN) backward = false;
            if(e.getCode() == KeyCode.RIGHT) clockwise = false;
            if(e.getCode() == KeyCode.LEFT) counterclockwise = false;
        });

        //Animate According to Keystroke
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double travel = 0, angle = 0;

                if (forward) travel = -6;
                if (backward) travel = 6;
                if (clockwise)  angle = 4;
                if (counterclockwise)  angle = -4;

                moveRobot(robot, travel,  angle);
            }
        };
        timer.start();
    }

    /**
     * Converts CM to Pixels
     *
     * @param cm    distance in centimeters
     * @return      double of pixels for given centimeters
     */
    private double getPixels(double cm) {
        return (cm * 700)/358.14;
    }

    /**
     * Sets Robot (Initialization)
     *
     * @param robot     robot rectangle shape
     * @param x         x coordinate for starting
     * @param y         y coordinate for starting
     * @param angle     angle for starting
     */
    private void setRobot(Rectangle robot, double x, double y, double angle) {
        robot.setX(x);
        robot.setY(700 - y);
        robot.setRotate(angle);

        currentX = x;
        currentY = y;
        currentAngle = angle;
    }

    /**
     * Moves Robot
     *
     * @param robot     robot rectangle shape
     * @param travel    distance travelled forwards or backwards
     * @param angle     global angle turned
     */
    private void moveRobot(Rectangle robot, double travel, double angle) {
        currentX = clip(0, 610, currentX + (-travel * Math.sin(Math.toRadians(currentAngle))));
        currentY = clip(0, 610, currentY + (travel * Math.cos(Math.toRadians(currentAngle))));
        currentAngle = clipAngle(currentAngle + angle);

        robot.setX(currentX);
        robot.setY(currentY);

        robot.setRotate(currentAngle);

        System.out.println("CurrentX: " + currentX + " Current Y: " + currentY + " Current Angle: " + currentAngle);
    }

    /**
     * Clips Double Within Bounds
     * @param lowerBound    lower bound to never cross
     * @param upperBound    upper bound to never cross
     * @param value         parameter being manipulated
     * @return              bounded value
     */
    private double clip(double lowerBound, double upperBound, double value) {
        if (value < lowerBound) value = lowerBound;
        else if (value > upperBound) value = upperBound;
        return value;
    }

    /**
     * Clips Angle to 0 - 360 Degrees
     *
     * @param angle     parameter being manipulated
     * @return          bounded angle
     */
    private double clipAngle(double angle) {
        if(angle <= 0) angle += 360;
        if(angle >= 360) angle -= 360;
        return angle;
    }
}
