### 占位式插件化框架

在宿主APP内使用代理类ProxyActivity来实现和插件(没有安装的apk文件)中的页面进行交互。

正因为有这个代理类，所以这种方式称为占位式或者插桩式。

插件没有组件环境即Context，解决办法是将宿主APP中的环境通过在代理Activity中传递过去；
ProxyActivity 起到的作用还有为插件包中的Activity提供了任务栈，使得按返回按钮时候可以实现出栈的过度效果；



#### 问题：

**1.在插件中为什么不能使用this？**
因为插件不需要安装在手机上运行，所以无法拥有组件环境。（没走安装过程，所以获取不到组件环境）

**2.为什么要有代理的Activity？**
因为插件中的Activity并不是一个能够运行的组件，所以需要代理Activity去代替插件中的Activity（例如：实现Activity任务进栈）

**3.这种插件化方式在写插件开发的时候注意事项有哪些？**
所有的关于操作组件环境的地方，都必须使用宿主的环境。（这也是占位式插件设计的缺点，在开发插件包的时候需要使用宿主的环境）







#### App加载插件的Activity

![img](file:///private/var/folders/8t/lx2d5qn94dd8yrxqk7mg0q3r0000gn/T/WizNote/80e6d31f-c868-431e-b4a8-688b68152509/index_files/82306852.png)







#### 插件内部跳转插件的Activity 

![img](file:///private/var/folders/8t/lx2d5qn94dd8yrxqk7mg0q3r0000gn/T/WizNote/80e6d31f-c868-431e-b4a8-688b68152509/index_files/52720120.png)







#### 插件内部启动插件的Service

![img](file:///private/var/folders/8t/lx2d5qn94dd8yrxqk7mg0q3r0000gn/T/WizNote/80e6d31f-c868-431e-b4a8-688b68152509/index_files/73512131.png)







#### 插件内部注册、接收广播接收者

![img](file:///private/var/folders/8t/lx2d5qn94dd8yrxqk7mg0q3r0000gn/T/WizNote/80e6d31f-c868-431e-b4a8-688b68152509/index_files/77918710.png)

![img](file:///private/var/folders/8t/lx2d5qn94dd8yrxqk7mg0q3r0000gn/T/WizNote/80e6d31f-c868-431e-b4a8-688b68152509/index_files/78006324.png)







#### 加载插件里的静态广播接收者，然后注册，发送

实现需要参考系统如何对apk解析，通过分析apk解析的原理来实现。

![img](file:///private/var/folders/8t/lx2d5qn94dd8yrxqk7mg0q3r0000gn/T/WizNote/80e6d31f-c868-431e-b4a8-688b68152509/index_files/80134743.png)

![img](file:///private/var/folders/8t/lx2d5qn94dd8yrxqk7mg0q3r0000gn/T/WizNote/80e6d31f-c868-431e-b4a8-688b68152509/index_files/72751438.png)