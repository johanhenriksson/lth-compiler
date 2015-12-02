#ifndef LIB_H
#include "stdint.h"
#include "stdbool.h"

// assert
void assert(bool v);

// io
void print(int64_t v);
int64_t read();

// casts
double promote(int64_t v);
int64_t truncate(double v);

#endif
