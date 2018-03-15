/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 Paul Campbell
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

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
