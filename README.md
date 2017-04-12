# socketran
单socket实现多种类信息传输

1.简单的实现思路
由于基于可靠的tcp连接，所以当发生多个数据时，数据会无丢失，无差错，按顺序到达。于是我在每个数据前加了一个“头”，这个“头”包含了一些用于校验偏移的信息。最主要的是包含了数据的长度，需要使用何种Data对象对数据进行处理。使用“头”将流里面的各个数据分离开来，还原给使用者。

2.简单使用
想开箱即用的我只写了文件传输的，首先需要配配置文件
服务端的配置文件：
#0:Client 1:Server
isServer = 1
port = 9696
saveFolder = c:/folder

客户端的配置文件：
#0:Client 1:Server
isServer = 0
host = xxx.xxx.xxx.xxx
port = 9696
byteLen = 2048
saveFolder = c:/folder

其中isServer、port和host（客户端）是必填的，其余默认，saveFolder：当前文件夹，byteLen：1024

将配置文件与jar包放在同一个文件夹下，在命令行里执行一下命令启动，[filePath可以是文件也可以是文件夹]
java -jar socketran.jar [filePath1 filePath2 ……]

3.使用更多
如果需要更多功能，需要实现io.Data这个接口，类文件对各个方法都有解释，这里就不多说了
还需要继承conversation.Server和conversation.Client这两个类。这个类表示一个会话，会话过程中可能会有多个数据（Data）进行传输
Conversation类的主要方法有：
//使用者返回用于会话握手/检验的信息
public abstract String createShakeHandInfo();
//处理对方发送过来用于会话握手/检验的信息
public abstract void dealShakeHandInfo(String shakeHandInfo);
//把数据加入发送队列，等待发送
public void addData(Data data);
//把数据从发送队列里移除
public boolean removeData(Data data);

在创建会话对象双方连接成功后，只需要调用Server或者Client的void addData(Data data)即可向对方发送数据。
具体例子exp包有
最后感谢雨神的技术姿瓷