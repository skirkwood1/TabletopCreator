package UI;
import Commands.*;
import Models.*;
import UI.BoardPaneObjects.CardDrawer;
import UI.BoardPaneObjects.DrawerInterface;
import UI.BoardPaneObjects.PlayerDrawer;
import UI.BoardPaneObjects.ResourceDrawer;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

public class BoardPane extends JPanel {
    private Game game;

    private final int SCALE = 40;
    private final int PADDING = 30;
    private int leftMarginOffset;
    private int topMarginOffset;

    private double zoom = 1.0;
    public enum PlacementType{SPACE,PIECE,CARD,RESOURCE,PLAYER,NONE}
    private boolean showGrid = true;
    private PlacementType placementType;

    private BufferedImage image;

    private Point imagePreview;
    private Point spacePreview;
    private Point spacePreviewEnd;
    private Point mousePoint;

    private CommandStack commandStack;
    private final JPopupMenu rightClickMenu;

    public BoardPane(Game game,CommandStack commandStack) {
        this.game = game;

        this.rightClickMenu = new JPopupMenu();

        int horizontalSize = game.getBoard().getSize()[0] + game.getBoard().getMargins()[2]+ game.getBoard().getMargins()[3];
        int verticalSize = game.getBoard().getSize()[1] + game.getBoard().getMargins()[0]+ game.getBoard().getMargins()[1];

        Dimension dimension = new Dimension((horizontalSize)* SCALE + PADDING*2,
                verticalSize*SCALE + PADDING*2);
        setPreferredSize(dimension);
        setSize(dimension);

        this.commandStack = commandStack;
        this.placementType = PlacementType.SPACE;

        setLayout(new GridBagLayout());

        addMouseListener(ma);
        addMouseMotionListener(ma);

        imagePreview = new Point(0,0);

//        ArrayList<Resource> res = new ArrayList<>();
//        res.add(new Resource("A",10));
//        res.add(new Resource("A long resource name for testing purposes",10));
//        res.add(new Resource("b",9999));
//        Player p1 = new Player("p1",res);
//
//        //ResourceDrawer rd = new ResourceDrawer(res.get(0));
//        PlayerDrawer pd = new PlayerDrawer(p1,new Point(60,60));
//        resourceDrawers.add(pd);
//        addMouseMotionListener(pd.getLocationTracker());
//        addMouseListener(pd.getLocationTracker());
    }

    MouseAdapter ma = new MouseAdapter() {
        Point originPoint;

        int start_x;
        int start_y;
        int end_x;
        int end_y;
        int last_x;
        int last_y;

        HashMap<CardInterface,Point> cards;

        Piece selectedPiece;
        DrawerInterface selectedDrawer = null;

        Point dragStart;
        Point dragEnd;

        @Override
        public void mouseClicked(MouseEvent e) {
            Graphics g = getGraphics();
            Graphics2D g2 = (Graphics2D)g;

            g2.setColor(game.getBoard().getColor());
            g2.scale(zoom,zoom);

            int[] size = game.getBoard().getSize();
            int x, y;

            if(SwingUtilities.isLeftMouseButton(e)){
                switch(placementType){
                    case SPACE:
                    case NONE:
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
                        break;
                    case CARD:
                        placeCard();
                        break;
                    case RESOURCE:
                        placeResource();
                        System.out.println("Placed resource: " + game.getSelectedComponent());
                        break;
                    case PLAYER:
                        placePlayer();
                        break;
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            Graphics2D g2 = (Graphics2D)getGraphics();

            start_x = (int)Math.floor((((e.getX()/zoom-PADDING)/SCALE))) - leftMarginOffset;
            start_y = (int)Math.floor((((e.getY()/zoom-PADDING)/SCALE))) - topMarginOffset;
            last_x = (int)(e.getX()/zoom);
            last_y = (int)(e.getY()/zoom);

            Point startPoint = new Point(start_x,start_y);
            originPoint = e.getPoint();

            Space[][] spaces = game.getBoard().getSpaces();
            int[] size = game.getBoard().getSize();

            if(SwingUtilities.isLeftMouseButton(e)){
                    if(placementType == PlacementType.NONE){
                        selectedDrawer = getSelectedResource();
                        if(selectedDrawer != null){
                            dragStart = selectedDrawer.getPoint();

                        }
                        if(start_x < size[0] && start_x >= 0 &&
                                start_y < size[1] && start_y >= 0
                                && selectedDrawer == null){
                            Space start_space = spaces[start_x][start_y];
                            selectedPiece = start_space.getPiece();

                            if(selectedPiece != null){
                                image = selectedPiece.getImage();
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
            //super.mousePressed(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            Graphics g = getGraphics();
            Graphics2D g2 = (Graphics2D)g;

            end_x = (int) Math.floor(((e.getX() / zoom - PADDING) / SCALE)) - leftMarginOffset;
            end_y = (int) Math.floor(((e.getY() / zoom - PADDING) / SCALE)) - topMarginOffset;

            //dragEnd = game.getPlacedComponents().get(selectedCard);
            int[] size = game.getBoard().getSize();

            if(SwingUtilities.isLeftMouseButton(e)) {


                switch(placementType){
                    case NONE:
                        if(selectedDrawer != null){
                            dragEnd = selectedDrawer.getPoint();
                            if(dragStart.x != dragEnd.x || dragStart.y != dragEnd.y){
                                MoveDrawerCommand mdc = new MoveDrawerCommand(game, selectedDrawer, dragStart, dragEnd);
                                commandStack.insertCommand(mdc);
                            }
                        }
                        else if((start_x != end_x || start_y != end_y) &&
                            selectedPiece != null){
                        PieceMoveCommand pmc = new PieceMoveCommand(game,start_x,start_y,end_x,end_y,selectedPiece);
                        commandStack.insertCommand(pmc);
                        }
                        break;
                    case SPACE:
//                            if(start_x == end_x & start_y == end_y){
//                                PlaceSpaceCommand psc = new PlaceSpaceCommand(game,start_x,start_y);
//                                commandStack.insertCommand(psc);
//                            } else{
                        if (end_x < size[0] && end_x >= 0 &&
                                end_y < size[1] && end_y >= 0 &&
                                start_x < size[0] && start_x >= 0 &&
                                start_y < size[1] && start_y >= 0) {
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
                selectedPiece = null;
                selectedDrawer = null;
            }
            spacePreviewEnd = null;
            int preview_x = (int)Math.floor((((e.getX()/zoom-PADDING)/SCALE)));
            int preview_y = (int)Math.floor((((e.getY()/zoom-PADDING)/SCALE)));

            if(preview_x < size[0] + leftMarginOffset && preview_x >= leftMarginOffset &&
                    preview_y < size[1] + topMarginOffset && preview_y >= topMarginOffset){
                spacePreview = new Point(preview_x,preview_y);
            }
            repaint();
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
            if(SwingUtilities.isLeftMouseButton(e)){
                switch(placementType){
                    case NONE:
                        if(selectedDrawer != null){
                            int mouse_x = (int)(e.getX() / zoom); //- leftMarginOffset*SCALE;
                            int mouse_y = (int)(e.getY() / zoom); //- topMarginOffset*SCALE;

                            int mouseDeltaX = mouse_x - last_x;
                            int mouseDeltaY = mouse_y - last_y;

                            Point cardPoint = selectedDrawer.getPoint();
                            int card_x = (int)cardPoint.getX();
                            int card_y = (int)cardPoint.getY();

                            Rectangle bounds = selectedDrawer.getBounds();

                            int new_x = card_x + mouseDeltaX;
                            int new_y = card_y + mouseDeltaY;

                            int horizontal_size = game.getBoard().getSize()[0] + game.getBoard().getMargins()[2]+ game.getBoard().getMargins()[3];
                            int vertical_size = game.getBoard().getSize()[1] + game.getBoard().getMargins()[0]+ game.getBoard().getMargins()[1];

                            if((new_x) >= (horizontal_size)*SCALE + PADDING - bounds.width){
                                new_x = (horizontal_size)*SCALE + PADDING - bounds.width;
                                mouse_x = last_x;
                            }
                            else if((new_x) <= PADDING){
                                new_x = PADDING;
                                mouse_x = last_x;
                            }
                            if((new_y) >= vertical_size*SCALE + PADDING- bounds.height){
                                new_y = vertical_size*SCALE + PADDING - bounds.height;
                                mouse_y = last_y;
                            }
                            else if((new_y) <= PADDING){
                                new_y = PADDING;
                                mouse_y = last_y;
                            }

                            Point newPoint = new Point(new_x, new_y);
                            selectedDrawer.move(newPoint);

                            last_x = mouse_x;//(int)(e.getX()/zoom);
                            last_y = mouse_y;
                        }
                        else{
                            int current_x = (int)(e.getX() / zoom - SCALE*.375) - leftMarginOffset*SCALE;
                            int current_y = (int)(e.getY() / zoom - SCALE*.375) - topMarginOffset*SCALE;

                            imagePreview = new Point(current_x,current_y);
                            int[] size = game.getBoard().getSize();
                            int end_x = (int)((e.getX() / zoom - PADDING) / SCALE);
                            int end_y = (int)((e.getY() / zoom - PADDING) / SCALE);
                            if(end_x < size[0] + leftMarginOffset && end_x >= leftMarginOffset &&
                                    end_y < size[1] + topMarginOffset && end_y >= topMarginOffset){
                                spacePreviewEnd = new Point(end_x,end_y);
                            }
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
            }
            //super.mouseDragged(e);
        }


        @Override
        public void mouseMoved(MouseEvent e) {
            mousePoint = e.getPoint();
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

        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHints(rh);

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
        g2.setColor(Color.BLACK);
        g2.drawRect(PADDING,PADDING, playWidth, playHeight);
        g2.setStroke(oldStroke);

        for (int i = left; i < width + left; i++) {
            for (int j = top; j < height + top; j++) {
                Space space = game.getBoard().getSpace(i-left, j-top);
                if(space.isUsingTexture()){
                    Texture texture = space.getTexture();
                    g2.drawImage(texture.getImage(),
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
                    g2.drawImage(space.getPiece().getImage(), spaceScale(i) + (int)(SCALE*.125),
                            spaceScale(j) + (int)(SCALE*.125), (int)(SCALE*.75), (int)(SCALE*.75), null);
                }
            }
        }

        g2.setColor(Color.BLACK);
        oldStroke = g2.getStroke();
        g2.setStroke(new BasicStroke(2f));
        g2.drawRect(PADDING + leftMarginOffset*SCALE,PADDING + topMarginOffset*SCALE,width*SCALE,height*SCALE);
        g2.setStroke(oldStroke);

        if (this.image != null) {
            g2.drawImage(image, (int) imagePreview.getX() + leftMarginOffset*SCALE,
                    (int) imagePreview.getY() + topMarginOffset*SCALE,
                    (int)(SCALE*.75), (int)(SCALE*.75), null);
        }

        if (spacePreview != null) {
            g2.setPaint(game.getBoard().getColor());
            if(spacePreviewEnd == null){
                if(placementType == PlacementType.SPACE){
                    g2.setComposite(makeComposite(0.2f));
                    drawSpace(g2,(int)spacePreview.getX(),(int)spacePreview.getY());
                }else{
                    g2.setComposite(makeComposite(0.8f));
                    oldStroke = g2.getStroke();
                    g2.setStroke(new BasicStroke(2f));
                    g2.drawRect(spaceScale((int)spacePreview.getX()),
                            spaceScale((int)spacePreview.getY()),
                            SCALE, SCALE);

                    g2.setStroke(oldStroke);
                }
                //g2.fillRect((int) spacePreview.getX() * 40 + 20, (int) spacePreview.getY() * 40 + 20, 40, 40);
            }else{
                int minx = (int)Math.min(spacePreview.getX(),spacePreviewEnd.getX());
                int maxx = (int)Math.max(spacePreview.getX(),spacePreviewEnd.getX());

                int miny = (int)Math.min(spacePreview.getY(),spacePreviewEnd.getY());
                int maxy = (int)Math.max(spacePreview.getY(),spacePreviewEnd.getY());

                if(placementType == PlacementType.SPACE){
                    g2.setComposite(makeComposite(0.2f));
                    for(int i = minx; i <= maxx; i++){
                        for(int j = miny; j <= maxy; j++){
                            drawSpace(g2,i,j);
                            //g2.fillRect(i*40+20,j*40+20,40,40);
                        }
                    }
                }else{
                    g2.setComposite(makeComposite(0.8f));
                    oldStroke = g2.getStroke();
                    g2.setStroke(new BasicStroke(2f));
                    g2.drawRect(spaceScale(minx),spaceScale(miny),
                            (maxx-minx+1)*SCALE,(maxy-miny+1)*SCALE);
                    g2.setStroke(oldStroke);
                }
            }
        }
        g2.setComposite(makeComposite(1f));
        for(DrawerInterface drawer: game.getPlacedComponents()){
            drawer.draw(g,zoom);
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

    public CardInterface getSelectedCard(){
        CardInterface selectedCard = null;
        for(DrawerInterface drawer: game.getPlacedComponents()){
            if(drawer.getComponent() instanceof CardInterface){
                CardInterface card = (CardInterface)drawer.getComponent();
                Point place = drawer.getPoint();
                Dimension d = scaleCard(card);
                Rectangle bounds = new Rectangle((int)(place.getX()*zoom),
                        (int)(place.getY()*zoom),
                        (int)(d.getWidth()*zoom),(int)(d.getHeight()*zoom));
                //g2.setColor(Color.BLUE);
                //g2.draw(bounds);

                if(bounds.contains(mousePoint)){
                    selectedCard = card;
                }
            }
        }
        return selectedCard;
    }

    public DrawerInterface getSelectedResource(){
        DrawerInterface selectedResource = null;
        Point selectionPoint = new Point((int)(mousePoint.getX()/zoom),
                (int)(mousePoint.getY()/zoom));
        for(DrawerInterface drawer: game.getPlacedComponents()){
            if(drawer.getBounds().contains(selectionPoint)){
                selectedResource = drawer;
            }
        }
        return selectedResource;
    }

    public Space getSelectedSpace(){
        if(spacePreview != null){
            int x = (int)spacePreview.getX() - leftMarginOffset;
            int y = (int)spacePreview.getY() - topMarginOffset;
            return game.getBoard().getSpace(x,y);
        }
        return null;
    }

    public ArrayList<Space> getSelectedSpaces(){
        if(spacePreview != null && spacePreviewEnd != null){
            int minx = (int)Math.min(spacePreview.getX(),spacePreviewEnd.getX());
            int maxx = (int)Math.max(spacePreview.getX(),spacePreviewEnd.getX());

            int miny = (int)Math.min(spacePreview.getY(),spacePreviewEnd.getY());
            int maxy = (int)Math.max(spacePreview.getY(),spacePreviewEnd.getY());

            ArrayList<Space> spaces = new ArrayList<>();

            if(placementType == PlacementType.NONE){
                for(int i = minx; i <= maxx; i++){
                    for(int j = miny; j <= maxy; j++){
                        int x = i - leftMarginOffset;
                        int y = j-topMarginOffset;
                        spaces.add(game.getBoard().getSpace(x,y));
                        //g2.fillRect(i*40+20,j*40+20,40,40);
                    }
                }
            }
            return spaces;
        }
        return null;
    }

    public void deleteSelection(){
        DrawerInterface selectedResource = getSelectedResource();

        if(selectedResource != null){
            //game.removePlacedComponent(selectedResource);
            DeleteDrawerCommand ddc = new DeleteDrawerCommand(game,selectedResource);
            commandStack.insertCommand(ddc);
        }else{

            deleteSelectedSpace();
        }
    }

    public void deleteSelectedSpace(){
        if(spacePreview != null){
            int x = (int)spacePreview.getX() - leftMarginOffset;
            int y = (int)spacePreview.getY() - topMarginOffset;

            Space space = game.getBoard().getSpace(x,y);

            DeleteSpaceCommand dsc = new DeleteSpaceCommand(game,x,y,space);
            commandStack.insertCommand(dsc);
        }
    }

    public void toggleGrid(){
        this.showGrid = !this.showGrid;
    }

    public void drawSpace(Graphics g, Space space){
        Graphics2D g2 = (Graphics2D)g.create();
        int[] coords = game.getBoard().getSpaceLocation(space);
        int x =(coords[0]+leftMarginOffset)*SCALE+PADDING;
        int y =(coords[1]+topMarginOffset)*SCALE+PADDING;

        g2.setComposite(makeComposite(0.5f));
        g2.setStroke(new BasicStroke(1.5f));
        g2.setPaint(game.getBoard().getColor());
        g2.drawRect(x,y,SCALE,SCALE);
    }

    public void updateGame(Game game){
        this.game = game;
    }

    public Dimension scaleCard(CardInterface card){
        double width = SCALE*1.5;
        double heightScale = card.getImage().getWidth() / width;
        double height = card.getImage().getHeight() / heightScale;

        return new Dimension((int)width,(int)height);
    }

    public void placeCard(){
        if(game.getSelectedCard() != null){
            CardInterface card = game.getSelectedCard();
            placeCard(card);
        }
    }

    public void placeCard(CardInterface card){
        Dimension d = scaleCard(card);
        Point point = new Point((int)(mousePoint.getX()/zoom-d.getWidth()/2),
                (int)(mousePoint.getY()/zoom-d.getHeight()/2));

        CardInterface copy = card.copy();

        CardDrawer cardDrawer = new CardDrawer(copy,point);
        //resourceDrawers.add(cardDrawer);
        PlaceDrawerCommand prc = new PlaceDrawerCommand(game,cardDrawer);
        commandStack.insertCommand(prc);
        //game.placeCard(copy,point);
        addMouseListener(cardDrawer.getLocationTracker());
        addMouseMotionListener(cardDrawer.getLocationTracker());

        //PlaceCardCommand cpc = new PlaceCardCommand(game,card,point);
        //commandStack.insertCommand(cpc);
    }

    public void placeResource(){
        if(game.getSelectedComponent() instanceof Resource){
            placeResource((Resource)game.getSelectedComponent());
        }
    }

    public void placeResource(Resource resource){
        Point placePoint = new Point((int)(mousePoint.x/zoom),
                (int)(mousePoint.getY()/zoom));

        Resource copy = resource.copy();

        ResourceDrawer resourceDrawer = new ResourceDrawer(copy,placePoint);
        //resourceDrawers.add(resourceDrawer);
        PlaceDrawerCommand prc = new PlaceDrawerCommand(game,resourceDrawer);
        commandStack.insertCommand(prc);
        //game.placeResource(copy,placePoint);
        addMouseListener(resourceDrawer.getLocationTracker());
        addMouseMotionListener(resourceDrawer.getLocationTracker());
    }

    public void placePlayer(){
        if(game.getSelectedComponent() instanceof Player){
            placePlayer((Player)game.getSelectedComponent());
        }
    }

    public void placePlayer(Player player){
        Point placePoint = new Point((int)(mousePoint.x/zoom),
                (int)(mousePoint.getY()/zoom));

        Player copy = player.copy();

        PlayerDrawer playerDrawer = new PlayerDrawer(copy,placePoint);
        //resourceDrawers.add(playerDrawer);
        PlaceDrawerCommand prc = new PlaceDrawerCommand(game,playerDrawer);
        commandStack.insertCommand(prc);
        //game.placePlayer(copy,placePoint);
        addMouseListener(playerDrawer.getLocationTracker());
        addMouseMotionListener(playerDrawer.getLocationTracker());
    }

    public int spaceScale(int size){
        return size * SCALE + PADDING;
    }

    public JPopupMenu getRightClickMenu(){
        return this.rightClickMenu;
    }

}
