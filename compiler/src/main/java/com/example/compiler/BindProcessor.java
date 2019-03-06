package com.example.compiler;

import com.example.qhh_annotation.BindView;
import com.google.auto.service.AutoService;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

/**
 * @author qinhaihang_vendor
 * @version $Rev$
 * @time 2019/3/6 19:32
 * @des
 * @packgename com.example.compiler
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes
 */
@AutoService(Processor.class)
public class BindProcessor extends AbstractProcessor {

    private Messager mMessager;
    private Filer mFileUtils;
    private Elements mElementUtils;

    private Map<String,ProxyInfo> mProxyInfoMap = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mMessager = processingEnvironment.getMessager();

        mFileUtils = processingEnvironment.getFiler();

        mElementUtils = processingEnvironment.getElementUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        mMessager.printMessage(Diagnostic.Kind.NOTE,"Start BindProcesser process method!!");

        //获取注解的元素变量
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(BindView.class);

        //循环处理注解的每一个元素，并且放入一个 map 中
        for(Element element : elements){

            //校验元素是否为 VariableElement
            if(!(element instanceof VariableElement)){
                return false;
            }

            //转换变量类型
            VariableElement variableElement = (VariableElement)element;

            //修饰变量所在的类
            TypeElement typeElement =  (TypeElement)variableElement.getEnclosingElement();

            //使用类的全路径作为key
            String qulifiedName = typeElement.getQualifiedName().toString();

            //获取 map 中是都已经有相关的代理信息
            ProxyInfo proxyInfo = mProxyInfoMap.get(qulifiedName);

            if(proxyInfo == null){
                proxyInfo = new ProxyInfo(typeElement,mElementUtils);
                mProxyInfoMap.put(qulifiedName,proxyInfo);
            }

            //获取注解
            BindView annotation = variableElement.getAnnotation(BindView.class);
            //注解上的控件ID
            int id = annotation.value();
            proxyInfo.injectVariables.put(id,variableElement);

            //第二步骤： 遍历Map生成代理类
            for(String key: mProxyInfoMap.keySet()){
                ProxyInfo proxyInfo2 = mProxyInfoMap.get(key);

                try {
                    //创建文件对象
                    JavaFileObject soureFile = mFileUtils.createSourceFile(
                            proxyInfo2.getProxyClassFullName(),   //文件名,全路径
                            proxyInfo2.getTypeElement());
                    //创建写入对象
                    Writer writer = soureFile.openWriter();
                    //写入内容
                    writer.write(proxyInfo2.generateJavaCode());
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotation = new LinkedHashSet<>();
        //getCanonicalName返回的就是跟我们声明类似的形式
        annotation.add(BindView.class.getCanonicalName());
        return annotation;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
