.global _start
.data
buf: .skip 1024

.text
_start:
    call main
    movq %rax, %rdi
    movq $60, %rax
    syscall

main:
    pushq %rbp
    movq %rsp, %rbp
    subq $8, %rsp
    movq $0, %rax
    movq %rax, -16(%rbp)
while3:
    movq -16(%rbp), %rax
    pushq %rax
    movq $3, %rax
    movq %rax, %rbx
    popq %rax
    movq %rax, %rcx
    movq $0, %rax
    movq $1, %rdx
    cmpq %rbx, %rcx
    cmovl %rdx, %rax
    cmpq $0, %rax
    je endwhile3
    movq -16(%rbp), %rax
    pushq %rax
    movq $1, %rax
    movq %rax, %rbx
    popq %rax
    addq %rbx, %rax
    movq %rax, -16(%rbp)
    jmp while3
endwhile3:
    movq -16(%rbp), %rax
    pushq %rax
    movq $3, %rax
    movq %rax, %rbx
    popq %rax
    subq %rbx, %rax
    movq %rbp, %rsp
    popq %rbp
    ret

print:
    pushq %rbp
    movq %rsp, %rbp
    ### convert integer to string
    movq 16(%rbp), %rax     # parameter
    movq $(buf+1023), %rsi  # write ptr (start from end of buf)
    movb $0x0a, (%rsi)      # insert newline
    movq $1, %rcx           # string length
itoa_loop:                      # do.. while (at least one iteration)
    movq $10, %rbx
    movq $0, %rdx
    idivq %rbx              # divide rdx:rax by 10
    addb $0x30, %dl         # remainder + '0'
    decq %rsi               # move string pointer
    movb %dl, (%rsi)
    incq %rcx               # increment string length
    cmpq $0, %rax
    jg itoa_loop            # produce more digits
itoa_done:
    movq $1, %rdi
    movq %rcx, %rdx
    movq $1, %rax
    syscall
    popq %rbp
    ret
read:
    pushq %rbp
    movq %rsp, %rbp
    movq $0, %rdi
    movq $buf, %rsi
    movq $1024, %rdx
    movq $0, %rax
    syscall                 # %rax = sys_read(0, buf, 1024)
    ### convert string to integer:
    ### %rax contains nchar
    ### %rsi contains ptr
    movq $0, %rdx           # sum = 0
atoi_loop:
    cmpq $0, %rax           # while (nchar > 0)
    jle atoi_done           # leave loop if nchar <= 0
    movzbq (%rsi), %rbx     # move byte, and sign extend to qword
    cmpq $0x30, %rbx        # test if < '0'
    jl atoi_done            # character is not numeric
    cmpq $0x39, %rbx        # test if > '9'
    jg atoi_done            # character is not numeric
    imulq $10, %rdx         # multiply sum by 10
    subq $0x30, %rbx        # value of character
    addq %rbx, %rdx         # add to sum
    incq %rsi               # step to next char
    decq %rax               # nchar--
    jmp atoi_loop           # loop back
atoi_done:
    movq %rdx, %rax         # return value in RAX
    popq %rbp
    ret
