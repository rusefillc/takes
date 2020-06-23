/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2019 Yegor Bugayenko
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
package org.takes.rq;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import lombok.EqualsAndHashCode;
import org.cactoos.io.WriterTo;
import org.cactoos.text.TextOf;
import org.takes.Request;

/**
 * Request decorator, to print it all.
 *
 * <p>The class is immutable and thread-safe.
 *
 * @todo #984:30m This class should implement Text in accordance with RsPrint.
 *  Make it implement text, then clean up code from
 *  `new TextOf(new RqPrint(...).print())` idiom and remove method `print`.
 *
 * @since 0.1
 */
@EqualsAndHashCode(callSuper = true)
public final class RqPrint extends RqWrap {

    /**
     * Ctor.
     * @param req Original request
     */
    public RqPrint(final Request req) {
        super(req);
    }

    /**
     * Print it all.
     * @return Text form of request
     * @throws IOException If fails
     */
    public String print() throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        this.print(baos);
        return new TextOf(baos.toByteArray()).asString();
    }

    /**
     * Print it all.
     * @param output Output stream
     * @throws IOException If fails
     */
    public void print(final OutputStream output) throws IOException {
        this.printHead(output);
        this.printBody(output);
    }

    /**
     * Print it all.
     * @return Text form of request
     * @throws IOException If fails
     */
    public String printHead() throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        this.printHead(baos);
        return new TextOf(baos.toByteArray()).asString();
    }

    /**
     * Print it all.
     * @param output Output stream
     * @throws IOException If fails
     */
    public void printHead(final OutputStream output) throws IOException {
        final String eol = "\r\n";
        try (Writer writer = new WriterTo(output)) {
            for (final String line : this.head()) {
                writer.append(line);
                writer.append(eol);
            }
            writer.append(eol);
            writer.flush();
        }
    }

    /**
     * Print body.
     * @return Text form of request
     * @throws IOException If fails
     */
    public String printBody() throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        this.printBody(baos);
        return new TextOf(baos.toByteArray()).asString();
    }

    /**
     * Print body.
     * @param output Output stream to print to
     * @throws IOException If fails
     */
    public void printBody(final OutputStream output) throws IOException {
        final InputStream input = new RqChunk(new RqLengthAware(this)).body();
        //@checkstyle MagicNumberCheck (1 line)
        final byte[] buf = new byte[4096];
        while (true) {
            final int bytes = input.read(buf);
            if (bytes < 0) {
                break;
            }
            output.write(buf, 0, bytes);
        }
    }

}
