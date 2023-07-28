package cn.javgo.mall.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 状态约束校验器
 * 说明：
 * 1.ConstraintValidator<A extends Annotation ,T> 接口的泛型参数 A 表示注解，T 表示注解修饰的目标类型，即被校验的类型。
 * 2.该接口是 Spring Validation 框架提供的，用于自定义校验注解的校验逻辑。接口中定义了两个方法：
 *   - initialize(A constraintAnnotation)：用于初始化校验器，在校验之前被调用，可以用于获取注解中的属性值。
 *   - isValid(T value, ConstraintValidatorContext context)：用于校验逻辑的实现，返回 true 表示校验通过，返回 false 表示校验失败。
 */
public class FlagValidatorClass implements ConstraintValidator<FlagValidator,Integer> {

    // 用于指定多个可选值，用于校验的时候，如果值不在指定范围内，就会报错
    private String[] values;

    /**
     * 用于初始化校验器，在校验之前被调用，可以用于获取注解中的属性值。
     */
    @Override
    public void initialize(FlagValidator flagValidator) {
        this.values = flagValidator.value();
    }

    /**
     * 用于校验逻辑的实现
     * @param value 被校验的值，也就是被 @FlagValidator 修饰的字段的值
     * @param context 用于获取默认提示信息
     *
     * @return true 校验通过，false 校验失败
     */
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        boolean isValid = false;

        // 当状态为空时使用默认值
        if(value == null){
            return true;
        }

        // 遍历可选值，如果值在指定范围内，就返回 true，否则返回 false
        for (int i = 0; i < values.length; i++) {
            if(values[i].equals(String.valueOf(value))){
                isValid = true;
                break;
            }
        }
        return isValid;
    }
}
