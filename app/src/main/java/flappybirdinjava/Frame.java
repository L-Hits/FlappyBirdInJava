package flappybirdinjava;
import java.awt.*;
import javax.swing.*;

public class Frame extends JFrame
{
    public Frame()
    {
        setTitle("Flappy bird in java");
		setSize(600,600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

        setLocationRelativeTo(null); 

        setMinimumSize(new Dimension(300, 300));


    }
}
