.text

# Generates an autostereogram inside of buffer
#
# Arguments:
#     autostereogram (unsigned char*)
#     depth_map (unsigned char*)
#     width
#     height
#     strip_size
calc_autostereogram:

        # Allocate 5 spaces for $s0-$s5
        # (add more if necessary)
        addiu $sp $sp -20
        sw $s0 0($sp)
        sw $s1 4($sp)
        sw $s2 8($sp)
        sw $s3 12($sp)
        sw $s4 16($sp)

        # autostereogram
        lw $s0 20($sp)
        
        # depth_map
        lw $s1 24($sp)
        
        # width_M
        lw $s2 28($sp)
        
        # height_N
        lw $s3 32($sp)
        
        # strip_size_S
        lw $s4 36($sp)

        # YOUR CODE HERE #

        #li $t5 0        # index for lfsr_random and depth(i, j)

FORLOOP:
        li $t0 0        # i = 0
        li $t1 0        # j = 0
        
LOOP1:
        slt $t2 $t0 $s2 # if i<M
        bne $t2 1 L2
        j LOOP2 # jump to inner for loop
LOOP2:
        slt $t3 $t1 $s3 # if j<N
        bne $t3 1 L1
        j IFELSE # jump to if and else
        
IFELSE: 

        
        slt $t4 $t0 $s4 # if i<S
        beq $t4 1 IF # go to if
        j ELSE # go to else
        
        
        
IF:
        li $t5 0 # initialize index
        mul $t5 $t1 $s2
        addu $t5 $t5 $t0 # get the index i+j*M
        addiu $sp $sp -48
        sw $t0 0($sp)
        sw $t1 4($sp)
        sw $t2 8($sp)
        sw $t3 12($sp)
        sw $t4 16($sp)
        sw $t5 20($sp)
        sw $ra 24($sp)
        
        sw $s0 28($sp)
        sw $s1 32($sp)
        sw $s2 36($sp)
        sw $s3 40($sp)
        sw $s4 44($sp)
        

        jal lfsr_random
        andi $v0 $v0 0xff
                lw $ra 24($sp)
        lw $t0 0($sp)
        lw $t1 4($sp)
        lw $t2 8($sp)
        lw $t3 12($sp)
        lw $t4 16($sp)
        lw $t5 20($sp)
        
        sw $s0 28($sp)
        sw $s1 32($sp)
        sw $s2 36($sp)
        sw $s3 40($sp)
        sw $s4 44($sp)
        addiu $sp $sp 48
        li $t6 0
        addu $t6 $t5 $s0
        #move $t7 $v0
        sb $v0 0($t6)
        

        
        j L3
        
ELSE:
        li $t5 0 # initialize index
        li $t6 0
        li $t7 0
        li $t8 0
        li $t9 0
        mul $t5 $t1 $s2
        addu $t5 $t5 $t0 # get the index i+j*M

        lb $t7 0($s1)
        sll $t5 $t5 0
        addu $t8 $t5 $s1
        lb $t8 0($t8) # depth(i,j)
        
        addu $t9 $t5 $s0      
        
        subu $t5 $t5 $s4 # i+j*M-S
        addu $t5 $t5 $t8 # i+j*M-S+depth(i,j)

        addu $t6 $t5 $s0 # I((i+depth(i,j)-S),j)

        lb $t6 0($t6)
        sb $t6 0($t9)
              
        j L3
        
L3:
        addu $t1 $t1 1 # j++
        j LOOP2
L1:     
        li $t1 0 # set j back to 0
        addiu $t0 $t0 1 # i++
        j LOOP1
        
L2:
        #li $t0 0
        j RETURN   
        

RETURN:
                
        lw $s0 0($sp)
        lw $s1 4($sp)
        lw $s2 8($sp)
        lw $s3 12($sp)
        lw $s4 16($sp)
        addiu $sp $sp 20
        jr $ra
