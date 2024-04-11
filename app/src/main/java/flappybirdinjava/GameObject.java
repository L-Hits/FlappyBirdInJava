package flappybirdinjava;

import java.awt.*;
import javax.swing.*;

//짜기 전에 생각할 것 ->목표, 주어진 입력, 만들어야 할 출력
public class GameObject 
{
    
}

class BackgroundPanel extends JPanel    //배경 오브젝트
{
    Image imgBackground = new ImageIcon(Main.getPath("/sprites/background.png")).getImage();


    private final int WIDTH = imgBackground.getWidth((this));
    private final int HEIGHT = imgBackground.getHeight((this));

    Frame frame;

    public BackgroundPanel()    //BackgroundPanel에 새랑 파이드 등을 붙일거니 이걸 null값을 줌
    {
        setLayout(null);


    }

    public void paintComponent(Graphics g) 
    {
        super.paintComponent(g);

        frame = Main.getFrame();
        float sizeMultiply = frame.getSizeMultiply();   //Frame.java에서 SizeMultiply를 가져와서 
                                                        //GameObject.java에서 사용가능하게 넣어줌. 
        int fixedWidth = (int)(WIDTH * sizeMultiply);
        int fixedHeight = (int)(HEIGHT * sizeMultiply);

        
        for(int i = 0; i < frame.getWidth() / fixedWidth + 1; i++)  //몇번 붙이냐? -> 배경이 부족한 만큼 붙여줌.
        {
            g.drawImage(imgBackground, i * fixedWidth, 0, fixedWidth, fixedHeight, this);
        }

        

    }
}
