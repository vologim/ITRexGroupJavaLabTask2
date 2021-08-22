
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Game {
    private static final String FREE_SPACE = ".";
    private static final String COLUMN = "o";
    private static final String PRINCE = "1";
    private static final String PRINCES = "2";
    private static final String FILE_NAME = "INPUT.txt";

    private String path;
    private int numberLevels;
    private int numberLines;
    private int numberColumns;
    private int step = 0;

    private int princessX;
    private int princessY;
    private int coordinatePrinceX;
    private int coordinatePrinceY;
    private int coordinateMapX;
    private int coordinateMapY;
    private List<List<String>> arrayListsArea = new ArrayList<>();

    public Game(String path) {
        this.path = path + FILE_NAME;
    }

    private List<String> readToFile() throws IOException {
        List<String> fileData = Files.readAllLines(Paths.get(path));
        return fileData;
    }

    private void createMap(List<String> fileData){
        String[] strings = fileData.get(0).split(" ");
        fileData.remove(0);

        numberLevels = Integer.parseInt(strings[0]);
        numberLines = Integer.parseInt(strings[1]);
        numberColumns = Integer.parseInt(strings[2]);

        for(int i = 0; i < numberColumns * numberLevels;){
            strings = fileData.get(i).split("");
            if(strings.length != numberLines){
                fileData.remove(i);
                continue;
            }
            arrayListsArea.add(Arrays.asList(strings));
            i++;
        }
    }

    private int[] figurePosition(String figure){
        int[] position = new int[2];
        for(int i = 0; i < numberColumns * numberLevels; i++){
            for(int j = 0; j < numberLines; j++){
                if(arrayListsArea.get(i).get(j).equals(figure)){
                    position[0] = i;
                    position[1] = j;
                    i = numberColumns * numberLevels;
                    break;
                }
            }
        }
        return position;
    }

    private void findPrincess(){
        boolean flag = true;

        princessX = figurePosition(PRINCES)[1];
        princessY = figurePosition(PRINCES)[0];
        coordinatePrinceX = figurePosition(PRINCE)[1];
        coordinatePrinceY = figurePosition(PRINCE)[0];
        coordinateMapX = numberLines - 1;
        coordinateMapY = numberLevels * numberColumns - step - 1;

        int moveCounter;
        Stack<Integer> stackCoordinateXYStep = new Stack<>();  // First add X-coordinate, second - Y-coordinate, third - step
                                                               // First pop - step, second Y-coordinate, third - X-coordinate

        int countLevels = 1;
        while (flag){
            if(coordinatePrinceX + 1 == princessX && coordinatePrinceY == princessY ||
               coordinatePrinceX - 1 == princessX && coordinatePrinceY == princessY ||
               coordinatePrinceX == princessX && coordinatePrinceY + 1 == princessY ||
               coordinatePrinceX == princessX && coordinatePrinceY - 1 == princessY ){
                step++;
                System.out.println(step * 5 + " seconds");
                flag = false;
            }

            if(coordinatePrinceY == (numberColumns * countLevels) - 1 &&
               coordinatePrinceY != (numberColumns * numberLevels) - 1){
                stepDown();
                stackCoordinateXYStep.clear();
                for(int i = 0; i < numberLines; i ++){
                    if(arrayListsArea.get(coordinatePrinceY + 1).get(i).equals(FREE_SPACE)){
                        stackCoordinateXYStep.push(i);
                        stackCoordinateXYStep.push(coordinatePrinceY + 1);
                        stackCoordinateXYStep.push(step + 1);
                    }
                }
                arrayListsArea.get(coordinatePrinceY).set(coordinatePrinceX, COLUMN);
                step = stackCoordinateXYStep.pop();
                coordinatePrinceY = stackCoordinateXYStep.pop();
                coordinatePrinceX = stackCoordinateXYStep.pop();
                arrayListsArea.get(coordinatePrinceY).set(coordinatePrinceX, PRINCE);
                countLevels++;
                continue;
            }

            moveCounter = chekAllMoves();
            if(moveCounter > 1){
                stackCoordinateXYStep.push(coordinatePrinceX);
                stackCoordinateXYStep.push(coordinatePrinceY);
                stackCoordinateXYStep.push(step);
                chekAllMovesAndStep();
            }
            else if(moveCounter == 1){
                arrayListsArea.get(coordinatePrinceY).set(coordinatePrinceX, COLUMN);
                chekAllMovesAndStep();
            }
            else if(moveCounter == 0){
                arrayListsArea.get(coordinatePrinceY).set(coordinatePrinceX, COLUMN);
                step = stackCoordinateXYStep.pop();
                coordinatePrinceY = stackCoordinateXYStep.pop();
                coordinatePrinceX = stackCoordinateXYStep.pop();
                arrayListsArea.get(coordinatePrinceY).set(coordinatePrinceX, PRINCE);
            }
        }
    }

    private int chekAllMoves(){
        int count = 0;
        if(chekStepLeft()){
            count++;
        }
        if(chekStepRight()){
            count++;
        }
        if(chekStepUp()){
            count++;
        }
        if(chekStepDown()){
            count++;
        }
        return count;
    }

    private void chekAllMovesAndStep(){
        if(chekStepDown()){
            stepDown();
        }
        else if(chekStepLeft()){
            stepLeft();
        }
        else if(chekStepRight()){
            stepRight();
        }
        else if(chekStepUp()){
            stepUp();
        }
    }

    private boolean chekStepLeft(){
        if(coordinatePrinceX - 1 < 0){
            return false;
        }
        if(arrayListsArea.get(coordinatePrinceY).get(coordinatePrinceX - 1).equals(COLUMN)){
            return false;
        }
        return true;
    }
    private void stepLeft(){
        step++;

        arrayListsArea.get(coordinatePrinceY).set(coordinatePrinceX, COLUMN);
        arrayListsArea.get(coordinatePrinceY).set(--coordinatePrinceX, PRINCE);
    }

    private boolean chekStepRight(){
        if(coordinatePrinceX + 1 > coordinateMapX){
            return false;
        }
        if(arrayListsArea.get(coordinatePrinceY).get(coordinatePrinceX + 1).equals(COLUMN)){
            return false;
        }
        return true;
    }
    private void stepRight(){
        step++;
        arrayListsArea.get(coordinatePrinceY).set(coordinatePrinceX, COLUMN);
        arrayListsArea.get(coordinatePrinceY).set(++coordinatePrinceX, PRINCE);
    }

    private boolean chekStepDown(){
        if(coordinatePrinceY + 1 > coordinateMapY){
            return false;
        }
        if(arrayListsArea.get(coordinatePrinceY + 1).get(coordinatePrinceX).equals(COLUMN)){
            return false;
        }
        return true;
    }
    private void stepDown(){
        step++;
        arrayListsArea.get(coordinatePrinceY).set(coordinatePrinceX, COLUMN);
        arrayListsArea.get(++coordinatePrinceY).set(coordinatePrinceX, PRINCE);
    }

    private boolean chekStepUp(){
        if(coordinatePrinceY - 1 < 0){
            return false;
        }
        if(arrayListsArea.get(coordinatePrinceY - 1).get(coordinatePrinceX).equals(COLUMN)){
            return false;
        }
        return true;
    }
    private void stepUp(){
        step++;
        arrayListsArea.get(coordinatePrinceY).set(coordinatePrinceX, COLUMN);
        arrayListsArea.get(--coordinatePrinceY).set(coordinatePrinceX, PRINCE);
    }

    private void drawMap(){
        int count = 0;
        for (int i = 0; i < arrayListsArea.size(); i++) {
            for (int j = 0; j < numberLines; j++) {
                System.out.print(arrayListsArea.get(i).get(j));
            }
            System.out.println();
        }
    }

    public void run(){
        try {
            List<String> fileData = readToFile();
            createMap(fileData);
            findPrincess();
        }
        catch (IOException ex){
            System.out.println("Invalid path or file problems");
        }
    }
}
