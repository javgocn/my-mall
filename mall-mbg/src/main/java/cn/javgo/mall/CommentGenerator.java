package cn.javgo.mall;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.internal.DefaultCommentGenerator;
import org.mybatis.generator.internal.util.StringUtility;
import java.util.Properties;

/**
 * 自定义注释生成器
 */
public class CommentGenerator extends DefaultCommentGenerator {
    private boolean addRemarkComments = false;
    private static final String EXAMPLE_SUFFIX="Example";
    private static final String MAPPER_SUFFIX="Mapper";
    private static final String API_MODEL_PROPERTY_FULL_CLASS_NAME="io.swagger.annotations.ApiModelProperty";

    @Override
    public void addConfigurationProperties(Properties props) {
        super.addConfigurationProperties(props);
        // 如果在配置文件中设置了 addRemarkComments 为 true，则对该属性进行赋值
        this.addRemarkComments = StringUtility.isTrue(props.getProperty("addRemarkComments"));
    }

    /**
     * 为生成的实体类字段添加 Swagger 注释
     */
    @Override
    public void addFieldComment(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        String remarks = introspectedColumn.getRemarks();
        if (addRemarkComments && StringUtility.stringHasValue(remarks)){
            // 将字段备注信息中的双引号替换为单引号，否则会导致注释中的双引号不完整
            if (remarks.contains("\"")){
                remarks = remarks.replace("\"", "'");
            }
            // addFileJavaDoc(field, remarks);
            field.addJavaDocLine("@ApiModelProperty(value = \"" + remarks + "\")");
        }
    }

    /**
     * 保留扩展使用的方法，用来给实体类添加常规的 Java 注释
     */
    private void addFileJavaDoc(Field field,String remarks){
        field.addJavaDocLine("/**");
        // line.separator 表示换行符
        String[] remarkLines = remarks.split(System.getProperty("line.separator"));
        for(String remarkLine : remarkLines){
            field.addJavaDocLine(" * " + remarkLine);
        }
        addJavadocTag(field, false);
        field.addJavaDocLine(" */");
    }

    @Override
    public void addJavaFileComment(CompilationUnit compilationUnit) {
        super.addJavaFileComment(compilationUnit);
        // 只为实体类添加 import 语句以导入 ApiModelProperty 注解的全类名
        if(!compilationUnit.getType().getFullyQualifiedName().contains(MAPPER_SUFFIX) &&
            !compilationUnit.getType().getFullyQualifiedName().contains(EXAMPLE_SUFFIX)){
            compilationUnit.addImportedType(new FullyQualifiedJavaType(API_MODEL_PROPERTY_FULL_CLASS_NAME));
        }
    }
}
