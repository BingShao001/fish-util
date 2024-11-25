package io.jagat.config.limit;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {
    int limit() default 10; // 每秒最大请求数
    long timeWindow() default 1000; // 时间窗口大小（毫秒）
    String preKey() default ""; // 限流的 Redis 键（默认为空，自动生成）
}
