#!/bin/bash
as --gstabs $1 -o asm.o
ld asm.o -o asm.out
./asm.out
echo "Returned: $?"
rm asm.o
rm asm.out
