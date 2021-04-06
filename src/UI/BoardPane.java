package UI;
import Commands.*;
import Models.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class BoardPane extends JPanel {

    private Game game;
    //private Dimension dimension;

    private double zoom = 1.0;
    public enum PlacementType{SPACE,PIECE,DECK,NONE}

    private boolean showGrid = true;

    private PlacementType placementType;

    Point imagePreview;
    BufferedImage image;

    Point spacePreview;
    Point spacePreviewEnd;

    CommandStack commandStack;

    public BoardPane(Game game,CommandStack commandStack) {
        this.game = game;
        Dimension dimension = new Dimension(game.getBoard().getSize()[0]*40+40+200,game.getBoard().getSize()[1]*40+40);
        setPreferredSize(dimension);
        setSize(dimension);

        this.commandStack = commandStack;
        this.placementType = PlacementType.NONE;

        setLayout(new GridBagLayout());

        addMouseListener(ma);
        addMouseMotionListener(ma);

        imagePreview = new Point(0,0);
    }

    MouseAdapter ma = new MouseAdapter() {

        Point origin;
        Point previewPoint;

        int start_x;
        int start_y;

        int end_x;
        int end_y;

        Piece selectedPiece;

        int buttonPressed = 0;

        @Override
        public void mouseClicked(MouseEvent e) {
            Graphics g = getGraphics();
            Graphics2D g2 = (Graphics2D)g;

            g2.setColor(game.getBoard().getColor());
            g2.scale(zoom,zoom);

            // get X and y position on board
            int x, y;
            x = (int)Math.floor((((e.getX()/zoom-20)/40)));
            y = (int)Math.floor((((e.getY()/zoom-20)/40))); ///zoom);

            int[] size = game.getBoard().getSize();

            if(x < size[0] && x >= 0 && y < size[1] && y >= 0){
                Space space = game.getBoard().getSpace(x,y);
                switch(placementType){
                    case SPACE:
                        //PlaceSpaceCommand psc = new PlaceSpaceCommand(game,x,y);
                        //commandStack.insertCommand(psc);
                        //g2.fillRect((int)((x*40+20)*zoom), (int)((y*40+20)*zoom), (int)(40*zoom), (int)(40*zoom));
                        if(game.getBoard().useTexture()){
                            g2.drawImage(game.getBoard().getTextureImage(),x*40+20,y*40+20,40,40,null);
                        }
                        else{
                            g2.fillRect(x*40+20,y*40+20,40,40);
                        }
                        break;
                    case PIECE:
                        //Piece piece = new Piece("t","t","C:\\Users\\Simon\\IdeaProjects\\TabletopCreator\\res\\icons8-save-100.png");
                        Piece piece = null;
                        if(game.getSelectedComponent() instanceof Piece){
                            piece = (Piece)game.getSelectedComponent();
                        }

                        if(piece != null){
                            PlacePieceCommand ppc = new PlacePieceCommand(game,x,y,piece);
                            commandStack.insertCommand(ppc);
                        }
//                            space.addPiece(piece);
//                        }
                        break;
                    case NONE:
                        break;
                }

                if(space.isOccupied()){
                    g2.drawImage(space.getPiece().getPicture(),x*40+25,y*40+25,30,30,null);
                }

                g2.setColor(Color.BLACK);
                g2.drawRect(x*40+20,y*40+20,40,40);

                //System.out.println("Placed space at " + x + ", " + y);

            }

        }

        @Override
        public void mousePressed(MouseEvent e) {

            Graphics2D g2 = (Graphics2D)getGraphics();

            start_x = (int)Math.floor((((e.getX()/zoom-20)/40)));
            start_y = (int)Math.floor((((e.getY()/zoom-20)/40)));

            origin = e.getPoint();
            previewPoint = e.getPoint();

            Space[][] spaces = game.getBoard().getSpaces();

            int[] size = game.getBoard().getSize();

            if((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) != 0){
                if(start_x < size[0] && start_x >= 0 && start_y < size[1] && start_y >= 0){
                    Space start_space = spaces[start_x][start_y];
                    if(placementType == PlacementType.NONE){
                        selectedPiece = start_space.getPiece();

                        if(selectedPiece != null){
                            image = selectedPiece.getPicture();
                            BufferedImage preview = new BufferedImage(image.getWidth(),image.getHeight(),BufferedImage.TYPE_INT_ARGB);

                            Graphics2D g2d = (Graphics2D)preview.getGraphics();
                            g2d.setComposite(AlphaComposite.SrcOver.derive(0.5f));

                            g2d.drawImage(image, 0, 0, null);

                            image = preview;
                        }
                    } else if (placementType == PlacementType.SPACE) {

                    }
                }

                buttonPressed = 1;
            //super.mousePressed(e);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {

            Graphics g = getGraphics();
            Graphics2D g2 = (Graphics2D)g;

            end_x = (int) Math.floor(((e.getX() / zoom - 20) / 40));
            end_y = (int) Math.floor(((e.getY() / zoom - 20) / 40));

            int[] size = game.getBoard().getSize();

            if(buttonPressed == 1) {
                if (end_x < size[0] && end_x >= 0 && end_y < size[1] && end_y >= 0) {
                    switch(placementType){
                        case NONE:
                            if(start_x != end_x || start_y != end_y){
                                PieceMoveCommand pmc = new PieceMoveCommand(game,start_x,start_y,end_x,end_y,selectedPiece);
                                commandStack.insertCommand(pmc);
                            }
                            break;
                        case SPACE:
                            if(start_x == end_x & start_y == end_y){
                                PlaceSpaceCommand psc = new PlaceSpaceCommand(game,start_x,start_y);
                                commandStack.insertCommand(psc);
                            } else if(start_x <= end_x & start_y <= end_y){
                                MultipleSpacesCommand msc = new MultipleSpacesCommand(game,start_x,start_y,end_x,end_y);
                                commandStack.insertCommand(msc);
                            }else if(start_x >= end_x & start_y <= end_y){
                                MultipleSpacesCommand msc = new MultipleSpacesCommand(game,end_x,start_y,start_x,end_y);
                                commandStack.insertCommand(msc);
                            }else if(start_x <= end_x & start_y >= end_y){
                                MultipleSpacesCommand msc = new MultipleSpacesCommand(game,start_x,end_y,end_x,start_y);
                                commandStack.insertCommand(msc);
                            }else if(start_x >= end_x & start_y >= end_y){
                                MultipleSpacesCommand msc = new MultipleSpacesCommand(game,end_x,end_y,start_x,start_y);
                                commandStack.insertCommand(msc);
                            }
                            break;
                    }
                }
                image = null;
                repaint();

                buttonPressed = 0;
            }

            spacePreviewEnd = null;

            //super.mouseReleased(e);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            super.mouseEntered(e);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            super.mouseExited(e);
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            super.mouseWheelMoved(e);
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            //Graphics g = getGraphics();
            //Graphics2D g2 = (Graphics2D)g;

            int current_x = (int)(e.getX() / zoom) - 15;
            int current_y = (int)(e.getY() / zoom) - 15;

            if(current_x >= (int)(game.getBoard().getSize()[0]*40)-5){
                current_x = (int)(game.getBoard().getSize()[0]*40)-5;
            }
            else if(current_x <= 15){
                current_x = 15;
            }

            if(current_y >= (int)(game.getBoard().getSize()[1]*40)-5){
                current_y = (int)(game.getBoard().getSize()[1]*40)-5;
            }
            else if(current_y <= 15){
                current_y = 15;
            }

            Point currentPoint = new Point(current_x,current_y);

            if(buttonPressed == 1){

                switch(placementType){
                    case NONE:
                        imagePreview = currentPoint;
                        break;
                    case SPACE:
                        int[] size = game.getBoard().getSize();
                        int end_x = (int)((e.getX() / zoom - 20) / 40);
                        int end_y = (int)((e.getY() / zoom - 20) / 40);
                        if(end_x < size[0] && end_x >= 0 && end_y < size[1] && end_y >= 0){
                            spacePreviewEnd = new Point(end_x,end_y);
                        }

                }

            }

            //super.mouseDragged(e);
        }


        @Override
        public void mouseMoved(MouseEvent e) {

            int[] size = game.getBoard().getSize();

            int preview_x = (int)Math.floor((((e.getX()/zoom-20)/40)));
            int preview_y = (int)Math.floor((((e.getY()/zoom-20)/40)));

            if(preview_x < size[0] && preview_x >= 0 && preview_y < size[1] && preview_y >= 0){
                spacePreview = new Point(preview_x,preview_y);
            }else{
                spacePreview = null;
            }

            //super.mouseMoved(e);
        }

    };

    public void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;

        //setBackground(Color.WHITE);

        int[] boardSize = game.getBoard().getSize();
        int width = boardSize[0];
        int height = boardSize[1];

        g2.scale(zoom, zoom);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Space space = game.getBoard().getSpace(i, j);

                if(space.isUsingTexture()){
                    Texture texture = space.getTexture();
                    g2.drawImage(texture.getTexture(),i*40+20,j*40+20,40,40,null);
                }
                else{
                    Color color = space.getColor();
                    g2.setColor(color);
                    g2.fillRect(i * 40 + 20, j * 40 + 20, 40, 40);
                }

                if(showGrid){
                    g2.setColor(Color.BLACK);
                    Stroke oldStroke = g2.getStroke();
                    g2.setStroke(new BasicStroke(0.5f));
                    g2.drawRect(i * 40 + 20, j * 40 + 20, 40, 40);
                    g2.setStroke(oldStroke);
                }

                if (space.getPiece() != null) {
                    g2.drawImage(space.getPiece().getPicture(), i * 40 + 25, j * 40 + 25, 30, 30, null);
                }
            }
        }

        g2.setColor(game.getBoard().getDefaultColor());
        g2.fillRect(width*40+20,20,300,height*40);

        g2.setColor(Color.BLACK);
        Stroke oldStroke = g2.getStroke();
        g2.setStroke(new BasicStroke(2f));
        g2.drawRect(20,20,width*40,height*40);
        g2.drawRect(width*40+20,20,300,height*40);
        g2.setStroke(oldStroke);

        if (this.image != null) {
            g2.drawImage(image, (int) imagePreview.getX(), (int) imagePreview.getY(), 30, 30, null);
        }

        if (spacePreview != null) {
            g2.setComposite(makeComposite(0.2f));
            g2.setPaint(game.getBoard().getColor());
            if(spacePreviewEnd == null){
                drawSpace(g2,(int)spacePreview.getX(),(int)spacePreview.getY());
                //g2.fillRect((int) spacePreview.getX() * 40 + 20, (int) spacePreview.getY() * 40 + 20, 40, 40);
            }else{
                if(spacePreview.getX() <= spacePreviewEnd.getX() & spacePreview.getY() <= spacePreviewEnd.getY()){
                    for(int i = (int)spacePreview.getX(); i <= spacePreviewEnd.getX(); i++){
                        for(int j = (int)spacePreview.getY(); j <= spacePreviewEnd.getY(); j++){
                            drawSpace(g2,i,j);
                            //g2.fillRect(i*40+20,j*40+20,40,40);
                        }
                    }
                }else if(spacePreview.getX() <= spacePreviewEnd.getX() & spacePreview.getY() >= spacePreviewEnd.getY()){
                    for(int i = (int)spacePreview.getX(); i <= spacePreviewEnd.getX(); i++){
                        for(int j = (int)spacePreviewEnd.getY(); j <= spacePreview.getY(); j++){
                            drawSpace(g2,i,j);
                            //g2.fillRect(i*40+20,j*40+20,40,40);
                        }
                    }
                }else if(spacePreview.getX() >= spacePreviewEnd.getX() & spacePreview.getY() <= spacePreviewEnd.getY()){
                    for(int i = (int)spacePreviewEnd.getX(); i <= spacePreview.getX(); i++){
                        for(int j = (int)spacePreview.getY(); j <= spacePreviewEnd.getY(); j++){
                            drawSpace(g2,i,j);
                            //g2.fillRect(i*40+20,j*40+20,40,40);
                        }
                    }
                }else if(spacePreview.getX() >= spacePreviewEnd.getX() & spacePreview.getY() >= spacePreviewEnd.getY()){
                    for(int i = (int)spacePreviewEnd.getX(); i <= spacePreview.getX(); i++){
                        for(int j = (int)spacePreviewEnd.getY(); j <= spacePreview.getY(); j++){
                            drawSpace(g2,i,j);
                            //g2.fillRect(i*40+20,j*40+20,40,40);
                        }
                    }
                }
            }
        }
    }

    public void drawSpace(Graphics2D g2, int x, int y){
        if(game.getBoard().useTexture()){
            g2.drawImage(game.getBoard().getTextureImage(),x*40+20,y*40+20,40,40,null);
        }else{
            g2.fillRect(x*40+20,y*40+20,40,40);
        }
    }

    void setZoom(double scale){
        this.zoom = scale;
    }

    void updateSize(){
        //this.dimension = new Dimension(board.getSize()[0]*40+40,board.getSize()[1]*40+40);


        double x = (this.game.getBoard().getSize()[0] * 40 + 240) * zoom;
        double y = (this.game.getBoard().getSize()[1] * 40 + 40) * zoom;

        setPreferredSize(new Dimension((int)x+200,(int)y));
        setSize(new Dimension((int)x+200,(int)y));
    }

    public void setPlacementType(PlacementType pt){
        this.placementType = pt;
    }

    private AlphaComposite makeComposite(float alpha) {
        int type = AlphaComposite.SRC_OVER;
        return(AlphaComposite.getInstance(type, alpha));
    }

    public void deleteSelectedSpace(){
        if(spacePreview != null){
            int x = (int)spacePreview.getX();
            int y = (int)spacePreview.getY();

            Space space = game.getBoard().getSpace(x,y);

            DeleteSpaceCommand dsc = new DeleteSpaceCommand(game,x,y,space);
            commandStack.insertCommand(dsc);
        }
    }

    public void toggleGrid(){
        this.showGrid = !this.showGrid;
    }

    public void updateGame(Game game){
        this.game = game;
    }

}
