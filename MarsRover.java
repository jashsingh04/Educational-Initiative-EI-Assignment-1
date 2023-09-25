
import java.util.*;
interface Command {
    void execute();
}

class Rover {
    int x, y;
    String direction;
    Map<String, Command> commands = new HashMap<>();

    Rover(int x, int y, String direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        commands.put("M", this::move);
        commands.put("L", this::turnLeft);
        commands.put("R", this::turnRight);
    }

    void move() {
        if (direction.equals("N")) y++;
        else if (direction.equals("S")) y--;
        else if (direction.equals("E")) x++;
        else if (direction.equals("W")) x--;
    }

    void turnLeft() {
        if (direction.equals("N")) direction = "W";
        else if (direction.equals("S")) direction = "E";
        else if (direction.equals("E")) direction = "N";
        else if (direction.equals("W")) direction = "S";
    }

    void turnRight() {
        if (direction.equals("N")) direction = "E";
        else if (direction.equals("S")) direction = "W";
        else if (direction.equals("E")) direction = "S";
        else if (direction.equals("W")) direction = "N";
    }

    void executeCommand(String command) {
        commands.get(command).execute();
    }
}

class Grid {
    int width, height;
    Set<Point> obstacles = new HashSet<>();

    Grid(int width, int height) {
        this.width = width;
        this.height = height;
    }

    void addObstacle(Point obstacle) {
        obstacles.add(obstacle);
    }

    boolean isObstacle(Point point) {
        return obstacles.contains(point);
    }
}

class Point {
    int x, y;

    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return x == point.x && y == point.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
public class MarsRover {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the starting position of the rover (x, y, direction):");
        
        int x, y;
        String direction;

        try {
            x = scanner.nextInt();
            y = scanner.nextInt();
            direction = scanner.next();
        } catch (InputMismatchException e) {
            System.out.println("Invalid input for rover's starting position. Exiting...");
            System.exit(1); // Exit the program with code 1 (indicating an error)
            return;
        }

        if (x < 0 || y < 0) {
            System.out.println("Rover starting position cannot have negative coordinates. Exiting...");
            System.exit(1); // Exit the program with code 1 (indicating an error)
        }

        Rover rover = new Rover(x, y, direction);

        System.out.println("Enter the size of the grid (width, height):");
        
        int width, height;

        try {
            width = scanner.nextInt();
            height = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Invalid input for grid size. Exiting...");
            System.exit(1); // Exit the program with code 1 (indicating an error)
            return;
        }

        if (width <= 0 || height <= 0) {
            System.out.println("Grid size must have positive dimensions. Exiting...");
            System.exit(1); // Exit the program with code 1 (indicating an error)
        }

        Grid grid = new Grid(width, height);

        System.out.println("Enter the number of obstacles:");
        int numObstacles;

        try {
            numObstacles = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Invalid input for the number of obstacles. Exiting...");
            System.exit(1); // Exit the program with code 1 (indicating an error)
            return;
        }

        for (int i = 0; i < numObstacles; i++) {
            System.out.println("Enter the coordinates of obstacle " + (i + 1) + " (x, y):");
            int obstacleX, obstacleY;

            try {
                obstacleX = scanner.nextInt();
                obstacleY = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input for obstacle coordinates. Exiting...");
                System.exit(1); // Exit the program with code 1 (indicating an error)
                return;
            }

            if (obstacleX < 0 || obstacleY < 0) {
                System.out.println("Obstacle coordinates cannot have negative values. Exiting...");
                System.exit(1); // Exit the program with code 1 (indicating an error)
            }

            // Check if an obstacle is at the starting position
            if (obstacleX == x && obstacleY == y) {
                System.out.println("An obstacle is present at the starting position. Exiting...");
                System.exit(1); // Exit the program with code 1 (indicating an error)
            }

            grid.addObstacle(new Point(obstacleX, obstacleY));
        }

        System.out.println("Enter the commands for the rover:");
        String commandsInput = scanner.next();
        
        // Validate the commandsInput string (only containing M, L, R)
        if (!commandsInput.matches("[MLR]+")) {
            System.out.println("Invalid input for commands. Exiting...");
            System.exit(1); // Exit the program with code 1 (indicating an error)
        }

        String[] commands = commandsInput.split("");

        boolean roverReachedEnd = false;
        Point lastAttemptedMove = null;

        for (String command : commands) {
            Point nextPoint = new Point(rover.x, rover.y);
            if (command.equals("M")) {
                if (rover.direction.equals("N")) nextPoint.y++;
                else if (rover.direction.equals("S")) nextPoint.y--;
                else if (rover.direction.equals("E")) nextPoint.x++;
                else if (rover.direction.equals("W")) nextPoint.x++;
            }

            if (nextPoint.x < 0 || nextPoint.x >= grid.width || nextPoint.y < 0 || nextPoint.y >= grid.height) {
                roverReachedEnd = true;
                break;
            }

            if (!grid.isObstacle(nextPoint)) {
                rover.executeCommand(command);
            } else {
                // Store the last attempted move
                lastAttemptedMove = new Point(nextPoint.x, nextPoint.y);
            }
        }

        if (roverReachedEnd) {
            System.out.println("Rover has reached the end of the grid. Exiting...");
            System.exit(0); // Exit the program with code 0 (indicating normal termination)
        } else {
            System.out.println("Final Position: (" + rover.x + ", " + rover.y + ", " + rover.direction + ")");
        }

        // Generate and print the status report
        String statusReport = "Rover is at (" + rover.x + ", " + rover.y + ") facing " + rover.direction + ". ";
        if (lastAttemptedMove != null) {
            statusReport += "Obstacle detected at (" + lastAttemptedMove.x + ", " + lastAttemptedMove.y + ").";
        } else {
            statusReport += "No obstacles detected.";
        }
        System.out.println(statusReport);
    }
}
