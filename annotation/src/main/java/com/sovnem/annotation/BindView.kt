package com.sovnem.annotation

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
annotation class BindView(val id: Int)