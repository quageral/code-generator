# 技术选型

前端

- React 开发框架 + 组件库 + 代码编辑器
- 前端工程化：ESLint + Prettier + TypeScript

后端

- Java Spring Boot + MySQL + MyBatis Plus（万用后端模板）
- Java 命令行应用开发
- **FreeMarker 模板引擎**
- **Vert.x 响应式编程**(并发能力超过spring)
- **Caffeine + Redis 多级缓存**
- 分布式任务调度系统
- 多种设计模式
- 多种系统设计的巧思
- 对象存储


# 项目三步走

第一阶段：代码生成器（给使用者的生成器）

业务流程
1）准备用于制作代码生成器的原始代码（比如 Java ACM 模板项目），用于后续生成
2）开发者基于原始代码，设置参数、编写动态模板
3）制作可交互的命令行工具，支持用户输入参数，得到代码生成器 jar 包
4）使用者得到代码生成器 jar 包，执行程序并输入参数，从而生成完整代码（eg. 若依）



![image-20240119105355892](https://s2.loli.net/2024/01/19/jqtRbzxoaMVKiED.png)



第二阶段：(给开发者的生成器)

![image-20240119105422066](https://s2.loli.net/2024/01/19/KfoeFNJ5W1jULDM.png)



第三阶段：线上平台（在线分享）

![image-20240119105438493](https://s2.loli.net/2024/01/19/Q3RmK6fDlS4dj8v.png)



## 第一阶段

使用Hutool：静态文件生成、进行文件复制等操作。

使用FreeMarker模板引擎：用"模板+数据模型"进行动态文件的生成。

使用Picocli：利用注解，开发基于命令行的代码生成器。支持可交互式输入。

使用命令模式：对generate、config、list三个参数的进行封装。<mark>优点：</mark>解耦请求发送者和接受者，让系统更加灵活、可扩展。由于每个操作都是一个独立的命令类，所以我们需要新增命令操作时，不需要改动现有代码。

使用定制化打包方式`maven-assembly-plugin`和脚本文件：可以在命令行运行jar包的Main类，生成所有代码文件。
