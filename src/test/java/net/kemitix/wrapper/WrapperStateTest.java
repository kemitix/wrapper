package net.kemitix.wrapper;

import lombok.val;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link WrapperState}.
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
public class WrapperStateTest {

    @Test
    public void canCreateWrapperStateForObject() {
        //given
        val o = createSubject();
        //when
        val wrapperState = createWrapperState(o);
        //then
        assertThat(wrapperState.getWrapperCore()).isSameAs(o);
        assertThat(wrapperState.getWrapperDelegate()).isSameAs(o);
    }

    @Test
    public void canCreateStateForWrappedObject() {
        //given
        val o = createSubject();
        val inner = createWrapperState(o);
        //when
        val wrapperState = createWrapperState(inner);
        //then
        assertThat(wrapperState.getWrapperDelegate()).isSameAs(inner);
    }

    private WrapperState<Subject> createWrapperState(final Subject o) {
        return new WrapperState<>(o);
    }

    private WrapperState<Subject> createWrapperState(final WrapperState<Subject> inner) {
        return new WrapperState<>(inner);
    }

    private Subject createSubject() {
        return new Subject();
    }

    private class Subject {

    }
}
