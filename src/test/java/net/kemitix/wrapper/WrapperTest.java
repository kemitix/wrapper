package net.kemitix.wrapper;

import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.fail;

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
        // outer -> inner -> subject
        //when
        final Optional<Wrapper<Subject>> result = Wrapper.remove(inner, outer);
        //then
        // result -> subject
        assertThat(result).isNotEmpty();
        result.ifPresent(r -> {
            assertThat(r.getInnerWrapper()).isEmpty();
            assertThat(r.getWrapperSubject()).isSameAs(subject);
        });
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
        final Optional<Wrapper<Subject>> result = Wrapper.remove(unknownWrapper, outer);
        //then
        // outer -> inner -> subject
        assertThat(result).isNotEmpty();
        result.ifPresent(r ->
            assertThat(r.getInnerWrapper()).containsSame(inner)
        );
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
        final Optional<Wrapper<Subject>> result = Wrapper.remove(middle, outer);
        //then
        // result -> inner -> subject
        assertThat(result).isNotEmpty();
        result.ifPresent(r ->
                assertThat(r.getInnerWrapper()).containsSame(inner));
    }

    @Test
    public void canRemoveAnInnerThatWrapsTheSubject() {
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
        final Optional<Wrapper<Subject>> result = Wrapper.remove(inner, outer);
        //then
        // result -> middle' -> subject
        assertThat(result).isNotEmpty();
        result.ifPresent(r -> {
            assertThat(r.getInnerWrapper()).isNotEmpty();
            r.getInnerWrapper().ifPresent(m -> {
                assertThat(m.getInnerWrapper()).isEmpty();
                assertThat(m.getWrapperSubject()).isSameAs(subject);
            });
        });
    }

    @Test
    public void removeForOnlyWrapperShould() {
        //given
        final Subject subject = new Subject();
        final Wrapper<Subject> wrapper = Wrapper.wrap(subject);
        // wrapper -> subject
        assertThat(wrapper.getInnerWrapper()).isEmpty();
        assertThat(wrapper.getWrapperSubject()).isSameAs(subject);
        //when
        final Optional<Wrapper<Subject>> result = Wrapper.remove(wrapper, wrapper);
        //then
        assertThat(result).isEmpty();
    }

    @Test
    public void canRemoveOuterWrapperForTwoWrappers() {
        //given
        final Subject subject = new Subject();
        final Wrapper<Subject> inner = Wrapper.wrap(subject);
        final Wrapper<Subject> outer = Wrapper.wrap(inner);
        // outer -> inner -> subject
        assertThat(outer.getInnerWrapper()).containsSame(inner);
        assertThat(inner.getInnerWrapper()).isEmpty();
        assertThat(inner.getWrapperSubject()).isSameAs(subject);
        //when
        final Optional<Wrapper<Subject>> result = Wrapper.remove(outer, outer);
        //then
        // result <=> inner -> subject
        assertThat(result).isNotEmpty();
        result.ifPresent(r ->
                assertThat(r).isSameAs(inner));
    }

    @Test
    public void canRemoveOuterWrapperForThreeWrappers() {
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
        final Optional<Wrapper<Subject>> result = Wrapper.remove(outer, outer);
        //then
        // result <=> middle -> inner -> subject
        assertThat(result).isNotEmpty();
        result.ifPresent(r ->
                assertThat(r).isSameAs(middle));
    }

    private class Subject {
    }

}
