# Wrapper

Wrapper for Generic types.

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/50dcbe2b48824c6f92cad099195d8858)](https://www.codacy.com/app/kemitix/wrapper?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=kemitix/wrapper&amp;utm_campaign=Badge_Grade)
[![Coverage Status](https://coveralls.io/repos/github/kemitix/wrapper/badge.svg?branch=master)](https://coveralls.io/github/kemitix/wrapper?branch=master)
[![codecov](https://codecov.io/gh/kemitix/wrapper/branch/master/graph/badge.svg)](https://codecov.io/gh/kemitix/wrapper)

## Usage

See [print-stream-wrapper](https://github.com/kemitix/print-stream-wrapper) for an example implementation.

## Notes

* Implementations of `Wrapper` do not need to implement the basic `Wrapper` methods. They only need to now implement the `WrapperState<T> getWrapperState()` method.
* Implementations of `Wrapper<T>` must  `extend` or `implement` the Wrapper's Generic type `T`.
* Implementations of `Wrapper<T>` must not override the default methods implemented in `Wrapper`.
