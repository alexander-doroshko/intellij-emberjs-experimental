package com.emberjs.xml

import com.dmarcotte.handlebars.file.HbFileViewProvider
import com.emberjs.gts.GtsFileViewProvider
import com.emberjs.hbs.TagReferencesProvider
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.impl.source.xml.TagNameReference
import com.intellij.psi.xml.XmlTag
import com.intellij.psi.xml.XmlToken
import com.intellij.psi.xml.XmlTokenType.XML_NAME
import com.intellij.xml.DefaultXmlExtension


class EmberTagNameReference(nameElement: ASTNode?, startTagFlag: Boolean) : TagNameReference(nameElement, startTagFlag) {
    override fun resolve(): PsiElement? {
        val element = TagReferencesProvider.forTag(nameElement.psi.parent as XmlTag, nameElement.psi.text)
        if (element != null) {
            return element
        }
        if (nameElement.text.startsWith(":") || nameElement.text.firstOrNull()?.isUpperCase() == true) {
            return null
        }
        return super.resolve()
    }
}

class HbXmlExtension: DefaultXmlExtension() {
    override fun isAvailable(file: PsiFile?): Boolean {
        return file?.viewProvider is GtsFileViewProvider || file?.viewProvider is HbFileViewProvider
    }
    override fun createTagNameReference(nameElement: ASTNode?, startTagFlag: Boolean): TagNameReference? {
        if (nameElement?.psi is XmlToken && nameElement.elementType == XML_NAME) {
            return EmberTagNameReference(nameElement, startTagFlag)
        }
        return super.createTagNameReference(nameElement, startTagFlag)
    }
}