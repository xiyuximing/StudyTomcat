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

### 4、Tomcat连接器组件Coyote

#### 4.1、Coyote简介

Coyote 是Tomcat 中连接器的组件名称 , 是对外的接⼝。客户端通过Coyote与服务器建⽴连接、发送请求并接受响应 。

（1）Coyote 封装了底层的⽹络通信（Socket 请求及响应处理）
（2）Coyote 使Catalina 容器（容器组件）与具体的请求协议及IO操作⽅式完全解耦
（3）Coyote 将Socket 输⼊转换封装为 Request 对象，进⼀步封装后交由Catalina 容器进⾏处理，处理请求完成后, Catalina 通过Coyote 提供的Response 对象将结果写⼊输出流
（4）Coyote 负责的是具体协议（应⽤层）和IO（传输层）相关内容

![image-20201104223721728](https://gitee.com/xiyuximing/image/raw/master/image-20201104223721728.png)

**Tomcat⽀持多种应⽤层协议和I/O模型，如下：**

![image-20201104223858567](https://gitee.com/xiyuximing/image/raw/master/image-20201104223858567.png)

**默认应用层协议为HTTP/1.1，默认IO模型在Tomcat8后改为NIO模型。**

在 8.0 之前 ，Tomcat 默认采⽤的I/O⽅式为 BIO，之后改为 NIO。 ⽆论 NIO、NIO2 还是 APR， 在性
能⽅⾯均优于以往的BIO。 如果采⽤APR， 甚⾄可以达到 Apache HTTP Server 的影响性能。

#### 4.2、Coyote内部组件及流程

![image-20201104225346841](https://gitee.com/xiyuximing/image/raw/master/image-20201104225346841.png)

​	

| 组件            | 作用                                                         |
| --------------- | ------------------------------------------------------------ |
| EndPoint        | EndPoint 是 Coyote 通信端点，即通信监听的接⼝，是具体Socket接收和发送处理器，是对传输层的抽象，因此EndPoint⽤来实现TCP/IP协议的 |
| Processor       | Processor 是Coyote 协议处理接⼝ ，如果说EndPoint是⽤来实现TCP/IP协议的，那么Processor⽤来实现HTTP协议，Processor接收来⾃EndPoint的Socket，读取字节流解析成Tomcat Request和Response对象，并通过Adapter将其提交到容器处理，Processor是对应⽤层协议的抽象 |
| ProtocolHandler | Coyote 协议接⼝， 通过Endpoint 和 Processor ， 实现针对具体协议的处理能⼒。Tomcat 按照协议和I/O 提供了6个实现类 ： AjpNioProtocol ，AjpAprProtocol， AjpNio2Protocol ， Http11NioProtocol ，Http11Nio2Protocol ，Http11AprProtocol |
| Adapter         | 由于协议不同，客户端发过来的请求信息也不尽相同，Tomcat定义了⾃⼰的Request类来封装这些请求信息。ProtocolHandler接⼝负责解析请求并⽣成Tomcat Request类。但是这个Request对象不是标准的ServletRequest，不能⽤Tomcat Request作为参数来调⽤容器。Tomcat设计者的解决⽅案是引⼊CoyoteAdapter，这是适配器模式的经典运⽤，连接器调⽤CoyoteAdapter的Sevice⽅法，传⼊的是Tomcat Request对象，CoyoteAdapter负责将Tomcat Request转成ServletRequest，再调⽤容器 |

### 5、Tomcat Servlet 容器 Catalina

#### 5.1、Tomcat 模块分层结构图及Catalina位置

Tomcat是⼀个由⼀系列可配置（conf/server.xml）的组件构成的Web容器，⽽Catalina是Tomcat的servlet容器。
从另⼀个⻆度来说，**Tomcat 本质上就是⼀款 Servlet 容器， 因为 Catalina 才是 Tomcat 的核心** ，其他模块都是为Catalina 提供⽀撑的。 ⽐如 ： 通过 Coyote 模块提供链接通信，Jasper 模块提供 JSP 引擎，Naming 提供JNDI 服务，Juli 提供⽇志服务。

![image-20201104230442088](https://gitee.com/xiyuximing/image/raw/master/image-20201104230442088.png)

#### 5.2、Servlet 容器 Catalina 的结构

Tomcat/Catalina实例

![image-20201104231631465](https://gitee.com/xiyuximing/image/raw/master/image-20201104231631465.png)

可以认为**整个Tomcat就是一个catalina实例**，因为Catalina是Tomcat的核⼼。Tomcat 启动的时候会初始化这个实例，Catalina实例通过加载**server.xml**完成其他实例的创建，创建并管理⼀个Server，Server创建并管理多个服务，每个服务⼜可以有多个Connector和⼀个Container。

一个catalina实例中有

​	一个Server实例中有

​		多个service，一个service中有

​			多个Connector实例及一个Container实例

- Catalina（一）

  负责解析Tomcat的配置⽂件（server.xml） , 以此来创建服务器Server组件并进⾏管理

- Server（一）

  服务器表示整个Catalina Servlet容器以及其它组件，负责组装并启动Servlaet引擎,Tomcat连接器。Server通过实现Lifecycle接⼝，提供了⼀种优雅的启动和关闭整个系统的⽅式

- Service（多个）

  服务是Server内部的组件，⼀个Server包含多个Service。它将若⼲个Connector组件绑定到⼀个Container

- Container（一个）

  容器，负责处理⽤户的servlet请求，并返回对象给web⽤户的模块

#### 5.3、Container 组件的具体结构

Container组件下有⼏种具体的组件，分别是Engine、Host、Context和Wrapper。这4种组件（容器）是⽗⼦关系。Tomcat通过⼀种分层的架构，使得Servlet容器具有很好的灵活性。

- Engine（一个）

  表示整个Catalina的Servlet引擎，⽤来管理多个虚拟站点，⼀个Service最多只能有⼀个Engine，但是⼀个引擎可包含多个Host

- Host（多个）

  代表⼀个虚拟主机，或者说⼀个站点，可以给Tomcat配置多个虚拟主机地址，⽽⼀个虚拟主机下可包含多个Context

- Context（多个）

  表示⼀个Web应⽤程序， ⼀个Web应⽤可包含多个Wrapper

- Wrapper（多个）

  表示⼀个Servlet，Wrapper 作为容器中的最底层，不能包含⼦容器

**上述组件的配置其实就体现在conf/server.xml中。**

## Tomcat 服务器核心配置

Tomcat主要配置文件是server.xml文件的配置，包含的了Servlet容器的相关配置，即Catalina的配置。

**主要结构：**

``` xml
<?xml version="1.0" encoding="UTF-8"?>
<!--Server 根元素，创建⼀个Server实例，⼦标签有 Listener、GlobalNamingResources、Service-->
<Server port="8005" shutdown="SHUTDOWN">
  <!--定义监听器-->
  <Listener className="org.apache.catalina.startup.VersionLoggerListener" />
  <!-- 定义服务器的全局JNDI资源
  -->
  <GlobalNamingResources>
    <!-- Editable user database that can also be used by
         UserDatabaseRealm to authenticate users
    -->
    <Resource name="UserDatabase" auth="Container"
              type="org.apache.catalina.UserDatabase"
              description="User database that can be updated and saved"
              factory="org.apache.catalina.users.MemoryUserDatabaseFactory"
              pathname="conf/tomcat-users.xml" />
  </GlobalNamingResources>

  <!--
  定义⼀个Service服务，⼀个Server标签可以有多个Service服务实例
  -->
  <Service name="Catalina">
  </Service>
</Server>
```

**Server标签：**

``` xml
<?xml version="1.0" encoding="UTF-8"?>
<!--
  port：关闭服务器的监听端⼝
  shutdown：关闭服务器的指令字符串
-->
<Server port="8005" shutdown="SHUTDOWN">
  <!-- 以⽇志形式输出服务器 、操作系统、JVM的版本信息 -->
  <Listener className="org.apache.catalina.startup.VersionLoggerListener" />
  <!-- Security listener. Documentation at /docs/config/listeners.html
  <Listener className="org.apache.catalina.security.SecurityListener" />
  -->
  <!--APR library loader. Documentation at /docs/apr.html -->
  <!-- 加载（服务器启动） 和 销毁 （服务器停⽌） APR。 如果找不到APR库， 则会输出⽇志， 并不影响 Tomcat启动 -->
  <Listener className="org.apache.catalina.core.AprLifecycleListener" SSLEngine="on" />
  <!-- Prevent memory leaks due to use of particular java/javax APIs-->
  <!-- 避免JRE内存泄漏问题 -->
  <Listener className="org.apache.catalina.core.JreMemoryLeakPreventionListener" />
  <!-- 加载（服务器启动） 和 销毁（服务器停⽌） 全局命名服务 -->
  <Listener className="org.apache.catalina.mbeans.GlobalResourcesLifecycleListener" />
  <!-- 在Context停⽌时重建 Executor 池中的线程， 以避免ThreadLocal 相关的内存泄漏 -->
  <Listener className="org.apache.catalina.core.ThreadLocalLeakPreventionListener" />

  <!-- Global JNDI resources
       Documentation at /docs/jndi-resources-howto.html
  -->
  <GlobalNamingResources>
    <!-- Editable user database that can also be used by
         UserDatabaseRealm to authenticate users
    -->
    <Resource name="UserDatabase" auth="Container"
              type="org.apache.catalina.UserDatabase"
              description="User database that can be updated and saved"
              factory="org.apache.catalina.users.MemoryUserDatabaseFactory"
              pathname="conf/tomcat-users.xml" />
  </GlobalNamingResources>

 
  <Service name="Catalina">

  </Service>
</Server>
```

**Service标签：**

``` xml
<!--
  该标签⽤于创建 Service 实例，默认使⽤ org.apache.catalina.core.StandardService。
  默认情况下，Tomcat 仅指定了Service 的名称， 值为 "Catalina"。
  Service ⼦标签为 ： Listener、Executor、Connector、Engine，
  其中：
  Listener ⽤于为Service添加⽣命周期监听器，
  Executor ⽤于配置Service 共享线程池，
  Connector ⽤于配置Service 包含的链接器，
  Engine ⽤于配置Service中链接器对应的Servlet 容器引擎
-->
<Service name="Catalina">
...
</Service>
```

**Executor 标签:**

``` xml
<!--
  默认情况下，Service 并未添加共享线程池配置。 
	如果我们想添加⼀个线程池， 可以在<Service> 下添加如下配置：
  name：线程池名称，⽤于 Connector中指定
  namePrefix：所创建的每个线程的名称前缀，⼀个单独的线程名称为
  namePrefix+threadNumber
  maxThreads：池中最⼤线程数
  minSpareThreads：活跃线程数，也就是核⼼池线程数，这些线程不会被销毁，会⼀直存在
  maxIdleTime：线程空闲时间，超过该时间后，空闲线程会被销毁，默认值为6000（1分钟），单位毫秒
  maxQueueSize：在被执⾏前最⼤线程排队数⽬，默认为Int的最⼤值，也就是⼴义的⽆限。除⾮特  殊情况，这个值 不需要更改，否则会有请求不会被处理的情况发⽣
  prestartminSpareThreads：启动线程池时是否启动 minSpareThreads部分线程。默认值为  false，即不启动
  threadPriority：线程池中线程优先级，默认值为5，值从1到10
  className：线程池实现类，未指定情况下，默认实现类为
  org.apache.catalina.core.StandardThreadExecutor。如果想使⽤⾃定义线程池⾸先需要实现
  org.apache.catalina.Executor接⼝
-->
<Executor name="commonThreadPool"
  namePrefix="thread-exec-"
  maxThreads="200"
  minSpareThreads="100"
  maxIdleTime="60000"
  maxQueueSize="Integer.MAX_VALUE"
  prestartminSpareThreads="false"
  threadPriority="5"
  className="org.apache.catalina.core.StandardThreadExecutor"/>
```

**Connector 标签:**
Connector 标签⽤于创建链接器实例
默认情况下，server.xml 配置了两个链接器，⼀个⽀持HTTP协议，⼀个⽀持AJP协议

``` xml
  <!--
  port：端⼝号，Connector ⽤于创建服务端Socket 并进⾏监听， 以等待客户端请求链接。如果该属性设置为0， Tomcat将会随机选择⼀个可⽤的端⼝号给当前Connector 使⽤
  protocol：当前Connector ⽀持的访问协议。 默认为 HTTP/1.1 ， 并采⽤⾃动切换机制选择⼀个基于 JAVA NIO 的链接器或者基于本地APR的链接器（根据本地是否含有Tomcat的本地库判定）
  connectionTimeOut:Connector 接收链接后的等待超时时间， 单位为 毫秒。 -1 表示不超时。
  redirectPort：当前Connector 不⽀持SSL请求， 接收到了⼀个请求， 并且也符合security-constraint 约束，  需要SSL传输，Catalina⾃动将请求重定向到指定的端⼝。
  executor：指定共享线程池的名称， 也可以通过maxThreads、minSpareThreads 等属性配置内部线程池。
  URIEncoding:⽤于指定编码URI的字符编码， Tomcat8.x版本默认的编码为 UTF-8 , Tomcat7.x版本默认为ISO-8859-1
	-->
	<Connector port="8080" protocol="HTTP/1.1"
               connectionTimeout="20000"
               redirectPort="8443" />
    <!-- A "Connector" using the shared thread pool-->
    <!--
    <Connector executor="tomcatThreadPool"
               port="8080" protocol="HTTP/1.1"
               connectionTimeout="20000"
               redirectPort="8443" />
    -->
		<Connector port="8009" protocol="AJP/1.3" redirectPort="8443" />
```

**Engine 标签:**

对应servlet引擎

``` xml
<!--
  name： ⽤于指定Engine 的名称， 默认为Catalina
  defaultHost：默认使⽤的虚拟主机名称， 当客户端请求指向的主机⽆效时， 将交由默认的虚拟主机处理， 默认为localhost
-->
<Engine name="Catalina" defaultHost="localhost">
	...
</Engine>
```

**Realm标签：**

Realm是Tomcat中为web应用程序提供访问认证和角色管理的机制

```       xml
      <!-- Use the LockOutRealm to prevent attempts to guess user passwords
           via a brute-force attack -->
      <Realm className="org.apache.catalina.realm.LockOutRealm">
        <!-- This Realm uses the UserDatabase configured in the global JNDI
             resources under the key "UserDatabase".  Any edits
             that are performed against this UserDatabase are immediately
             available for use by the Realm.  -->
        <Realm className="org.apache.catalina.realm.UserDatabaseRealm"
               resourceName="UserDatabase"/>
      </Realm>
```

**host标签：**

``` xml
<!--
	name:主机名称
	appBase:程序目录
	unpackWARs:是否解压war包
	autoDeploy:热部署
-->
<Host name="localhost"  appBase="webapps"
            unpackWARs="true" autoDeploy="true">
```

**Valve标签：**

记录日志处理器，将日志以固定格式记录至logs目录下

``` xml
        <Valve className="org.apache.catalina.valves.AccessLogValve" directory="logs"
               prefix="localhost_access_log" suffix=".txt"
               pattern="%h %l %u %t &quot;%r&quot; %s %b" />
```

**Context 标签:**

Context 标签⽤于配置⼀个Web应⽤，如下：

``` xml
<Host name="www.abc.com" appBase="webapps" unpackWARs="true" autoDeploy="true">
  <!--
    docBase：Web应⽤⽬录或者War包的部署路径。可以是绝对路径，也可以是相对于 Host appBase的
    相对路径。
    path：Web应⽤的Context 路径。如果我们Host名为localhost， 则该web应⽤访问的根路径为：
    http://localhost:8080/web_demo。
  -->
  <Context docBase="/Users/yingdian/web_demo" path="/web3"></Context>
  <Valve className="org.apache.catalina.valves.AccessLogValve"
  directory="logs"
  prefix="localhost_access_log" suffix=".txt"
  pattern="%h %l %u %t &quot;%r&quot; %s %b" />
</Host>
```



**实践1：**

同时配置www.abc.com和www.def.com两个站点。

1. 配置两个host标签

   ``` xml
       <Host name="www.abc.com"  appBase="webapps"
               unpackWARs="true" autoDeploy="true">
   			<Valve className="org.apache.catalina.valves.AccessLogValve" directory="logs"
                  prefix="localhost_access_log" suffix=".txt"
                  pattern="%h %l %u %t &quot;%r&quot; %s %b" />
   
       </Host>
   	  <Host name="www.edf.com"  appBase="webapps2"
   		unpackWARs="true" autoDeploy="true">
   
           <!-- SingleSignOn valve, share authentication between web applications
                Documentation at: /docs/config/valve.html -->
           <!--
           <Valve className="org.apache.catalina.authenticator.SingleSignOn" />
           -->
   
           <!-- Access log processes all example.
                Documentation at: /docs/config/valve.html
                Note: The pattern used is equivalent to using pattern="common" -->
           <Valve className="org.apache.catalina.valves.AccessLogValve" directory="logs"
                  prefix="localhost_access_log" suffix=".txt"
                  pattern="%h %l %u %t &quot;%r&quot; %s %b" />
   
   ```

2. 修改hosts

   127.0.0.1 www.abc.com
   127.0.0.1 www.edf.com

3. 效果：

   ![image-20201105234755363](https://gitee.com/xiyuximing/image/raw/master/image-20201105234755363.png)

![image-20201105234811193](C:\Users\Yang\AppData\Roaming\Typora\typora-user-images\image-20201105234811193.png)

**示例2：**

配置context

1. 复制webapps下的ROOT目录至E:\tomcat\webapps-context\ROOT，配置server.xml

   ```xml
     <Host name="www.abc.com"  appBase="webapps"
           unpackWARs="true" autoDeploy="true">
   	<Context docBase="E:\tomcat\webapps-context\ROOT" path="/web3"></Context>
   	<Valve className="org.apache.catalina.valves.AccessLogValve" directory="logs"
              prefix="localhost_access_log" suffix=".txt"
              pattern="%h %l %u %t &quot;%r&quot; %s %b" />
   
     </Host>
   ```

2. 效果

   ![image-20201105235451744](https://gitee.com/xiyuximing/image/raw/master/image-20201105235451744.png)

## 手写迷你版tomcat

#### 1.0浏览器请求http://localhost:8080,返回一个固定的字符串到页面"Hello Minicat!"

关键代码：

``` java
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("=====>>>Minicat start on port：" + port);
        while (true) {
            Socket socket = serverSocket.accept();
            OutputStream outputStream = socket.getOutputStream();
            String responseText = "Hello minCat !";
            outputStream.write(HttpPotocolUtil.getHttpResponse200(responseText).getBytes());
          	socket.close();
        }
```

#### 2.0需求：封装Request和Response对象，返回html静态资源文件



#### 3.0需求：可以请求动态资源（Servlet）
## Tomcat 源码构建及核心流程源码剖析



## Tomcat 类加载机制剖析

### 1、JVM类加载机制

| 类加载器                                     | 作⽤                                                         |
| -------------------------------------------- | ------------------------------------------------------------ |
| 引导启动类加载器BootstrapClassLoader         | c++编写，加载java核⼼库 java.*,⽐如rt.jar中的类，构造ExtClassLoader和AppClassLoader |
| 扩展类加载器 ExtClassLoader                  | java编写，加载扩展库 JAVA_HOME/lib/ext⽬录下的jar中的类，如classpath中的jre ，javax.*或者java.ext.dir指定位置中的类 |
| 系统类加载器SystemClassLoader/AppClassLoader | 默认的类加载器，搜索环境变量 classpath 中指明的路径          |
| 自定义类加载器                               | 加载指定路径的 class ⽂件                                    |

![image-20201108144003721](https://gitee.com/xiyuximing/image/raw/master/image-20201108144003721.png)

#### 2、双亲委派机制

![image-20201108145455741](https://gitee.com/xiyuximing/image/raw/master/image-20201108145455741.png)

当某个类加载器需要加载某个.class⽂件时，它⾸先把这个任务委托给他的上级类加载器，递归这个操作，如果上级的类加载器没有加载，⾃⼰才会去加载这个类。

- 如果⼀直到底层的类加载都没有加载到，那么就会抛出异常 ClassNotFoundException
- 如果同样在 classpath 指定的⽬录中和⾃⼰⼯作⽬录中存放相同的class，会优先加载 classpath ⽬录中的⽂件

**双亲委派机制的作⽤**

- 防⽌重复加载同⼀个.class。通过委托去向上⾯问⼀问，加载过了，就不⽤再加载⼀遍。保证数据安全
- 保证核⼼.class不能被篡改。通过委托⽅式，不会去篡改核⼼.class，即使篡改也不会去加载，即使加载也不会是同⼀个.class对象了。不同的加载器加载同⼀个.class也不是同⼀个.class对象。这样保证了class执⾏安全（如果⼦类加载器先加载，那么我们可以写⼀些与java.lang包中基础类同名
  的类， 然后再定义⼀个⼦类加载器，这样整个应⽤使⽤的基础类就都变成我们⾃⼰定义的类了。）
  Object类 -----> ⾃定义类加载器（会出现问题的，那么真正的Object类就可能被篡改了）

## Tomcat对HTTPS的支持

### https简介

![image-20201108153555173](https://gitee.com/xiyuximing/image/raw/master/image-20201108153555173.png)

Http超⽂本传输协议，明⽂传输 ，传输不安全，https在传输数据的时候会对数据进⾏加密
ssl协议
TLS(transport layer security)协议

**HTTPS和HTTP的主要区别**

- HTTPS协议使⽤时需要到电⼦商务认证授权机构（CA）申请SSL证书
- tomcat中HTTP默认使⽤8080端⼝，HTTPS默认使⽤8443端⼝
- HTTPS则是具有SSL加密的安全性传输协议，对数据的传输进⾏加密，效果上相当于HTTP的升级版
- HTTP的连接是⽆状态的，不安全的；HTTPS协议是由SSL+HTTP协议构建的可进⾏加密传输、身份认证的⽹络协议，⽐HTTP协议安全

### HTTPS⼯作原理

![image-20201108153730837](https://gitee.com/xiyuximing/image/raw/master/image-20201108153730837.png)

### Tomcat 对 HTTPS 的⽀持

1. 使⽤ JDK 中的 keytool ⼯具⽣成免费的秘钥库⽂件(证书)。

   ``` shell
   keytool -genkey -alias test -keyalg RSA -keystore test.keystore
   ```

   ![image-20201108155935741](https://gitee.com/xiyuximing/image/raw/master/image-20201108155935741.png)

   2. 配置service.xml中的Connector标签

      ``` xml
          <Connector port="8443" protocol="org.apache.coyote.http11.Http11NioProtocol"
                     maxThreads="150" SSLEnabled="true">
              <SSLHostConfig>
                  <Certificate certificateKeystoreFile="conf/test.keystore"
      						 certificateKeystorePassword="cy123456"
                               type="RSA" />
              </SSLHostConfig>
          </Connector>
      ```

      效果

      ![image-20201108160406732](https://gitee.com/xiyuximing/image/raw/master/image-20201108160406732.png)

## Tomcat性能优化

### 性能指标

- 响应时间
- 吞吐量：系统在给定时间内能够⽀持的事务数量，单位为TPS（事务数/秒）

### 优化方式

- 从JVM虚拟机优化
- Tomcat本身的优化

### JVM虚拟机优化

**主要通过内存分配及垃圾回收策略优化：**

- 内存分配优化影响的是服务器的运行效率和吞吐量
- 垃圾回收机制会不同程度的导致运行程序终端

**JVM主要参数：**

| 参数                 | 参数作用                                          | 优化建议                |
| -------------------- | ------------------------------------------------- | ----------------------- |
| -server              | 启动Server，以服务端模式运行                      | 服务端模式建议开启      |
| -Xms                 | 最⼩堆内存                                        | 建议与-Xmx设置相同      |
| -Xmx                 | 最⼤堆内存                                        | 建议设置为可⽤内存的80% |
| -XX:MetaspaceSize    | 元空间初始值                                      |                         |
| -XX:MaxMetaspaceSize | 元空间最⼤内存                                    | 默认⽆限                |
| -XX:NewRatio         | 年轻代和⽼年代⼤⼩⽐值，取值为整数，默认为2       | 不需要修改              |
| -XX:SurvivorRatio    | Eden区与Survivor区⼤⼩的⽐值，取值为整数，默认为8 | 不需要修改              |

![image-20201108171537114](https://gitee.com/xiyuximing/image/raw/master/image-20201108171537114.png)

参数调整示例：

在conf/catalina.bat加入相应参数配置

``` shell
JAVA_OPTS="-server -Xms2048m -Xmx2048m -XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=512m"
```

windows在

``` shell
rem Guess CATALINA_HOME if not defined
set "CURRENT_DIR=%cd%"
```

后加入配置：

``` shell
set JAVA_OPTS=-server -Xms2048m -Xmx2048m -XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=512m
```

![image-20201108173917979](https://gitee.com/xiyuximing/image/raw/master/image-20201108173917979.png)

效果查看

``` shell
jmap -heap 16972
```

![image-20201108173802874](https://gitee.com/xiyuximing/image/raw/master/image-20201108173802874.png)

### 垃圾回收（GC）策略

垃圾回收性能指标:

- 吞吐量：⼯作时间（排除GC时间）占总时间的百分⽐， ⼯作时间并不仅是程序运⾏的时间，还包含内存分配时间。
- 暂停时间：由垃圾回收导致的应⽤程序停⽌响应次数/时间。

**垃圾处理器:**

- 串⾏收集器（Serial Collector）

  单线程执⾏所有的垃圾回收⼯作， 适⽤于单核CPU服务器
  ⼯作进程-----|（单线程）垃圾回收线程进⾏垃圾收集|---⼯作进程继续

- 并⾏收集器（Parallel Collector）

  ⼯作进程-----|（多线程）垃圾回收线程进⾏垃圾收集|---⼯作进程继续
  ⼜称为吞吐量收集器（关注吞吐量）， 以并⾏的⽅式执⾏年轻代的垃圾回收， 该⽅式可以显著降低垃圾回收的开销(指多条垃圾收集线程并⾏⼯作，但此时⽤户线程仍然处于等待状态)。适⽤于多处理器或多线程硬件上运⾏的数据量较⼤的应⽤

- 并发收集器（Concurrent Collector）

  以并发的⽅式执⾏⼤部分垃圾回收⼯作，以缩短垃圾回收的暂停时间。适⽤于那些响应时间优先于吞吐量的应⽤， 因为该收集器虽然最⼩化了暂停时间(指⽤户线程与垃圾收集线程同时执⾏,但不⼀定是并⾏的，可能会交替进⾏)， 但是会降低应⽤程序的性能

- CMS收集器（Concurrent Mark Sweep Collector）

  并发标记清除收集器， 适⽤于那些更愿意缩短垃圾回收暂停时间并且负担的起与垃圾回收共享处理器资源的应⽤

- G1收集器（Garbage-First Garbage Collector）

  适⽤于⼤容量内存的多核服务器， 可以在满⾜垃圾回收暂停时间⽬标的同时， 以最⼤可能性实现⾼吞吐量(JDK1.7之后)

**垃圾处理器参数：**

| 参数                           | 描述                                                         |
| ------------------------------ | ------------------------------------------------------------ |
| -XX:+UseSerialGC               | 启⽤串⾏收集器                                               |
| -XX:+UseParallelGC             | 启⽤并⾏垃圾收集器，配置了该选项，那么 -XX:+UseParallelOldGC默认启⽤ |
| -XX:+UseParNewGC               | 年轻代采⽤并⾏收集器，如果设置了 -XX:+UseConcMarkSweepGC选项，⾃动启⽤ |
| -XX:ParallelGCThreads          | 年轻代及⽼年代垃圾回收使⽤的线程数。默认值依赖于JVM使⽤的CPU个数 |
| -XX:+UseConcMarkSweepGC（CMS） | 对于⽼年代，启⽤CMS垃圾收集器。 当并⾏收集器⽆法满⾜应⽤的延迟需求是，推荐使⽤CMS或G1收集器。启⽤该选项后， -XX:+UseParNewGC⾃动启⽤。 |
| -XX:+UseG1GC                   | 启⽤G1收集器。 G1是服务器类型的收集器， ⽤于多核、⼤内存的机器。它在保持⾼吞吐量的情况下，⾼概率满⾜GC暂停时间的⽬标。 |

配置方法也是在conf/catalina.bat加入相应参数配置

``` shell
JAVA_OPTS="-XX:+UseConcMarkSweepGC"
```

### Tomcat自身优化

- 使用tomcat线程池

  ![image-20201108174542738](https://gitee.com/xiyuximing/image/raw/master/image-20201108174542738.png)

- 调整tomcat的连接器配置

  调整tomcat/conf/server.xml 中关于链接器的配置可以提升应⽤服务器的性能。

  | 参数           | 说明                                                         |
  | -------------- | ------------------------------------------------------------ |
  | maxConnections | 最⼤连接数，当到达该值后，服务器接收但不会处理更多的请求， 额外的请求将会阻塞直到连接数低于maxConnections 。可通过ulimit -a 查看服务器限制。对于CPU要求更⾼(计算密集型)时，建议不要配置过⼤ ; 对于CPU要求不是特别⾼时，建议配置在2000左右(受服务器性能影响)。 当然这个需要服务器硬件的⽀持 |
  | maxThreads     | 最⼤线程数,需要根据服务器的硬件情况，进⾏⼀个合理的设置      |
  | acceptCount    | 最⼤排队等待数,当服务器接收的请求数量到达maxConnections ，此时Tomcat会将后⾯的请求，存放在任务队列中进⾏排序， acceptCount指的就是任务队列中排队等待的请求数 。 ⼀台Tomcat的最⼤的请求处理数量，是maxConnections+acceptCount |

  

- 调整tomcat的连接器

  ![image-20201108174605241](C:\Users\Yang\AppData\Roaming\Typora\typora-user-images\image-20201108174605241.png)

- 调整 IO 模式

  Tomcat8之前的版本默认使⽤BIO（阻塞式IO），对于每⼀个请求都要创建⼀个线程来处理，不适合⾼并发；Tomcat8以后的版本默认使⽤NIO模式（⾮阻塞式IO）

  ![image-20201108174616847](https://gitee.com/xiyuximing/image/raw/master/image-20201108174616847.png)

  当Tomcat并发性能有较⾼要求或者出现瓶颈时，我们可以尝试使⽤APR模式，APR（Apache PortableRuntime）是从操作系统级别解决异步IO问题，使⽤时需要在操作系统上安装APR和Native（因为APR原理是使⽤使⽤JNI技术调⽤操作系统底层的IO接⼝）

- 动静分离

  可以使⽤Nginx+Tomcat相结合的部署⽅案，Nginx负责静态资源访问，Tomcat负责Jsp等动态资源访问处理（因为Tomcat不擅⻓处理静态资源）。