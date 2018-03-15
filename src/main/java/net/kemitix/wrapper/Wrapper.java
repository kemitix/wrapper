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
     * Gets the Wrapper's state object.
     *
     * @return the WrapperState
     */
    WrapperState<T> getWrapperState();

    /**
     * Fetch the core item being Wrapper.
     *
     * @return the core item
     */
    default T getWrapperCore() {
        return getWrapperState().getWrapperCore();
    }

    /**
     * Gets the inner wrapper if one is present.
     *
     * @return An Optional containing the inner wrapper if present, otherwise is empty
     */
    default Optional<Wrapper<T>> findInnerWrapper() {
        return getWrapperState().findInnerWrapper();
    }

    /**
     * Remove the wrapper from the chain of wrappers.
     *
     * @param wrapper the wrapper to remove
     *
     * @return {@code this} Wrapper if {@code wrapper} is not {@code this}, otherwise the Wrapper Delegate, which may be
     * the Wrapper Core
     */
    @SuppressWarnings("unchecked")
    default T removeWrapper(@NonNull Wrapper<T> wrapper) {
        if (wrapper == this) {
            return getWrapperDelegate();
        }
        return Optional.ofNullable(getWrapperState().removeWrapper(wrapper))
                       .orElse((T) this);
    }

    /**
     * Provides the core item's own interface.
     *
     * @return the core item as a T object
     */
    @SuppressWarnings("unchecked")
    default T asCore() {
        return (T) this;
    }

    default T getWrapperDelegate() {
        return getWrapperState()
                .findInnerWrapper()
                .<T>map(Wrapper::asCore)
                .orElseGet(this::getWrapperCore);
    }

    /**
     * Checks if the item is a Wrapper, and returns it as one, inside an Optional, if it is, otherwise it returns empty.
     *
     * @param item The item to check
     * @param <T>  The type of the item
     *
     * @return an Optional containing either the item as a Wrapper, or empty is item isn't a Wrapper
     */
    @SuppressWarnings("unchecked")
    static <T> Optional<Wrapper<T>> asWrapper(@NonNull T item) {
        if (item instanceof Wrapper) {
            return Optional.of((Wrapper<T>) item);
        }
        return Optional.empty();
    }
}
