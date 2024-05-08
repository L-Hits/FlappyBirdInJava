package flappybirdinjava;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;

public class Frame extends JFrame {
    private BackgroundPanel pnlGame = new BackgroundPanel();
    private Timer timer = new Timer();

    //준영이 버전 하기 전 찬이의 생각
    //private Timer respawn = new Timer();

    private TimerTask timerTask;

    private Timer pipeSpawnTimer;
    private TimerTask pipeSpawnTimerTask;

    private static Dimension scrnSize = Toolkit.getDefaultToolkit().getScreenSize();
    private static Rectangle winSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
    private static int taskBarHeight = (int)( scrnSize.getHeight() - winSize.getHeight() );

    //Components
    private Bird bird = new Bird();
    private ScoreText scoreText = new ScoreText();
    //Variable
    private float sizeMultiply = 1.0f;
    private final int ORIGIN_SIZE = 512;
    private boolean flagGameOver = false;

    public Frame() {
        //Initialize
        setTitle("Flappy Bird In Java");
        setSize(513, 512);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setMinimumSize( new Dimension(256, 256) );
        setLayout( new CardLayout() );  //유니티의 prefabs 

        //Game Screen
        pnlGame.setLayout(null);
        scoreText.setLocation(0,0);
        scoreText.setSize(0,0);
        pnlGame.add(scoreText);

        bird.setLocation(100, 100);
        bird.setSize(100, 100);
        pnlGame.add(bird);


        // /* 
        // pipe_up.setLocation(480, 300);
        // pipe_up.setSize(20,40 );
        // pnlGame.add(pipe_up);

        // pipe_down.setLocation(480, -200);
        // pipe_down.setSize(20,40 );
        // pnlGame.add(pipe_down);
        // */



        add(pnlGame, "Game");
        pnlGame.addMouseListener( new MyMouseListener() );


        //Timer
        timerTask = new TimerTask() {   
            @Override
            public void run() {

                pnlGame.update();   //패널 전체 업데이트


            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 10);


        /* 
        //준영이 버전 하기 전 찬이의 생각
        timerTask = new TimerTask() {
            @Override
            public void run() {
                respawnPipe();

            }
        };
        respawn.scheduleAtFixedRate(timerTask, 0, 2000);
        */


        pipeSpawnTimer = new Timer();
        pipeSpawnTimerTask = new TimerTask()
        {
            @Override
            public void run()   //파이프 랜덤 생성
            {
                // #TODO : 파이프 생성 구문 추가
                int randY = (int)(Math.random() * 472);
                int clampY = Main.clamp(randY, PipeSpawn.GAP + Pipe.MIN_HEIGHT,
                    472 - PipeSpawn.GAP - Pipe.MIN_HEIGHT );
                
                PipeSpawn.spawnPipe( pnlGame, clampY );

            }
        };
        pipeSpawnTimer.scheduleAtFixedRate(pipeSpawnTimerTask, 0, PipeSpawn.SPAWN_DELAY);


    } //Constructor

    public float getSizeMultiply() {
        return sizeMultiply;
    }

    public int getTaskBarHeight() {
        return taskBarHeight;
    }

    public void addScore()
    {
        scoreText.addScore(1);
    }

    public Bird getBird()
    {
        return bird;
    }

    public void gameOver()
    {
        flagGameOver = true;
        pipeSpawnTimer.cancel();    //타이머 끄는게 캔슬

    }

    public boolean isgameOver()
    {
        return flagGameOver;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        int width = getWidth();
        int height = getHeight();

        if (width > height) {
            setSize(height, height);
        }
        else {
            setSize(width, width);
        }
        sizeMultiply = (float)(getHeight() - taskBarHeight) / (float)(ORIGIN_SIZE - taskBarHeight);
    }

    //Listeners
    private class MyMouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            bird.jump();
        }
    }


    //준영이 버전 하기 전 찬이의 생각
    // private void respawnPipe()  //파이프 생성
    // {
    //     Pipe_up pipe_up = new Pipe_up();
    //     Pipe_down pipe_down = new Pipe_down();

        

    //     pipe_up.setLocation(480, 200);      //-300~200?
    //     pipe_up.setSize(20,40 );
    //     pnlGame.add(pipe_up);

    //     pipe_down.setLocation(480, -300);       //-300~200?
    //     pipe_down.setSize(20,40 );
    //     pnlGame.add(pipe_down);
    // }



    private class MyKeyAdapter extends KeyAdapter{
        //TODO 스페이스만 눌렀을 때만 점프
        public void keyPressed(KeyEvent e) {
            
        }
    }
    
} //Frame class






