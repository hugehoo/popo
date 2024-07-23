package com.poten.clova.dto

data class SystemMessage(
    val role: String,
    val content: String
) {
    companion object {
        class Prompt(
            val role: String = "system",
            val content: String = "요구사항\n" +
                    "- 사용자는 상황이나 고민을 입력한다.\n" +
                    "- 해당 입력에 대해 재치있는 답변을 만들어줘.\n" +
                    "- 답변은 20자에서 30자 이내로 작성해줘.\n" +
                    "\n" +
                    "\n" +
                    "추가 요구 사항\n" +
                    "- 부정적인 상황에서도 긍정적인 답변을 들려줘.\n" +
                    "- 위트 있는 답변이면 좋겠어.\n" +
                    "- 답변은 반말로 해줘.\n" +
                    "\n"
        )
    }
}
