package flappybirdinjava;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.stream.Stream;

import javax.swing.*;

public abstract class GameObject extends JLabel {
    private Image image;
    private int image_width, image_height;

    protected int x;
    protected int y;

    public GameObject(Image image) {
        super();
        this.image = image;
        setOpaque(false);

        image_width = image.getWidth(null);
        image_height = image.getHeight(null);
    }

    public void update() {
        float sizeMultiply = Main.getSizeMultiply();
        setSize( (int)(image_width * sizeMultiply), (int)(image_height * sizeMultiply) );
    }

    public int getImageWidth()
    {
        return image.getWidth(null);
    }
    public int getImageHeight()
    {
        return image.getHeight(null);
    }

    public void setIamge(Image image)
    {
        this.image = image;
        image_width = image.getWidth(null);
        image_height = image.getHeight(null);
        repaint();
    }

    public boolean isCollided(GameObject object)
    {
        Rectangle rectThis = new Rectangle( getX(), getY(), getWidth(), getHeight() );

        Rectangle rectObject = new Rectangle( object.getX(), object.getY(), object.getWidth(), object.getHeight() );

        return rectThis.intersects(rectObject); //this가 object에 부딪히면 true가 반환됨

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

class BackgroundPanel extends JPanel {  //배경
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
    private final static Image imageMid = new ImageIcon( Main.getPath("/sprites/bird_midflap.png") ).getImage();
    private final static Image imageUp = new ImageIcon( Main.getPath("/sprites/bird_upflap.png") ).getImage();
    private final static Image imageDown = new ImageIcon( Main.getPath("/sprites/bird_downflap.png") ).getImage();
    private final static Image[] ImageArr = { imageMid, imageUp, imageDown };
    
    public static int ImageArrCount = 0;
    private float jump = 0f;
    private final float GRAVITY = 3f;
    private final float G_FORCE = 0.5f;

    public Bird() {
        super(imageMid);
        //super(ImageArr[ImageArrCount]);
    }

    @Override
    public void update() {

        /* 
        if(ImageArrCount > 2)
        {
            ImageArrCount = 0;
        }
        else
        {   ImageArrCount++;
            super(ImageArr[ImageArrCount]);
        }
        */

        super.update(); //이미지 적용

        if ( jump > -GRAVITY) {
            jump -= G_FORCE;
        }
        else{
            jump = -GRAVITY;
        }

        y = Main.clamp( (int)(y - jump), 0, 472 - imageMid.getHeight(null) );
        setLocation(x, y);
    }

    public void jump() {
        if( Main.getFrame().isgameOver() == false )
        {
            jump = 10;
        } 
        
    }
} //Bird class

class Pipe extends GameObject   //파이프
{
    private Bird bird;
    public final String TAG;

    private final int _SPEED = 1;
    public static final int MIN_HEIGHT = 50;

    public Pipe(Image image, String tag) {
        super(image);
        bird = Main.getFrame().getBird();
        TAG = tag;

    }

    @Override
    public void update()
    {
        super.update();

        //이동
        x -= _SPEED;
        setLocation(x, y);

        //제거
        if(x <= -50)
        {
            getParent().remove(this);   //내가 있는 add()한 위치까지 찾아가서 지운다.
        }


        //Collision //충돌
        if( Main.getFrame().isgameOver() || getX() + getWidth() < bird.getX() )
        {
            return;
        }

        if(isCollided(bird))
        {
            switch(TAG)
            {
                case "Pipe":
                    Main.getFrame().gameOver();
                    break;

                case "ScoreAdder":
                    getParent().remove(this);
                    Main.getFrame().addScore();
                    break;
            }
        }
    }
}

class Pipe_up extends Pipe
{
    private static final Image image = new ImageIcon( Main.getPath("/sprites/pipe_up.png") ).getImage();

    public Pipe_up()
    {
        super(image, "Pipe");
    }

    @Override
    public void setLocation(int x, int y)
    {
        int clampY = Main.clamp(y, 472 - image.getHeight(null), 472 - Pipe.MIN_HEIGHT);

        super.setLocation(x, clampY);
    }

}

class Pipe_down extends Pipe
{
    private static final Image image = new ImageIcon( Main.getPath("/sprites/pipe_down.png") ).getImage();

    public Pipe_down()
    {
        super(image, "Pipe");
    }

    @Override
    public void setLocation(int x, int y)
    {
        int clampY = Main.clamp(y, -image.getHeight(null) + Pipe.MIN_HEIGHT, 0);

        super.setLocation(x, clampY);
    }

}


class PipeSpawn
{
    public static final int SPAWN_DELAY = 2500;
    public static final int GAP = 100;

    public static void spawnPipe(BackgroundPanel root, int y)
    {
        if( Main.getFrame().isgameOver() )
        {
            return;
        } 

        Pipe_down piuPipe_down = new Pipe_down();
        Pipe_up pipe_up = new Pipe_up();
        ScoreAdder scoreAdder = new ScoreAdder();


        pipe_up.setLocation(600, y + GAP);
        piuPipe_down.setLocation(600, y - piuPipe_down.getImageHeight() - GAP);
        scoreAdder.setLocation(642, y - scoreAdder.getImageHeight() / 2);

        root.add(pipe_up);
        root.add(piuPipe_down);
        root.add(scoreAdder);
    }

}

class ScoreAdder extends Pipe
{
    public ScoreAdder() {
        super(getImage(), "ScoreAdder");
    }

    public static Image getImage()
    {
        BufferedImage bufferedImage = new BufferedImage(10, 
                                    1024, BufferedImage.TYPE_INT_ARGB);

        return new ImageIcon(bufferedImage).getImage();
    }
    
}


class ScoreText extends GameObject
{
   private static final Image[] aryImage = { 
        new ImageIcon(Main.getPath("/sprites/0.png") ).getImage(),
        new ImageIcon(Main.getPath("/sprites/1.png") ).getImage(),
        new ImageIcon(Main.getPath("/sprites/2.png") ).getImage(),
        new ImageIcon(Main.getPath("/sprites/3.png") ).getImage(),
        new ImageIcon(Main.getPath("/sprites/4.png") ).getImage(),
        new ImageIcon(Main.getPath("/sprites/5.png") ).getImage(),
        new ImageIcon(Main.getPath("/sprites/6.png") ).getImage(),
        new ImageIcon(Main.getPath("/sprites/7.png") ).getImage(),
        new ImageIcon(Main.getPath("/sprites/8.png") ).getImage(),
        new ImageIcon(Main.getPath("/sprites/9.png") ).getImage()
    };

    private class Margin
    {
        public static final int X = 3;
        public static final int Y = 12;
    }
    private int score = 0;
    private Image image;

    public ScoreText()
    {
        super( aryImage[0] );
        updateImage();
    }

    public void addScore(int score)
    {
        try{
            this.score += score;
            updateImage();
        }
        catch(Exception e){ }
    }

    public void updateImage()
    {
        int[] parsedScore = Stream.of(String.valueOf(score).split("")).mapToInt(Integer::parseInt).toArray();
        int height = aryImage[0].getHeight(null) + Margin.Y;

        BufferedImage newImage = new BufferedImage(512, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = newImage.createGraphics();

        int offset = 0;

        for(int k : parsedScore)
        {
            offset += aryImage[k].getWidth(null) + Margin.X;
        }
    

        int x = 256 - offset/2;

        for(int k : parsedScore)
        {
            Image imageNumber = aryImage[k];
            graphics.drawImage(imageNumber, x, Margin.Y, null);
            x += imageNumber.getWidth(null) + Margin.X;
        }
        graphics.dispose();

        setIamge(new ImageIcon(newImage).getImage());
        
    }

}
