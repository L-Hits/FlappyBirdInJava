package flappybirdinjava;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Frame extends JFrame
{
    //동사 + 명사 + 형용사 or 목적어
    //변수, 함수 통합

    //Variable(변수)
    private float sizeMultiply = 1.0f; //프레임 크기 배율을 정해주기 위함
    private final int ORIGIN_SIZE = 512;    //상수값 프레임 사이즈
    
    BackgroundPanel panBackground = new BackgroundPanel();
   
    public Frame()
    {
        setTitle("Flappy bird in java");
		setSize(512,512);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

        setLocationRelativeTo(null); //중앙 표시
        setMinimumSize(new Dimension(256, 256)); //최소화면 설정

        


        add(panBackground);
    }

    public float getSizeMultiply() //GameObject에서 Frame의 크기를 받기위한 get함수
    {
        return sizeMultiply;
    }

    @Override
    public void paint(Graphics g) //프레임의 변경이 일어날 경우
    {
        super.paint(g); //paint에 들어가 있는 모든 정보를 긁어옴

        int width = getWidth();
        int height = getHeight();

        if (width > height) //작은 변 기준으로 화면이 바뀜
        {
            setSize(height, height);
        }else 
        {
            setSize(width, width);
        }

        //변화된 가로변 : 원래 비율 = 변화할 변 : 1
        
        sizeMultiply = (float)getWidth() / (float)ORIGIN_SIZE; //화면 비율을 정해줌
    }

}
