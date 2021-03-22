# Tabletop Game Creator

## Summary
Tabletop Game Creator is a tool that allows the user to design, test and eventually share their ideas for board games. It currently uses an interface designed in Swing to allow the user to model the board layout, import cards and pieces, and save their design via an object output stream. It is still in the early stages of development, with many more features planned for the future.

## Usage
### Component Creation
The bottom of the GUI contains buttons for piece and card creation. These each open a dialog that allows the user to select an image file and write a name and description for the component. The newly created component will appear in the tree on the left, and can be viewed via the right-most panels.

### Board Design
The board can be resized via a dialog opened in the "Edit" menu. A color can be selected (defaults to red) via the toolbar, and clicking on the board will place a space in the clicked square. The board panel can be zoomed in and out of with the mouse wheel, and panned when applicable via the scroll bars or clicking and dragging.

### Undo/Redo
The program stores a stack of previously executed commands, allowing the user to undo and redo as many times as necessary. Some commands are not fully supported by this yet. Go to Edit -> Undo to undo your previous command and Edit -> Redo to redo an undone command.
