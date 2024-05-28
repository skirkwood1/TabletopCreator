# Tabletop Game Creator

## Summary
Tabletop Game Creator is a tool that allows the user to design, test and eventually share their ideas for board games. It currently uses an interface designed in Swing to allow the user to model the board layout, import cards and pieces, and save their design via an object output stream. It is still in the early stages of development, with many more features planned for the future.

## User Guide
### Board
The center of the screen is the board. By default, it is a 10x10 grid and filled with white spaces.
The board is where the main visualization and prototyping of the game will be centered around.
The right side of the board contains a space that in the future will be used for the placements of cards.

There are several ways to edit and customize the board.
On the upper toolbar, there is a button with the current color on it.
This color defaults to red, but by clicking the button, the user can open a color chooser to switch to another RGB color.
This will swap both the color of the button, and a preview label to the right of the toolbar.
By clicking the button labelled "Space", the user can use the mouse to change the color of spaces on the board,
either by single clicking or by pressing and dragging over multiple squares.
The user can also place textures, explained further below.

The user can delete a square they have placed by hovering their mouse over the square and hitting the "delete" key on their keyboard.
Additionally, the hotkey "Ctrl+G" can be used to show or hide the grid displayed on the board.
"Ctrl+Z" and "Ctrl+Y" can be used to undo/redo actions, respectively.

To change the dimensions of the board, navigate to the top menu bar and click the menu labelled "Edit".
Then click the button labelled "Change Size". A pop-up window will open allowing the user to choose a new width and height, and confirm the action.

### Project Components
Use the "Add" menu to create new components to import into the project.
For now, components mostly consist of a name, an optional description, and an image that represents its appearance.
Clicking to import a piece or card will open a pop-up menu allowing the user to select their chosen image file and type in a name and description.
Importing a texture will open an identical dialog, but the description box will be ignored (to fix).
Rules are not yet implemented, but will likely just be a name and text.

Pieces, once imported, can be placed on the board. Make sure the piece is selected (highlighted) in the Project tree.
Then, click on the button on the toolbar labelled "Piece". This will deselect "Space" if it was previously selected.
Clicking on a space on the board will then place that piece on that square.
By deselecting Piece, the user can press and drag the left mouse button to move pieces from one space to another.

Cards are a work-in-progress. They can be organized into decks under the corresponding menu.
Multiple copies of each card can be added to decks, and multiple decks can contain the same card.
In the future, the user will be able to place decks and cards on and around the board, with options to shuffle, draw, discard, etc.

Textures are an alternative to placing solid colors on the board. They represent a certain type of space, and can be any image.
For best results, use an image that is both square (same width and height) and tiles (the left side seamlessly connects to the right side, and top to bottom).
By selecting a texture in the Project tree, its image will appear in the square preview on the top toolbar.
Any space placed will then appear as the texture or color displayed in that preview.

### Chat Window
The Java project contains a second executable called "ServerWindow". Running this will open a webserver for use in the chat window.
While in the main program, click the arrow icon on the top toolbar to open the chat. It will prompt the user to enter a username.
Type in the desired username and hit "Enter". From there, the user can send messages to anyone else connected to the webserver,
and it will appear alongside the username of the user.

In the future, this will be have much more functionality. It will allow users to share game files via links, send messages directly to other users,
and have separate posts with support for rating and reviewing games and collecting feedback.

### Command Terminal
The bottom of the main program window contains a text panel. In the future, there will be a set of commands that fulfill certain roles.
For now, the only command is "/roll x", which rolls an x-sided die and prints the result.

## TODO List
- Custom space layout besides grid
  - Hexagonal arrangement and freeform with custom space placement
- Add players and assign components to players
  - Enable hiding of components based on player, and switching current active player
- Rulesheets displayed with markdown formatting
- Board background images
- Board visualization tools such as custom space drawing, and visual x and y axis values
- Value trackers (attach values to players, components, etc. and manipulate them)
- Card and piece manipulation
  - Rotating, replacing, attaching tokens and markers, etc.
- Allow multiple pieces in one space (customizable)
### Stretch goals (unlikely)
- Three dimensional games (board layers)
- PDF/image exports
- Scripting language for game rule automation

## Programmer Guide

### Model Classes
Game- represents the overall game, contains every other model class within it.
Basically just a collection of the board, cards, pieces, textures, decks, etc. that exists, and methods meant to add or remove those components.

Board- Contains a 2D array of spaces

### GUI/Swing Design 

### Observer Pattern

### Command Pattern

