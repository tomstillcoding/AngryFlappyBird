import java.awt.event.KeyEvent;
import java.util.ArrayList;

//Game类实现的功能：
//监视、返回含有所有对象的容器、移动管道、判断撞击
public class MainGame {

    public static final int PIPE_DELAY = 100;
    //游戏属性
    public int score;
    //判断游戏是否继续
    public Boolean gameover;
    public Boolean started;
    private Boolean paused;
    //控制按键时的相邻两次空格
    private int pauseDelay;
    private int restartDelay;
    private int pipeDelay;
    private Bird bird;
    private ArrayList<Tube> pipes;
    private final Keyboard keyboard;

    public MainGame() {
        keyboard = Keyboard.getSample();
        restart();
    }

    //功能：监视
    private void watchForStart() {
        if (!started && keyboard.Pushed(KeyEvent.VK_SPACE)) {
            started = true;
        }
    }

    private void watchForPause() {
        if (pauseDelay > 0)
            pauseDelay--;

        if (keyboard.Pushed(KeyEvent.VK_P) && pauseDelay <= 0) {
            paused = !paused;
            pauseDelay = 10;
        }
    }

    private void watchForReset() {
        if (restartDelay > 0)
            restartDelay--;

        if (keyboard.Pushed(KeyEvent.VK_R) && restartDelay <= 0) {
            restart();
            restartDelay = 10;
            return;
        }
    }

    //每一次刷新页面时都要判断暂停和
    public void update() {
        //监视开始
        watchForStart();

        if (!started)
            return;
        //监视暂停
        watchForPause();
        //监视重置
        watchForReset();

        if (paused)
            return;

        bird.update();

        if (gameover)
            return;
        //全都没有，移动管道+检查撞击=更新
        movePipes();
        checkForCollisions();
    }

    //功能：返回容器
    //将背景、管道、小鸟全部绘制到Render容器中并返回给GamePanel处理
    public ArrayList<Drawer> getDrawers() {
        ArrayList<Drawer> drawers = new ArrayList<Drawer>();
        drawers.add(new Drawer(0, 0, "assets/background.png"));


        for (Tube pipe : pipes)
            drawers.add(pipe.getDrawer());
        drawers.add(bird.getDrawer());
        return drawers;
    }


    //功能：移动管道
    private void movePipes() {
        pipeDelay--;
        //什么时候生产新的上下管道
        if (pipeDelay < 0) {
            pipeDelay = PIPE_DELAY;
            Tube northPipe = null;
            Tube southPipe = null;

            // 
            for (Tube pipe : pipes) {
                if (pipe.x - pipe.width < 0) {
                    if (northPipe == null) {
                        northPipe = pipe;
                    } else if (southPipe == null) {
                        southPipe = pipe;
                        break;
                    }
                }
            }
            //生成N管道，详细见class pipe
            if (northPipe == null) {
                Tube pipe = new Tube("north");
                pipes.add(pipe);
                northPipe = pipe;
            } else {
                northPipe.renew();
            }
            //生成S管道
            if (southPipe == null) {
                Tube pipe = new Tube("south");
                pipes.add(pipe);
                southPipe = pipe;
            } else {
                southPipe.renew();
            }
            //使得中间部分可以通过
            northPipe.y = southPipe.y + southPipe.height + 175;
        }

        for (Tube pipe : pipes) {
            pipe.update();
        }
    }


    //功能：检测撞击
    private void checkForCollisions() {

        for (Tube pipe : pipes) {
            if (pipe.collides(bird.x, bird.y, bird.width, bird.height)) {
                gameover = true;
                bird.death = true;
            } else if (pipe.x == bird.x && pipe.direction.equalsIgnoreCase("south")) {
                score++;
            }
        }

        // 检测撞地
        if (bird.y + bird.height > Main.HEIGHT - 80) {
            gameover = true;
            bird.y = Main.HEIGHT - 80 - bird.height;
        }
    }

    //按R键进行初始化
    public void restart() {
        paused = false;
        started = false;
        gameover = false;

        score = 0;
        pauseDelay = 0;
        restartDelay = 0;
        pipeDelay = 0;

        bird = new Bird();
        pipes = new ArrayList<Tube>();
    }
}
