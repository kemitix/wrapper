package net.kemitix.wrapper;

import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Tests for {@link Wrapper}.
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
public class WrapperTest {

    private WrapperState<Object> wrapperState;

    private Wrapper<Object> objectWrapper;

    private Object o;

    @Before
    public void setUp() {
        initMocks(this);
        o = new Object();
        objectWrapper = () -> wrapperState;
    }

    @Test
    public void canGetWrapperCoreFromState() {
        //given
        wrapperState = new WrapperState<>(o);
        //when
        final Object wrapperCore = objectWrapper.getWrapperCore();
        //then
        assertThat(wrapperCore).isSameAs(o);
    }

    @Test
    public void canFindInnerWrapperInState() {
        //given
        final Wrapper<Object> inner = new WrapperState<>(o);
        wrapperState = new WrapperState<>(inner);
        //when
        final Optional<Wrapper<Object>> innerWrapper = objectWrapper.findInnerWrapper();
        //then
        assertThat(innerWrapper).contains(inner);
    }

    @Test
    public void canRemoveWrapperFromState() {
        //given
        final WrapperState<Object> innerWrapper = new WrapperState<>(o);
        wrapperState = new WrapperState<>(innerWrapper);
        //when
        objectWrapper.removeWrapper(innerWrapper);
        //then
        assertThat(objectWrapper.getWrapperDelegate()).isSameAs(o);
    }

    @Test
    public void requireAWrapperToBeRemoved() {
        assertThatNullPointerException().isThrownBy(() -> objectWrapper.removeWrapper(null))
                                        .withMessage("wrapper");
    }
}
