package net.kemitix.wrapper;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

class SubjectWrapper<T>
        extends AtomicReference<T>
        implements Wrapper<T> {

    SubjectWrapper(final T subject) {
        super(subject);
    }

    @Override
    public T getCore() {
        return get();
    }

    @Override
    public Optional<Wrapper<T>> getInner() {
        return Optional.empty();
    }

    @Override
    public String toString() {
        return "SubjectWrapper["+ get() + "]";
    }
}
