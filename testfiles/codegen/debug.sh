#!/bin/bash
as --gstabs $1 -o asm.o
ld asm.o -o asm.out
ddd asm.out
rm asm.o
rm asm.out
