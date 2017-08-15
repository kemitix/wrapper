package net.kemitix.wrapper;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link WrapperState}.
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
public class WrapperStateTest {

    @Test
    public void canCreateStateForAnObject() {
        //given
        final Object o = new Object();
        //when
        final WrapperState<Object> wrapperState = new WrapperState<>(o);
        //then
        assertThat(wrapperState.getWrapperCore()).isSameAs(o);
        assertThat(wrapperState.getWrapperDelegate()).isSameAs(o);
    }

    @Test
    public void canCreateStateForWrappedObject() {
        //given
        final Object o = new Object();
        final WrapperState<Object> existingWrapperState = new WrapperState<>(o);
        //when
        final WrapperState<Object> wrapperState = new WrapperState<>(existingWrapperState);
        //then
        assertThat(wrapperState.getWrapperDelegate()).isSameAs(existingWrapperState);
    }

    @Test
    public void whenOneInnerWrapperCanRemoveIt() {
        //given
        final Object o = new Object();
        final WrapperState<Object> first = new WrapperState<>(o);
        final WrapperState<Object> second = new WrapperState<>(first);
        //when
        second.removeWrapper(first);
        //then
        assertThat(second.getWrapperDelegate()).isSameAs(o);
    }

    @Test
    public void whenTwoInnerWrappersCanRemoveFirst() {
        //given
        final Object o = new Object();
        final WrapperState<Object> first = new WrapperState<>(o);
        final WrapperState<Object> second = new WrapperState<>(first);
        final WrapperState<Object> third = new WrapperState<>(second);
        //when
        third.removeWrapper(first);
        //then
        assertThat(second.getWrapperDelegate()).isSameAs(o);// second now wraps o directly
        assertThat(third.getWrapperDelegate()).isSameAs(second);// no change
    }

    @Test
    public void whenTwoInnerWrappersCanRemoveSecond() {
        //given
        final Object o = new Object();
        final WrapperState<Object> first = new WrapperState<>(o);
        final WrapperState<Object> second = new WrapperState<>(first);
        final WrapperState<Object> third = new WrapperState<>(second);
        assertThat(first.getWrapperDelegate()).isSameAs(o);
        assertThat(second.getWrapperDelegate()).isSameAs(first);
        assertThat(third.getWrapperDelegate()).isSameAs(second);
        //when
        third.removeWrapper(second);
        //then
        assertThat(first.getWrapperDelegate()).isSameAs(o);// no change
        assertThat(third.getWrapperDelegate()).isSameAs(first);// third now wraps first
    }
}
