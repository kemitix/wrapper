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
    public void whenSingleWrapperCanRemoveItself() {
        //given
        val o = new Object();
        val wrapper = new WrapperState<>(o);
        //when
        val result = wrapper.removeWrapper(wrapper);
        //then
        assertThat(result).isSameAs(o);
    }

    @Test
    public void whenWrapperToRemoveIsInvalidThenIgnoreAndReturnSelf() {
        //given
        val o = new Object();
        val wrapper = new WrapperState<>(o);
        //when
        val result = ((Wrapper<Object>) wrapper.removeWrapper(new WrapperState<>(o)));
        //then
        assertThat(result).isSameAs(wrapper);
        assertThat(result.getWrapperDelegate()).isSameAs(o);
    }

    @Test
    public void whenTwoWrappersCanRemoveInner() {
        //given
        val o = new Object();
        val inner = new WrapperState<Object>(o);
        val outer = new WrapperState<Object>(inner);
        //when
        val result = ((Wrapper<Object>) outer.removeWrapper(inner));
        //then
        assertThat(result).isSameAs(outer);
        assertThat(result.getWrapperDelegate()).isSameAs(o);
    }

    @Test
    public void whenTwoInnerWrappersCanRemoveFirst() {
        //given
        val o = new Object();
        val inner = new WrapperState<Object>(o);
        val middle = new WrapperState<Object>(inner);
        val outer = new WrapperState<Object>(middle);
        //when
        val result = (Wrapper<Object>) outer.removeWrapper(inner);
        //then
        assertThat(result).isSameAs(outer);
        assertThat(outer.getWrapperDelegate()).isSameAs(middle);
        assertThat(middle.getWrapperDelegate()).isSameAs(o);
    }

    @Test
    public void whenTwoInnerWrappersCanRemoveSecond() {
        //given
        final Object o = new Object();
        val inner = new WrapperState<Object>(o);
        val middle = new WrapperState<Object>(inner);
        val outer = new WrapperState<Object>(middle);
        //when
        val result = (Wrapper<Object>) outer.removeWrapper(middle);
        //then
        assertThat(result).isSameAs(outer);
        assertThat(result.getWrapperDelegate()).isSameAs(inner);
        assertThat(inner.getWrapperDelegate()).isSameAs(o);
    }

    @Test
    public void canRemoveOnlyWrapper() {
        //given
        val o = new Object();
        val wrapper = new WrapperState<Object>(o);
        //when
        val result = wrapper.removeWrapper(wrapper);
        //then
        assertThat(result).isSameAs(o);
    }
}
