package flappybirdinjava;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;

public class Frame extends JFrame {
    private BackgroundPanel pnlGame = new BackgroundPanel();
    private Timer timer = new Timer();

    private TimerTask timerTask;

    private Timer pipeSpawnTimer;
    private TimerTask pipeSpawnTimerTask;

    private static Dimension scrnSize = Toolkit.getDefaultToolkit().getScreenSize();
    private static Rectangle winSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
    private static int taskBarHeight = (int)( scrnSize.getHeight() - winSize.getHeight() );

    //Components
    private Bird bird = new Bird();
    private ScoreText scoreText = new ScoreText();
    private StartScreen startScreen = new StartScreen();
    private GameOverScreen gameOverScreen = new GameOverScreen();
    private ResetButton resetButton = new ResetButton();

    //Variable
    private float sizeMultiply = 1.0f;
    private final int ORIGIN_SIZE = 512;

    private boolean flagGameOver = false;
    private boolean flagGameStart = false;

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


        startScreen.setLocation(164, 123);
        startScreen.setSize(0, 0);
        pnlGame.add(startScreen);

        gameOverScreen.setLocation(160, 145);
        gameOverScreen.setSize(0, 0);
        pnlGame.add(gameOverScreen);

        resetButton.setLocation(204, 276);
        resetButton.setSize(0, 0);
        pnlGame.add(resetButton);

        scoreText.setLocation(0,0);
        scoreText.setSize(0,0);
        pnlGame.add(scoreText);

        
        bird.setLocation(100, 224);
        bird.setSize(100, 100);
        pnlGame.add(bird);
        
        add(pnlGame, "Game");

        pnlGame.addMouseListener( new MyMouseListener() );
        pnlGame.addKeyListener( new MyKeyAdapter() );

        pnlGame.setFocusable(true);  // 컴포넌트가 포커스를 받을 수 있도록 설정
        pnlGame.requestFocus();     // 컴포넌트에 포커스를 강제로 지정

        //Timer
        timerTask = new TimerTask() {   
            @Override
            public void run() {
                pnlGame.update();   //패널 전체 업데이트
            }
        };

        timer.scheduleAtFixedRate(timerTask, 0, 10);


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

    public void StartGame()
    {
        if(flagGameStart)
        {
            return;
        }

        flagGameStart = true;
        flagGameOver = false;
        startScreen.setVisible(false);
       


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
    }

    public void resetGame()
    {
        if(flagGameOver == false)
        {
            return;
        }

        flagGameStart = false;

        pipeSpawnTimer.cancel();
        pipeSpawnTimer.purge(); //그전에 있던 타이머를 없앰

        startScreen.setVisible(true);  //수정
        gameOverScreen.setVisible(false);
        resetButton.setVisible(false);

        scoreText.resetScore();

        bird.setLocation(100, 224);
        for (Component k  : pnlGame.getComponents() ) 
        {
            try 
            {
                Pipe pipe = (Pipe)k;
                pnlGame.remove(pipe);
            } 
            catch (Exception e){}
        }
        repaint();  //객체를 지우면 그 것을 바로 실행해달라.
        revalidate();
    }

    public void initGame()
    {
        pnlGame.update();
    }
        
    public boolean isGameStart()
    {
        return flagGameStart;
    }

    public Bird getBird()
    {
        return bird;
    }

    public void gameOver()
    {
        if(flagGameOver)
        {
            return;
        }

        flagGameOver = true;
        pipeSpawnTimer.cancel();    //타이머 끄는게 캔슬

        gameOverScreen.setVisible(true);
        resetButton.setVisible(true);

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
            StartGame();
            bird.jump();
        }
    }

    private class MyKeyAdapter extends KeyAdapter{
        public void keyPressed(KeyEvent e) 
        {
            int keycode = e.getKeyCode();

            switch (keycode) {
                case KeyEvent.VK_SPACE:
                    bird.jump();
                    break;
            }
        }
    }
    
} //Frame class






