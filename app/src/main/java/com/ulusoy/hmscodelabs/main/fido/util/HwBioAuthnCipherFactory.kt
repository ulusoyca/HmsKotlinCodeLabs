/*
 * Copyright 2020 Cagatay Ulusoy (Ulus Oy Apps). All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ulusoy.hmscodelabs.main.fido.util

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.io.IOException
import java.security.*
import java.security.cert.CertificateException
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.NoSuchPaddingException
import javax.crypto.SecretKey
import timber.log.Timber

/**
 * constructed function
 *
 * @param storeKey store key name
 * @param isUserAuthenticationRequired Sets whether the key is authorized to be used only
 * if the user has been authenticated.
 */
class HwBioAuthnCipherFactory(
    private val storeKey: String,
    private val isUserAuthenticationRequired: Boolean
) {
    private var keyStore: KeyStore? = null

    private var keyGenerator: KeyGenerator? = null

    private var defaultCipher: Cipher? = null

    init {
        try {
            initDefaultCipherObject()
        } catch (e: Exception) {
            defaultCipher = null
            Timber.e("Failed to init Cipher. %s", e.message)
        }
    }

    private fun initDefaultCipherObject() {
        keyStore = try {
            KeyStore.getInstance("AndroidKeyStore")
        } catch (e: KeyStoreException) {
            throw RuntimeException(
                "Failed to get an instance of KeyStore(AndroidKeyStore). ${e.message}",
                e
            )
        }
        keyGenerator = try {
            KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES,
                "AndroidKeyStore"
            )
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(
                "Failed to get an instance of KeyGenerator(AndroidKeyStore). ${e.message}",
                e
            )
        } catch (e: NoSuchProviderException) {
            throw RuntimeException(
                "Failed to get an instance of KeyGenerator(AndroidKeyStore). ${e.message}",
                e
            )
        }
        createSecretKey(storeKey, true)
        try {
            val defaultCipher = Cipher.getInstance(
                KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC +
                        "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7
            )
            initCipher(defaultCipher, storeKey)
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException("Failed to get an instance of Cipher", e)
        } catch (e: NoSuchPaddingException) {
            throw RuntimeException("Failed to get an instance of Cipher", e)
        }
    }

    private fun initCipher(
        cipher: Cipher,
        storeKeyName: String
    ) {
        keyStore?.let {
            try {
                keyStore?.load(null)
                val secretKey = keyStore?.getKey(storeKeyName, null) as SecretKey
                cipher.init(Cipher.ENCRYPT_MODE, secretKey)
            } catch (e: KeyStoreException) {
                throw RuntimeException("Failed to init Cipher. " + e.message, e)
            } catch (e: CertificateException) {
                throw RuntimeException("Failed to init Cipher. " + e.message, e)
            } catch (e: UnrecoverableKeyException) {
                throw RuntimeException("Failed to init Cipher. " + e.message, e)
            } catch (e: IOException) {
                throw RuntimeException("Failed to init Cipher. " + e.message, e)
            } catch (e: NoSuchAlgorithmException) {
                throw RuntimeException("Failed to init Cipher. " + e.message, e)
            } catch (e: InvalidKeyException) {
                throw RuntimeException("Failed to init Cipher. " + e.message, e)
            }
        }
    }

    private fun createSecretKey(
        storeKeyName: String,
        isInvalidatedByBiometricEnrollment: Boolean
    ) {
        keyStore?.let {
            try {
                keyStore?.load(null)
                var keyParamBuilder: KeyGenParameterSpec.Builder? = null
                keyParamBuilder = KeyGenParameterSpec.Builder(
                    storeKeyName,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC) // This key is authorized to be used only if the user has been authenticated.
                    .setUserAuthenticationRequired(false)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                keyParamBuilder.setInvalidatedByBiometricEnrollment(
                    isInvalidatedByBiometricEnrollment
                )
                keyGenerator?.init(keyParamBuilder.build())
                keyGenerator?.generateKey()
            } catch (e: NoSuchAlgorithmException) {
                throw RuntimeException("Failed to create secret key. " + e.message, e)
            } catch (e: InvalidAlgorithmParameterException) {
                throw RuntimeException("Failed to create secret key. " + e.message, e)
            } catch (e: CertificateException) {
                throw RuntimeException("Failed to create secret key. " + e.message, e)
            } catch (e: IOException) {
                throw RuntimeException("Failed to create secret key. " + e.message, e)
            }
        }
    }

    fun getCipher(): Cipher? {
        return defaultCipher
    }
}
