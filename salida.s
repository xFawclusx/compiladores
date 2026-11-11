    .text
    .globl  main
    .type   main, @function
main:
    pushq   %rbp
    movq    %rsp, %rbp
    subq    $48, %rsp
    movq    $0, -8(%rbp)
    movq    $0, -16(%rbp)
    movq    $0, -24(%rbp)

    # x = 0
    movq    $0, %rax
    movq    %rax, -8(%rbp)

    # seguir = true
    movq    $1, %rax
    movq    %rax, -16(%rbp)

    # L1:
.L1:

    # ifFalse seguir goto L2
    movq    -16(%rbp), %rax
    cmpq    $0, %rax
    je      .L2

    # interno = true
    movq    $1, %rax
    movq    %rax, -24(%rbp)

    # L3:
.L3:

    # ifFalse interno goto L4
    movq    -24(%rbp), %rax
    cmpq    $0, %rax
    je      .L4

    # t1 = x + 1
    movq    -8(%rbp), %rax
    addq    $1, %rax
    movq    %rax, -32(%rbp)

    # x = t1
    movq    -32(%rbp), %rax
    movq    %rax, -8(%rbp)

    # interno = false
    movq    $0, %rax
    movq    %rax, -24(%rbp)

    # goto L3
    jmp     .L3

    # L4:
.L4:

    # ifFalse true goto L5
    movq    $1, %rax
    cmpq    $0, %rax
    je      .L5

    # ifFalse false goto L6
    movq    $0, %rax
    cmpq    $0, %rax
    je      .L6

    # x = 0
    movq    $0, %rax
    movq    %rax, -8(%rbp)

    # goto L7
    jmp     .L7

    # L6:
.L6:

    # t2 = x + 10
    movq    -8(%rbp), %rax
    addq    $10, %rax
    movq    %rax, -40(%rbp)

    # x = t2
    movq    -40(%rbp), %rax
    movq    %rax, -8(%rbp)

    # L7:
.L7:

    # L5:
.L5:

    # seguir = false
    movq    $0, %rax
    movq    %rax, -16(%rbp)

    # goto L1
    jmp     .L1

    # L2:
.L2:

    # return x
    movq    -8(%rbp), %rax
    leave
    ret

    # fin
    movq    $0, %rax
    leave
    ret
    .size   main, .-main
    .section .note.GNU-stack,"",@progbits
