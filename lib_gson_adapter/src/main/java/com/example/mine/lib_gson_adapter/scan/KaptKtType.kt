package com.example.mine.lib_gson_adapter.scan

import com.example.mine.lib_gson_adapter.base.IElementOwner
import com.example.mine.lib_gson_adapter.base.IType
import com.example.mine.lib_gson_adapter.base.JsonTokenDelegate
import com.example.mine.lib_gson_adapter.base.Variance
import javax.lang.model.element.Element

data class KaptKtType(
    override val rawType: String,
    override val nullable: Boolean,
    override val variance: Variance,
    override val jsonTokenName: JsonTokenDelegate,
    override val generics: List<IType>,
    override val target: Element?
) : IType(), IElementOwner {
    override fun copy(nullable: Boolean, variance: Variance): IType {
        return KaptKtType(
            rawType = this.rawType,
            nullable = nullable,
            variance = variance,
            jsonTokenName = this.jsonTokenName,
            generics = this.generics,
            target = this.target
        )
    }
}
