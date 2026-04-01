# Sheep-Grass Environmental Stress Simulation

This is my individual final project for CSC 8840. It extends the standard DEVSJAVA Sheep-Grass model by adding a seasonal clock (summer and winter) to test how the ecosystem handles environmental stress.

**Note:** The majority of this repository is the base DEVSJAVA framework provided by my professor, Dr. Xiaolin Hu. My specific code is located in the `Homework2025/PredatorPrey/KGanta/` folder. 

## How to Run
I ran this project using the IntelliJ IDE, so please use the same to ensure optimal results. Here are the exact steps to get to the file and run it properly:

1. Open the project folder in IntelliJ.
2. On the left side project panel, open the folders in this exact order: `Homework2025` -> `PredatorPrey` -> `KGanta`.
3. Inside the `KGanta` folder, find the `SheepGrassCellSpace` file.
4. Right-click on the `SheepGrassCellSpace` file and click **Run** to get the graphic output and start the simulation.
5. Once the program is run, the visuals take some time to appear, but the console on the bottom will also show that program is successfully running by displaying the current season.

## How to Change Scenarios
I have set up  different experimental scenarios in the code to test the system. 

* To change the scenarios, open the `GlobalRef` file and uncomment the scenario you want to run. 
* **Important:** Make sure the other scenarios are commented out before you run it to ensure there are no errors!
