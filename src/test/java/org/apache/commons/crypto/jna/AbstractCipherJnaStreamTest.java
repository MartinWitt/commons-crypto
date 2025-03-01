 /*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.crypto.jna;

import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.TimeUnit;

import org.apache.commons.crypto.cipher.AbstractCipherTest;
import org.apache.commons.crypto.stream.AbstractCipherStreamTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

public abstract class AbstractCipherJnaStreamTest extends AbstractCipherStreamTest {

    private static final String CIPHER_OPENSSL_JNA = OpenSslJna.getCipherClass().getName();

    @BeforeEach
    public void init() {
        assumeTrue(OpenSslJna.isEnabled());
    }

    /** Test byte buffer read with different buffer size. */
    @Override
    @Timeout(value = 120000, unit = TimeUnit.MILLISECONDS)
    public void testByteBufferRead() throws Exception {
        doByteBufferRead(CIPHER_OPENSSL_JNA, false);

        doByteBufferRead(CIPHER_OPENSSL_JNA, true);
    }

    /** Test byte buffer write. */
    @Override
    @Timeout(value = 120000, unit = TimeUnit.MILLISECONDS)
    public void testByteBufferWrite() throws Exception {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        doByteBufferWrite(CIPHER_OPENSSL_JNA, baos, false);

        doByteBufferWrite(CIPHER_OPENSSL_JNA, baos, true);
    }

    @Override
    @Test
    public void testReadWrite() throws Exception {
        doReadWriteTest(0, CIPHER_OPENSSL_JNA, CIPHER_OPENSSL_JNA, iv);
        doReadWriteTest(count, CIPHER_OPENSSL_JNA, CIPHER_OPENSSL_JNA, iv);
        doReadWriteTest(count, AbstractCipherTest.JCE_CIPHER_CLASSNAME, CIPHER_OPENSSL_JNA, iv);
        doReadWriteTest(count, CIPHER_OPENSSL_JNA, AbstractCipherTest.JCE_CIPHER_CLASSNAME, iv);
        // Overflow test, IV: xx xx xx xx xx xx xx xx ff ff ff ff ff ff ff ff
        for (int i = 0; i < 8; i++) {
            iv[8 + i] = (byte) 0xff;
        }
        doReadWriteTest(count, CIPHER_OPENSSL_JNA, CIPHER_OPENSSL_JNA, iv);
        doReadWriteTest(count, AbstractCipherTest.JCE_CIPHER_CLASSNAME, CIPHER_OPENSSL_JNA, iv);
        doReadWriteTest(count, CIPHER_OPENSSL_JNA, AbstractCipherTest.JCE_CIPHER_CLASSNAME, iv);
    }

    /** Test skip. */
    @Override
    @Test
    @Timeout(value = 120000, unit = TimeUnit.MILLISECONDS)
    public void testSkip() throws Exception {
        doSkipTest(CIPHER_OPENSSL_JNA, false);

        doSkipTest(CIPHER_OPENSSL_JNA, true);
    }
}
