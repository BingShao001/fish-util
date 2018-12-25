fish-utils说明
----------

**为唯品会商家管理平台MP搭建了一个基础框架的扩展组件工具包，取名fish-utils，希望能帮助大家提高开发效率，并能实现高稳定性，高可用性。如它的名字一样，穿梭在不同的项目中供大家使用，无侵入，无感知。**

特殊声明
----
**由于涉及到公司的未开源敏感代码，部分内容其他方式代替，并去掉敏感信息。部分工具需要进行改造，见谅。**

组件内容
----

 1. 校验逻辑分离断言。
 2. 主键生成器（单点）。
 3. 主键生成器（集群基于雪花算法）。
 4. 计算模板（非精度）。
 5. 计算模板（精度BigDecimal）。
 6. 异步任务线程池。
 7. 分布式锁。
 8. 分布式事务延时最终一直。
 
依赖pom:

    <repositories>
        <repository>
            <id>maven-repo-master</id>
            <url>https://raw.github.com/BingShao001/fish-util-repo/master/</url>
            <snapshots>
                <enabled>true</enabled>
                <updatePolicy>always</updatePolicy>
            </snapshots>
        </repository>
    </repositories>
    
    <dependency>
      <groupId>com.yb.mp</groupId>
      <artifactId>common-yb-fish-utils</artifactId>
      <version>1.2-SNAPSHOT</version>
    </dependency>


