# StudyTomcat

## Tomcat架构

### 1、Tomcat目录结构

一级目录
 bin ——Tomcat执行脚本目录
 conf ——Tomcat配置文件
 lib ——Tomcat运行需要的库文件（JARS）
 logs ——Tomcat执行时的LOG文件
 temp ——Tomcat临时文件存放目录
 webapps ——Tomcat的主要Web发布目录（存放我们自己的JSP,SERVLET,类）
 work ——Tomcat的工作目录，Tomcat将翻译JSP文件到的Java文件和class文件放在这里。

二级目录（仅列出一级目录下几个重要的文件）
 (1) bin目录下的文件
 catalina.sh 用于启动和关闭tomcat服务器
 configtest.sh 用于检查配置文件
 startup.sh 启动Tomcat脚本
 shutdown.sh 关闭Tomcat脚本
 (2) conf目录下的文件
 server.xml Tomcat 的全局配置文件
 web.xml 为不同的Tomcat配置的web应用设置缺省值的文件
 tomcat-users.xml Tomcat用户认证的配置文件
 (3) lib目录下的文件
 包含被Tomcat使用的各种各样的jar文件。
 (4) logs目录下的文件
 localhost_access_log.2013-09-18.txt 访问日志
 localhost.2013-09-18.log 错误和其它日志
 manager.2013-09-18.log 管理日志
 catalina.2013-09-18.log Tomcat启动或关闭日志文件
 (5) webapps目录下的文件
 含Web应用的程序（JSP、Servlet和JavaBean等）
 (6) work目录下的文件
 由Tomcat自动生成，这是Tomcat放置它运行期间的中间(intermediate)文件(诸如编译的JSP文件)地方。如果当Tomcat运行时，你删除了这个目录那么将不能够执行包含JSP的页面。

### 2、浏览器访问服务器的流程

![image-20201103231341921](https://gitee.com/xiyuximing/image/raw/master/image-20201103231341921.png)

浏览器访问服务器使用的是http协议。http协议是应用层协议，只是定义了数据的通信格式。数据传输还是依靠的TCP/IP协议。

TCP数据格式由TCP请求头和TCP请求数据组成。而TCP请求数据是按http协议进行组织，包括http请求头和http请求体。

### 3、Tomcat系统架构

#### 3.1、Tomcat请求处理大致过程

**Tomcat是一个HTTP服务器，可以接收并处理HTTP请求**

![image-20201103233410692](https://gitee.com/xiyuximing/image/raw/master/image-20201103233410692.png)

HTTP 服务器接收到请求之后把请求交给Servlet容器来处理，Servlet 容器通过Servlet接⼝调⽤业务类。Servlet接⼝和Servlet容器这⼀整套内容叫作Servlet规范。

Tomcat既按照Servlet规范的要求去实现了Servlet容器。**tomcat即是http服务器，也是一个servlet容器。**

#### 3.2、Servlet容器处理流程

![image-20201103234154133](https://gitee.com/xiyuximing/image/raw/master/image-20201103234154133.png)

当⽤户请求某个URL资源时
1）HTTP服务器会把请求信息request对象使⽤ServletRequest对象封装起来
2）进⼀步去调⽤Servlet容器中某个具体的Servlet
3）在 2）中，Servlet容器拿到请求后，根据URL和Servlet的映射关系，找到相应的Servlet
4）如果Servlet还没有被加载，就⽤反射机制创建这个Servlet，并调⽤Servlet的init⽅法来完成初始化
5）接着调⽤这个具体Servlet的service⽅法来处理请求，请求处理结果使⽤ServletResponse对象封装
6）把ServletResponse对象返回给HTTP服务器，HTTP服务器会把响应response对象发送给客户端

#### 3.3 Tomcat 系统总体架构

![image-20201104000524557](https://gitee.com/xiyuximing/image/raw/master/image-20201104000524557.png)

Tomcat 设计了两个核⼼组件**连接器**（Connector）和**容器**（Container）来完成 Tomcat 的两⼤核⼼功能。
**连接器**，负责对外交流： 处理Socket连接，负责⽹络字节流与Request和Response对象的转化；
**容器**，负责内部处理：加载和管理Servlet，以及具体处理Request请求；

