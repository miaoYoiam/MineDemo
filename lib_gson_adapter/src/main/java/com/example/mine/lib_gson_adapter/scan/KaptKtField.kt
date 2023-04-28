package com.example.mine.lib_gson_adapter.scan

import com.example.mine.lib_gson_adapter.base.*
import javax.lang.model.element.Element

data class KaptKtField(
    override val isFinal: Boolean,
    override val fieldName: String,
    override val keys: List<String>,
    override val type: IType,
    override val initializer: FieldInitializer,
    override val declarationScope: DeclarationScope,
    override val transient: Boolean,
    override val target: Element?
) : IFiled(), IElementOwner {

    override fun copy(declarationScope: DeclarationScope): IFiled {
        return KaptKtField(
            isFinal = this.isFinal,
            fieldName = this.fieldName,
            keys = this.keys,
            type = this.type,
            initializer = this.initializer,
            declarationScope = declarationScope,
            transient = this.transient,
            target = this.target
        )
    }
}