package net.kemitix.wrapper;

import org.assertj.core.api.WithAssertions;
import org.junit.Test;

import java.util.Optional;

/**
 * Tests for {@link Wrapper}.
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
public class WrapperTest implements WithAssertions {

    @Test
    public void wrapperRequiresSubject() {
        assertThatNullPointerException()
                .isThrownBy(() -> Wrapper.wrap((Subject) null))
                .withMessageStartingWith("subject is marked");
    }

    @Test
    public void wrapRequiresInner() {
        final Wrapper<String> wrapper = null;
        assertThatNullPointerException()
                .isThrownBy(() -> Wrapper.wrap(wrapper))
                .withMessageStartingWith("wrapper is marked");
    }

    @Test
    public void canWrapAndUnwrapASubject() {
        //given
        final Subject subject = new Subject();
        //when
        final Wrapper<Subject> wrapped = Wrapper.wrap(subject);
        //then
        assertThat(wrapped).returns(subject, Wrapper::getWrapperSubject);
    }

    @Test
    public void canWrapAWrappedSubjectAndGetSubject() {
        //given
        final Subject subject = new Subject();
        final Wrapper<Subject> inner = Wrapper.wrap(subject);
        //when
        final Wrapper<Subject> outer = Wrapper.wrap(inner);
        //then
        assertThat(outer).returns(subject, Wrapper::getWrapperSubject);
    }

    @Test
    public void canWrapAndUnwrapAWrappedSubject() {
        //given
        final Subject subject = new Subject();
        final Wrapper<Subject> inner = Wrapper.wrap(subject);
        //when
        final Wrapper<Subject> outer = Wrapper.wrap(inner);
        //then
        assertThat(outer.getInnerWrapper()).contains(inner);
    }

    @Test
    public void singleWrapperHasNoInnerWrapper() {
        //given
        final Subject subject = new Subject();
        final Wrapper<Subject> wrapper = Wrapper.wrap(subject);
        //when
        final Optional<Wrapper<Subject>> result = wrapper.getInnerWrapper();
        //then
        assertThat(result).isEmpty();
    }

    private class Subject {
    }

}
