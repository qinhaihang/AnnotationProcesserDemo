package com.example.compiler;

import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

/**
 * @author qinhaihang_vendor
 * @version $Rev$
 * @time 2019/3/6 19:54
 * @des 代理信息类
 * @packgename com.example.compiler
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes
 */
public class ProxyInfo {

    private String packageName;   //包名 --com.tpnet.processordemo

    private String proxyClassName;  // 生成的类的名称 --IndexMativity$$ViewInject

    TypeElement mTypeElement;

    Elements mElementUtils;

    //存放view的id,元素
    public Map<Integer, VariableElement> injectVariables = new HashMap<>();

    public static final String PROXY = "ViewInject";   //这个名称，需要对应api的Module里面的ViewInject接口的名称

    /**
     *
     * @param typeElement element 所在的类
     * @param elementUtils
     */
    public ProxyInfo(TypeElement typeElement, Elements elementUtils) {
        mTypeElement = typeElement;
        mElementUtils = elementUtils;

        //获取包名
        PackageElement packageElement = elementUtils.getPackageOf(typeElement);
        this.packageName = packageElement.getQualifiedName().toString();

        this.proxyClassName = typeElement.getSimpleName() + "$$" + PROXY;
    }

    /**
     * 生成java文件代码
     * @return
     */
    public String generateJavaCode() {
        StringBuilder builder = new StringBuilder();
        builder.append("// Generated code. Do not modify!\n");
        builder.append("package ").append(packageName).append(";\n\n");

        //注意，这个ImPort的包路径，是api的包路径
        builder.append("import com.example.qhh_api.*;\n");
        builder.append('\n');

        builder.append("public class ").append(proxyClassName).append(" implements " + ProxyInfo.PROXY + "<" + mTypeElement.getQualifiedName() + ">");
        builder.append(" {\n");

        generateMethods(builder);
        builder.append('\n');

        builder.append("}\n");
        return builder.toString();

    }


    /**
     * 生成方法
     * @param builder
     */
    private void generateMethods(StringBuilder builder) {

        builder.append("@Override\n ");
        builder.append("public void inject(" + mTypeElement.getQualifiedName() + " host, Object source ) {\n");

        for (int id : injectVariables.keySet()) {
            VariableElement element = injectVariables.get(id);
            String name = element.getSimpleName().toString();
            String type = element.asType().toString();
            builder.append(" if(source instanceof android.app.Activity){\n");
            builder.append("host." + name).append(" = ");
            builder.append("(" + type + ")(((android.app.Activity)source).findViewById( " + id + "));\n");
            builder.append("\n}else{\n");
            builder.append("host." + name).append(" = ");
            builder.append("(" + type + ")(((android.view.View)source).findViewById( " + id + "));\n");
            builder.append("\n}");
        }
        builder.append("  }\n");
    }

    public String getProxyClassFullName() {
        return packageName + "." + proxyClassName;
    }

    public TypeElement getTypeElement() {
        return mTypeElement;
    }

}
