import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

class TreasureHunt{
    void create_population(List<Person> individuals, List<Person> individuals_nextgen){ ///  creation of the first population

        int min = 0;  // min
        int max = 255;  // max
        int[] current_person = new int[64];

        int rows = 0; // rows
        int cols = 0; // cols
        int[][] grid = new int[rows][cols];

        for(int s = 0; s < 60; s++)
        {
            for(int i = 0; i < 64; i++){
                int randomNumber = Main.generateRandomNumber(min, max);
                current_person[i] = randomNumber;
            }
            ////////////////////////////////////////////// reading from file
            try {
                File file = new File("C:\\Users\\asususer\\IdeaProjects\\UI_Z2_b\\src\\grid.txt"); // Имя вашего файла
                Scanner scanner = new Scanner(file);

                // Read the matrix dimensions from the first row
                if (scanner.hasNextLine()) {
                    String dimensions = scanner.nextLine();
                    String[] dimensionTokens = dimensions.split(" ");
                    if (dimensionTokens.length == 2) {
                        rows = Integer.parseInt(dimensionTokens[0]);
                        cols = Integer.parseInt(dimensionTokens[1]);
                    }
                }

                // We create a matrix based on the read sizes
                grid = new int[rows][cols];

                // Filling the matrix with values from the file
                for (int i = 0; i < rows; i++) {
                    if (scanner.hasNextLine()) {
                        String line = scanner.nextLine();
                        String[] tokens = line.split(" ");
                        for (int j = 0; j < cols && j < tokens.length; j++) {
                            if (tokens[j].equals("1")) {
                                grid[i][j] = 1; // Сокровище
                            } else if (tokens[j].equals("2")) {
                                grid[i][j] = 2; // start point
                            } else {
                                grid[i][j] = 0;
                            }
                        }
                    }
                }
                scanner.close();
            } catch (FileNotFoundException e) {
                System.out.println("File not found: " + e.getMessage());
            }

            personal_evolution(current_person, grid, individuals);

        }
    }

    void personal_evolution(int[] current_person, int[][] grid, List<Person> individuals) {  /// evolution of each individual

        int vypis_operation = 0;
        int j = 0;
        int[] current_person_clone = Arrays.copyOf(current_person, current_person.length);

        int playerX = 0; // Initializing player coordinates
        int playerY = 0;
        int founded_treasure = 0;
        int pocet_treasures = 0;
        boolean foundAllTreasures = false;

        for (int i_map = 0; i_map < grid.length; i_map++) {
            for (int j_map = 0; j_map < grid[0].length; j_map++) {
                if (grid[i_map][j_map] == 1) {
                    pocet_treasures += 1; // all treasures
                } else if (grid[i_map][j_map] == 2) {
                    playerX = i_map; // player position
                    playerY = j_map;
                }
            }
        }

        char[] charPostup = new char[500];
        int index = 0;

        for (int i = 0; i < 500; i++) {

            int current_state = current_person[j];
            int current_operation = (current_state >> 6) & 0b11;
            int jump_adress = current_state & 0b111111;

            if (current_operation == 0)// inkrementacia ++
            {
                if (current_person[jump_adress] == 63){
                    current_person[jump_adress] = 0;
                } else{
                    current_person[jump_adress] += 1;
                }
                j++;

            } else if (current_operation == 1)// dekrementacia --
            {

                if (current_person[jump_adress] == 0){
                    current_person[jump_adress] = 63;
                } else{
                    current_person[jump_adress] -= 1;
                }
                j++;

            } else if (current_operation == 2)// skok
            {
                j = jump_adress;
            } else if (current_operation == 3) // vypis
            {
                vypis_operation = current_state & 0b11;

                int out_of_field = 0;

                // Depending on the operation, we update the player’s coordinates
                switch (vypis_operation) {
                    case 0 -> {
                        if (playerY + 1 < grid[0].length) {
                            grid[playerX][playerY] = 0; // Clearing the current player position
                            playerY += 1;

                            if(grid[playerX][playerY] == 1){ // checking whether the player has become a treasure
                                founded_treasure +=1;
                            }

                            grid[playerX][playerY] = 2; // Move the player to the right

                            charPostup[index] = 'P';
                            index++;

                        } else {
                            out_of_field++;
                        }
                    }
                    case 1 -> {
                        if (playerY - 1 >= 0) {
                            grid[playerX][playerY] = 0;

                            playerY -= 1;

                            if(grid[playerX][playerY] == 1){
                                founded_treasure += 1;
                            }

                            grid[playerX][playerY] = 2;

                            charPostup[index] = 'L';
                            index++;

                        }else {
                            out_of_field++;
                        }
                    }
                    case 2 -> {
                        if (playerX + 1 < grid.length) {
                            grid[playerX][playerY] = 0;

                            playerX += 1;

                            if(grid[playerX][playerY] == 1){
                                founded_treasure += 1;
                            }

                            grid[playerX][playerY] = 2;

                            charPostup[index] = 'D';
                            index++;

                        }else {
                            out_of_field++;
                        }
                    }
                    case 3 -> {
                        if (playerX - 1 >= 0) {
                            grid[playerX][playerY] = 0;

                            playerX -= 1;

                            if(grid[playerX][playerY] == 1){
                                founded_treasure += 1;
                            }

                            grid[playerX][playerY] = 2;

                            charPostup[index] = 'H';
                            index++;

                        }else {
                            out_of_field++;
                        }
                    }
                }

                if (pocet_treasures == founded_treasure) {
                    foundAllTreasures = true;
                    System.out.println("All treasures found!");

                    for (int i1 = 0; i1 < grid.length; i1++) {
                        for (int j1 = 0; j1 < grid[i1].length; j1++) {
                            System.out.print(grid[i1][j1] + " ");
                        }
                        System.out.println();
                    }
                    break;

                } else if (out_of_field != 0) {

                    break;
                }
                j++;

            } else {
                System.out.println("Smt goes wrong");
                break;
            }

            if(j == 64)
            {
                j = 0;
            }
        }

        String resultingString = new String(charPostup, 0, index);
        ///////////////  ADDING A NEW INDIVIDUAL TO THE TABLE OF INDIVIDUALS
        Person person = new Person(founded_treasure, current_person_clone, resultingString);
        individuals.add(person);

    }

    public class Person {
        private int intValue;
        private int[] intArray;

        private String resultingString;

        public Person(int intValue, int[] intArray, String resultingString) {
            this.intValue = intValue;
            this.intArray = intArray;
            this.resultingString = resultingString;
        }

    }

    boolean tournament_pairing(List<Person> individuals, List<Person> individuals_nextgen, int[][] grid){

            for (int i = 0; i < 30; i++) {

                Random random = new Random();
                double randomValue = random.nextDouble();

                List<Person> selectedParents;

                if (randomValue <= 0.5) {
                    // Select the first type of selection (tournament)
                    selectedParents = tournamentSelection(individuals);
                } else {
                    // roulette
                    selectedParents = rouletteWheelSelection(individuals);
                }

                onePointCrossover(selectedParents.get(0).intArray, selectedParents.get(1).intArray, individuals_nextgen);

            }

            individuals.clear();

            boolean foundAllTreasures = false;

            for (Person individual : individuals_nextgen) {

                double mutationType = Math.random();

                if (mutationType < 0.1) {
                    int mutationIndex = (int) (Math.random() * individual.intArray.length);
                    int newMutationValue;
                    do {
                        newMutationValue = (int) (Math.random() * 64); // Random number from 0 to 63
                    } while (newMutationValue == individual.intArray[mutationIndex]);

                    individual.intArray[mutationIndex] = newMutationValue;

                }
                else if (mutationType < 0.5) {
                    // Mutation: inversion of a random bit

                    int mutationIndex = (int) (Math.random() * individual.intArray.length);
                    for (int i = 0; i < 8; i++) {
                        individual.intArray[mutationIndex] ^= (1 << i);
                    }
                }


                int rows = 0;
                int cols = 0;
                int playerX = 0;
                int playerY = 0;
                int founded_treasure = 0;
                int pocet_treasures = 0;
                foundAllTreasures = false;

                try {
                    File file = new File("C:\\Users\\asususer\\IdeaProjects\\UI_Z2_b\\src\\grid.txt"); // Имя вашего файла
                    Scanner scanner = new Scanner(file);


                    if (scanner.hasNextLine()) {
                        String dimensions = scanner.nextLine();
                        String[] dimensionTokens = dimensions.split(" ");
                        if (dimensionTokens.length == 2) {
                            rows = Integer.parseInt(dimensionTokens[0]);
                            cols = Integer.parseInt(dimensionTokens[1]);
                        }
                    }

                    grid = new int[rows][cols];

                    for (int iss = 0; iss < rows; iss++) {
                        if (scanner.hasNextLine()) {
                            String line = scanner.nextLine();
                            String[] tokens = line.split(" ");
                            for (int jss = 0; jss < cols && jss < tokens.length; jss++) {
                                if (tokens[jss].equals("1")) {
                                    grid[iss][jss] = 1;
                                } else if (tokens[jss].equals("2")) {
                                    grid[iss][jss] = 2;
                                } else {
                                    grid[iss][jss] = 0;
                                }
                            }
                        }
                    }

                    founded_treasure = 0;
                    pocet_treasures = 0;
                    foundAllTreasures = false;

                    for (int i_map = 0; i_map < grid.length; i_map++) {
                        for (int j_map = 0; j_map < grid[0].length; j_map++) {
                            if (grid[i_map][j_map] == 1) {
                                pocet_treasures += 1;
                            } else if (grid[i_map][j_map] == 2) {
                                playerX = i_map;
                                playerY = j_map;
                            }
                        }
                    }


                } catch (FileNotFoundException e) {
                    System.out.println("File not found: " + e.getMessage());
                }
                int vypis_operation = 0;
                int j_pos = 0;
                int[] current_person_clone = Arrays.copyOf(individual.intArray, individual.intArray.length);

                char[] charPostup = new char[500];
                int index = 0;


                for (int i = 0; i < 500; i++) {

                    int current_state = individual.intArray[j_pos];
                    int current_operation = (current_state >> 6) & 0b11;
                    int jump_adress = current_state & 0b111111;


                    if (current_operation == 0)// inkrementacia ++
                    {

                        if (individual.intArray[jump_adress] == 63){
                            individual.intArray[jump_adress] = 0;
                        } else{
                            individual.intArray[jump_adress] += 1;
                        }

                        j_pos++;

                    } else if (current_operation == 1)// dekrementacia --
                    {
                        if (individual.intArray[jump_adress] == 0){
                            individual.intArray[jump_adress] = 63;
                        } else{
                            individual.intArray[jump_adress] -= 1;
                        }

                        j_pos++;

                    } else if (current_operation == 2)// skok
                    {
                        j_pos = jump_adress;
                    } else if (current_operation == 3) // vypis
                    {
                        vypis_operation = current_state & 0b11;

                        int out_of_field = 0;


                        switch (vypis_operation) {
                            case 0 -> {
                                if (playerY + 1 < grid[0].length) {
                                    grid[playerX][playerY] = 0;
                                    playerY += 1;

                                    if(grid[playerX][playerY] == 1){
                                        founded_treasure +=1;
                                    }

                                    grid[playerX][playerY] = 2;

                                    charPostup[index] = 'P';
                                    index++;

                                } else {
                                    out_of_field++;
                                }
                            }
                            case 1 -> {
                                if (playerY - 1 >= 0) {
                                    grid[playerX][playerY] = 0;

                                    playerY -= 1;

                                    if(grid[playerX][playerY] == 1){
                                        founded_treasure += 1;
                                    }

                                    grid[playerX][playerY] = 2;

                                    charPostup[index] = 'L';
                                    index++;

                                }else {
                                    out_of_field++;
                                }
                            }
                            case 2 -> {
                                if (playerX + 1 < grid.length) {
                                    grid[playerX][playerY] = 0;

                                    playerX += 1;

                                    if(grid[playerX][playerY] == 1){
                                        founded_treasure += 1;
                                    }

                                    grid[playerX][playerY] = 2;

                                    charPostup[index] = 'D';
                                    index++;

                                }else {
                                    out_of_field++;
                                }
                            }
                            case 3 -> {
                                if (playerX - 1 >= 0) {
                                    grid[playerX][playerY] = 0;

                                    playerX -= 1;

                                    if(grid[playerX][playerY] == 1){
                                        founded_treasure += 1;
                                    }

                                    grid[playerX][playerY] = 2;

                                    charPostup[index] = 'H';
                                    index++;

                                }else {
                                    out_of_field++;
                                }
                            }

                        }

                        if (pocet_treasures == founded_treasure) {
                            foundAllTreasures = true;
                            System.out.println("All treasures found!");


                            for (int i1 = 0; i1 < grid.length; i1++) {
                                for (int j1 = 0; j1 < grid[i1].length; j1++) {
                                    System.out.print(grid[i1][j1] + " ");
                                }
                                System.out.println();
                            }
                            break;
                            //////////////////////////
                        } else if (out_of_field != 0) {
                            break;
                        }

                        j_pos++;

                    } else {
                        System.out.println("Smt goes wrong");
                        break;
                    }

                    if(j_pos == 64)
                    {
                        j_pos = 0;
                    }
                }

                String resultingString = new String(charPostup, 0, index);

                Person person = new Person(founded_treasure, current_person_clone, resultingString);
                individuals.add(person);

                if(foundAllTreasures)
                {
                    System.out.println(resultingString);
                    break;
                }
            }

            individuals_nextgen.clear();

            return foundAllTreasures;
    }

    public static List<Person> tournamentSelection(List<Person> individuals) {
        int tournamentSize = 3; // Tournament size (number of participants in the tournament)
        List<Person> selectedParents = new ArrayList<>();
        List<Person> buffer = new ArrayList<>(individuals); // We create a buffer and fill it with all individuals

        Random random = new Random();

        for (int i = 0; i < 2; i++) { // Select 2 parents
            List<Person> tournamentParticipants = new ArrayList<>();

            for (int j = 0; j < tournamentSize; j++) {
                if (buffer.isEmpty()) {
                    // If the buffer is empty, refill it and mix
                    buffer.addAll(individuals);
                    Collections.shuffle(buffer);
                }

                int randomIndex = random.nextInt(buffer.size());
                tournamentParticipants.add(buffer.remove(randomIndex)); // Removing the selected individual from the buffer
            }

            // We find the best participant (with maximum fitness) in the tournament
            Person bestParticipant = tournamentParticipants.get(0);
            for (Person participant : tournamentParticipants) {
                if (participant.intValue > bestParticipant.intValue) {
                    bestParticipant = participant;
                }
            }

            selectedParents.add(bestParticipant);
        }

        return selectedParents;
    }

    public static List<Person> rouletteWheelSelection(List<Person> individuals) {
        List<Person> selectedParents = new ArrayList<>();
        List<Double> probabilities = new ArrayList<>();

        // Calculate the sum of fitnesses
        double totalFitness = 0;
        for (Person individual : individuals) {
            totalFitness += individual.intValue;
        }

        // We calculate the probabilities of choosing each individual
        for (Person individual : individuals) {
            double probability = individual.intValue / totalFitness;
            probabilities.add(probability);
        }

        Random random = new Random();

        for (int i = 0; i < 2; i++) {
            double randomValue = random.nextDouble();
            double cumulativeProbability = 0;
            int selectedParticipantIndex = -1;

            for (int j = 0; j < probabilities.size(); j++) {
                cumulativeProbability += probabilities.get(j);
                if (randomValue <= cumulativeProbability) {
                    selectedParticipantIndex = j;
                    break;
                }
            }

            if (selectedParticipantIndex != -1) {
                selectedParents.add(individuals.get(selectedParticipantIndex));
            }
        }
        return selectedParents;
    }


    public void onePointCrossover(int[] parent1, int[] parent2, List<Person> individuals_newgen) {
        int[] child1 = new int[parent1.length];
        int[] child2 = new int[parent1.length];
        Random random = new Random();
        int crossoverPoint1, crossoverPoint2;

        // Generating two different cut points
        do {
            crossoverPoint1 = random.nextInt(parent1.length);
            crossoverPoint2 = random.nextInt(parent1.length);
        } while (crossoverPoint1 == crossoverPoint2);

        // Arranging the cutting points
        int start = Math.min(crossoverPoint1, crossoverPoint2);
        int end = Math.max(crossoverPoint1, crossoverPoint2);

        for (int i = 0; i < parent1.length; i++) {
            if (i < start || i >= end) {
                child1[i] = parent1[i];
                child2[i] = parent2[i];
            } else {
                child1[i] = parent2[i];
                child2[i] = parent1[i];
            }
        }

        Person person_newgen1 = new Person(0, child1, "");
        Person person_newgen2 = new Person(0, child2, "");
        individuals_newgen.add(person_newgen1);
        individuals_newgen.add(person_newgen2);

    }


    public Person findBestIndividual(List<Person> individuals) {

        Person bestIndividual = individuals.get(0);
        for (Person individual : individuals) {
            if (individual.intValue > bestIndividual.intValue) {
                bestIndividual = individual;
            }
        }
        return bestIndividual;
    }


public class Main {
    public static void main(String[] args) {
        int rows = 0;
        int cols = 0;
        int[][] grid = new int[rows][cols];
        try {
            File file = new File("C:\\Users\\asususer\\IdeaProjects\\UI_Z2_b\\src\\grid.txt"); // Имя вашего файла
            Scanner scanner = new Scanner(file);


            // Read the matrix dimensions from the first row
            if (scanner.hasNextLine()) {
                String dimensions = scanner.nextLine();
                String[] dimensionTokens = dimensions.split(" ");
                if (dimensionTokens.length == 2) {
                    rows = Integer.parseInt(dimensionTokens[0]);
                    cols = Integer.parseInt(dimensionTokens[1]);
                }
            }

            grid = new int[rows][cols];

            for (int i = 0; i < rows; i++) {
                if (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] tokens = line.split(" ");
                    for (int j = 0; j < cols && j < tokens.length; j++) {
                        if (tokens[j].equals("1")) {
                            grid[i][j] = 1;
                        } else if (tokens[j].equals("2")) {
                            grid[i][j] = 2;
                        } else {
                            grid[i][j] = 0;
                        }
                    }
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }

        TreasureHunt hunt = new TreasureHunt();

        List<Person> individuals = new ArrayList<>(); // Creating a list for storing individuals

        List<Person> individuals_nextgen = new ArrayList<>(); // We create a list for storing individuals after crossing

        hunt.create_population(individuals, individuals_nextgen);

        int j = 0;
        int cycle = 100; //!!!!!!!!!!!!!!!!!!

        while (j < cycle) {
            boolean treasureFound = hunt.tournament_pairing(individuals, individuals_nextgen, grid);

            if (treasureFound) {
                System.out.println("Generation " + j);
                break; // Exit the loop if a treasure is found
            }
            j++;

            if (cycle == j) {
                Person bestIndividual = hunt.findBestIndividual(individuals);
                System.out.println("Best: Found treasures = " + bestIndividual.intValue + ", Array = " + Arrays.toString(bestIndividual.intArray));
                System.out.println("Way = " + bestIndividual.resultingString);

                System.out.println("The search has ended. What would you like to do?");
                System.out.println("1. Exit the program");
                System.out.println("2. Continue with additional cycles");
                System.out.print("Enter your choice: ");

                Scanner scanner = new Scanner(System.in);
                int choice = scanner.nextInt();

                if (choice == 1) {
                    System.out.println("Program exiting...");
                    break;
                } else if (choice == 2) {
                    System.out.println("Continuing with additional cycles...");
                    Scanner scanner1 = new Scanner(System.in);

                    int intValue = scanner1.nextInt();
                    cycle += intValue;
                    System.out.println("Continuing for an additional " + intValue + " cycles.");

                } else {
                    System.out.println("Invalid choice. Exiting the program.");
                    System.exit(1);
                }
            }
        }
    }

    // Method for generating a random number in a given range
    public static int generateRandomNumber(int min, int max) {
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }
}
}