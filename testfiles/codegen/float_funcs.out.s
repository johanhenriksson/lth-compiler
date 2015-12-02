	.section	__TEXT,__text,regular,pure_instructions
	.macosx_version_min 14, 0
	.section	__TEXT,__literal8,8byte_literals
	.align	3
LCPI0_0:
	.quad	4615198826136736891     ## double 3.5600000000000001
	.section	__TEXT,__text,regular,pure_instructions
	.globl	_main
	.align	4, 0x90
_main:                                  ## @main
	.cfi_startproc
## BB#0:
	pushq	%rax
Ltmp0:
	.cfi_def_cfa_offset 16
	movabsq	$4615198826136736891, %rax ## imm = 0x400C7AE147AE147B
	movq	%rax, (%rsp)
	movsd	LCPI0_0(%rip), %xmm0    ## xmm0 = mem[0],zero
	callq	_trunc
	popq	%rcx
	retq
	.cfi_endproc


.subsections_via_symbols
