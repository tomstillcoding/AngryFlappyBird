# FlappyAngryBird

## 一、程序的功能说明

本程序复刻了曾经风靡一时的魔性小游戏，实现了移动、跳跃、碰撞判定、计 分等一系列游戏功能

<img src="./assets/34-angrybirds.assets/Screen Shot 2022-06-16 at 00.36.16.png" alt="Screen Shot 2022-06-16 at 00.36.16" style="zoom:40%;" />

<img src="./assets/34-angrybirds.assets/Screen Shot 2022-06-16 at 00.36.39.png" alt="Screen Shot 2022-06-16 at 00.36.39" style="zoom:30%;" />

## 二、程序的算法和开发部分 

### 1、程序的算法

不断更新小鸟和水管的位置和状态，并不断在面板上重新绘制，以达到运动的效果。

其中，为了小鸟的飞行轨迹看起来是非常顺滑的抛物线，因此需要转动小鸟，Bird 类中控制小鸟旋转的 getDrawer 方法中我们建立了如下的模型: 假设小鸟的速度为 v，则小鸟在水平方向的偏转角 θ(单位为度) 为：

<img src="./assets/34-angrybirds.assets/Screen Shot 2022-06-16 at 00.37.59.png" alt="Screen Shot 2022-06-16 at 00.37.59" style="zoom: 50%;" />

但是在向下旋转时有可能会出现速度过大而使 θ > 90 的情况，因此在 θ > 90 时应将 θ 固定在 90 度，即 π/2 弧度，因此有了 getDrawer 方法中的算法。

### 2、数据结构和面向对象实现

- 总览
  - 程序共 8 个类，分别是 Main,MainGame,Panel,Drawer,Loader,Keybord,Bird,Tube. 其中 Panel 类继承了 JPanel 类，实现了 Runnable 接口，Keyborad 类实现了 KeyListener 接口。
- Drawer：包括 int 型属性 x 和 y，Image 型的属性 image 和 AffineTransform 型的属性 transform，其中 x 和 y 代表图片的位置，而 image 代表的是图片对象， 这个时候就需要新建的类 ImageLoader 来返回 Image 对象到 Drawer 中，最后一 个属性 transform 代表图像的变换形式，在小鸟的飞行过程中，它的旋转就会用到 transform。
- ImageLoader：由于我们不希望每次读取图片地址时都要进行 IO 操作来读写 文件，所以经过翻阅网上的资料，我们找到了利用 hashmap 实现对图片地址的缓 存，即可以用户查到数据，查询条件相同时，第一次走数据库，第二次以后在一段 时间内走缓存，提高响应的速度。
- Tube：类表示管道，它包含的属性包括 int 型属性 x, y width, height, speed; String 属性 direction，Image 属性 image。reset() 能够重置管道，将管道位置放在窗口右边显示部分之外，并且能够将 South 管道所在位置用 Math.random() 初始 化;update() 可以让管道移动向左按 speed 速度移动，collides() 方法可以对小鸟进 行碰撞检测，如果小鸟图片各个位置的坐标与 tube 有接触，那么就代表碰撞，即 游戏结束。
- Keyboard：我们将键盘的响应设计成改变键值对应的 boolean 类型数组下标对 应的值，来获得用户的反馈，我们建立了 keys[] 数组，其中包括了键值 P、R、 SPACE 的对应 boolean 类型值，每次 update 进行更新游戏每帧各个类的参数的时 候，我们通过 Override keyPressed() 和 keyReleased() 方法来监视键值 keys[] 的改 变，来判断是否 SPACE、R、P 是否被按下，来实现跳跃、重新开始、暂停这样 的简单功能。
- Panel：此类即是代表窗口对象，继承 JPnel，实现 Runnable 接口，新建线程， 实例化 MainGame 类，通过不停 update() 各个对象的各种属性，来将对应不同时 刻时的图像都 draw 进窗口中，而通过控制线程 Thread.sleep(30)，和管道、小鸟下 坠和飞行速度，来达到同步画面。

## 三、其他说明 

### 1、画面表现

- 控制线程休眠时间达到控制连续刷新画面的频率，使窗口不停绘制图像，达到 连续播放的画面。
- 通过使用 HashMap 缓存图片，降低读写文件次数，提升程序性能。
- 通过设计 boolean keys[] 同时判断多个按键是否被按下。利用 AffineTransform 实现简单的 2 维空间的坐标仿射变换来实现让图片旋转的目的，使鸟飞行方式显得更自然不僵硬。
- 通过设置 Delay 属性避免在快速连续按键时的键盘监听 bug。

### 2、特殊问题

- 如何实现动画的连续播放？我们在设计动画程序的时候(非 GUI 程序)遇到 了如下两个较为棘手的问题。
  - 第一个问题，如何实现动画的动态属性。采用的 是不停访问 Panel 类的 update() 方法，更新数值并 repaint() 从而不停地重画 该界面，实现我们能看到的动的效果。
  - 第二个问题，动画过快或者过慢，或者 出现屏闪。通过查与资料发现主要是和线程的睡眠时间有关，睡眠时间越短， 鸟儿速度失控就越严重并且会出现屏闪。解决办法就是提前将所有画面的属性 都改好，再一次性绘制在屏幕上，所以我们使用了 Drawer 类来统一所有图片 对象，先把所有东西的下一帧出现的位置都设置好，然后一次性把画好的内容 显示出来。
- 如何使小鸟飞行时有旋转的动作？在 Drawer 中添加 AffineTransform 型的属 性 transform，代表图像的变换形式。
- 如何同时监听多个键盘按键？建立 keys[256] 数组，其中包括键值 P、R、 SPACE 的对应 boolean 类型值，每次 update 进行更新游戏每帧各个类的参数 的时候，通过 Override keyPressed() 和 keyReleased() 方法来监视键值 keys[] 的改变，来判断是否 SPACE、R、P 是否被按下，来实现跳跃、重新开始、暂 停这样的简单功能。

## 四、参考资料

1、关于 paint(),repaint(),paintcomponent() https://blog.csdn.net/zi_jun/article/details/7624732

2、JPanel中使用paintComponent()方法绘图时，调用repaint()为何不能刷新窗口? https://bbs.csdn.net/topics/340015900

3、Graphics2D使用详解[转] https://blog.csdn.net/qq_32786873/article/details/71405933

4、JFrame(框架)中添加和设置JPanel(面板)的方法 https://blog.csdn.net/lyxaiclr/article/details/7366145/

5、关于 KeyListener 的简单使用 https://blog.csdn.net/u010142437/article/details/8914773

6、javakeypressed长按的时候会有一小段的延迟 https://zhidao.baidu.com/question/744767566807027372.html

7、HashMap实现简单的缓存Cache https://blog.csdn.net/u012260707/article/details/50592893

8、使用 HashMap 做一个缓存案例 [缓存一张图片] https://blog.csdn.net/zhyzh134/article/details/50754854

9、解决 Java 设计游戏的时候，画面不动和闪烁问题，双缓冲 https://blog.csdn.net/utnewbear/article/details/8190817

