/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Yegor Bugayenko
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.takes.facets.auth;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Unit tests for {@link org.takes.facets.auth.PsBasic.Default}.
 * @author Georgy Vlasov (wlasowegor@gmail.com)
 * @version $Id$
 * @since 0.22
 */
public final class PsBasicDefaultTest {
    /**
     * URN of one of users.
     */
    private static final String URN = "urn:foo:robert";
    /**
     * Login of one of users.
     */
    private static final String BOB_LOGIN = "bob";

    /**
     * Password of one of users.
     */
    private static final String BOB_PASSWORD = "qwerty";

    /**
     * Existing users.
     */
    private final transient String[] users = new String[] {
        "bob qwerty urn:foo:robert",
        "alice 123 urn:foo:alice",
    };

    /**
     * PsBasic.Default can accept a correct login/password pair.
     * @throws Exception If fails
     */
    @Test
    public void acceptsCorrectLoginPasswordPair() throws Exception {
        MatcherAssert.assertThat(
            new PsBasic.Default(this.users)
                .enter(
                    PsBasicDefaultTest.BOB_LOGIN,
                    PsBasicDefaultTest.BOB_PASSWORD
            )
                .get()
                .urn(),
            Matchers.equalTo(URN)
        );
    }

    /**
     * PsBasic.Default can reject incorrect password.
     * @throws Exception If fails
     */
    @Test
    public void rejectsIncorrectPassword() throws Exception {
        MatcherAssert.assertThat(
            new PsBasic.Default(this.users)
                .enter(PsBasicDefaultTest.BOB_LOGIN, "wrongpassword")
                .has(),
            Matchers.is(false)
        );
    }

    /**
     * PsBasic.Default can reject a non-existing login.
     * @throws Exception If fails
     */
    @Test
    public void rejectsIncorrectLogin() throws Exception {
        MatcherAssert.assertThat(
            new PsBasic.Default(this.users)
                .enter("mike", "anything")
                .has(),
            Matchers.is(false)
        );
    }
}
