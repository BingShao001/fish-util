由于MP各业务线较多，为了更好实现代码风格统一，代码规范，以及代码复用率高，
  形成统一化，组件化，服务化。
  封装出一套独立的组件平台common-yb-fish-utils，供大家开发使用。
  希望能帮助大家提高开发效率，并能实现高稳定性，高可用性。
  如它的名字一样，穿梭在不同的项目中供大家使用，无侵入，无感知。
  同时也欢迎对技术热爱的同学加入，一起提升，一起维护起来。
  
<!--外部环境依赖配置--> 
<context:component-scan base-package="com.yb.mp.fish.*" />
<asgard:service name="OspCommonAuthSDK" />
 
<bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
   <property name="dataSource" ref="OspCommonAuthSDK"></property>
</bean>