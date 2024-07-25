package com.droiddude.apps.sample_gemini_ai_app.screens.chat

class ChatUiState(
    messages: List<ChatMessage> = emptyList()
) {
    private val _messages: MutableList<ChatMessage> = messages.toMutableList()
    val messages : List<ChatMessage> = _messages

    fun addMessage(message : ChatMessage) {
        _messages.add(message)
    }

    fun replaceLastPendingMessage() {
        val lastMessage = _messages.lastOrNull()
        lastMessage?.let {
            val newMessage = lastMessage.apply {
                isPending = false
            }
            _messages.removeLast()
            _messages.add(newMessage)
        }
    }

}