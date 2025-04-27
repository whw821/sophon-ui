package com.processor.annotation

@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS)
annotation class Slot(val title: String)