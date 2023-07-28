package cn.javgo.mall.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 自定义注解：用于验证状态是否在指定范围内（扩展 Hibernate Validator 注解的不足）
 * 说明：
 * 1.Hibernate Validator 提供的注解只能校验基本类型的数据，如果需要校验自定义类型的数据，就需要自定义注解。
 * 2.优点是不需要重复的编写校验逻辑，只需要在需要校验的字段上添加注解即可。
 * 3.缺点是需要编写注解的校验逻辑，比较麻烦。还需要在控制层方法参数中注入 BindingResult 对象，用于接收校验结果。同时
 *   Hibernate Validator 只支持一些简单的校验逻辑，如果涉及到要查询数据库的校验逻辑，就无法实现了。（使用全局异常处理器弥补）
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.PARAMETER}) // 作用于字段和方法参数
@Constraint(validatedBy = FlagValidatorClass.class) // 指定校验器，即校验逻辑的具体实现类
public @interface FlagValidator {

    // 用于指定多个可选值，用于校验的时候，如果值不在指定范围内，就会报错
    String[] value() default {};

    // 校验不通过时的提示信息
    String message() default "flag is not found";

    /*
        下面两个属性必须添加，否则会报错
        groups：用于指定校验属于哪个分组，可以指定多个分组
        payload：用于指定校验的级别，可以指定多个级别
     */
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
