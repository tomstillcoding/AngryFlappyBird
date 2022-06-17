import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;

public class Bird {

    public int x;
    public int y;
    public int width;
    public int height;

    public boolean death;

    public double ySpeed;
    public double gravity;

    private int jumpDelay; //控制相邻两次跳跃的最小间隔
    private double rotation;

    private Image image;
    private final Keyboard keyboard;

    //固有属性
    public Bird() {
        //起始坐标
        x = 70;
        y = 210;
        //垂直速度，鸟图片属性，重力加速度
        ySpeed = 0;
        width = 40;
        height = 40;
        gravity = 0.5;
        jumpDelay = 0;//控制相邻两次跳跃的间隔
        rotation = 0.0;
        death = false;
        keyboard = Keyboard.getSample();
    }

    //更新图片角度达到旋转
    public Drawer getDrawer() {
        Drawer r = new Drawer();
        r.x = x;
        r.y = y;
        if (image == null) {
            image = ImageLoader.loadImage("assets/bird.png");
        }
        r.image = image;
        rotation = (90 * (ySpeed + 20) / 20) - 90;
        rotation = rotation * Math.PI / 180;
        //向下旋转不能超过90度！！
        if (rotation > Math.PI / 2)
            rotation = Math.PI / 2;
        r.transform = new AffineTransform();
        r.transform.translate(x + width / 2, y + height / 2);
        r.transform.rotate(rotation);
        r.transform.translate(-width / 2, -height / 2);

        return r;
    }

    //更新鸟的位置
    public void update() {
        if (y >= 390) {
            y = 390;
            return;
        }
        if (ySpeed < 0 && death) {
            ySpeed = 0;
        }
        ySpeed += gravity;

        if (jumpDelay > 0)
            jumpDelay--;

        if (!death && keyboard.Pushed(KeyEvent.VK_SPACE) && jumpDelay <= 0) {
            //由于按了SPACE竖直方向速度变为-10
            ySpeed = -10;
            jumpDelay = 2;
        }

        y += (int) ySpeed;
        if (y < 0) {
            y = 0;
            ySpeed = 0;
        }

    }
}
