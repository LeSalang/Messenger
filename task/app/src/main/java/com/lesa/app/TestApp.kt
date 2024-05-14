package com.lesa.app

class TestApp() : App() {
    override fun provideBaseUrl(): String {
        return TEST_BASE_URL
    }

    companion object {
        const val TEST_BASE_URL = "http://localhost:8080"
    }
}