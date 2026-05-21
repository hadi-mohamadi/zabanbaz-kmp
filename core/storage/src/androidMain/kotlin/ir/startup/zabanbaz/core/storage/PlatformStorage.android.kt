package ir.startup.zabanbaz.core.storage

actual fun createSecureKeyValueStore(): SecureKeyValueStore =
    SecureKeyValueStore(storageContext)
