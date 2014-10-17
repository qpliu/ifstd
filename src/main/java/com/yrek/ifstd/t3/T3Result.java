package com.yrek.ifstd.t3;

class T3Result {
    final T3Value result;
    final T3Error error;

    T3Result(T3Value result) {
        this.result = result;
        this.error = null;
    }

    T3Result(T3Error error) {
        this.result = null;
        this.error = error;
    }

    static final T3Result NIL = new T3Result(T3Value.NIL);
    static final T3Result INT0 = new T3Result(T3Value.INT0);
    static final T3Result INT1 = new T3Result(T3Value.INT1);
    static final T3Result INTM1 = new T3Result(T3Value.INTM1);
    static final T3Result TRUE = new T3Result(T3Value.TRUE);

    static final T3Result ERROR_BAD_TYPE_ADD = new T3Result(T3Error.BAD_TYPE_ADD);
    static final T3Result ERROR_BAD_TYPE_BNOT = new T3Result(T3Error.BAD_TYPE_BNOT);
    static final T3Result ERROR_BAD_TYPE_MUL = new T3Result(T3Error.BAD_TYPE_MUL);
    static final T3Result ERROR_BAD_TYPE_SUB = new T3Result(T3Error.BAD_TYPE_SUB);
    static final T3Result ERROR_INVALID_COMPARISON = new T3Result(T3Error.INVALID_COMPARISON);
    static final T3Result ERROR_NUM_VAL_REQD = new T3Result(T3Error.NUM_VAL_REQD);
}
