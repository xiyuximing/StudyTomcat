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



