package net.kemitix.wrapper;

import lombok.val;
import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

/**
 * Tests for {@link Wrapper}.
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
public class WrapperTest {

    @Test
    public void canGetWrapperCore() {
        //given
        val subject = createSubject();
        val wrapper = createWrapper(subject);
        //when
        final Subject wrapperCore = wrapper.getWrapperCore();
        //then
        assertThat(wrapperCore).isSameAs(subject);
    }

    @Test
    public void canFindInnerWrapperInState() {
        //given
        val subject = createSubject();
        val inner = createWrapper(subject);
        val outer = createWrapper(inner);
        //when
        final Optional<Wrapper<Subject>> innerWrapper = outer.findInnerWrapper();
        //then
        assertThat(innerWrapper).contains(inner);
    }

    @Test
    public void removeWrapperRequiresAWrapper() {
        //given
        val subject = createSubject();
        val wrapper = createWrapper(subject);
        //then
        assertThatNullPointerException().isThrownBy(() ->
                                                            //when
                                                            wrapper.removeWrapper(null))
                                        //and
                                        .withMessage("wrapper");
    }

    @Test
    public void whenSingleWrapperCanRemoveItself() {
        //given
        val subject = createSubject();
        val wrapper = createWrapper(subject);
        assertThat(wrapper.getWrapperDelegate()).isSameAs(subject);
        //when
        val result = doRemoveWrapper(wrapper, wrapper);
        //then
        assertThat(result).isSameAs(subject);
    }

    @Test
    public void whenWrapperToRemoveIsInvalidThenIgnoreAndReturnSelf() {
        //given
        val subject = createSubject();
        val wrapper = createWrapper(subject);
        val other = createWrapper(subject);
        //when
        val result = doRemoveWrapper(wrapper, other);
        //then
        assertThat(result).isSameAs(wrapper);
        val resultAsWrapper = verifyIsAWrapper(result);
        assertThat(resultAsWrapper.getWrapperDelegate()).isSameAs(subject);
    }

    @Test
    public void whenTwoWrappersCanRemoveInner() {
        //given
        val subject = createSubject();
        val inner = createWrapper(subject);
        val outer = createWrapper(inner);
        //when
        val result = doRemoveWrapper(outer, inner);
        //then
        assertThat(result).isSameAs(outer);
        val resultAsWrapper = verifyIsAWrapper(result);
        assertThat(resultAsWrapper.getWrapperDelegate()).isSameAs(subject);
    }

    @Test
    public void whenTwoInnerWrappersCanRemoveFirst() {
        //given
        val subject = createSubject();
        val inner = createWrapper(subject);
        val middle = createWrapper(inner);
        val outer = createWrapper(middle);
        //when
        val result = doRemoveWrapper(outer, inner);
        //then
        assertThat(result).isSameAs(outer);
        assertThat(outer.getWrapperDelegate()).isSameAs(middle);
        assertThat(middle.getWrapperDelegate()).isSameAs(subject);
    }

    @Test
    public void whenTwoInnerWrappersCanRemoveSecond() {
        //given
        val subject = createSubject();
        val inner = createWrapper(subject);
        val middle = createWrapper(inner);
        val outer = createWrapper(middle);
        //when
        val result = doRemoveWrapper(outer, middle);
        //then
        assertThat(result).isSameAs(outer);
        val resultAsWrapper = verifyIsAWrapper(result);
        assertThat(resultAsWrapper.getWrapperDelegate()).isSameAs(inner);
        assertThat(inner.getWrapperDelegate()).isSameAs(subject);
    }

    @Test
    public void canRemoveOnlyWrapper() {
        //given
        val subject = createSubject();
        val wrapper = createWrapper(subject);
        //when
        val result = doRemoveWrapper(wrapper, wrapper);
        //then
        assertThat(result).isSameAs(subject);
    }

    @Test
    public void whenItemIsReallyAWrapperThenCanGetItAsAWrapper() {
        //given
        val subject = createSubject();
        val wrapper = createWrapper(subject);
        val asSubject = (Subject) wrapper;
        //when
        val asWrapper = Wrapper.asWrapper(asSubject);
        //then
        assertThat(asWrapper).contains(wrapper);
    }

    @Test
    public void whenItemIsNotReallyAWrapperThenDoNotGetItAsAWrapper() {
        //given
        val subject = createSubject();
        //when
        val asWrapper = Wrapper.asWrapper(subject);
        //then
        assertThat(asWrapper).isEmpty();
    }

    @Test
    public void whenItemIsNullThenAsWrapperThrowsException() {
        assertThatNullPointerException().isThrownBy(() -> Wrapper.asWrapper(null))
                                        .withMessage("item");
    }

    @SuppressWarnings("unchecked")
    private Wrapper<Subject> verifyIsAWrapper(final Subject subject) {
        assertThat(subject).isInstanceOf(Wrapper.class);
        return (Wrapper<Subject>) subject;
    }

    private Subject doRemoveWrapper(final Wrapper<Subject> from, final Wrapper<Subject> what) {
        return from.removeWrapper(what);
    }

    private Wrapper<Subject> createWrapper(final Subject o) {
        return new SubjectWrapper(o);
    }

    private Wrapper<Subject> createWrapper(final Wrapper<Subject> inner) {
        return new SubjectWrapper(inner);
    }

    private Subject createSubject() {
        return new Subject();
    }

    private class SubjectWrapper extends Subject implements Wrapper<Subject> {

        private WrapperState<Subject> wrapperState;

        SubjectWrapper(final Subject o) {
            wrapperState = new WrapperState<>(o);
        }

        SubjectWrapper(final Wrapper<Subject> inner) {
            wrapperState = new WrapperState<>(inner);
        }

        @Override
        public WrapperState<Subject> getWrapperState() {
            return this.wrapperState;
        }
    }

    private class Subject {

    }
}
