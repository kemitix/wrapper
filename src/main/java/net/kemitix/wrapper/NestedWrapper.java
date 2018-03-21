/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 Paul Campbell
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.kemitix.wrapper;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A Wrapper that contains another Wrapper.
 *
 * @param <T> the type of the subject.
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
class NestedWrapper<T> extends AtomicReference<Wrapper<T>> implements Wrapper<T> {

    /**
     * Constructor.
     *
     * @param wrapper the wrapper to wrap
     */
    NestedWrapper(Wrapper<T> wrapper) {
        super(wrapper);
    }

    @Override
    public T getSubject() {
        return get().getSubject();
    }

    @Override
    public Optional<Wrapper<T>> getInner() {
        return Optional.of(get());
    }
}
