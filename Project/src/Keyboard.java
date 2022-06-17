import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

//此类需封装
public class Keyboard implements KeyListener {

    //静态变量sample随时可调用
    private static Keyboard sample;

    private final boolean[] keys;

    //将按键进行标记
    private Keyboard() {
        keys = new boolean[256];
    }

    //返回键盘类
    public static Keyboard getSample() {

        if (sample == null) {
            sample = new Keyboard();
        }
        return sample;
    }

    //只要有按了某个键，就将其对应的boolean数组改成true
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() >= 0 && e.getKeyCode() < keys.length) {
            keys[e.getKeyCode()] = true;
        }
    }

    public boolean Pushed(int key) {

        if (key >= 0 && key < keys.length) {
            return keys[key];
        }

        return false;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() >= 0 && e.getKeyCode() < keys.length) {
            keys[e.getKeyCode()] = false;
        }
    }

    public void keyTyped(KeyEvent e) {
    }


}
