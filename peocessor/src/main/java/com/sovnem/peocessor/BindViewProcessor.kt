package com.sovnem.peocessor

import com.sovnem.annotation.BindView
import com.sovnem.annotation.Constants
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Filer
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements

class BindViewProcessor : AbstractProcessor() {
    lateinit var mElementsUtil: Elements
    lateinit var mFiler: Filer

    override fun init(p0: ProcessingEnvironment) {
        super.init(p0)
        mFiler = p0.filer
        mElementsUtil = p0.elementUtils
    }

    override fun process(
        p0: MutableSet<out TypeElement>?,
        environment: RoundEnvironment
    ): Boolean {
        val classSet = HashSet<Element>()
        val classMap = HashMap<TypeElement, ArrayList<Element>>()
        environment.getElementsAnnotatedWith(BindView::class.java).forEach {
            classSet.add(it.enclosingElement)
            if (classMap.containsKey(it.enclosingElement as TypeElement)) {
                classMap[it.enclosingElement as TypeElement]?.add(it)
            } else {
                val list = ArrayList<Element>()
                list.add(it)
                classMap[it.enclosingElement as TypeElement] = list
            }
        }

        classMap.forEach { (typeEle, arrayList) ->
            println("哪几个类：${typeEle.qualifiedName}")
            val cons = MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC)
                .addParameter(Class.forName("android.app.Activity"), "activity", Modifier.FINAL)
                .apply {
                    println("几个啊：" + arrayList.size)
                    arrayList.forEach {
                        val fieldName = it.simpleName
                        val resId = it.getAnnotation(BindView::class.java).id
                        addStatement(
                            "((${typeEle.qualifiedName})activity).\$N=activity.findViewById($resId)",
                            fieldName
                        )
                    }
                }
                .build()
            val outClass = TypeSpec.classBuilder("" + typeEle.simpleName + Constants.CLASS_TAIL)
                .addMethod(cons).addModifiers(Modifier.PUBLIC).build()
            val javaFile = JavaFile.builder(Constants.PACKAGE, outClass).build()
            javaFile.writeTo(mFiler)
        }
        return false
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(BindView::class.java.name)
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }
}