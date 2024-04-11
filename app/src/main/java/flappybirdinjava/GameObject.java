package flappybirdinjava;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

//짜기 전에 생각할 것 ->목표, 주어진 입력, 만들어야 할 출력

/*
 * -오버라이딩-
 * 함수의 이름이 동일할 것
 * 함수의 인자가 동일할 것
 * 함수의 반환값이 동일할 것
 * 단, 클래스가 하위 클래스 일것
 * 사용할 때 들일 습관 -> 오버라이딩 쓸 때 그 위에 @Override 쓰자.
 * 이건 오버라이딩이 됐는지 확인해줌
 * 
 * -오버로딩-
 * 함수의 이름이 동일할 것
 * 인자 혹은 반환값이 다를 것
 *  반환값이 다를것
 *  인자의 개수가 다를것
 *  인자의 자료형이 다를것
 * 같은 클래스에 존재할 것
 * 
 * 
 * 추상 클래스, 메소드
 * 반드시 써야할 함수나 변수가 존재하다면 
 * 새로운 메소드나 클래스를 만들 때 그 필수 조건이 걸림
 * 까먹어서 안쓰면 경고 뜸
 */
public abstract class GameObject extends JPanel
{
    abstract void update(); //업데이트가 반드시 필요하니 추상 메소드로 설정.


    public GameObject()
    {
        setBackground(new Color(255,0,0,0));
    }

}

class BackgroundPanel extends JPanel    //배경 오브젝트
{
    private Image imgBackground = new ImageIcon(Main.getPath("/sprites/background.png")).getImage();


    private final int WIDTH = imgBackground.getWidth((this));
    private final int HEIGHT = imgBackground.getHeight((this));
    private Bird bird = new Bird();

    public BackgroundPanel()    //BackgroundPanel에 새랑 파이드 등을 붙일거니 이걸 null값을 줌
    {
        setLayout(null);
        
        bird.setLocation(100, 100);
        bird.setSize( bird.getWidth(), bird.getHeight() );
        add(bird);

        addMouseListener( new MyMouseListener() );

    }

    public void update()
    {
        bird.update();

       

    }
    


    public void paintComponent(Graphics g) 
    {
        super.paintComponent(g);

        Frame frame = Main.getFrame();
        float sizeMultiply = frame.getSizeMultiply();   //Frame.java에서 SizeMultiply를 가져와서 
                                                        //GameObject.java에서 사용가능하게 넣어줌. 
        int fixedWidth = (int)(WIDTH * sizeMultiply);
        int fixedHeight = (int)(HEIGHT * sizeMultiply);

        
        for(int i = 0; i < frame.getWidth() / fixedWidth + 1; i++)  //몇번 붙이냐? -> 배경이 부족한 만큼 붙여줌.
        {
            //drawImage(그릴 객체, x좌표, y좌표, 가로 너비, 높이, 사용하고있는 물건)
            g.drawImage(imgBackground, i * fixedWidth, 0, fixedWidth, fixedHeight, this);
        }


        
    }
    
    private class MyMouseListener extends MouseAdapter{
        @Override
        public void mousePressed(MouseEvent e)
        {
            bird.setJumpPower(-10);
        }
    }
}

class Bird extends GameObject   //새 오브젝트
{
    private Image imgBird = new ImageIcon(Main.getPath("/sprites/bird_midflap.png")).getImage();

    private final int WIDTH = imgBird.getWidth((this));
    private final int HEIGHT = imgBird.getHeight((this));
    private int jumpPower = -1;
    private final int MAX_JUMP_POWER = 2;
    private int y = getY();

    public void update()    //아마 사진이랑 위치를 업데이트 해줄 듯?
    {
        y = Main.Clamp( y + jumpPower , getHeight(), Main.getFrame().getBackgroundPanel().getHeight() );
        setLocation(100, y - getHeight() );


        if(jumpPower < MAX_JUMP_POWER) //중력 가속도
        {
            jumpPower += 1;
        }

    }

    public int getWidth() //배경 오브젝트에서 새의 크기를 가져오기 위한 함수. 왜냐? private이기 때문
    {
        return WIDTH;
    }
    public int getHeight()//배경 오브젝트에서 새의 크기를 가져오기 위한 함수. 왜냐? private이기 때문
    {
        return HEIGHT;
    }

    public void setJumpPower(int jumpPower)
    {
        this.jumpPower = jumpPower;
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Frame frame = Main.getFrame();
        float sizeMultiply = frame.getSizeMultiply();   //Frame.java에서 SizeMultiply를 가져와서 
                                                        //GameObject.java에서 사용가능하게 넣어줌. 
        int fixedWidth = (int)(WIDTH * sizeMultiply);
        int fixedHeight = (int)(HEIGHT * sizeMultiply);

        g.drawImage(imgBird, 0, 0, fixedWidth, fixedHeight, this);

        setSize(fixedWidth,fixedHeight);
        

    }
}
