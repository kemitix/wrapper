/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 Paul Campbell
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

import lombok.Getter;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * The state for {@link Wrapper} implementations.
 *
 * @param <T> the type of object to wrap
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
public class WrapperState<T> implements Wrapper<T> {

    @Getter
    private final T wrapperCore;

    private final AtomicReference<Wrapper<T>> innerWrapper = new AtomicReference<>();

    /**
     * Constructor for wrapping a {@link T} object.
     *
     * @param object the object to wrap
     */
    public WrapperState(final T object) {
        this.wrapperCore = object;
    }

    /**
     * Constructor for wrapping an already wrapped {@link T} object.
     *
     * @param wrapper the wrapped object
     */
    public WrapperState(final Wrapper<T> wrapper) {
        this.innerWrapper.set(wrapper);
        this.wrapperCore = wrapper.getWrapperCore();
    }

    @Override
    public final WrapperState<T> getWrapperState() {
        return this;
    }

    @Override
    public final Optional<Wrapper<T>> findInnerWrapper() {
        return Optional.ofNullable(innerWrapper.get());
    }

    /**
     * This implementation, used only by {@link Wrapper}, returns a {@code null} if there is no change in the outermost
     * wrapper.
     *
     * @param wrapper the wrapper to remove
     *
     * @return the new outermost wrapper, or null
     */
    @Override
    @SuppressWarnings("unchecked")
    public final T removeWrapper(final Wrapper<T> wrapper) {
        findInnerWrapper().ifPresent(inner -> {
            final T newDelegate = inner.removeWrapper(wrapper);
            if (newDelegate instanceof Wrapper) {
                innerWrapper.set((Wrapper<T>) newDelegate);
            } else {
                innerWrapper.set(null);
            }
        });
        return null;
    }
}
