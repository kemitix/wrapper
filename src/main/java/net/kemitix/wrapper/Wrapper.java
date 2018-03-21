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

import lombok.NonNull;

import java.nio.channels.WritePendingException;
import java.util.Optional;

/**
 * Wrapper for Generic types.
 *
 * <p>N.B. all classes that implement this interface <strong>must</strong> also extend or implement {@code T}.</p>
 *
 * <p>N.B. all classes that implement this interface <strong>must not</strong> override the default methods.</p>
 *
 * @param <T> the type of object to wrap
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
public interface Wrapper<T> {

    static <T> Wrapper<T> wrap(@NonNull final T subject) {
        return new SubjectWrapper<T>(subject);
    }

    T getCore();

    static <T> Wrapper<T> wrap(@NonNull final Wrapper<T> inner) {
        return new NestedWrapper<T>(inner);
    }

    Optional<Wrapper<T>> getInner();

    static <T> Wrapper<T> remove(
            @NonNull final Wrapper<T> remove,
            @NonNull final Wrapper<T> wrapper
    ) {
        return wrapper.getInner()
                .map(inner -> {
                    if (inner.equals(remove)) {
                        return inner.getInner()
                                .map(Wrapper::wrap)
                                .orElseGet(() -> Wrapper.wrap(inner.getCore()));
                    }
                    return Wrapper.wrap(Wrapper.remove(remove, inner));
                })
                .orElse(wrapper);
    }
}
