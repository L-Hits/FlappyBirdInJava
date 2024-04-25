package flappybirdinjava;

import java.awt.*;
import javax.swing.*;

public abstract class GameObject extends JLabel {
    private final Image image;
    private final int IMAGE_WIDTH, IMAGE_HEIGHT;
    protected int x;
    protected int y;

    public GameObject(Image image) {
        super();
        this.image = image;
        setOpaque(false);

        IMAGE_WIDTH = image.getWidth(null);
        IMAGE_HEIGHT = image.getHeight(null);
    }

    public void update() {
        float sizeMultiply = Main.getSizeMultiply();
        setSize( (int)(IMAGE_WIDTH * sizeMultiply), (int)(IMAGE_HEIGHT * sizeMultiply) );
    }

    @Override
    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
        int fixedX = (int)( x * Main.getSizeMultiply() );
        int fixedY = (int)( y * Main.getSizeMultiply() );
        super.setLocation(fixedX, fixedY);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
    }
} //GameObject class

class BackgroundPanel extends JPanel {
    private Image imgBackground = new ImageIcon( Main.getPath("/sprites/background.png") ).getImage();
    private final int WIDTH = imgBackground.getWidth(null);
    private final int HEIGHT = imgBackground.getHeight(null);

    public void update() {
        for ( Component k : getComponents() ) {
            GameObject obj = (GameObject)k;
            obj.update();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        float sizeMultiply = Main.getFrame().getSizeMultiply();
        int fixedWidth = (int)(WIDTH * sizeMultiply);
        int fixedHeight = (int)(HEIGHT * sizeMultiply);

        g.drawImage(imgBackground, 0, 0, fixedWidth, fixedHeight, this);
    }
} //BackgroundPanel class


class Bird extends GameObject {
    private final static Image image = new ImageIcon( Main.getPath("/sprites/bird_midflap.png") ).getImage();

    private float jump = 0f;
    private final float GRAVITY = 3f;
    private final float G_FORCE = 0.5f;

    public Bird() {
        super(image);
    }

    @Override
    public void update() {
        super.update();

        if ( jump > -GRAVITY) {
            jump -= G_FORCE;
        }
        else{
            jump = -GRAVITY;
        }

        y = Main.clamp( (int)(y - jump), 0, 472 - image.getHeight(null) );
        setLocation(x, y);
    }

    public void jump() {
        jump = 10;
    }
} //Bird class

class Pipe extends GameObject
{
    private final int _SPEED = 1;

    public Pipe(Image image) {
        super(image);
    }

    @Override
    public void update()
    {
        super.update();
        x -= _SPEED;
        setLocation(x, y);

        if(x <= -50)
        {
            getParent().remove(this);   //내가 있는 add()한 위치까지 찾아가서 지운다.
        }

    }
}

class Pipe_up extends Pipe
{
    private static final Image image = new ImageIcon( Main.getPath("/sprites/pipe_up.png") ).getImage();

    public Pipe_up()
    {
        super(image);
    }

}

class Pipe_down extends Pipe
{
    private static final Image image = new ImageIcon( Main.getPath("/sprites/pipe_down.png") ).getImage();

    public Pipe_down()
    {
        super(image);
    }

}