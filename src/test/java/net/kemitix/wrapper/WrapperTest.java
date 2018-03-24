package net.kemitix.wrapper;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

/**
 * Tests for {@link Wrapper}.
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
public class WrapperTest {

    @Test
    public void wrapperRequiresSubject() {
        assertThatNullPointerException()
                .isThrownBy(() -> Wrapper.wrap((Subject) null))
                .withMessage("subject");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void wrapRequiresInner() {
        assertThatNullPointerException()
                .isThrownBy(() -> Wrapper.wrap((Wrapper) null))
                .withMessage("wrapper");
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
    public void removeRequiresWrapperToBeRemoved() {
        assertThatNullPointerException()
                .isThrownBy(() -> Wrapper.remove(null, Wrapper.wrap(new Subject())))
                .withMessage("remove");
    }

    @Test
    public void removeRequiresWrapperToRemoveFrom() {
        assertThatNullPointerException()
                .isThrownBy(() -> Wrapper.remove(Wrapper.wrap(new Subject()), null))
                .withMessage("wrapper");
    }

    @Test
    public void canRemoveInnerWrapper() {
        //given
        final Subject subject = new Subject();
        final Wrapper<Subject> inner = Wrapper.wrap(subject);
        final Wrapper<Subject> outer = Wrapper.wrap(inner);
        //when
        final Wrapper<Subject> result = Wrapper.remove(inner, outer);
        //then
        assertThat(result).returns(subject, Wrapper::getWrapperSubject);
        assertThat(result.getInnerWrapper()).isEmpty();
    }

    @Test
    public void attemptingToRemoveAnUnknownWrapperReturnsOriginalWrapper() {
        //given
        final Subject subject = new Subject();
        final Wrapper<Subject> inner = Wrapper.wrap(subject);
        final Wrapper<Subject> outer = Wrapper.wrap(inner);
        final Wrapper<Subject> unknownWrapper = Wrapper.wrap(new Subject());
        // outer -> inner -> subject
        assertThat(outer.getInnerWrapper()).containsSame(inner);
        assertThat(inner.getInnerWrapper()).isEmpty();
        assertThat(inner.getWrapperSubject()).isSameAs(subject);
        //when
        final Wrapper<Subject> result = Wrapper.remove(unknownWrapper, outer);
        //then
        assertThat(result.getInnerWrapper()).containsSame(inner);
        assertThat(inner.getInnerWrapper()).isEmpty();
        assertThat(inner.getWrapperSubject()).isSameAs(subject);
    }

    @Test
    public void canRemoveAnInnerThatHasAnInner() {
        //given
        final Subject subject = new Subject();
        final Wrapper<Subject> inner = Wrapper.wrap(subject);
        final Wrapper<Subject> middle = Wrapper.wrap(inner);
        final Wrapper<Subject> outer = Wrapper.wrap(middle);
        // outer -> middle -> inner -> subject
        assertThat(outer.getInnerWrapper()).containsSame(middle);
        assertThat(middle.getInnerWrapper()).containsSame(inner);
        assertThat(inner.getInnerWrapper()).isEmpty();
        assertThat(inner.getWrapperSubject()).isSameAs(subject);
        //when
        final Wrapper<Subject> result = Wrapper.remove(middle, outer);
        //then
        // result -> inner' -> subject
        assertThat(result.getInnerWrapper()).isNotEmpty();
        result.getInnerWrapper()
                .ifPresent(wrapper ->
                        assertThat(wrapper).isSameAs(inner)
        );
    }

    private class Subject {
    }

}
