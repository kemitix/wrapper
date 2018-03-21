package net.kemitix.wrapper;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

class NestedWrapper<T> extends AtomicReference<Wrapper<T>> implements Wrapper<T> {

    NestedWrapper(Wrapper<T> wrapper) {
        super(wrapper);
    }

    @Override
    public T getCore() {
        return get().getCore();
    }

    @Override
    public Optional<Wrapper<T>> getInner() {
        return Optional.of(get());
    }

    @Override
    public String toString() {
        return "NestedWrapper["+ getInner() + "]";
    }
}
