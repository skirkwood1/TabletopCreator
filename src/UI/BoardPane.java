package UI;
import Commands.*;
import Models.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

public class BoardPane extends JPanel {

    private Game game;
    //private Dimension dimension;

    private final int SCALE = 50;
    private final int PADDING = 30;
    private int leftMarginOffset;
    private int topMarginOffset;

    private double zoom = 1.0;
    public enum PlacementType{SPACE,PIECE,CARD,NONE}

    private boolean showGrid = true;

    private PlacementType placementType;

    Point imagePreview;
    BufferedImage image;

    Point spacePreview;
    Point spacePreviewEnd;

    CommandStack commandStack;

    public BoardPane(Game game,CommandStack commandStack) {
        this.game = game;

        int horizontalSize = game.getBoard().getSize()[0] + game.getBoard().getMargins()[2]+ game.getBoard().getMargins()[3];
        int verticalSize = game.getBoard().getSize()[1] + game.getBoard().getMargins()[0]+ game.getBoard().getMargins()[1];

        Dimension dimension = new Dimension((horizontalSize)* SCALE + PADDING*2,
                verticalSize*SCALE + PADDING*2);
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

        //Point origin;
        Point originPoint;

        int start_x;
        int start_y;

        int end_x;
        int end_y;

        int last_x;
        int last_y;

        HashMap<Card,Point> cards;

        Card selectedCard;
        Piece selectedPiece;

        int buttonPressed = 0;

        @Override
        public void mouseClicked(MouseEvent e) {
            Graphics g = getGraphics();
            Graphics2D g2 = (Graphics2D)g;

            g2.setColor(game.getBoard().getColor());
            g2.scale(zoom,zoom);

            int[] size = game.getBoard().getSize();

            int x, y;

            switch(placementType){
                case SPACE:
                    break;
                case PIECE:
                    // get X and y position on board
                    x = (int)Math.floor((((e.getX()/zoom-PADDING)/SCALE))) - leftMarginOffset;
                    y = (int)Math.floor((((e.getY()/zoom-PADDING)/SCALE))) - topMarginOffset; ///zoom);
                    if(x < size[0] && x >= 0 && y < size[1] && y >= 0){
                        //Space space = game.getBoard().getSpace(x,y);
                        //Piece piece = new Piece("t","t","C:\\Users\\Simon\\IdeaProjects\\TabletopCreator\\res\\icons8-save-100.png");
                        Piece piece = null;
                        if(game.getSelectedComponent() instanceof Piece){
                            piece = (Piece)game.getSelectedComponent();
                        }

                        if(piece != null){
                            PlacePieceCommand ppc = new PlacePieceCommand(game,x,y,piece);
                            commandStack.insertCommand(ppc);
                        }
                    }
//                            space.addPiece(piece);
//                        }
                    break;
                case CARD:
                    if(game.getSelectedComponent() instanceof Card){
                        Card card = (Card)game.getSelectedComponent();
                        Dimension d = scaleCard(card);
                        Point point = new Point((int)(e.getX()/zoom-d.getWidth()/2),(int)(e.getY()/zoom-d.getHeight()/2));
                        game.placeCard(card,point);
                    }
                    break;
                case NONE:
                    break;
            }

//            if(x < size[0] && x >= 0 && y < size[1] && y >= 0){
//                Space space = game.getBoard().getSpace(x,y);
//                switch(placementType){
//                    case SPACE:
//                        break;
//                    case PIECE:
//                        //Piece piece = new Piece("t","t","C:\\Users\\Simon\\IdeaProjects\\TabletopCreator\\res\\icons8-save-100.png");
//                        Piece piece = null;
//                        if(game.getSelectedComponent() instanceof Piece){
//                            piece = (Piece)game.getSelectedComponent();
//                        }
//
//                        if(piece != null){
//                            PlacePieceCommand ppc = new PlacePieceCommand(game,x,y,piece);
//                            commandStack.insertCommand(ppc);
//                        }
////                            space.addPiece(piece);
////                        }
//                        break;
//                    case CARD:
//                        Card card = new Card("t","t","C:\\Users\\Simon\\IdeaProjects\\TabletopCreator\\res\\icons8-save-100.png");
//                        game.placeCard(card,e.getPoint());
//                        break;
//                    case NONE:
//                        break;
//                }

//                if(space.isOccupied()){
//                    g2.drawImage(space.getPiece().getPicture(),x*40+25,y*40+25,30,30,null);
//                }

//                g2.setColor(Color.BLACK);
//                g2.drawRect(x*40+20,y*40+20,40,40);

                //System.out.println("Placed space at " + x + ", " + y);

            //}

        }

        @Override
        public void mousePressed(MouseEvent e) {

            cards = game.getPlacedCards();

            Graphics2D g2 = (Graphics2D)getGraphics();

            start_x = (int)Math.floor((((e.getX()/zoom-PADDING)/SCALE))) - leftMarginOffset;
            start_y = (int)Math.floor((((e.getY()/zoom-PADDING)/SCALE))) - topMarginOffset;

            last_x = (int)(e.getX()/zoom);
            last_y = (int)(e.getY()/zoom);

            Point startPoint = new Point(start_x,start_y);
            //origin = e.getPoint();
            originPoint = e.getPoint();

            Space[][] spaces = game.getBoard().getSpaces();

            int[] size = game.getBoard().getSize();

            if(SwingUtilities.isLeftMouseButton(e)){
                    if(placementType == PlacementType.NONE){
                        for(Map.Entry<Card,Point> cardEntry: game.getPlacedCards().entrySet()){
                            Card card = cardEntry.getKey();
                            Point place = cardEntry.getValue();

                            Dimension d = scaleCard(card);

                            Rectangle bounds = new Rectangle((int)(place.getX()*zoom),
                                    (int)(place.getY()*zoom),
                                    (int)(d.getWidth()*zoom),(int)(d.getHeight()*zoom));
                            g2.setColor(Color.BLUE);
                            g2.draw(bounds);

                            if(bounds.contains(originPoint)){
                                System.out.println(card);
                                selectedCard = card;
                            }
                        }

                        if(start_x < size[0] && start_x >= 0 &&
                                start_y < size[1] && start_y >= 0){
                            Space start_space = spaces[start_x][start_y];
                            selectedPiece = start_space.getPiece();

                            if(selectedPiece != null){
                                image = selectedPiece.getPicture();
                                BufferedImage preview = new BufferedImage(image.getWidth(),image.getHeight(),BufferedImage.TYPE_INT_ARGB);

                                Graphics2D g2d = (Graphics2D)preview.getGraphics();
                                g2d.setComposite(AlphaComposite.SrcOver.derive(0.5f));

                                g2d.drawImage(image, 0, 0, null);

                                image = preview;
                            }
                        }
                    } else if (placementType == PlacementType.SPACE) {

                    }
            }

                buttonPressed = 1;
            //super.mousePressed(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {

            Graphics g = getGraphics();
            Graphics2D g2 = (Graphics2D)g;

            end_x = (int) Math.floor(((e.getX() / zoom - PADDING) / SCALE)) - leftMarginOffset;
            end_y = (int) Math.floor(((e.getY() / zoom - PADDING) / SCALE)) - topMarginOffset;

            int[] size = game.getBoard().getSize();

            if(buttonPressed == 1) {
                if (end_x < size[0] && end_x >= 0 &&
                        end_y < size[1] && end_y >= 0 &&
                        start_x < size[0] && start_x >= 0 &&
                        start_y < size[1] && start_y >= 0) {
                    switch(placementType){
                        case NONE:
                            if(start_x != end_x || start_y != end_y){
                                PieceMoveCommand pmc = new PieceMoveCommand(game,start_x,start_y,end_x,end_y,selectedPiece);
                                commandStack.insertCommand(pmc);
                            }
                            break;
                        case SPACE:
//                            if(start_x == end_x & start_y == end_y){
//                                PlaceSpaceCommand psc = new PlaceSpaceCommand(game,start_x,start_y);
//                                commandStack.insertCommand(psc);
//                            } else{
                                int minx = Math.min(start_x, end_x);
                                int maxx = Math.max(start_x, end_x);

                                int miny = Math.min(start_y,end_y);
                                int maxy = Math.max(start_y,end_y);

                                MultipleSpacesCommand msc = new MultipleSpacesCommand(game,minx,miny,maxx,maxy);
                                commandStack.insertCommand(msc);
                            //}
                            break;
                    }
                }
                image = null;
                selectedCard = null;
                selectedPiece = null;
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

            if(buttonPressed == 1){

                switch(placementType){
                    case NONE:
                        if(selectedCard != null){
                            int mouse_x = (int)(e.getX() / zoom); //- leftMarginOffset*SCALE;
                            int mouse_y = (int)(e.getY() / zoom); //- topMarginOffset*SCALE;

                            int mouseDeltaX = mouse_x - last_x;
                            int mouseDeltaY = mouse_y - last_y;

                            Point cardPoint = cards.get(selectedCard);
                            int card_x = (int)cardPoint.getX();
                            int card_y = (int)cardPoint.getY();

                            int new_x = card_x + mouseDeltaX;
                            int new_y = card_y + mouseDeltaY;

                            int horizontal_size = game.getBoard().getSize()[0] + game.getBoard().getMargins()[2]+ game.getBoard().getMargins()[3];
                            int vertical_size = game.getBoard().getSize()[1] + game.getBoard().getMargins()[0]+ game.getBoard().getMargins()[1];

                            Dimension d = scaleCard(selectedCard);
                            int cardWidth = (int)d.getWidth();
                            int cardHeight = (int)d.getHeight();

                            if((new_x) >= (horizontal_size)*SCALE - cardWidth/4){
                                new_x = (horizontal_size)*SCALE - cardWidth/4;
                            }
                            if((new_x) <= cardWidth/4){
                                new_x = cardWidth/4;
                            }
                            if((new_y) >= vertical_size*SCALE - cardHeight/4){
                                new_y = vertical_size*SCALE- cardHeight/4;
                            }
                            if((new_y) <= cardHeight/4){
                                new_y = cardHeight/4;
                            }

                            Point newPoint = new Point(new_x, new_y);
                            cards.put(selectedCard,newPoint);
                        }else{
                            int current_x = (int)(e.getX() / zoom - SCALE*.375) - leftMarginOffset*SCALE;
                            int current_y = (int)(e.getY() / zoom - SCALE*.375) - topMarginOffset*SCALE;

                            if(current_x >= (int)(game.getBoard().getSize()[0]*SCALE)+leftMarginOffset*SCALE - SCALE*.375){
                                current_x = (int)(game.getBoard().getSize()[0]*SCALE)+leftMarginOffset*SCALE - (int)(SCALE*.375);
                            }
                            else if(current_x <= leftMarginOffset*SCALE - SCALE*.375){
                                current_x = (int)(leftMarginOffset*SCALE - SCALE*.375);
                            }

                            if(current_y >= (int)(game.getBoard().getSize()[1]*SCALE) + topMarginOffset*SCALE - SCALE*.375){
                                current_y = (int)(game.getBoard().getSize()[1]*SCALE) + topMarginOffset*SCALE - (int)(SCALE*.375);
                            }
                            else if(current_y <= topMarginOffset*SCALE - SCALE*.375){
                                current_y = (int)(topMarginOffset*SCALE - SCALE*.375);
                            }

                            imagePreview = new Point(current_x,current_y);
                        }
                        break;
                    case SPACE:
                        int[] size = game.getBoard().getSize();
                        int end_x = (int)((e.getX() / zoom - PADDING) / SCALE);
                        int end_y = (int)((e.getY() / zoom - PADDING) / SCALE);
                        if(end_x < size[0] + leftMarginOffset && end_x >= leftMarginOffset &&
                                end_y < size[1] + topMarginOffset && end_y >= topMarginOffset){
                            spacePreviewEnd = new Point(end_x,end_y);
                        }
                        break;
                    default:
                        break;
                }
                last_x = (int)(e.getX()/zoom);
                last_y = (int)(e.getY()/zoom);
            }

            //super.mouseDragged(e);
        }


        @Override
        public void mouseMoved(MouseEvent e) {

            int[] size = game.getBoard().getSize();

            int preview_x = (int)Math.floor((((e.getX()/zoom-PADDING)/SCALE)));
            int preview_y = (int)Math.floor((((e.getY()/zoom-PADDING)/SCALE)));

            if(preview_x < size[0] + leftMarginOffset && preview_x >= leftMarginOffset &&
                    preview_y < size[1] + topMarginOffset && preview_y >= topMarginOffset){
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

        int[] boardMargins = game.getBoard().getMargins();
        int top = boardMargins[0];
        int bottom = boardMargins[1];
        int left = boardMargins[2];
        int right = boardMargins[3];

        g2.scale(zoom, zoom);

        int playWidth = (left + width + right)*SCALE;
        int playHeight = (top + height + bottom)*SCALE;
        this.topMarginOffset = top;
        this.leftMarginOffset = left;

        g2.setColor(game.getBoard().getDefaultColor());
        g2.fillRect(PADDING,PADDING, playWidth, playHeight);

        Stroke oldStroke = g2.getStroke();
        g2.setStroke(new BasicStroke(2f));
        //g2.drawRect(width*SCALE+20,20,300,height*SCALE);
        g2.setColor(Color.BLACK);
        g2.drawRect(PADDING,PADDING, playWidth, playHeight);
        g2.setStroke(oldStroke);

        for (int i = left; i < width + left; i++) {
            for (int j = top; j < height + top; j++) {
                Space space = game.getBoard().getSpace(i-left, j-top);

                if(space.isUsingTexture()){
                    Texture texture = space.getTexture();
                    g2.drawImage(texture.getPicture(),
                            spaceScale(i),
                            spaceScale(j),
                            SCALE,SCALE,null);
                }
                else{
                    Color color = space.getColor();
                    g2.setColor(color);
                    g2.fillRect(spaceScale(i),
                            spaceScale(j),
                            SCALE, SCALE);
                }

                if(showGrid){
                    g2.setColor(Color.BLACK);
                    oldStroke = g2.getStroke();
                    g2.setStroke(new BasicStroke(0.5f));
                    g2.drawRect(spaceScale(i), spaceScale(j), SCALE, SCALE);
                    g2.setStroke(oldStroke);
                }

                if (space.getPiece() != null) {
                    g2.drawImage(space.getPiece().getPicture(), spaceScale(i) + (int)(SCALE*.125),
                            spaceScale(j) + (int)(SCALE*.125), (int)(SCALE*.75), (int)(SCALE*.75), null);
                }
            }
        }

//        g2.setColor(game.getBoard().getDefaultColor());
//        g2.fillRect(spaceScale(width),20,300,height*SCALE);

        g2.setColor(Color.BLACK);
        oldStroke = g2.getStroke();
        g2.setStroke(new BasicStroke(2f));
        g2.drawRect(PADDING + leftMarginOffset*SCALE,PADDING + topMarginOffset*SCALE,width*SCALE,height*SCALE);
        //g2.drawRect(width*SCALE+20,20,300,height*SCALE);
        g2.setStroke(oldStroke);

        for(HashMap.Entry<Card,Point> placedCard: game.getPlacedCards().entrySet()){
            Card card = placedCard.getKey();
            Point point = placedCard.getValue();

            Dimension d = scaleCard(card);

            g2.drawImage(card.getPicture(),(int)point.getX(),(int)point.getY(),(int)d.getWidth(),(int)d.getHeight(),null);
        }

        if (this.image != null) {
            g2.drawImage(image, (int) imagePreview.getX() + leftMarginOffset*SCALE,
                    (int) imagePreview.getY() + topMarginOffset*SCALE,
                    (int)(SCALE*.75), (int)(SCALE*.75), null);
        }

        if (spacePreview != null) {
            g2.setComposite(makeComposite(0.2f));
            g2.setPaint(game.getBoard().getColor());
            if(spacePreviewEnd == null){
                drawSpace(g2,(int)spacePreview.getX(),(int)spacePreview.getY());
                //g2.fillRect((int) spacePreview.getX() * 40 + 20, (int) spacePreview.getY() * 40 + 20, 40, 40);
            }else{
                int minx = (int)Math.min(spacePreview.getX(),spacePreviewEnd.getX());
                int maxx = (int)Math.max(spacePreview.getX(),spacePreviewEnd.getX());

                int miny = (int)Math.min(spacePreview.getY(),spacePreviewEnd.getY());
                int maxy = (int)Math.max(spacePreview.getY(),spacePreviewEnd.getY());

                for(int i = minx; i <= maxx; i++){
                    for(int j = miny; j <= maxy; j++){
                            drawSpace(g2,i,j);
                            //g2.fillRect(i*40+20,j*40+20,40,40);
                    }
                }
            }
        }
    }

    public void drawSpace(Graphics2D g2, int x, int y){
        if(game.getBoard().useTexture()){
            g2.drawImage(game.getBoard().getTextureImage(),spaceScale(x),spaceScale(y),SCALE,SCALE,null);
        }else{
            g2.fillRect(spaceScale(x), spaceScale(y), SCALE, SCALE);
        }
    }

    public void setZoom(double scale){
        this.zoom = scale;
    }

    public void updateSize(){
        int horizontalSize = game.getBoard().getSize()[0] + game.getBoard().getMargins()[2]+ game.getBoard().getMargins()[3];
        int verticalSize = game.getBoard().getSize()[1] + game.getBoard().getMargins()[0]+ game.getBoard().getMargins()[1];

        setPreferredSize(new Dimension((int)(horizontalSize*SCALE*zoom)+(int)(PADDING*zoom*2),(int)(verticalSize*SCALE*zoom)+(int)(PADDING*zoom*2)));
        setSize(new Dimension((int)(horizontalSize*SCALE*zoom)+(int)(PADDING*zoom*2),(int)(verticalSize*SCALE*zoom)+(int)(PADDING*zoom*2)));
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

    public Dimension scaleCard(Card card){
        double width = SCALE*1.5;
        double heightScale = card.getPicture().getWidth() / width;
        double height = card.getPicture().getHeight() / heightScale;

        return new Dimension((int)width,(int)height);
    }

    public int spaceScale(int size){
        return size * SCALE + PADDING;
    }

}
