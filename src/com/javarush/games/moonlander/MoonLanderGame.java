package com.javarush.games.moonlander;
import com.javarush.engine.cell.*;

public class MoonLanderGame extends Game{
    public static final int WIDTH=64;
    public static final int HEIGHT=64;
    private Rocket rocket;
    private GameObject landscape;
    private GameObject platform;
    private boolean isUpPressed;
    private boolean isLeftPressed;
    private boolean isRightPressed;
    private boolean isGameStopped;
    private int score;

    private void check(){
        if (rocket.isCollision(landscape) && !(rocket.isCollision(platform) && rocket.isStopped()))
            gameOver();
        if (rocket.isCollision(platform) && rocket.isStopped())
            win();
    }
    
    private void win(){
        isGameStopped=true;
        rocket.land();
        showMessageDialog(Color.YELLOW,"***!!!WIN!!!***",Color.BLUE,40);
        stopTurnTimer();
        
    }
    
    private void gameOver(){
        isGameStopped=true;
        score=0;
        rocket.crash();
        showMessageDialog(Color.YELLOW,"***!!!GAME OVER!!!***",Color.BLUE,50);
        stopTurnTimer();
        
    }
    
    private void createGameObjects(){
        rocket = new Rocket(WIDTH/2,0);
        landscape = new GameObject(0, 25, ShapeMatrix.LANDSCAPE);
        platform = new GameObject(23,HEIGHT-1, ShapeMatrix.PLATFORM);
    }

    @Override
    public void onKeyPress(Key key){
        if (!isGameStopped){
            if (key==Key.UP){
                isUpPressed=true;
            }else
            if (key==Key.LEFT){
                isLeftPressed=true;
                isRightPressed=false;
            }else
            if (key==Key.RIGHT){
                isRightPressed=true;
                isLeftPressed=false;
            }
        }else
        if (key==Key.SPACE)
            createGame();
    }
    @Override
    public void onKeyReleased(Key key){
        if (key==Key.UP){
            isUpPressed=false;
        }else
        if (key==Key.LEFT){
            isLeftPressed=false;
        }else
        if (key==Key.RIGHT){
            isRightPressed=false;
        }
    }
    
    @Override
    public void onTurn(int i){
        rocket.move(isUpPressed,isLeftPressed,isRightPressed);
        check();
        drawScene();
        if (score>0) --score;
        setScore(score);
    }

    @Override
    public void setCellColor(int x, int y, Color color){
        if (x>0 && x<WIDTH && y>0 && y<HEIGHT)
            super.setCellColor(x,y,color);
    }

    @Override
    public void initialize() {
        showGrid(false);
        setScreenSize(WIDTH,HEIGHT);
        createGame();
    }

    private void createGame(){
        createGameObjects();
        drawScene();
        setTurnTimer(50);
        isUpPressed=false;
        isLeftPressed=false;
        isRightPressed=false;
        isGameStopped=false;
        score=1000;
    }

    private void drawScene(){
        for (int y=0;y<HEIGHT;y++)
            for (int x=0;x<WIDTH;x++)
                setCellColor(x,y,Color.BLACK);
        rocket.draw(this);
        landscape.draw(this);
    }
}
