package cn.com.guardiantech.aofgo.backend.service.email

class MailTemplate constructor(var templateContent: String) {
    var recipientAddress: String = ""

    fun setStringValue(templateKey: String, value: Any) {
        templateContent = this.templateContent.replace("{{$templateKey}}", value.toString())
    }

    fun setListValue(templateKey: String, value: List<Any>) {
        val sb: StringBuilder = StringBuilder()
        value.forEach { v ->
            sb.append(v.toString()).append("<br>")
        }
        this.setStringValue(templateKey, sb.toString())
    }

    fun encode() = templateContent
}