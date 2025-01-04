# Treasure hunter

This program was created to search for treasures on a user-specified map.

## About the Project
The program uses an evolutionary approach to find treasures on the map.

## Rules
1) The treasure hunter must find all the treasures to win.
2) The hunter is indicated by the number 2,
   treasures by the number 1,
   empty cells available for moves - by the number 0
3) If the hunter goes beyond the boundaries of the map, he loses.

## How to use
1) Download the project
2) Set the combination in the "grid.txt" file(only 1 configuration per run):
   1) Set the map size in the first row of the text file using two digits separated by a space
   2) Next, fill out the text file so that you get a map with the scales you previously specified.
   3) ATTENTION! There must be ONLY ONE hunter on the map and at least one treasure.
4) Run project. Further instructions will be displayed on the screen.

An example of a table (3x3) that should be in a text file:
| 3 | 3 |   |
|-------------|-------------|-------------|
| 0 |0 | 1 |
| 1 | 0 | 0 |
| 0 | 0 | 2 |

## Documentation
Details about how the code works and how to use the program are in the documentation and also in comments inside the code. (Ignore the design details as this was a class project)
