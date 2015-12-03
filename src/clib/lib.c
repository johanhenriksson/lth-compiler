#include "lib.h"

#include "stdlib.h"
#include "stdio.h"
#include "stdint.h"
#include "stdbool.h"

void print(int64_t v) {
    printf("%lld\n", v);
}

int64_t read() {
    int64_t v;
    scanf("%lld", &v);
    return v;
}

int64_t truncate(double v) {
    return (int64_t)v;
}

double promote(int64_t v) {
    return (double)v;
}

void assert(bool v) {
    if (v == false)
        exit(1);
}

void except(bool v) {
    if (v != false)
        exit(1);
}
