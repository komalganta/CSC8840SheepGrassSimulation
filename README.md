# Sheep-Grass Environmental Stress Simulation

This is my individual final project for CSC 8840. It extends the standard DEVSJAVA Sheep-Grass model by adding a seasonal clock (summer and winter) to test how the ecosystem handles environmental stress.

**Note:** The majority of this repository is the base DEVSJAVA framework provided by my professor, Dr. Xiaolin Hu. My specific code is located in the `Homework2025/PredatorPrey/KGanta/` folder. 

## Requirements

- Java (JDK 8 or higher)
- IntelliJ IDEA (recommended)

## How to Run
I developed and tested this project using IntelliJ IDEA, which is recommended for easiest setup. Here are the exact steps to get to the file and run it properly:

1. Open the project folder in IntelliJ.
2. On the left side project panel, open the folders in this exact order: `Homework2025` -> `PredatorPrey` -> `KGanta`.
3. Inside the `KGanta` folder, find the `SheepGrassCellSpace` file.
4. Right-click on the `SheepGrassCellSpace` file and click **Run** to get the graphic output and start the simulation.
5. Once the program is run, the visuals take some time to appear, but the console on the bottom will also show that program is successfully running by displaying the current season.

## How to Change Scenarios

Different experimental scenarios are defined in the `GlobalRef.java` file.

To switch scenarios:

1. Open `GlobalRef.java`
2. Locate the scenario parameter sections
3. Uncomment **one** scenario you want to run
4. Ensure all other scenarios are commented out
5. Run the simulation again

Each scenario modifies parameters such as:
- Grass growth rate (summer vs winter)
- Sheep reproduction rate
- Season duration

## Expected Outputs

- **Summer:** Grass grows quickly, sheep population increases  
- **Winter:** Grass growth decreases, sheep population decreases  

Based on the selected scenario, you may see:
- Stable population cycles
- Overpopulation followed by collapse
- Extinction under extreme conditions
