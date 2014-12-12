.data

lfsr:
        .align 4
        .half
        0x1

.text

# Implements a 16-bit lfsr
#
# Arguments: None
lfsr_random:

        la $t0 lfsr     #address to lfsr
        lhu $v0 0($t0)   # initial introduction of $v0 as reg (uint16_t)

        # YOUR CODE HERE #
     
        li $t1 0 #initialize i to 0

        loop: 
        	# reg >> ? 
        	srl $t2, $v0, 2
        	srl $t3, $v0, 3
        	srl $t4, $v0, 5

        	# uint16_t highest declaration
        	xor $t5, $v0, $t2	# (reg>>0) ^ (reg >> 2)
        	xor $t6, $t5, $t3	# (reg>>0) ^ (reg >>2) ^ (reg>>3)
        	xor $t7, $t6, $t4       # (reg>>0) ^ (reg >> 2) ^ (reg>>3) ^ (reg>>5) = highest, so $t7 is highest

        	srl $t2, $v0, 1   	# $t2 = (reg >> 1)
        	sll $t3, $t7, 15    # $t3 = (highest << 15)

        	or $v0, $t2, $t3    # $v0 = reg = (reg >> 1) | (highest << 15) 
		andi $v0 65535
        	addi $t1, $t1, 1   # increment i        	
        	slti $t4, $t1, 16  # i < 16 ? 
        	bne $t4, $0, loop 
        	
	la $t0 lfsr
        sh $v0 0($t0)	# $v0 (reg) must be saved before calling jr
        jr $ra



