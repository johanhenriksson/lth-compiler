#include "lib.h"

#include "stdlib.h"
#include "stdio.h"
#include "stdint.h"
#include "stdbool.h"

int64_t __malloc_counter = 0;

// malloc wrapper
void* __malloc(int32_t size) {
    void* ptr = malloc(size);
    if (ptr == NULL) {
        exit(1);
    }
    __malloc_counter++;
    return ptr;
}

// free wrapper
void __delete(void* ptr) {
    free(ptr);
    __malloc_counter--;
}

void __mem_check() {
    if (__malloc_counter == 0)
        return;

    printf("Memory leak\n");
    exit(1);
}

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
