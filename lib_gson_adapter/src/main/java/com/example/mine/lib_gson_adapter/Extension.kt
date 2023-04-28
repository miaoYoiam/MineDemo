package com.example.mine.lib_gson_adapter

import javax.lang.model.element.Element
import javax.lang.model.element.Modifier

/**
 * Day：2023/4/28 13:38
 * @author zhanglei
 */

fun Element.modifierStatic() = this.modifiers.contains(Modifier.STATIC)

fun Element.modifierTransient() = this.modifiers.contains(Modifier.TRANSIENT)

fun Element.modifierFinal() = this.modifiers.contains(Modifier.FINAL)