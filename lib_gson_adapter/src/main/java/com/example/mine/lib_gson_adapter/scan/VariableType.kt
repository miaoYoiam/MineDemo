package com.example.mine.lib_gson_adapter.scan

import com.example.mine.lib_gson_adapter.base.ElementType
import com.example.mine.lib_gson_adapter.base.JsonTokenDelegate
import com.example.mine.lib_gson_adapter.base.Variance

/**
 * Day：2023/4/27 17:18
 * @author zhanglei
 */
class VariableType(
    override val rawType: String,
    override val nullable: Boolean,
    override val variance: Variance,
    override val jsonTokenName: JsonTokenDelegate,
    override val generics: List<ElementType>
): ElementType() {
    override fun copy(nullable: Boolean, variance: Variance): ElementType {
        return VariableType(
            rawType = this.rawType,
            nullable = nullable,
            variance = variance,
            jsonTokenName = this.jsonTokenName,
            generics = this.generics
        )
    }
}