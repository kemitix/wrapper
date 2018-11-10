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

    /**
     * Wrap the subject.
     *
     * @param subject the subject to wrap
     * @param <T>     the type of the subject
     *
     * @return a Wrapper containing the subject
     */
    public static <T> Wrapper<T> wrap(@NonNull final T subject) {
        return new SubjectWrapper<>(subject);
    }

    /**
     * Wrap a wrapper.
     *
     * @param wrapper the wrapper to wrap
     * @param <T>     the type of the subject of the wrapper
     *
     * @return a Wrapper containing the wrapper
     */
    public static <T> Wrapper<T> wrap(@NonNull final Wrapper<T> wrapper) {
        return new NestedWrapper<>(wrapper);
    }

    /**
     * The subject within the innermost wrapper.
     *
     * @return the subject
     */
    public abstract T getWrapperSubject();

    /**
     * The wrapper immediately within the current wrapper.
     *
     * @return an Optional containing the nested wrapper, or empty if this wrapper has no inner wrapper.
     */
    public abstract Optional<Wrapper<T>> getInnerWrapper();
}
