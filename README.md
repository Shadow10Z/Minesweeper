# Adapted Minesweeper

This is an adapted version of minesweeper, where the player controls a character to go from point A to point B in a minefield.

In Minesweeper, the player has to discover all the mines in a given terrain. The objective is to “clear” the land of mines, marking their location.

# More about the project

The game will show a menu where you will need to enter "1" to start the game.
Then it will be asked to put the number of lines, columns and mines (if you press ENTER, it will give a default number).
Next, you will see the terrain with the position of the player ("P"), the numbers, the mines that you can't see and the finishing line ("f").
If you die to a bomb or if you finish the game, you will see the terrain with all the mines that were hiding. 

# Verifications

**Player Name**

-> The program will ask for your first and second name. If the first letter from the first name or second is not uppercase, the program will give you a error message and wait again for another response.

**Show Legend**

-> Here is where the program wants to know if you want a legend around the terrain and colored.

**How many lines and how many columns**

-> The game will now ask for a number of lines and columns and if it is lower than 4 or more than 9 the program will print a message with "Invalid Response"

**How many mines**

-> The program no wants to know the number of mines. When introduced a number, there is a function where validates the number of mines. The number mines MUST be more than 0 and it must be equal or lower to this algorithm (numLines * numColumns - 2). If the user presses ENTER the game will calculated a number of mines.

**Terrain Moves**

-> When you play the game, you need to choose a target cell (e.g 2D). If the player chooses a target cell that is more than 1 block away the game will printf an error message and will wait again for another target cell.
