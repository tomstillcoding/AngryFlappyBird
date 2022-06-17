import java.awt.*;

public class Tube {

    public int x;
    public int y;
    public int width;
    public int height;
    public int speed = 3;

    public String direction;//方向

    private Image image;

    public Tube(String direction) {
        this.direction = direction;
        renew();
    }

    //最右边去重置为初始默认位置
    public void renew() {
        width = 66;
        height = 400;
        x = Main.WIDTH + 2;

        if (direction.equals("south")) {
            y = -(int) (Math.random() * 120) - height / 2;
        }
    }

    //管道的移动
    public void update() {
        x -= speed;
    }

    //如果撞到管道返回true
    //带下划线的都是鸟的位置
    //不带下划线的x y 均代表管道空白的位置
    public boolean collides(int _x, int _y, int _width, int _height) {

        int margin = 2;

        if (_x + _width - margin > x && _x + margin < x + width) {

            if (direction.equals("south") && _y < y + height) {
                return true;
            } else return direction.equals("north") && _y + _height > y;
        }
        return false;
    }

    //返回所有对象的属性，坐标+图片对象
    public Drawer getDrawer() {
        Drawer r = new Drawer();
        r.x = x;
        r.y = y;

        if (image == null) {
            image = ImageLoader.loadImage("assets/pipe-" + direction + ".png");
        }
        r.image = image;

        return r;
    }
}
